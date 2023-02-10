package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Tag.TAGS_TITLE

@JsonClass(generateAdapter = true)
data class TagItemRemote(
    @Json(name = TAGS_TITLE) val title: String
)