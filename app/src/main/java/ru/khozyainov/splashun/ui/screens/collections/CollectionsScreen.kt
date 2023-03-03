package ru.khozyainov.splashun.ui.screens.collections

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
import ru.khozyainov.splashun.ui.screens.SplashUnImage
import ru.khozyainov.splashun.utils.ImageLoadState
import ru.khozyainov.splashun.utils.basicMarquee

object CollectionsDestination : NavigationDestination {
    override val route: String = "collections"
    override val titleRes: Int = R.string.collections
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectionsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onScrollToTop: () -> Unit,
    scrollToTop: Boolean = false,
    displayWidthHeight: Pair<Int, Int>,
    onClickCollection: (collectionTitle: String) -> Unit
) {
    val collectionsViewModel: CollectionsViewModel = hiltViewModel()

    val uiState by collectionsViewModel.uiCollectionsState.collectAsStateWithLifecycle()
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
                        key = { collection ->
                            collection.id
                        }
                    ) { collection ->
                        CollectionCard(
                            modifier = modifier,
                            photoCollection = collection,
                            displayWidthHeight = displayWidthHeight,
                            onClickCard = { photoCollectionId ->
                                onClickCollection(collection?.title ?: photoCollectionId)
                                navController.navigate("${CollectionsDestination.route}/$photoCollectionId")
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
) {
    val context = LocalContext.current
    val boxHeight = 200
    if (photoCollection == null) {
        Card(
            modifier = modifier
                .height(boxHeight.dp)
                .fillMaxWidth()
        ) {
            LoadingScreen()
        }
    } else {

        Card(
            modifier = modifier
                .height(boxHeight.dp)
                .fillMaxWidth(),
            onClick = {
                onClickCard(photoCollection.id)
            },
            shape = RectangleShape
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    SplashUnImage(
                        modifier = modifier,
                        imageWithSize = photoCollection,
                        displayWidthHeight = displayWidthHeight,
                        context = context,
                        contentScale = ContentScale.Crop,
                        boxHeight = boxHeight
                    )
                }

                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Text(
                            text = if (photoCollection.photosCount == 1) stringResource(id = R.string.collection_one_photo)
                                    else stringResource(id = R.string.collection_photo_count, photoCollection.photosCount.toString()),
                            style = MaterialTheme.typography.h2.copy(
                                shadow = Shadow(
                                    color = DefaultShadowColor,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 2f
                                )
                            ),
                            color = Color.White,

                        )

                        Text(
                            modifier = modifier
                                .basicMarquee(),
                            text = photoCollection.title,
                            style = MaterialTheme.typography.h1.copy(
                                shadow = Shadow(
                                    color = DefaultShadowColor,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 2f
                                )
                            ),
                            fontSize = 56.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

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
}