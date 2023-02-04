package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Search.RESULTS

@JsonClass(generateAdapter = true)
data class SearchPhotoRemote(
    @Json(name = RESULTS) val itemPhotoRemoteList: List<PhotoRemote>
)