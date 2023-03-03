package ru.khozyainov.splashun.ui.screens.collections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.paging.compose.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.models.Author
import ru.khozyainov.splashun.ui.models.Photo
import ru.khozyainov.splashun.ui.models.PhotoCollection
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.ExceptionScreen
import ru.khozyainov.splashun.ui.screens.LoadingScreen
import ru.khozyainov.splashun.ui.screens.SplashUnImage
import ru.khozyainov.splashun.ui.screens.home.RibbonDestination
import ru.khozyainov.splashun.ui.theme.SplashUnTheme
import ru.khozyainov.splashun.utils.ConnectionState
import ru.khozyainov.splashun.utils.basicMarquee
import ru.khozyainov.splashun.utils.getLikeCountString

object CollectionDetailDestination : NavigationDestination {
    const val argName = "collectionId"
    override val route: String = "${CollectionsDestination.route}/{$argName}"
    override val titleRes: Int = R.string.collection_details
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollectionDetailScreen(
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
    connectionState: ConnectionState,
    navController: NavController,
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
                        TitleCollectionCard(
                            modifier = modifier,
                            collection = currentCollection,
                            displayWidthHeight = displayWidthHeight
                        )
                    }

                    items(
                        items = photoList,
                        key = { photo ->
                            photo.id
                        }
                    ) { photo ->
                        PhotoCardInCollection(
                            modifier = modifier,
                            photo = photo,
                            displayWidthHeight = displayWidthHeight,
                            connectionState = connectionState,
                            onClickLike = {
                                photo?.let {
                                    collectionDetailViewModel.setLike(photo)
                                }
                            },
                            onClickCard = { photoId ->
                                navController.navigate("${RibbonDestination.route}/$photoId")
                            }
                        )
                    }
                }

                if (photoList.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
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

@Composable
fun TitleCollectionCard(
    collection: PhotoCollection?,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>
) {

    val boxHeight = 120

    if (collection == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(boxHeight.dp)
        ) {
            LoadingScreen()
        }
    } else {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(boxHeight.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            SplashUnImage(
                modifier = modifier,
                imageWithSize = collection,
                displayWidthHeight = displayWidthHeight,
                context = LocalContext.current,
                contentScale = ContentScale.Crop,
                boxHeight = boxHeight * 2
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        Color.White.copy(
                            alpha = 0.8f
                        )
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = collection.title.uppercase(),
                        style = MaterialTheme.typography.h3,
                        color = Color.Black,
                        modifier = modifier
                            .padding(vertical = 4.dp, horizontal = 24.dp)
                    )

                    Text(
                        text = collection.description,
                        style = MaterialTheme.typography.h3,
                        color = Color.Black,
                        modifier = modifier
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                            .basicMarquee(),
                        maxLines = 1
                    )
                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = if (collection.photosCount == 1) stringResource(
                            id = R.string.collection_one_image,
                            collection.author.name
                        )
                        else stringResource(
                            id = R.string.collection_image_count,
                            collection.photosCount.toString(),
                            collection.author.name
                        ),
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        modifier = modifier
                            .basicMarquee(),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoCardInCollection(
    photo: Photo?,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
    onClickLike: () -> Unit,
    onClickCard: (String) -> Unit,
    connectionState: ConnectionState
){
    val context = LocalContext.current
    val boxHeight = 300

    if (photo == null) {
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
                .fillMaxWidth(),
            onClick = {
                onClickCard(photo.id)
            },
            shape = RectangleShape
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
                    SplashUnImage(
                        modifier = modifier,
                        imageWithSize = photo,
                        displayWidthHeight = displayWidthHeight,
                        context = context,
                        contentScale = ContentScale.FillBounds,
                        boxHeight = boxHeight
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
                                .data(photo.author.image)
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
                                text = photo.author.fullName,
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
                                text = photo.author.name,
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

                    Row(
                        modifier = if (connectionState == ConnectionState.Available) {
                            modifier
                                .clickable {
                                    onClickLike()
                                }
                        } else{
                            modifier
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = photo.likes.getLikeCountString(),
                            style = MaterialTheme.typography.body1.copy(
                                shadow = Shadow(
                                    color = DefaultShadowColor,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 2f
                                )
                            ),
                            color = Color.White
                        )

                        Spacer(modifier = modifier.padding(2.dp))

                        Image(
                            painter = painterResource(id = if (photo.like) R.drawable.ic_like else R.drawable.ic_like_empty),
                            contentDescription = stringResource(id = R.string.like_icon),
                            modifier = modifier
                                .size(16.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = CircleShape,
                                    clip = true
                                )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TitleCollectionCardPreview(){
    SplashUnTheme {
        TitleCollectionCard(
            collection = PhotoCollection(
                id = "1",
                description = "description",
                title = "title",
                photosCount = 123,
                height = 400,
                width = 400,
                image = "",
                author = Author(
                    name = "Ivanov",
                    fullName = "Ivanov Ivam",
                    image = "",
                    about = ""
                )
            ),
            displayWidthHeight = Pair(400, 400)
        )
    }
}
