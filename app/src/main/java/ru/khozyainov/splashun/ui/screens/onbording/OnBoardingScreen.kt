package ru.khozyainov.splashun.ui.screens.onbording

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.login.LoginDestination
import ru.khozyainov.splashun.ui.theme.SplashUnTheme
import ru.khozyainov.splashun.ui.theme.theme_light_button_disabled

object OnBoardingDestination : NavigationDestination {
    override val route: String = "onboarding"
    override val titleRes: Int = R.string.app_name
}

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    expand: Boolean = false,
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel(),
    darkTheme: Boolean = isSystemInDarkTheme(),
) {

    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )

    val pagerState = rememberPagerState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = modifier
                .fillMaxSize()
        ) { position ->
            PagerScreen(
                onBoardingPage = pages[position],
                modifier = modifier,
                darkTheme = darkTheme,
                expand = expand
            )
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = if (expand) 0.3f else 0.2f)
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
                activeColor = MaterialTheme.colors.secondary,
                inactiveColor = theme_light_button_disabled
            )

            FinishButton(
                pagerState = pagerState,
                expand = expand,
                modifier = modifier
                    .weight(1f)
            ) {
                onBoardingViewModel.saveOnBoardingState(completed = true)
                navController.popBackStack()
                navController.navigate(LoginDestination.route)
            }
        }
    }
}

@Composable
fun PagerScreen(
    onBoardingPage: OnBoardingPage,
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    expand: Boolean = false,
) {
    if (expand) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = modifier.padding(20.dp))

            Image(
                painter = painterResource(id = if (darkTheme) onBoardingPage.imageDark else onBoardingPage.image),
                contentDescription = stringResource(id = R.string.onboarding_image_description),
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = 0.4f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = modifier.padding(20.dp))

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_green_ellipse_90),
                    contentDescription = stringResource(id = R.string.green_ellipse),
                    modifier = modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )

                Text(
                    text = stringResource(id = onBoardingPage.title),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp, bottom = 100.dp, start = 100.dp, end = 40.dp),
                    style = MaterialTheme.typography.h2,
                    color = Color.Black
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(modifier = modifier.padding(20.dp))

            Image(
                painter = painterResource(id = if (darkTheme) onBoardingPage.imageDark else onBoardingPage.image),
                contentDescription = stringResource(id = R.string.onboarding_image_description),
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.4f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = modifier.padding(20.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_green_ellipse),
                    contentDescription = stringResource(id = R.string.green_ellipse),
                    modifier = modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = stringResource(id = onBoardingPage.title),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 100.dp, start = 20.dp, end = 20.dp),
                    style = MaterialTheme.typography.h2,
                    color = Color.Black
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun FinishButton(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    expand: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = if (expand) 160.dp else 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth(),
            visible = pagerState.currentPage == (pagerState.pageCount - 1)
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.done),
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun OnBoardingScreenPreview() {
    SplashUnTheme(darkTheme = false) {
        PagerScreen(onBoardingPage = OnBoardingPage.First)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun OnBoardingScreenPreviewDarkTheme() {
    SplashUnTheme(darkTheme = true) {
        PagerScreen(onBoardingPage = OnBoardingPage.First)
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun OnBoardingScreenExpandedPreview() {
    SplashUnTheme {
        PagerScreen(
            onBoardingPage = OnBoardingPage.First,
            expand = true
        )
    }
}