package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Photo.ID
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Profile.FIRST_NAME
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Profile.LAST_NAME
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Profile.NAME
import ru.khozyainov.splashun.data.network.models.RemoteContracts.Profile.TOTAL_LIKES

@JsonClass(generateAdapter = true)
data class ProfileRemote(
    @Json(name = ID) val id: String,
    @Json(name = NAME) val name: String,
    @Json(name = FIRST_NAME) val firstName: String,
    @Json(name = LAST_NAME) val lastName: String,
    @Json(name = TOTAL_LIKES) val likes: Long
)