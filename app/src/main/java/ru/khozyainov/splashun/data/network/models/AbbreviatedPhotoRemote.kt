package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.ID
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.LIKES
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Photo.LIKE_BY_USER

@JsonClass(generateAdapter = true)
data class AbbreviatedPhotoRemote(
    @Json(name = ID) val id: String,
    @Json(name = LIKES) val likes: Long,
    @Json(name = LIKE_BY_USER) val like: Boolean
)
