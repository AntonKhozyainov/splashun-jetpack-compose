package ru.khozyainov.splashun.ui.screens.collections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.models.PhotoCollection
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.ExceptionScreen
import ru.khozyainov.splashun.ui.screens.LoadingScreen
import ru.khozyainov.splashun.utils.ImageLoadState

object CollectionsDestination : NavigationDestination {
    override val route: String = "collections_ribbon"
    override val titleRes: Int = R.string.collection
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    onScrollToTop: () -> Unit,
    scrollToTop: Boolean = false,
    displayWidthHeight: Pair<Int, Int>,
) {
    val collectionsViewModel: CollectionsViewModel = hiltViewModel()

    val uiState by collectionsViewModel.uiCollectionsState.collectAsState()
    val collectionsList = uiState.pagingCollectionsFlow.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val refreshing = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = {
            coroutineScope.launch {
                refreshing.value = true
                collectionsViewModel.refreshState()
                delay(500)
                refreshing.value = false
            }
        }
    )

    if (uiState.errorMessage.isNotBlank()) {
        ExceptionScreen(
            exceptionMessage = uiState.errorMessage
        ) {
            collectionsViewModel.refreshState()
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn(
                modifier = modifier
                    //.animateItemPlacement()
                    .fillMaxSize(),
                //verticalArrangement = Arrangement.spacedBy(4.dp),
                state = listState
            ) {
                if (!refreshing.value) {

                    items(
                        items = collectionsList,
                        key = { collecton ->
                            collecton.id
                        }
                    ) { collecton ->
                        CollectionCard(
                            modifier = modifier,
                            photoCollection = collecton,
                            displayWidthHeight = displayWidthHeight,
                            onClickCard = { photoCollectionId ->
                                //navController.navigate("${RibbonDestination.route}/$photoId")
                            }
                        )
                    }
                }

                if (collectionsList.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            if (scrollToTop) {
                LaunchedEffect(listState) {
                    listState.animateScrollToItem(index = 0)
                    onScrollToTop()
                }
            }

            PullRefreshIndicator(
                refreshing.value,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectionCard(
    photoCollection: PhotoCollection?,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
    onClickCard: (String) -> Unit,
){
    val context = LocalContext.current

    if (photoCollection == null) {
        Card(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.4f),
            elevation = 2.dp,
        ) {
            LoadingScreen()
        }
    } else {
        val width = displayWidthHeight.first
        val height = (photoCollection.height.toDouble() / photoCollection.width.toDouble() * width).toInt()

        Card(
            modifier = modifier
                .height(300.dp)
                .fillMaxWidth(),
            onClick = {
                onClickCard(photoCollection.id)
            }
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    val imageLoadState = remember {
                        mutableStateOf(ImageLoadState.LOADING)
                    }

                    AsyncImage(
                        model = ImageRequest.Builder(context = context)
                            .data(photoCollection.image + "&fm=pjpg&w=$width&h=$height&fit=clamp")
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(id = R.string.photo),
                        contentScale = ContentScale.Crop,
                        onLoading = {
                            imageLoadState.value = ImageLoadState.LOADING
                        },
                        onSuccess = {
                            imageLoadState.value = ImageLoadState.SUCCESS
                        },
                        onError = {
                            imageLoadState.value = ImageLoadState.ERROR
                        }
                    )

                    AnimatedVisibility(
                        visible = imageLoadState.value != ImageLoadState.SUCCESS
                    ) {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = if (imageLoadState.value == ImageLoadState.LOADING) {
                                    painterResource(id = R.drawable.ic_photo_placeholder)
                                } else {
                                    painterResource(id = R.drawable.ic_loading_error)
                                },
                                contentDescription = null
                            )
                        }
                    }
                }

                //TODO сделать норм разметку
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = context)
                                .data(photoCollection.author.image)
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(id = R.string.author_image),
                            contentScale = ContentScale.FillBounds,
                            placeholder = painterResource(id = R.drawable.ic_photo_placeholder),
                            error = painterResource(id = R.drawable.ic_loading_error),
                            modifier = modifier
                                .size(27.dp)
                                .clip(CircleShape)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = CircleShape,
                                    clip = true
                                )
                        )

                        Spacer(modifier = modifier.padding(2.dp))

                        Column(
                            modifier = modifier,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Text(
                                text = photoCollection.author.fullName,
                                style = MaterialTheme.typography.body1.copy(
                                    shadow = Shadow(
                                        color = DefaultShadowColor,
                                        offset = Offset(2f, 2f),
                                        blurRadius = 2f
                                    )
                                ),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = photoCollection.author.name,
                                style = MaterialTheme.typography.body2.copy(
                                    shadow = Shadow(
                                        color = DefaultShadowColor,
                                        offset = Offset(2f, 2f),
                                        blurRadius = 2f
                                    )
                                ),
                                color = Color.White
                            )

                        }
                    }

                }
            }
        }
    }
}