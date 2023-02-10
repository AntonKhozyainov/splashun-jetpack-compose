package ru.khozyainov.splashun.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Position.LOCATION_POSITION_LATITUDE
import ru.khozyainov.splashun.data.network.models.PhotoRemoteContract.Position.LOCATION_POSITION_LONGITUDE

@JsonClass(generateAdapter = true)
data class LocationPositionRemote(
    @Json(name = LOCATION_POSITION_LATITUDE) val latitude: Double? = null,
    @Json(name = LOCATION_POSITION_LONGITUDE) val longitude: Double? = null
)