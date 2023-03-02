package ru.khozyainov.splashun.ui.screens.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.models.PhotoCollection
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.ExceptionScreen
import ru.khozyainov.splashun.ui.screens.LoadingScreen
import ru.khozyainov.splashun.ui.screens.SplashUnImage
import ru.khozyainov.splashun.ui.screens.home.PhotoCard
import ru.khozyainov.splashun.ui.screens.home.PhotoListTitle
import ru.khozyainov.splashun.ui.screens.home.RibbonDestination

object CollectionDetailDestination : NavigationDestination {
    const val argName = "collectionId"
    override val route: String = "${CollectionsDestination.route}/{$argName}"
    override val titleRes: Int = R.string.ribbon
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectionDetailScreen(
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
) {

    val collectionDetailViewModel: CollectionDetailViewModel = hiltViewModel()

    val uiState by collectionDetailViewModel.uiCollectionDetailState.collectAsStateWithLifecycle()
    val currentCollection = uiState.photoCollection
    val photoList = uiState.pagingPhotoFlow.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val refreshing = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = {
            coroutineScope.launch {
                refreshing.value = true
                collectionDetailViewModel.refreshState()
                delay(500)
                refreshing.value = false
            }
        }
    )

    if (uiState.errorMessage.isNotBlank()) {
        ExceptionScreen(
            exceptionMessage = uiState.errorMessage
        ) {
            collectionDetailViewModel.refreshState()
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                state = listState

            ) {
                if (!refreshing.value) {

                    item {
                        CollectionCard(
                            modifier = modifier,
                            collection = currentCollection,
                            displayWidthHeight = displayWidthHeight
                        )
                    }

//                    items(
//                        items = photoList,
//                        key = { photo ->
//                            photo.id
//                        }
//                    ) { photo ->
//                        PhotoCard(
//                            modifier = modifier,
//                            photo = photo,
//                            displayWidthHeight = displayWidthHeight,
//                            connectionState = connectionState,
//                            onClickLike = {
//                                photo?.let {
//                                    homeViewModel.setLike(photo)
//                                }
//                            },
//                            onClickCard = { photoId ->
//                                navController.navigate("${RibbonDestination.route}/$photoId")
//                            }
//                        )
//                    }
                }
            }
        }
    }
}

@Composable
fun CollectionCard(
    collection: PhotoCollection?,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>
) {

    if (collection == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            LoadingScreen()
        }
    } else {
        val width = displayWidthHeight.first
        val height = (collection.height.toDouble() / collection.width.toDouble() * width).toInt()

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            SplashUnImage(
                modifier = modifier,
                image = collection.image,
                height = height,
                width = width,
                context = LocalContext.current,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp, horizontal = 12.dp)
                    .background(
                        Color.White.copy(
                            alpha = 0.5f
                        )
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = collection.title.uppercase(),
                    style = MaterialTheme.typography.h3,
                    color = Color.Black,
                    modifier = modifier
                        .padding()
                )

            }
        }


    }

}
