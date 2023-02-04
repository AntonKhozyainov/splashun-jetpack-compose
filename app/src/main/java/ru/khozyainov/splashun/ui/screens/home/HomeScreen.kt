package ru.khozyainov.splashun.ui.screens.home

import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import ru.khozyainov.splashun.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.khozyainov.splashun.ui.models.Author
import ru.khozyainov.splashun.ui.models.Photo
import ru.khozyainov.splashun.ui.screens.LoadingScreen
import ru.khozyainov.splashun.ui.theme.SplashUnTheme
import kotlin.random.Random


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    searchText: String = String(),
    expand: Boolean = false
) {

    val homeViewModel: HomeViewModel = hiltViewModel()

    homeViewModel.setSearchBy(searchText)

    val photoList = homeViewModel.photoFlow.collectAsLazyPagingItems()
    //val photoList = getTestList()

    val refreshing = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing.value,
        onRefresh = {
            homeViewModel.refresh()
        }
    )

    if (photoList.loadState.refresh == LoadState.Loading) {
        LoadingScreen()
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyVerticalStaggeredGrid(
                modifier = modifier
                    //.animateItemPlacement()
                    .fillMaxSize(),
                columns = StaggeredGridCells.Fixed(count = if (expand) 3 else 2),
                contentPadding = PaddingValues(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),

                ) {


                if (!refreshing.value) {
                    items(
                        items = photoList.itemSnapshotList,
                        key = { photo ->
                            photo?.id ?: throw Exception("TODO")//TODO
                        }
                    ) { photo ->
                        PhotoCard(
                            modifier = modifier,
                            photo = photo ?: throw Exception("TODO")//TODO
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


            // Animate scroll to the first item  //TODO
//            onClick = {
//                coroutineScope.launch {
//
//                    listState.animateScrollToItem(index = 0)
//                }
//            }


            PullRefreshIndicator(
                refreshing.value,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun PhotoCard(
    photo: Photo,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .size(width = photo.width.dp, height = photo.height.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = context)
                        .data(photo.image)
                        //.data("photo.image")
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.photo),
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(id = R.drawable.ic_photo_placeholder),
                    error = painterResource(id = R.drawable.ic_loading_error)
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
                    )

                    Spacer(modifier = modifier.padding(2.dp))

                    Column(
                        modifier = modifier,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Text(
                            text = photo.author.fullName,
                            style = MaterialTheme.typography.body1,
                            color = Color.White,
                            fontWeight = Bold
                        )

                        Text(
                            text = photo.author.name,
                            style = MaterialTheme.typography.body2,
                            color = Color.White
                        )

                    }
                }

                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = photo.likes.toString(),
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )

                    Spacer(modifier = modifier.padding(2.dp))

                    Image(
                        painter = painterResource(id = if (photo.like) R.drawable.ic_like else R.drawable.ic_like_empty),
                        contentDescription = stringResource(id = R.string.like_icon),
                        modifier = modifier
                            .size(14.dp)
                    )
                }

            }

        }

    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SplashUnTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoCardAppPreview() {
    SplashUnTheme(darkTheme = false) {
        PhotoCard(
            photo = Photo(
                id = "1",
                width = 100,
                height = Random.nextInt(100, 200),
                image = "",
                like = Random.nextBoolean(),
                likes = Random.nextLong(0, 1000),
                author = Author(
                    name = "Ivan",
                    fullName = "Ivanov Ivan",
                    image = "",
                    about = "about"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoCardDarkThemePreview() {
    SplashUnTheme(darkTheme = true) {
        PhotoCard(
            photo = Photo(
                id = "1",
                width = 100,
                height = Random.nextInt(100, 200),
                image = "",
                like = Random.nextBoolean(),
                likes = Random.nextLong(0, 1000),
                author = Author(
                    name = "Ivan",
                    fullName = "Ivanov Ivan",
                    image = "",
                    about = "about"
                )
            )
        )

    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun PhotoCardExpandedPreview() {
    SplashUnTheme {
        PhotoCard(
            photo = Photo(
                id = "1",
                width = 100,
                height = Random.nextInt(100, 200),
                image = "",
                like = Random.nextBoolean(),
                likes = Random.nextLong(0, 1000),
                author = Author(
                    name = "Ivan",
                    fullName = "Ivanov Ivan",
                    image = "",
                    about = "about"
                )
            )
        )
    }
}

//private fun getTestList(): List<Photo> {
//    val photoList = mutableListOf<Photo>()
//
//    val listImages = listOf(
//        "https://img1.akspic.ru/attachments/crops/2/2/4/0/50422/50422-senokosnoye_ugodye-pole-selskoe_hozyajstvo-zakat-risovoe_pole-2560x1440.jpg",
//        "https://postila.ru/data/84/8a/b3/26/848ab326ece3525ee7e62518ed393c3713a4e0cab497c39336489a5b1c93b4a7.jpg",
//        "https://mobimg.b-cdn.net/v3/fetch/a1/a18e2d8887219fc023833334f58c45fc.jpeg",
//        "https://ic.pics.livejournal.com/brandexpert/21141127/4813/4813_original.jpg",
//    )
//    repeat(20) {
//        photoList.add(
//            Photo(
//                id = it.toString(),
//                width = 100,
//                height = Random.nextInt(100, 200),
//                image = listImages.random(),
//                like = Random.nextBoolean(),
//                likes = Random.nextLong(0, 1000),
//                author = Author(
//                    name = "Ivan",
//                    fullName = "Ivanov Ivan",
//                    image = "https://www.joelsartore.com/wp-content/uploads/stock/HIS011/HIS011-00161.jpg",
//                    about = "about"
//                )
//            )
//        )
//    }
//
//    return photoList
//}