package ru.khozyainov.splashun.ui.screens.login

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.ExceptionScreen
import ru.khozyainov.splashun.ui.screens.LoadingScreen
import ru.khozyainov.splashun.ui.screens.SplashunDestination
import ru.khozyainov.splashun.ui.theme.SplashUnTheme

object LoginDestination : NavigationDestination {
    override val route: String = "login"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    expand: Boolean = false
) {

    val uiState = loginViewModel.uiState.collectAsState()

    val isLaunch = remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val dataIntent = activityResult.data ?: return@rememberLauncherForActivityResult
            handleAuthResponseIntent(dataIntent, loginViewModel)
        }

    val intentFlow = uiState.value.openAuthPageIntent?.collectAsState(initial = null)

    if (!isLaunch.value) {
        intentFlow?.value?.let { intent ->
            launcher.launch(intent)
            isLaunch.value = true
        }
    }

    if (uiState.value.loginIsDone) {
        navController.popBackStack()
        navController.navigate(SplashunDestination.route)
    }

    if (uiState.value.errorMessage.isNotBlank()) {
        ExceptionScreen(
            exceptionMessage = uiState.value.errorMessage
        ) {
            loginViewModel.refreshState()
        }
    } else {
        if (uiState.value.loading) {
            LoadingScreen()
        } else {
            LoginContentScreen(
                modifier = modifier,
                expand = expand,
                onClick = {
                    loginViewModel.openLoginPage()
                }
            )
        }
    }
}

@Composable
fun LoginContentScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    expand: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight(fraction = if (expand) 0.75f else 0.55f)
                .fillMaxWidth()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_login_bangs),
                contentDescription = stringResource(id = R.string.bangs),
                modifier = modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )

            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_icon_launcher),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = modifier
                            .size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.h1,
                        color = Color.White,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 60.sp
                    )
                }
            }
        }

        Button(
            onClick = onClick,
            modifier = modifier
                .width(250.dp)
                .padding(horizontal = 20.dp, vertical = if (expand) 8.dp else 50.dp)
        ) {
            Text(text = stringResource(id = R.string.sing_in_unsplash))
        }

    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoginScreenPreview() {
    SplashUnTheme(darkTheme = false) {
        LoginContentScreen(
            onClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoginScreenDarkTheme() {
    SplashUnTheme(darkTheme = true) {
        LoginContentScreen(
            onClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1000)
fun LoginScreenPreviewExpanded() {
    SplashUnTheme {
        LoginContentScreen(
            expand = true,
            onClick = {}
        )
    }
}

//Нужно получить код и обменять его на токен.
//При этом в ответе может прийти ошибка авторизации, ее тоже нужно обработать.
private fun handleAuthResponseIntent(
    intent: Intent,
    loginViewModel: LoginViewModel
) {
    // пытаемся получить ошибку из ответа. null - если все ок
    val exception = AuthorizationException.fromIntent(intent)
    // пытаемся получить запрос для обмена кода на токен, null - если произошла ошибка
    val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
        ?.createTokenExchangeRequest()
    when {
        // авторизация завершались ошибкой
        exception != null -> loginViewModel.onAuthFailed(exception)
        // авторизация прошла успешно, меняем код на токен
        tokenExchangeRequest != null ->
            loginViewModel.getTokenByRequest(tokenExchangeRequest)
    }
}