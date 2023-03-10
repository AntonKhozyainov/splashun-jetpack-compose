package ru.khozyainov.splashun.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.khozyainov.splashun.ui.theme.SplashUnTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoadingScreenPreview(){
    SplashUnTheme(darkTheme = false) {
        LoadingScreen()
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoadingScreenDarkTheme(){
    SplashUnTheme(darkTheme = true) {
        LoadingScreen()
    }
}