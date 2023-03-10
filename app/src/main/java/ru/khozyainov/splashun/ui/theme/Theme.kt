package ru.khozyainov.splashun.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(

    background = theme_dark_background,
    onBackground = White,

    primary = theme_top_app_bar,
    onPrimary = Black,

    surface = White,
    secondary = White,

    primaryVariant = Black
    //surface = Black,
    //onSurface = White,


//    onSecondary = Black,
    //primaryVariant = Red,//TODO
)

private val LightColorPalette = lightColors(

    background = theme_light_background,
    onBackground = Black,

    primary = theme_top_app_bar,
    onPrimary = White,

    surface = theme_light_button_disabled,
    secondary = Black,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)


@Composable
fun SplashUnTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    //val systemUiController = rememberSystemUiController()
    rememberSystemUiController().setStatusBarColor(
        color = theme_top_app_bar
    )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}