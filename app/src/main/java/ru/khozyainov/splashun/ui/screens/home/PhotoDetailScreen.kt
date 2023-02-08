package ru.khozyainov.splashun.ui.screens.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavigationDestination

object PhotoDetailDestination : NavigationDestination {
    const val argName = "photoId"
    override val route: String = "${RibbonDestination.route}/{$argName}"
    override val titleRes: Int = R.string.photo

}

@Composable
fun PhotoDetailScreen(
    photoId: String?,
    modifier: Modifier = Modifier,
){
    Text(text = "PhotoDetailScreen = $photoId") //TODO
}