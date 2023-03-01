package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Collection.COVER_PHOTO
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Collection.DESCRIPTION
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Collection.ID
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Collection.TITLE
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Collection.TOTAL_PHOTOS
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Collection.USER

@JsonClass(generateAdapter = true)
data class CollectionRemote(
    @Json(name = ID) val id: String,
    @Json(name = TITLE) val title: String,
    @Json(name = DESCRIPTION) val description: String?,
    @Json(name = TOTAL_PHOTOS) val totalPhotos: Int,
    @Json(name = COVER_PHOTO) val coverPhoto: PhotoRemote,
    @Json(name = USER) val author: AuthorRemote,
)
