package ru.khozyainov.splashun.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.MainNavGraph
import ru.khozyainov.splashun.ui.navigation.NavItem
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.home.PhotoDetailDestination
import ru.khozyainov.splashun.ui.screens.home.RibbonDestination
import ru.khozyainov.splashun.ui.screens.topappbar.SplashunTopAppBar
import ru.khozyainov.splashun.ui.theme.SplashUnTheme
import ru.khozyainov.splashun.utils.ConnectionState
import ru.khozyainov.splashun.utils.connectivityState


enum class SnackBarType {
    DOWNLOAD_PHOTO_SNACK_BAR,
    NO_NETWORK_SNACK_BAR
}

object SplashunDestination : NavigationDestination {
    override val route: String = "splashun"
    override val titleRes: Int = R.string.app_name
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SplashunApp(
    modifier: Modifier = Modifier,
    expand: Boolean = false,
    // splashUnAppViewModel: SplashUnAppViewModel = hiltViewModel()
) {

    //val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val navController = rememberNavController()

    val navItems = listOf(
        NavItem.Home,
        NavItem.Collections,
        NavItem.Profile
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val currentBottomNavRoute = getCurrentNavRoute(route = currentRoute)
    val currentDestination = getCurrentDestination(route = currentRoute)

    val searchText = remember {
        mutableStateOf(String())
    }

    val scrollToTop = remember {
        mutableStateOf(false)
    }

    val photoIdForShare: MutableState<String?> = remember {
        mutableStateOf(null)
    }

    val scaffoldState = rememberScaffoldState()

    val connection by connectivityState()
    val currentSnackBarData = scaffoldState.snackbarHostState.currentSnackbarData
    if (connection === ConnectionState.Available) {
        if (currentSnackBarData?.message == SnackBarType.NO_NETWORK_SNACK_BAR.name)
            currentSnackBarData.dismiss()
    } else {
        LaunchedEffect(key1 = null) {
            scaffoldState.snackbarHostState.showSnackbar(
                SnackBarType.NO_NETWORK_SNACK_BAR.name,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    val downloadedPhotoUri = remember {
        mutableStateOf(String())
    }

    val userNotifiedThatPhotoDownloaded = remember {
        mutableStateOf(false)
    }

    if (downloadedPhotoUri.value.isNotEmpty()) {
        userNotifiedThatPhotoDownloaded.value = false
        LaunchedEffect(key1 = downloadedPhotoUri.value) {
            scaffoldState.snackbarHostState.showSnackbar(
                SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR.name,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    if (expand) {
        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = {
                SplashUnSnackBarHost(
                    modifier = modifier,
                    snackBarHostState = it,
                    onClickSnackBar = { snackBarType ->
                        if (snackBarType == SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR) {
                            userNotifiedThatPhotoDownloaded.value = true
                            showDownloadedPhoto(
                                downloadedPhotoUri.value,
                                context
                            )
                            currentSnackBarData?.dismiss()
                        }
                    },
                    onClickCloseSnackBar = { snackBarType ->
                        if (snackBarType == SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR) {
                            userNotifiedThatPhotoDownloaded.value = true
                        }
                    }
                )
            },
            topBar = {
                SplashunTopAppBar(
                    navigationDestination = currentDestination,
                    canNavigateBack = canNavigateBack(currentDestination),
                    navigateUp = {
                        navController.navigateUp()
                    },
                    modifier = modifier,
                    onPressSearch = {
                        searchText.value = it
                    },
                    photoIdForShare = photoIdForShare.value
                )
            },
            modifier = modifier
        ) { innerPadding ->
            innerPadding.toString()//TODO remove it

            Row {
                RailNavigation(
                    navController = navController,
                    modifier = modifier,
                    items = navItems,
                    currentNavRoute = currentBottomNavRoute,
                    scrollToTop = {
                        scrollToTop.value = it
                    }
                )

                MainNavGraph(
                    navController = navController,
                    expand = true,
                    modifier = modifier,
                    searchText = searchText.value,
                    scrollToTop = scrollToTop.value,
                    onScrollToTop = {
                        scrollToTop.value = false
                    },
                    photoIdForShare = { photoId ->
                        photoIdForShare.value = photoId
                    },
                    photoDownloaded = {
                        downloadedPhotoUri.value = it
                    },
                    userNotifiedThatPhotoDownloaded = userNotifiedThatPhotoDownloaded.value
                )
            }
        }
    } else {
        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = {
                SplashUnSnackBarHost(
                    modifier = modifier,
                    snackBarHostState = it,
                    onClickSnackBar = { snackBarType ->
                        if (snackBarType == SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR) {
                            userNotifiedThatPhotoDownloaded.value = true
                            showDownloadedPhoto(
                                downloadedPhotoUri.value,
                                context
                            )
                            currentSnackBarData?.dismiss()
                        }
                    },
                    onClickCloseSnackBar = { snackBarType ->
                        if (snackBarType == SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR) {
                            userNotifiedThatPhotoDownloaded.value = true
                        }
                    }
                )
            },
            topBar = {
                SplashunTopAppBar(
                    navigationDestination = currentDestination,
                    canNavigateBack = canNavigateBack(currentDestination),
                    navigateUp = { navController.navigateUp() },
                    modifier = modifier,
                    onPressSearch = {
                        searchText.value = it
                    },
                    photoIdForShare = photoIdForShare.value
                )
            },
            bottomBar = {
                BottomNavigation(
                    navController = navController,
                    modifier = modifier,
                    items = navItems,
                    currentNavRoute = currentBottomNavRoute,
                    scrollToTop = {
                        scrollToTop.value = it
                    }
                )
            },
            modifier = modifier
        ) { innerPadding ->
            MainNavGraph(
                navController = navController,
                innerPadding = innerPadding,
                modifier = modifier,
                searchText = searchText.value,
                scrollToTop = scrollToTop.value,
                onScrollToTop = {
                    scrollToTop.value = false
                },
                photoIdForShare = { photoId ->
                    photoIdForShare.value = photoId
                },
                photoDownloaded = {
                    downloadedPhotoUri.value = it
                },
                userNotifiedThatPhotoDownloaded = userNotifiedThatPhotoDownloaded.value
            )
        }
    }
}

@Composable
fun SplashUnSnackBarHost(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onClickSnackBar: (SnackBarType) -> Unit,
    onClickCloseSnackBar: (SnackBarType) -> Unit,
) {
    SnackbarHost(snackBarHostState) { data ->

        when (val snackBarType = messageToSnackBarType(data.message)) {
            SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR -> {
                SplashUnDownloadPhotoSnackBar(
                    modifier = modifier,
                    snackBarHostState = snackBarHostState,
                    onClickCloseSnackBar = onClickCloseSnackBar,
                    snackBarType = snackBarType,
                    onClickSnackBar = onClickSnackBar
                )
            }
            SnackBarType.NO_NETWORK_SNACK_BAR -> {
                SplashUnNoNetworkSnackBar(
                    modifier = modifier,
                    snackBarHostState = snackBarHostState,
                    onClickCloseSnackBar = onClickCloseSnackBar,
                    snackBarType = snackBarType
                )
            }
        }
    }
}

@Composable
fun SplashUnNoNetworkSnackBar(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onClickCloseSnackBar: (SnackBarType) -> Unit,
    snackBarType: SnackBarType
) {
    Snackbar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background.copy(alpha = 0.8f),
        contentColor = MaterialTheme.colors.onBackground,
        action = {
            IconButton(onClick = {
                snackBarHostState.currentSnackbarData?.dismiss()
                onClickCloseSnackBar(snackBarType)
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close_icon),
                    tint = Color.Red
                )
            }
        }
    ) {
        Text(
            text = stringResource(id = R.string.no_internet_snack_bar),
            style = MaterialTheme.typography.h3,
            color = Color.Red
        )
    }
}

@Composable
fun SplashUnDownloadPhotoSnackBar(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onClickSnackBar: (SnackBarType) -> Unit,
    onClickCloseSnackBar: (SnackBarType) -> Unit,
    snackBarType: SnackBarType
) {
    Snackbar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background.copy(alpha = 0.8f),
        contentColor = MaterialTheme.colors.onBackground,
        action = {
            IconButton(onClick = {
                snackBarHostState.currentSnackbarData?.dismiss()
                onClickCloseSnackBar(snackBarType)
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close_icon),
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
    ) {
        Text(
            text = stringResource(id = R.string.photo_uploaded_snack_bar),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground,
            modifier = modifier
                .clickable {
                    onClickSnackBar(snackBarType)
                }
        )
    }
}


@Composable
fun RailNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    currentNavRoute: String,
    scrollToTop: (Boolean) -> Unit
) {
    NavigationRail(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.surface
    ) {
        items.forEach { item ->
            NavigationRailItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                selectedContentColor = MaterialTheme.colors.onBackground,
                unselectedContentColor = MaterialTheme.colors.onPrimary,
                selected = currentNavRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                        scrollToTop(currentNavRoute == item.screenRoute)
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    currentNavRoute: String,
    scrollToTop: (Boolean) -> Unit
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.surface
    ) {

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                selectedContentColor = MaterialTheme.colors.onBackground,
                unselectedContentColor = MaterialTheme.colors.onPrimary,
                selected = currentNavRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true

                        scrollToTop(currentNavRoute == item.screenRoute)
                    }
                }
            )
        }
    }
}

private fun getCurrentNavRoute(
    route: String?
): String {
    return when (route) {
        NavItem.Collections.screenRoute -> route
        NavItem.Profile.screenRoute -> route
        else -> NavItem.Home.screenRoute
    }
}

private fun getCurrentDestination(
    route: String?
): NavigationDestination {
    return when (route) {
        //NavItem.Collections.screenRoute -> route
        PhotoDetailDestination.route -> PhotoDetailDestination
        else -> RibbonDestination
    }
}

private fun canNavigateBack(currentDestination: NavigationDestination): Boolean =
    when (currentDestination) {
        is PhotoDetailDestination -> true
        else -> false
    }

private fun messageToSnackBarType(message: String): SnackBarType =
    when (message) {
        SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR.name -> SnackBarType.DOWNLOAD_PHOTO_SNACK_BAR
        SnackBarType.NO_NETWORK_SNACK_BAR.name -> SnackBarType.NO_NETWORK_SNACK_BAR
        else -> throw Exception("Недопустимое значение message = $message")
    }

private fun showDownloadedPhoto(photoUri: String, context: Context){

    val localUri = photoUri.toUri()
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(localUri, context.contentResolver.getType(localUri))
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun SplashunAppPreview() {
    SplashUnTheme(darkTheme = false) {
        SplashunApp()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashunAppDarkThemePreview() {
    SplashUnTheme(darkTheme = true) {
        SplashunApp()
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun SplashunAppExpandedPreview() {
    SplashUnTheme {
        SplashunApp(
            expand = true
        )
    }
}
