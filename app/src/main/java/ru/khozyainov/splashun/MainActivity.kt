package ru.khozyainov.splashun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.khozyainov.splashun.ui.LauncherViewModel
import ru.khozyainov.splashun.ui.navigation.SetupNavGraph
import androidx.compose.runtime.getValue
import ru.khozyainov.splashun.ui.theme.SplashUnTheme
import ru.khozyainov.splashun.utils.isExpand

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashUnTheme {

                val viewModel: LauncherViewModel by viewModels()
                val screen by viewModel.startDestination.collectAsState()
                val isExpand = calculateWindowSizeClass(this).widthSizeClass.isExpand()

                SetupNavGraph(
                    navController = rememberNavController(),
                    startDestination = screen,
                    expand = isExpand
                )

            }
        }
    }
}
