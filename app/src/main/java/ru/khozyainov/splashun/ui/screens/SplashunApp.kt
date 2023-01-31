package ru.khozyainov.splashun.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavItem
import ru.khozyainov.splashun.ui.navigation.MainNavGraph
import ru.khozyainov.splashun.ui.navigation.NavigationDestination

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

    if (expand) {
        Scaffold(
            modifier = modifier
        ) { innerPadding ->
            innerPadding.toString()//TODO remove it

            Row {
                RailNavigation(
                    navController = navController,
                    modifier = modifier,
                    items = navItems
                )

                MainNavGraph(
                    navController = navController,
                    expand = true,
                    modifier = modifier
                )
            }
        }
    } else {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    navController = navController,
                    modifier = modifier,
                    items = navItems
                )
            },
            modifier = modifier
        ) { innerPadding ->
            MainNavGraph(
                navController = navController,
                innerPadding = innerPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun RailNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    items: List<NavItem>
) {

    NavigationRail(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {

        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route
        items.forEach { item ->
            NavigationRailItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.secondary,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
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
    items: List<NavItem>
) {

    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.secondary,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}