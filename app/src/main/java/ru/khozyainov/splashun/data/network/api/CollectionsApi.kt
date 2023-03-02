package ru.khozyainov.splashun.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.khozyainov.splashun.data.db.entities.CollectionEntity
import ru.khozyainov.splashun.ui.models.PhotoCollection

interface CollectionsApi {

    @GET("/collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<CollectionEntity>

    @GET("/collections/{id}")
    suspend fun getCollectionById(
        @Path("id") id: String
    ): PhotoCollection
}