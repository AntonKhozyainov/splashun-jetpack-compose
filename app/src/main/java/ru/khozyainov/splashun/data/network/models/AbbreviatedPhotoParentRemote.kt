package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.AbbreviatedPhoto.PHOTO
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.AbbreviatedPhoto.USER

@JsonClass(generateAdapter = true)
data class AbbreviatedPhotoParentRemote(
    @Json(name = PHOTO) val photo: AbbreviatedPhotoRemote,
    @Json(name = USER) val user: ProfileRemote
)


