package ru.khozyainov.splashun.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class AppNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL
}


fun WindowWidthSizeClass.isExpand(): Boolean =
    when (this) {
        WindowWidthSizeClass.Expanded -> true
        WindowWidthSizeClass.Medium -> true
        else -> false
    }