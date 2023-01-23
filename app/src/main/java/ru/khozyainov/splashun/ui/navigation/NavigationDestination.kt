package ru.khozyainov.splashun.ui.navigation

import androidx.annotation.StringRes

interface NavigationDestination {
    val route: String
    val titleRes: Int
}