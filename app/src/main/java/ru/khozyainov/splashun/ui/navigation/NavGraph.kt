package ru.khozyainov.splashun.ui.navigation

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.khozyainov.splashun.ui.screens.onbording.OnBoardingDestination
import ru.khozyainov.splashun.ui.screens.onbording.OnBoardingScreen
import ru.khozyainov.splashun.ui.screens.home.HomeDestination
import ru.khozyainov.splashun.ui.screens.home.HomeScreen
import ru.khozyainov.splashun.ui.screens.login.LoginDestination
import ru.khozyainov.splashun.ui.screens.login.LoginScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    expand: Boolean = false
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = OnBoardingDestination.route) {
            OnBoardingScreen(
                modifier = modifier,
                navController = navController,
                expand = expand
            )
        }

        composable(route = LoginDestination.route) {
            LoginScreen(
                modifier = modifier,
                navController = navController,
                expand = expand
            )
        }

        composable(route = HomeDestination.route) {
            HomeScreen(modifier = modifier)
        }
    }

}