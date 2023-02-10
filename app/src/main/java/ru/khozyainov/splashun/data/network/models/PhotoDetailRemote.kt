package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.AbbreviatedPhoto.USER
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.BLUR
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.DOWNLOADS
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.EXIF
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.ID
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.IMAGE_HEIGHT
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.IMAGE_URLS
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.IMAGE_WIDTH
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.LIKES
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.LIKE_BY_USER
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.LOCATION
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.TAGS

@JsonClass(generateAdapter = true)
data class PhotoDetailRemote(
    @Json(name = ID) val id: String,
    @Json(name = IMAGE_WIDTH) val width: Int,
    @Json(name = IMAGE_HEIGHT) val height: Int,
    @Json(name = BLUR) val blur: String,
    @Json(name = DOWNLOADS) val downloads: Int,
    @Json(name = LIKES) val likes: Long,
    @Json(name = LIKE_BY_USER) val likeByUser: Boolean,
    @Json(name = EXIF) val exif: ExifRemote,
    @Json(name = LOCATION) val location: LocationRemote,
    @Json(name = TAGS) val tags: List<TagItemRemote>,
    @Json(name = IMAGE_URLS) val image: ImagesRemote,
    @Json(name = USER) val author: AuthorRemote
)
