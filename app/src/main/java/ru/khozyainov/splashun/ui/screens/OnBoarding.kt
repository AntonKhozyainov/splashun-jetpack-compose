package ru.khozyainov.splashun.ui.screens

import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavigationDestination

object OnBoardingDestination: NavigationDestination {
    override val route: String = "onboarding"
    override val titleRes: Int = R.string.app_name
}

class OnBoarding {
}