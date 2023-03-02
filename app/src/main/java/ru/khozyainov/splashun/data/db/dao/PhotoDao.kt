package ru.khozyainov.splashun.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.CREATED_AT
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.ID
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.SEARCH
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.TABLE_NAME
import ru.khozyainov.splashun.data.db.entities.PhotoEntity
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.COLLECTION_ID

@Dao
interface PhotoDao {

    @Query("SELECT * FROM $TABLE_NAME " +
            "WHERE (:query IS NULL OR $SEARCH = :query) AND (:collectionId IS NULL OR $COLLECTION_ID = :collectionId) " +
            "ORDER BY $CREATED_AT")
    fun getPagingSource(
        query: String?,
        collectionId: String?
    ): PagingSource<Int, PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(photos: List<PhotoEntity>)

    @Update
    suspend fun updatePhoto(photo: PhotoEntity)

    @Query("SELECT * FROM $TABLE_NAME " +
            "WHERE $ID = :id")
    suspend fun getPhotoByID(id: String): PhotoEntity?

    @Query("DELETE FROM $TABLE_NAME " +
            "WHERE (:query IS NULL OR $SEARCH = :query) AND (:collectionId IS NULL OR $COLLECTION_ID = :collectionId)")
    suspend fun clear(query: String?, collectionId: String?)

    @Transaction
    suspend fun refresh(query: String?, collectionId: String?, photos: List<PhotoEntity>) {
        clear(query, collectionId)
        save(photos)
    }

    suspend fun save(photo: PhotoEntity) {
        save(listOf(photo))
    }

//    @Query("SELECT * FROM $TABLE_NAME WHERE :query IS NULL OR $SEARCH = :query ORDER BY $CREATED_AT")
//    fun getPagingSource(
//        query: String?
//    ): PagingSource<Int, PhotoEntity>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun save(photos: List<PhotoEntity>)
//
//    @Update
//    suspend fun updatePhoto(photo: PhotoEntity)
//
//    @Query("SELECT * FROM $TABLE_NAME WHERE $ID = :id")
//    suspend fun getPhotoByID(id: String): PhotoEntity?
//
//    @Query("DELETE FROM $TABLE_NAME WHERE :query IS NULL OR $SEARCH = :query")
//    suspend fun clear(query: String?)
//
//    @Transaction
//    suspend fun refresh(query: String?, photos: List<PhotoEntity>) {
//        clear(query)
//        save(photos)
//    }
//
//    suspend fun save(photo: PhotoEntity) {
//        save(listOf(photo))
//    }

}