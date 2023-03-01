package ru.khozyainov.splashun.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import ru.khozyainov.splashun.data.db.entities.CollectionEntity
import ru.khozyainov.splashun.data.network.models.CollectionRemote

class CollectionsAdapterJSON {

    @FromJson
    fun fromJSON(listCollectionsRemote: List<CollectionRemote>): List<CollectionEntity> =
        listCollectionsRemote.map { itemCollectionRemote ->
            CollectionEntity(
                id = itemCollectionRemote.id,
                title = itemCollectionRemote.title,
                description = itemCollectionRemote.description ?: "",
                photosCount = itemCollectionRemote.totalPhotos,
                author = itemCollectionRemote.author.name,
                authorFullName = itemCollectionRemote.author.fullName,
                authorImage = itemCollectionRemote.author.images.image,
                createdAt = System.currentTimeMillis(),
                coverPhoto = itemCollectionRemote.coverPhoto.images.imageRaw,
                coverPhotoWidth = itemCollectionRemote.coverPhoto.width,
                coverPhotoHeight = itemCollectionRemote.coverPhoto.height,
            )
        }


    @ToJson
    fun toJSON(listCollectionsEntity: List<CollectionEntity>): List<CollectionRemote> =
        listOf()
}