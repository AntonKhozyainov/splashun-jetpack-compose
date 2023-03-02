package ru.khozyainov.splashun.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import ru.khozyainov.splashun.data.network.models.*
import ru.khozyainov.splashun.ui.models.Author
import ru.khozyainov.splashun.ui.models.PhotoCollection
import kotlin.random.Random

class PhotoCollectionAdapterJSON {

    @FromJson
    fun fromJSON(collectionsRemote: CollectionRemote): PhotoCollection =
        PhotoCollection(
            id = collectionsRemote.id,
            title = collectionsRemote.title,
            photosCount = collectionsRemote.totalPhotos,
            author = Author(
                name = collectionsRemote.author.name,
                fullName = collectionsRemote.author.fullName,
                image = collectionsRemote.author.images.image,
                about = collectionsRemote.author.about ?: ""
            ),
            width = collectionsRemote.coverPhoto.width,
            height = collectionsRemote.coverPhoto.height,
            image = collectionsRemote.coverPhoto.images.imageRaw,
            description = collectionsRemote.description ?: ""
        )


    @ToJson
    fun toJSON(photoCollectionDetail: PhotoCollection): CollectionRemote =
        CollectionRemote(
            id = Random.nextLong().toString(),
            title = "title",
            description = null,
            totalPhotos = Random.nextInt(),
            coverPhoto = PhotoRemote(
                id = Random.nextLong().toString(),
                blur = "",
                likes = Random.nextLong(),
                likeByUser = false,
                author = AuthorRemote(
                    name = "",
                    fullName = "",
                    images = AuthorImagesRemote(
                        image = ""
                    ),
                    about = null
                ),
                images = ImagesRemote(
                    imageFull = "",
                    imageRaw = ""
                ),
                height = Random.nextInt(),
                width = Random.nextInt()
            ),
            author = AuthorRemote(
                name = "",
                fullName = "",
                images = AuthorImagesRemote(
                    image = ""
                ),
                about = null
            )
        )
}