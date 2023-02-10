package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Location.LOCATION_CITY
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Location.LOCATION_COUNTRY
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Location.LOCATION_POSITION

@JsonClass(generateAdapter = true)
data class LocationRemote(
    @Json(name = LOCATION_CITY) val city: String? = null,
    @Json(name = LOCATION_COUNTRY) val country: String? = null,
    @Json(name = LOCATION_POSITION) val position: LocationPositionRemote? = null
)