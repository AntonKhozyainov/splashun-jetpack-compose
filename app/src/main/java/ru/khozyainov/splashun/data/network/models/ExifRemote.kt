package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Exif.EXIF_APERTURE
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Exif.EXIF_EXPOSURE
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Exif.EXIF_FOCAL
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Exif.EXIF_ISO
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Exif.EXIF_MADE
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Exif.EXIF_MODEL

@JsonClass(generateAdapter = true)
data class ExifRemote(
    @Json(name = EXIF_MADE) val make: String? = null,
    @Json(name = EXIF_MODEL) val model: String? = null,
    @Json(name = EXIF_EXPOSURE) val exposureTime: String? = null,
    @Json(name = EXIF_APERTURE) val aperture: Double? = null,
    @Json(name = EXIF_FOCAL) val focalLength: Double? = null,
    @Json(name = EXIF_ISO) val iso: Int? = null

)