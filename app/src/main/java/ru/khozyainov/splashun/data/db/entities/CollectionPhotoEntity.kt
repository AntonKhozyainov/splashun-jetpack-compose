package ru.khozyainov.splashun.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.COLLECTION_ID
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.CREATED_AT
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.HEIGHT
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.ID
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.LIKE
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.LIKES
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.PHOTO_RAW
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.Columns.WIDTH
import ru.khozyainov.splashun.data.db.entities.CollectionPhotoEntityContract.TABLE_NAME

//@Entity(
//    tableName = TABLE_NAME,
//    indices = [
//        Index(CREATED_AT)
//    ]
//)
//data class CollectionPhotoEntity(
//    @PrimaryKey
//    @ColumnInfo(name = ID) val id: String,
//    @ColumnInfo(name = COLLECTION_ID) val collectionId: String,
//    @ColumnInfo(name = PHOTO_RAW) val image: String,
//    @ColumnInfo(name = LIKE) val like: Boolean,
//    @ColumnInfo(name = LIKES) val likes: Long,
//    @ColumnInfo(name = CREATED_AT) val createdAt: Long,
//    @ColumnInfo(name = HEIGHT) val height: Int,
//    @ColumnInfo(name = WIDTH) val width: Int
//
////    @ColumnInfo(name = PhotoEntityContract.Columns.AUTHOR) val author: String,
////    @ColumnInfo(name = PhotoEntityContract.Columns.AUTHOR_FULL_NAME) val authorFullName: String,
////    @ColumnInfo(name = PhotoEntityContract.Columns.AUTHOR_AVATAR) val authorImage: String,
////    @ColumnInfo(name = PhotoEntityContract.Columns.SEARCH) val search: String?,
//)
