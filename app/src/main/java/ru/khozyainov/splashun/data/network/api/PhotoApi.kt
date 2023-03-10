package ru.khozyainov.splashun.data.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import ru.khozyainov.splashun.data.db.entities.PhotoEntity
import ru.khozyainov.splashun.data.db.entities.SearchPhoto
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoParentRemote
import ru.khozyainov.splashun.ui.models.PhotoDetail

interface PhotoApi {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<PhotoEntity>

    @GET("/collections/{id}/photos")
    suspend fun getPhotosOfCollection(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<PhotoEntity>

    @GET("/photos/{id}")
    suspend fun getPhotoByID(
        @Path("id") id: String
    ): PhotoDetail

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): SearchPhoto

    @POST("/photos/{id}/like")
    fun likePhoto(
        @Path("id") id: String
    ): Call<AbbreviatedPhotoParentRemote>

    @DELETE("/photos/{id}/like")
    fun deleteLikePhoto(
        @Path("id") id: String
    ): Call<AbbreviatedPhotoParentRemote>

    @GET
    suspend fun downloadPhoto(
        @Url url: String
    ): ResponseBody

    @GET("/photos/{id}/download")
    fun notifyDownload(
        @Path("id") id: String
    ): Call<ResponseBody>
}