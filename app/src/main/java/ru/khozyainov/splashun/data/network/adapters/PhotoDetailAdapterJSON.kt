package ru.khozyainov.splashun.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import ru.khozyainov.splashun.data.network.models.*
import ru.khozyainov.splashun.ui.models.Author
import ru.khozyainov.splashun.ui.models.Exif
import ru.khozyainov.splashun.ui.models.Location
import ru.khozyainov.splashun.ui.models.PhotoDetail

class PhotoDetailAdapterJSON {

    @FromJson
    fun fromJson(photoDetailRemote: PhotoDetailRemote): PhotoDetail =
        PhotoDetail(
            id = photoDetailRemote.id,
            width = photoDetailRemote.width,
            height = photoDetailRemote.height,
            placeholder = photoDetailRemote.blur,
            image = photoDetailRemote.image.imageRaw,
            like = photoDetailRemote.likeByUser,
            likes = photoDetailRemote.likes,
            author = Author(
                name = "@${photoDetailRemote.author.name}",
                fullName = photoDetailRemote.author.fullName,
                image = photoDetailRemote.author.images.image,
                about = photoDetailRemote.author.about  ?: ""
            ),
            location = Location(
                country = photoDetailRemote.location.country ?: "",
                city = photoDetailRemote.location.city ?: "",
                latitude = photoDetailRemote.location.position?.latitude  ?: 0.0,
                longitude = photoDetailRemote.location.position?.longitude ?: 0.0
            ),
            exif = Exif(
                made = photoDetailRemote.exif.make ?: "-",
                model = photoDetailRemote.exif.model ?: "-",
                exposure = photoDetailRemote.exif.exposureTime ?: "-",
                aperture = photoDetailRemote.exif.aperture ?: 0.0,
                focalLength = photoDetailRemote.exif.focalLength ?: 0.0,
                iso = photoDetailRemote.exif.iso ?: 0
            ),
            tags = photoDetailRemote.tags.map { it.title },
            downloads = photoDetailRemote.downloads
        )

    @ToJson
    fun toJson(photoDetails: PhotoDetail): PhotoDetailRemote = PhotoDetailRemote(
        id = photoDetails.id,
        width = photoDetails.width,
        height = photoDetails.height,
        blur = photoDetails.placeholder,
        downloads = photoDetails.downloads,
        likes = photoDetails.likes,
        likeByUser = photoDetails.like,
        exif = ExifRemote(
            make = photoDetails.exif.made,
            model = photoDetails.exif.model,
            exposureTime = photoDetails.exif.exposure,
            aperture = photoDetails.exif.aperture,
            focalLength = photoDetails.exif.focalLength,
            iso = photoDetails.exif.iso
        ),
        location = LocationRemote(
            city = photoDetails.location.city,
            country = photoDetails.location.country,
            position = LocationPositionRemote(
                latitude = photoDetails.location.latitude,
                longitude = photoDetails.location.longitude
            )
        ),
        tags = listOf(),
        image = ImagesRemote(
            imageRaw = photoDetails.image
        ),
        author = AuthorRemote(
            name = photoDetails.author.name,
            fullName = photoDetails.author.fullName,
            images = AuthorImagesRemote(
                image = photoDetails.author.image
            ),
            about = photoDetails.author.about
        )
    )

}