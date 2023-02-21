package ru.khozyainov.splashun.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import ru.khozyainov.splashun.data.db.entities.PhotoEntity
import ru.khozyainov.splashun.data.network.models.PhotoRemote

class PhotoListAdapterJSON {

    @FromJson
    fun fromJSON(listPhotoRemote: List<PhotoRemote>): List<PhotoEntity> =
        listPhotoRemote.map { itemPhotoRemote ->
            PhotoEntity(
                id = itemPhotoRemote.id,
                image = itemPhotoRemote.images.imageRaw,
                placeholder = itemPhotoRemote.blur,
                like = itemPhotoRemote.likeByUser,
                likes = itemPhotoRemote.likes,
                author = itemPhotoRemote.author.name,
                authorFullName = itemPhotoRemote.author.fullName,
                authorImage = itemPhotoRemote.author.images.image,
                authorAbout = itemPhotoRemote.author.about ?: "",
                search = null,
                createdAt = System.currentTimeMillis(),
                width = itemPhotoRemote.width,
                height = itemPhotoRemote.height,
            )
        }


    @ToJson
    fun toJSON(listPhotoEntity: List<PhotoEntity>): List<PhotoRemote> =
        listOf()
}