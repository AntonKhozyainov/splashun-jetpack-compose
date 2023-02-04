package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Author.AUTHOR_ABOUT
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Author.AUTHOR_FULL_NAME
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Author.AUTHOR_IMAGES
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Author.AUTHOR_NAME

@JsonClass(generateAdapter = true)
data class AuthorRemote(
    @Json(name = AUTHOR_NAME) val name: String,
    @Json(name = AUTHOR_FULL_NAME) val fullName: String,
    @Json(name = AUTHOR_IMAGES) val images: AuthorImagesRemote,
    @Json(name = AUTHOR_ABOUT) val about: String? = null
)