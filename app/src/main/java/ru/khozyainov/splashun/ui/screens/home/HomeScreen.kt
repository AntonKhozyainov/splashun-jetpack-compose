package ru.khozyainov.splashun.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
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
import ru.khozyainov.splashun.ui.models.Photo
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.ExceptionScreen
import ru.khozyainov.splashun.ui.screens.LoadingScreen
import ru.khozyainov.splashun.ui.screens.SplashUnImage
import ru.khozyainov.splashun.utils.ConnectionState
import ru.khozyainov.splashun.utils.ImageLoadState
import ru.khozyainov.splashun.utils.getLikeCountString

object RibbonDestination : NavigationDestination {
    override val route: String = "ribbon"
    override val titleRes: Int = R.string.ribbon
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    searchText: String = String(),
    expand: Boolean = false,
    displayWidthHeight: Pair<Int, Int>,
    onScrollToTop: () -> Unit,
    scrollToTop: Boolean = false,
    connectionState: ConnectionState
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    homeViewModel.setSearchBy(searchText)

    val uiState by homeViewModel.uiHomeState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val photoList = uiState.pagingPhotoFlow.collectAsLazyPagingItems()

    val refreshing = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = {
            coroutineScope.launch {
                refreshing.value = true
                homeViewModel.refresh()
                delay(500)
                refreshing.value = false
            }
        }
    )

    if (uiState.errorMessage.isNotBlank()) {
        ExceptionScreen(
            exceptionMessage = uiState.errorMessage
        ) {
            homeViewModel.refreshState()
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
                contentPadding = PaddingValues(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                state = listState

            ) {
                if (!refreshing.value) {

                    item {
                        PhotoListTitle(
                            modifier = modifier,
                            searchText = searchText
                        )
                    }

                    items(
                        items = photoList,
                        key = { photo ->
                            photo.id
                        }
                    ) { photo ->
                        PhotoCard(
                            modifier = modifier,
                            photo = photo,
                            displayWidthHeight = displayWidthHeight,
                            connectionState = connectionState,
                            onClickLike = {
                                photo?.let {
                                    homeViewModel.setLike(photo)
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

@Composable
fun PhotoListTitle(
    modifier: Modifier = Modifier,
    searchText: String = String(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        if (searchText.isBlank()) {
            Text(
                text = stringResource(id = R.string.photo_list_title),
                style = MaterialTheme.typography.h1,
                fontWeight = Bold
            )

            Text(
                text = stringResource(id = R.string.photo_list_title_2),
                style = MaterialTheme.typography.body1,
                fontWeight = Bold
            )
        } else {
            Text(
                text = stringResource(id = R.string.photo_list_title_search),
                style = MaterialTheme.typography.h1,
                fontWeight = Bold
            )

            Text(
                text = stringResource(id = R.string.photo_list_title_search_2, searchText),
                style = MaterialTheme.typography.body1,
                fontWeight = Bold
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotoCard(
    photo: Photo?,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
    onClickLike: () -> Unit,
    onClickCard: (String) -> Unit,
    connectionState: ConnectionState
) {
    val context = LocalContext.current
    val boxHeight = 300

    if (photo == null) {
        Card(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(boxHeight.dp),
            elevation = 2.dp,
        ) {
            LoadingScreen()
        }
    } else {

        Card(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            elevation = 2.dp,
            onClick = {
                onClickCard(photo.id)
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
                                fontWeight = Bold
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


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    SplashUnTheme(darkTheme = false) {
//        HomeScreen()
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun PhotoCardAppPreview() {
//    SplashUnTheme(darkTheme = false) {
//        PhotoCard(
//            photo = Photo(
//                id = "1",
//                width = 100,
//                height = Random.nextInt(100, 200),
//                image = "",
//                like = Random.nextBoolean(),
//                likes = Random.nextLong(0, 1000),
//                author = Author(
//                    name = "Ivan",
//                    fullName = "Ivanov Ivan",
//                    image = "",
//                    about = "about"
//                )
//            )
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PhotoCardDarkThemePreview() {
//    SplashUnTheme(darkTheme = true) {
//        PhotoCard(
//            photo = Photo(
//                id = "1",
//                width = 100,
//                height = Random.nextInt(100, 200),
//                image = "",
//                like = Random.nextBoolean(),
//                likes = Random.nextLong(0, 1000),
//                author = Author(
//                    name = "Ivan",
//                    fullName = "Ivanov Ivan",
//                    image = "",
//                    about = "about"
//                )
//            )
//        )
//
//    }
//}
//
//@Preview(showBackground = true, widthDp = 1000)
//@Composable
//fun PhotoCardExpandedPreview() {
//    SplashUnTheme {
//        PhotoCard(
//            photo = Photo(
//                id = "1",
//                width = 100,
//                height = Random.nextInt(100, 200),
//                image = "",
//                like = Random.nextBoolean(),
//                likes = Random.nextLong(0, 1000),
//                author = Author(
//                    name = "Ivan",
//                    fullName = "Ivanov Ivan",
//                    image = "",
//                    about = "about"
//                )
//            )
//        )
//    }
//}
