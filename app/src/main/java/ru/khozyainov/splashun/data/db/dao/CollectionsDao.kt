package ru.khozyainov.splashun.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import ru.khozyainov.splashun.data.db.entities.CollectionEntity
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.CREATED_AT
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.ID
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.TABLE_NAME

@Dao
interface CollectionsDao {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $CREATED_AT")
    fun getCollectionsPagingSource(): PagingSource<Int, CollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(collections: List<CollectionEntity>)

    @Update
    suspend fun updateCollection(collection: CollectionEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE $ID = :id")
    suspend fun getCollectionByID(id: String): CollectionEntity?

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clear()

    @Transaction
    suspend fun refresh(collections: List<CollectionEntity>) {
        clear()
        save(collections)
    }

    suspend fun save(collection: CollectionEntity) {
        save(listOf(collection))
    }
}