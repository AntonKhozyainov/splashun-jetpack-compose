package ru.khozyainov.splashun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.khozyainov.splashun.ui.navigation.SetupNavGraph
import ru.khozyainov.splashun.ui.screens.OnBoardingViewModel
import ru.khozyainov.splashun.ui.theme.SplashUnTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: OnBoardingViewModel by viewModels()

        setContent {
            SplashUnTheme {
                SetupNavGraph(
                    navController = rememberNavController(),
                    startDestination =
                )

            }
        }
    }
}
