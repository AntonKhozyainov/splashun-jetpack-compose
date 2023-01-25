package ru.khozyainov.splashun.ui.screens.onbording

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.khozyainov.splashun.R

sealed class OnBoardingPage(
    @DrawableRes val image: Int,
    @DrawableRes val imageDark: Int,
    @StringRes val title: Int
){
    object First: OnBoardingPage(
        image = R.drawable.ic_onboarding_image_1,
        imageDark = R.drawable.ic_onboarding_dark_image_1,
        title = R.string.onboarding_page_1
    )

    object Second: OnBoardingPage(
        image = R.drawable.ic_onboarding_image_2,
        imageDark = R.drawable.ic_onboarding_dark_image_2,
        title = R.string.onboarding_page_2
    )

    object Third: OnBoardingPage(
        image = R.drawable.ic_onboarding_image_3,
        imageDark = R.drawable.ic_onboarding_dark_image_3,
        title = R.string.onboarding_page_3
    )
}
