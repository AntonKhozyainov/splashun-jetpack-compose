package ru.khozyainov.splashun.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.khozyainov.splashun.ui.navigation.SetupNavGraph
import ru.khozyainov.splashun.ui.theme.SplashUnTheme
import ru.khozyainov.splashun.utils.isExpand
import ru.khozyainov.splashun.utils.launchAndCollect

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        showSplashScreen(splashScreen = splashScreen, isLoading = true)

        super.onCreate(savedInstanceState)

        val viewModel: LauncherViewModel by viewModels()
        viewModel.uiState.launchAndCollect(lifecycle) { state ->
            when (state) {
                is LauncherViewModel.LauncherState.Loading -> {
                    showSplashScreen(splashScreen = splashScreen, isLoading = state.loading)
                }
                is LauncherViewModel.LauncherState.Success -> {
                    setContent(startDestination = state.route)
                }
            }
        }
    }

    private fun setContent(startDestination: String) =
        setContent {
            SplashUnTheme {
                val isExpand = calculateWindowSizeClass(this).widthSizeClass.isExpand()
                SetupNavGraph(
                    navController = rememberNavController(),
                    startDestination = startDestination,
                    expand = isExpand
                )
            }
        }

    private fun showSplashScreen(splashScreen: SplashScreen, isLoading: Boolean) =
        splashScreen.setKeepOnScreenCondition {
            !isLoading
        }
}
