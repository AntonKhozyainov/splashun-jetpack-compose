package ru.khozyainov.splashun.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.khozyainov.splashun.R

sealed class NavItem(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
    val screenRoute: String
) {

    object Home : NavItem(
        titleRes = R.string.home,
        iconRes = R.drawable.ic_bottom_nav_menu_home,
        screenRoute = "home"
    )

    object Collections : NavItem(
        titleRes = R.string.collections,
        iconRes = R.drawable.ic_bottom_nav_menu_collections,
        screenRoute = "collections"
    )

    object Profile : NavItem(
        titleRes = R.string.profile,
        iconRes = R.drawable.ic_bottom_nav_menu_profile,
        screenRoute = "profile"
    )
}