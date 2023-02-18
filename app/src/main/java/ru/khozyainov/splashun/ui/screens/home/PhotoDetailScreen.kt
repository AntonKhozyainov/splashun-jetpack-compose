package ru.khozyainov.splashun.ui.screens.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.ui.models.PhotoDetail
import ru.khozyainov.splashun.ui.navigation.NavigationDestination
import ru.khozyainov.splashun.ui.screens.ExceptionScreen
import ru.khozyainov.splashun.ui.screens.LoadingScreen
import ru.khozyainov.splashun.utils.underLine
import androidx.compose.runtime.getValue
import ru.khozyainov.splashun.utils.getLikeCountString

object PhotoDetailDestination : NavigationDestination {
    const val argName = "photoId"
    override val route: String = "${RibbonDestination.route}/{$argName}"
    override val titleRes: Int = R.string.photo
}

@Composable
fun PhotoDetailScreen(
    photoId: String?,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
    photoDetailViewModel: PhotoDetailViewModel = hiltViewModel(),
    photoDownloaded: (String) -> Unit,
    userNotifiedThatPhotoDownloaded: Boolean = false
) {

    photoDetailViewModel.getPhotoById(photoId)
    val uiState by photoDetailViewModel.uiPhotoDetailState.collectAsState()

    if (userNotifiedThatPhotoDownloaded){
        photoDetailViewModel.cancelWork()
    }

    if (uiState.downloadPhotoUri.isNotEmpty() && !userNotifiedThatPhotoDownloaded){
        photoDownloaded(uiState.downloadPhotoUri)
        //Toast.makeText(LocalContext.current, uiState.downloadPhotoLink, Toast.LENGTH_LONG).show()
    }

    val photo = uiState.photoDetail

    if (uiState.errorMessage.isNotBlank()) {
        ExceptionScreen(
            exceptionMessage = uiState.errorMessage
        ) {
            photoDetailViewModel.refreshState()
        }
    } else {
        if (photo == null) {
            LoadingScreen()
        } else {

            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PhotoCardDetail(
                    photo = photo,
                    like = uiState.like,
                    likes = uiState.likes,
                    modifier = modifier,
                    displayWidthHeight = displayWidthHeight,
                    onClickLike = {
                        photoDetailViewModel.setLike()
                    }
                )

                PhotoLocation(
                    photo = photo,
                    modifier = modifier
                )

                PhotoTags(
                    photo = photo,
                    modifier = modifier,
                )

                PhotoMadeAbout(
                    photo = photo,
                    modifier = modifier,
                )

                PhotoDownload(
                    photo = photo,
                    modifier = modifier,
                    onClickDownload = {
                        photoDetailViewModel.downloadPhoto()
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoDownload(
    photo: PhotoDetail,
    modifier: Modifier = Modifier,
    onClickDownload: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = modifier
                .padding(vertical = 4.dp, horizontal = 18.dp)
                .clickable {
                    onClickDownload()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    id = R.string.downloads,
                    photo.downloads.toString()
                ).underLine(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground
            )

            Spacer(modifier = modifier.padding(4.dp))

            Icon(
                modifier = modifier.size(22.dp),
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = stringResource(id = R.string.download_icon),
                tint = MaterialTheme.colors.onBackground,
            )
        }
    }
}

@Composable
fun PhotoMadeAbout(
    photo: PhotoDetail,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 18.dp)
            .fillMaxWidth()
    ) {

        val exif = photo.exif

        Text(
            modifier = modifier
                .fillMaxWidth(fraction = 0.5f),
            text = stringResource(
                id = R.string.exif,
                exif.made,
                exif.model,
                exif.exposure,
                exif.aperture.toString(),
                exif.focalLength.toString(),
                exif.iso.toString()
            ),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = modifier.padding(8.dp))

        Text(
            modifier = modifier,
            text = stringResource(id = R.string.about, photo.author.name, photo.author.about),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun PhotoTags(
    photo: PhotoDetail,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = photo.tags.joinToString(separator = " ") { "#${it} " }.trim(),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PhotoLocation(
    photo: PhotoDetail,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clickable {
                showLocationOnMap(
                    context = context,
                    photo = photo
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = stringResource(id = R.string.location_icon),
            tint = MaterialTheme.colors.onBackground,
        )

        Spacer(modifier = modifier.padding(4.dp))

        Text(
            modifier = modifier,
            style = MaterialTheme.typography.body1,
            text = getLocationString(photo, context),
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun PhotoCardDetail(
    photo: PhotoDetail,
    like: Boolean,
    likes: Long,
    modifier: Modifier = Modifier,
    displayWidthHeight: Pair<Int, Int>,
    onClickLike: () -> Unit
) {
    val context = LocalContext.current

    val width = displayWidthHeight.first
    val height = (photo.height.toDouble() / photo.width.toDouble() * width).toInt()

    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = context)
                        .data(photo.image + "&fm=pjpg&w=${width}&h=${height}")
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
                            .size(34.dp)
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
                    modifier = modifier
                        .clickable {
                            onClickLike()
                        },
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Text(
                        text = likes.getLikeCountString(),
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
                        painter = painterResource(id = if (like) R.drawable.ic_like else R.drawable.ic_like_empty),
                        contentDescription = stringResource(id = R.string.like_icon),
                        modifier = modifier
                            .size(20.dp)
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

private fun getLocationString(
    photo: PhotoDetail,
    context: Context
): String =
    if (photo.location.city.isEmpty() && photo.location.country.isEmpty()) context.getString(R.string.no_location)
    else {
        when {
            photo.location.city.isEmpty() && photo.location.country.isNotEmpty() -> photo.location.country
            photo.location.country.isEmpty() && photo.location.city.isNotEmpty() -> photo.location.city
            else -> context.getString(
                R.string.location,
                photo.location.city,
                photo.location.country
            )
        }
    }

private fun showLocationOnMap(
    photo: PhotoDetail,
    context: Context,
) {
    val longitude = photo.location.longitude
    val latitude = photo.location.latitude
    val address = "${photo.location.country} ${photo.location.city}"

    if (longitude == 0.0 && latitude == 0.0 && address.trim().isBlank()) {
        Toast.makeText(context, R.string.no_location, Toast.LENGTH_LONG).show()
    } else {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latitude,$longitude?q=$address"))

        intent.setPackage("com.google.android.apps.maps")

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.no_activities_for_display_location, Toast.LENGTH_LONG)
                .show()
        }

    }

}