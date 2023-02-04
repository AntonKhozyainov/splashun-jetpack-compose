package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Image.IMAGE_RAW

@JsonClass(generateAdapter = true)
data class ImagesRemote(
    @Json(name = IMAGE_RAW) val imageRaw: String
)