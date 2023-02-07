package ru.khozyainov.splashun.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavItem
import ru.khozyainov.splashun.ui.navigation.MainNavGraph
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.topappbar.SplashunTopAppBar
import ru.khozyainov.splashun.ui.theme.SplashUnTheme

object SplashunDestination : NavigationDestination {
    override val route: String = "splashun"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun SplashunApp(
    modifier: Modifier = Modifier,
    expand: Boolean = false
) {
    val navController = rememberNavController()

    val navItems = listOf(
        NavItem.Home,
        NavItem.Collections,
        NavItem.Profile
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val currentScreen = navItems.first { navItem ->
        navItem.screenRoute == (navBackStackEntry.value?.destination?.route
            ?: navItems.first().screenRoute)
    }

    val searchText = remember {
        mutableStateOf(String())
    }

    val scrollToTop = remember {
        mutableStateOf(false)
    }

    if (expand) {
        Scaffold(
            topBar = {
                SplashunTopAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    modifier = modifier,
                    onPressSearch = {
                        searchText.value = it
                    }
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
                    currentScreen = currentScreen,
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
                    }
                )
            }
        }
    } else {
        Scaffold(
            topBar = {
                SplashunTopAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    modifier = modifier,
                    onPressSearch = {
                        searchText.value = it
                    }
                )
            },
            bottomBar = {
                BottomNavigation(
                    navController = navController,
                    modifier = modifier,
                    items = navItems,
                    currentScreen = currentScreen,
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
                }
            )
        }
    }
}


@Composable
fun RailNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    currentScreen: NavItem,
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
                selected = currentScreen.screenRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                        scrollToTop(currentScreen.screenRoute == item.screenRoute)
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
    currentScreen: NavItem,
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
                selected = currentScreen.screenRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true

                        scrollToTop(currentScreen.screenRoute == item.screenRoute)
                    }
                }
            )
        }
    }
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