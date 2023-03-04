package ru.khozyainov.splashun.ui.screens.topappbar

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.home.PhotoDetailDestination
import ru.khozyainov.splashun.ui.screens.home.RibbonDestination
import ru.khozyainov.splashun.ui.theme.SplashUnTheme

@Composable
fun SplashunTopAppBar(
    navigationDestination: NavigationDestination,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onPressSearch: (String) -> Unit,
    viewModel: TopAppBarViewModel = hiltViewModel(),
    photoIdForShare: String? = null
) {

    val context = LocalContext.current
    val searchWidgetState = viewModel.searchWidgetState
    val searchTextState = viewModel.searchTextState

    if (canNavigateBack) {
        TopAppBarWithNavIcon(
            context = context,
            navigationDestination = navigationDestination,
            modifier = modifier,
            navigateUp = navigateUp,
            searchWidgetState = searchWidgetState,
            searchTextState = searchTextState,
            viewModel = viewModel,
            onPressSearch = onPressSearch,
            photoIdForShare = photoIdForShare
        )
    } else {
        TopAppBarWithoutNavIcon(
            context = context,
            navigationDestination = navigationDestination,
            modifier = modifier,
            searchWidgetState = searchWidgetState,
            searchTextState = searchTextState,
            viewModel = viewModel,
            onPressSearch = onPressSearch,
            photoIdForShare = photoIdForShare
        )
    }
}

@Composable
fun TopAppBarWithoutNavIcon(
    context: Context,
    navigationDestination: NavigationDestination,
    modifier: Modifier = Modifier,
    searchWidgetState: State<SearchWidgetState>,
    searchTextState: State<String>,
    viewModel: TopAppBarViewModel,
    onPressSearch: (String) -> Unit,
    photoIdForShare: String? = null
) {

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = navigationDestination.titleRes),
                style = MaterialTheme.typography.h1,
                color = Color.Black
            )
        },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            TopAppBarAction(
                navigationDestination = navigationDestination,
                modifier = modifier,
                searchWidgetState = searchWidgetState.value,
                searchTextState = searchTextState.value,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                    onPressSearch(String())
                },
                onSearchClicked = onPressSearch,
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                },
                onSharedPhoto = {
                    photoIdForShare?.let {
                        sharePhoto(
                            context = context,
                            photoId = it
                        )
                    }
                }
            )
        }
    )
}

@Composable
fun TopAppBarWithNavIcon(
    context: Context,
    navigationDestination: NavigationDestination,
    modifier: Modifier = Modifier,
    searchWidgetState: State<SearchWidgetState>,
    searchTextState: State<String>,
    viewModel: TopAppBarViewModel,
    onPressSearch: (String) -> Unit,
    navigateUp: () -> Unit,
    photoIdForShare: String? = null
) {

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = navigationDestination.titleRes),
                style = MaterialTheme.typography.h1,
                color = Color.Black
            )
        },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            TopAppBarAction(
                navigationDestination = navigationDestination,
                modifier = modifier,
                searchWidgetState = searchWidgetState.value,
                searchTextState = searchTextState.value,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                    onPressSearch(String())
                },
                onSearchClicked = onPressSearch,
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                },
                onSharedPhoto = {
                    photoIdForShare?.let {
                        sharePhoto(
                            context = context,
                            photoId = it
                        )
                    }
                }
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateUp,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                    tint = Color.Black
                )
            }
        }
    )
}

@Composable
fun TopAppBarAction(
    navigationDestination: NavigationDestination,
    searchWidgetState: SearchWidgetState,
    modifier: Modifier = Modifier,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onSharedPhoto: () -> Unit
) {
    when (navigationDestination) {
        is RibbonDestination -> {
            when (searchWidgetState) {
                SearchWidgetState.CLOSED -> {
                    DefaultHomeAppBar(
                        onSearchClicked = onSearchTriggered,
                        modifier = modifier
                    )
                }
                SearchWidgetState.OPENED -> {
                    SearchAppBar(
                        text = searchTextState,
                        onTextChange = onTextChange,
                        onCloseClicked = onCloseClicked,
                        onSearchClicked = onSearchClicked,
                        modifier = modifier
                    )
                }
            }
        }
        is PhotoDetailDestination ->{
            IconButton(
                modifier = modifier,
                onClick = {
                    onSharedPhoto()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(id = R.string.share_icon),
                    tint = Color.Black
                )
            }
        }
        else -> {}
    }
}

@Composable
fun DefaultHomeAppBar(
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = {
            onSearchClicked()
        },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_toolbar_menu_search),
            contentDescription = stringResource(id = R.string.search_icon),
            tint = Color.Black
        )
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colors.primary
    ) {
        TextField(modifier = modifier
            .fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = modifier
                        .alpha(ContentAlpha.medium),
                    text = stringResource(id = R.string.search),
                    color = Color.Black,
                    style = MaterialTheme.typography.body1
                )
            },
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_toolbar_menu_search),
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = Color.Black
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.search_close_icon),
                        tint = Color.Black
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.Black.copy(alpha = ContentAlpha.medium)
            ))
    }
}

@Preview
@Composable
fun DefaultHomeAppBarPreview() {
    SplashUnTheme() {
        DefaultHomeAppBar(
            onSearchClicked = {}
        )
    }
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SplashUnTheme() {
        SearchAppBar(
            text = "Search",
            onTextChange = {},
            onCloseClicked = {},
            onSearchClicked = {}
        )
    }
}

private fun sharePhoto(
    context: Context,
    photoId: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        //putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, "https://unsplash.com/photos/$photoId")
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.share_photo_link)
        )
    )
}