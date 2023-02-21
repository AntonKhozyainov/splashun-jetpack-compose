package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.AbbreviatedPhoto.USER
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.BLUR
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.ID
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.IMAGE_HEIGHT
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.IMAGE_URLS
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.IMAGE_WIDTH
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.LIKES
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.LIKE_BY_USER

@JsonClass(generateAdapter = true)
data class PhotoRemote(
    @Json(name = ID) val id: String,
    @Json(name = BLUR) val blur: String,
    @Json(name = LIKES) val likes: Long,
    @Json(name = LIKE_BY_USER) val likeByUser: Boolean,
    @Json(name = USER) val author: AuthorRemote,
    @Json(name = IMAGE_URLS) val images: ImagesRemote,
    @Json(name = IMAGE_HEIGHT) val height: Int,
    @Json(name = IMAGE_WIDTH) val width: Int
)




