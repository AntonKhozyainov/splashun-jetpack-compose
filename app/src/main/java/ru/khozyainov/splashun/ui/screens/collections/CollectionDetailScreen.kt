package ru.khozyainov.splashun.ui.screens.collections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavigationDestination

object CollectionDetailDestination : NavigationDestination {
    const val argName = "collectionId"
    override val route: String = "${CollectionsDestination.route}/{$argName}"
    override val titleRes: Int = R.string.ribbon
}

@Composable
fun CollectionDetailScreen(
    modifier: Modifier = Modifier,
){

    //TODO
    //photoDetailViewModel.getPhotoById(photoId)

}
