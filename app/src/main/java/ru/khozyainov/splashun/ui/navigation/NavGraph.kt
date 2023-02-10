package ru.khozyainov.splashun.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.khozyainov.splashun.ui.screens.SplashunApp
import ru.khozyainov.splashun.ui.screens.SplashunDestination
import ru.khozyainov.splashun.ui.screens.onbording.OnBoardingDestination
import ru.khozyainov.splashun.ui.screens.onbording.OnBoardingScreen
import ru.khozyainov.splashun.ui.screens.home.HomeScreen
import ru.khozyainov.splashun.ui.screens.home.PhotoDetailDestination
import ru.khozyainov.splashun.ui.screens.home.PhotoDetailScreen
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

        composable(route = SplashunDestination.route) {
            SplashunApp(
                modifier = modifier,
                //navController = navController,
                expand = expand
            )
        }
    }

}

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    searchText: String = String(),
    expand: Boolean = false,
    scrollToTop: Boolean = false,
    onScrollToTop: () -> Unit
) {

    //TODO remember?
    val displayWidthHeight =
        LocalContext.current.resources.displayMetrics.widthPixels to LocalContext.current.resources.displayMetrics.heightPixels

    NavHost(
        navController = navController,
        startDestination = NavItem.Home.screenRoute,
        modifier = modifier.padding(innerPadding)
    ) {

        homeGraph(
            navController = navController,
            searchText = searchText,
            modifier = modifier,
            expand = expand,
            displayWidthHeight = displayWidthHeight,
            scrollToTop = scrollToTop,
            onScrollToTop = onScrollToTop
        )

        composable(
            route = NavItem.Collections.screenRoute
        ) {
            //FriendsList(navController)
        }

        composable(
            route = NavItem.Profile.screenRoute
        ) {
            //FriendsList(navController)
        }
    }

}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    searchText: String = String(),
    expand: Boolean = false,
    scrollToTop: Boolean = false,
    onScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>
) {
    composable(
        route = NavItem.Home.screenRoute
    ) {
        HomeScreen(
            navController = navController,
            searchText = searchText,
            modifier = modifier,
            expand = expand,
            displayWidthHeight = displayWidthHeight,
            scrollToTop = scrollToTop,
            onScrollToTop = onScrollToTop
        )
    }

    composable(
        route = PhotoDetailDestination.route,
        arguments = listOf(navArgument(PhotoDetailDestination.argName) { type = NavType.StringType })
    ) { backStackEntry ->
        val photoId = backStackEntry.arguments?.getString(PhotoDetailDestination.argName)
        PhotoDetailScreen(
            photoId = photoId,
            modifier = modifier,
            displayWidthHeight = displayWidthHeight
        )
    }
}