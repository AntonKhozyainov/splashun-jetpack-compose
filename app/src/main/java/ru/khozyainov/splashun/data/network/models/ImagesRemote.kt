package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Image.IMAGE_FULL
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Image.IMAGE_RAW

@JsonClass(generateAdapter = true)
data class ImagesRemote(
    @Json(name = IMAGE_RAW) val imageRaw: String,
    @Json(name = IMAGE_FULL) val imageFull: String
)