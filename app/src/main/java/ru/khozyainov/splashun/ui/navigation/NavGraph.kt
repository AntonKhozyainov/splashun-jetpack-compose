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
import ru.khozyainov.splashun.ui.screens.collections.CollectionDetailDestination
import ru.khozyainov.splashun.ui.screens.collections.CollectionDetailScreen
import ru.khozyainov.splashun.ui.screens.collections.CollectionsScreen
import ru.khozyainov.splashun.ui.screens.onbording.OnBoardingDestination
import ru.khozyainov.splashun.ui.screens.onbording.OnBoardingScreen
import ru.khozyainov.splashun.ui.screens.home.HomeScreen
import ru.khozyainov.splashun.ui.screens.home.PhotoDetailDestination
import ru.khozyainov.splashun.ui.screens.home.PhotoDetailScreen
import ru.khozyainov.splashun.ui.screens.login.LoginDestination
import ru.khozyainov.splashun.ui.screens.login.LoginScreen
import ru.khozyainov.splashun.utils.ConnectionState

private const val DEEP_LINK_URI = "https://unsplash.com/photos"

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
    onScrollToTop: () -> Unit,
    photoIdForShare: (String?) -> Unit,
    photoDownloaded: (String) -> Unit,
    userNotifiedThatPhotoDownloaded: Boolean = false,
    connectionState: ConnectionState
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
            onScrollToTop = onScrollToTop,
            photoIdForShare = photoIdForShare,
            photoDownloaded = photoDownloaded,
            userNotifiedThatPhotoDownloaded = userNotifiedThatPhotoDownloaded,
            connectionState = connectionState
        )

        collectionsGraph(
            navController = navController,
            modifier = modifier,
            expand = expand,
            displayWidthHeight = displayWidthHeight,
            scrollToTop = scrollToTop,
            onScrollToTop = onScrollToTop,
            photoIdForShare = photoIdForShare,
            photoDownloaded = photoDownloaded,
            userNotifiedThatPhotoDownloaded = userNotifiedThatPhotoDownloaded,
            connectionState = connectionState
        )

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
    displayWidthHeight: Pair<Int, Int>,
    photoIdForShare: (String?) -> Unit,
    photoDownloaded: (String) -> Unit,
    userNotifiedThatPhotoDownloaded: Boolean = false,
    connectionState: ConnectionState
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
            onScrollToTop = onScrollToTop,
            connectionState = connectionState
        )
    }

    composable(
        route = PhotoDetailDestination.route,
        arguments = listOf(navArgument(PhotoDetailDestination.argName) { type = NavType.StringType }),
        deepLinks = listOf(navDeepLink { uriPattern = "$DEEP_LINK_URI/{${PhotoDetailDestination.argName}}" })
    ) { backStackEntry ->
        val photoId = backStackEntry.arguments?.getString(PhotoDetailDestination.argName)
        photoIdForShare(photoId)
        PhotoDetailScreen(
            photoId = photoId,
            modifier = modifier,
            displayWidthHeight = displayWidthHeight,
            photoDownloaded = photoDownloaded,
            userNotifiedThatPhotoDownloaded = userNotifiedThatPhotoDownloaded
        )
    }
}

fun NavGraphBuilder.collectionsGraph(
    navController: NavController,
    expand: Boolean = false,
    scrollToTop: Boolean = false,
    onScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
    photoIdForShare: (String?) -> Unit,
    photoDownloaded: (String) -> Unit,
    userNotifiedThatPhotoDownloaded: Boolean = false,
    connectionState: ConnectionState
) {
    composable(
        route = NavItem.Collections.screenRoute
    ) {
        CollectionsScreen(
            modifier = modifier,
            scrollToTop = scrollToTop,
            onScrollToTop = onScrollToTop,
            displayWidthHeight = displayWidthHeight
        )
    }

    composable(
        route = CollectionDetailDestination.route,
        arguments = listOf(navArgument(CollectionDetailDestination.argName) { type = NavType.StringType })
    ) { backStackEntry ->
        val collectionId = backStackEntry.arguments?.getString(CollectionDetailDestination.argName)
        CollectionDetailScreen(
            modifier = modifier,
//            displayWidthHeight = displayWidthHeight,
//            photoDownloaded = photoDownloaded,
//            userNotifiedThatPhotoDownloaded = userNotifiedThatPhotoDownloaded
        )
    }

}