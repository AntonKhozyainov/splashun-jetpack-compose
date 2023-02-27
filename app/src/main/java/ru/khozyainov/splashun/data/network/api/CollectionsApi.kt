package ru.khozyainov.splashun.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.khozyainov.splashun.data.db.entities.CollectionEntity

interface CollectionsApi {

    @GET("/collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<CollectionEntity>
}