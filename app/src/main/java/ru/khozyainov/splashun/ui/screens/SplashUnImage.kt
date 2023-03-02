package ru.khozyainov.splashun.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.utils.ImageLoadState

@Composable
fun SplashUnImage(
    image: String,
    width: Int,
    height: Int,
    context: Context,
    modifier: Modifier = Modifier,
    contentScale: ContentScale,
) {
    val imageLoadState = remember {
        mutableStateOf(ImageLoadState.LOADING)
    }

    AsyncImage(
        model = ImageRequest.Builder(context = context)
            .data(buildString {
                append(image)
                append("&fm=pjpg&w=$width&h=$height&fit=clamp")
            })
            .crossfade(true)
            .build(),
        contentDescription = stringResource(id = R.string.photo),
        contentScale = contentScale,
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

    if (imageLoadState.value != ImageLoadState.SUCCESS) {
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