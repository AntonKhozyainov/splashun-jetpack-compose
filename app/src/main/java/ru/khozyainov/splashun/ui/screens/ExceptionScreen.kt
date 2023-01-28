package ru.khozyainov.splashun.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.screens.login.LoginScreen
import ru.khozyainov.splashun.ui.theme.SplashUnTheme

@Composable
fun ExceptionScreen(
    exceptionMessage: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = exceptionMessage,
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground,
                modifier = modifier
                    .padding(vertical = 10.dp, horizontal = 30.dp)
            )
            Button(
                onClick = onClick
            ) {
                Text(
                    text = stringResource(id = R.string.retry),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ExceptionScreenPreview(){
    SplashUnTheme(darkTheme = false) {
        ExceptionScreen(
            exceptionMessage = "Error",
            onClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ExceptionScreenPreviewDarkTheme(){
    SplashUnTheme(darkTheme = true) {
        ExceptionScreen(
            exceptionMessage = "Error",
            onClick = {}
        )
    }
}