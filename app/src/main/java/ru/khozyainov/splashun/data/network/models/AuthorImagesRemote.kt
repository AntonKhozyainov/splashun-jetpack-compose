package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.ImageAuthor.AUTHOR_IMAGE_SMALL

@JsonClass(generateAdapter = true)
data class AuthorImagesRemote(
    @Json(name = AUTHOR_IMAGE_SMALL) val image: String
)