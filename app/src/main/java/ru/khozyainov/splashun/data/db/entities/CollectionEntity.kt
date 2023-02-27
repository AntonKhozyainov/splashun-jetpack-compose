package ru.khozyainov.splashun.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.AUTHOR
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.AUTHOR_AVATAR
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.AUTHOR_FULL_NAME
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.COVER_PHOTO
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.COVER_PHOTO_HEIGHT
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.COVER_PHOTO_WIDTH
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.CREATED_AT
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.ID
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.PHOTOS_COUNT
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.Columns.TITLE
import ru.khozyainov.splashun.data.db.entities.CollectionEntityContract.TABLE_NAME
import ru.khozyainov.splashun.ui.models.Author
import ru.khozyainov.splashun.ui.models.PhotoCollection

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(CREATED_AT)
    ]
)
data class CollectionEntity(
    @PrimaryKey
    @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = CREATED_AT) val createdAt: Long,
    @ColumnInfo(name = PHOTOS_COUNT) val photosCount: Int,
    @ColumnInfo(name = COVER_PHOTO) val coverPhoto: String,
    @ColumnInfo(name = AUTHOR) val author: String,
    @ColumnInfo(name = AUTHOR_FULL_NAME) val authorFullName: String,
    @ColumnInfo(name = AUTHOR_AVATAR) val authorImage: String,
    @ColumnInfo(name = COVER_PHOTO_WIDTH) val coverPhotoWidth: Int,
    @ColumnInfo(name = COVER_PHOTO_HEIGHT) val coverPhotoHeight: Int,
){
    fun toPhotoCollection(): PhotoCollection =
        PhotoCollection(
            id = this.id,
            width = this.coverPhotoWidth,
            height = this.coverPhotoHeight,
            image = this.coverPhoto,
            photosCount = this.photosCount,
            title = this.title,
            author = Author(
                name = "@${this.author}",
                fullName = this.authorFullName,
                image = this.authorImage
            )
        )
}