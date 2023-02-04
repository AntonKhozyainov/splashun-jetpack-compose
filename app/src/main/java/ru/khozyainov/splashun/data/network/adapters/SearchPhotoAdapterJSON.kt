package ru.khozyainov.splashun.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import ru.khozyainov.splashun.data.db.entities.SearchPhoto
import ru.khozyainov.splashun.data.network.models.SearchPhotoRemote

class SearchPhotoAdapterJSON(
  private val itemPhotoListAdapterJSON: PhotoListAdapterJSON
) {
    @FromJson
    fun fromJSON(searchItemPhotoRemote: SearchPhotoRemote): SearchPhoto =
        SearchPhoto(itemPhotoListAdapterJSON.fromJSON(searchItemPhotoRemote.itemPhotoRemoteList))

    @ToJson
    fun toJSON(searchPhotoItems: SearchPhoto): SearchPhotoRemote =
        SearchPhotoRemote(listOf())
}