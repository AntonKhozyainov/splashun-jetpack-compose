package ru.khozyainov.splashun.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.AUTHOR
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.AUTHOR_AVATAR
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.AUTHOR_FULL_NAME
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.CREATED_AT
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.HEIGHT
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.ID
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.LIKE
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.LIKES
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.PHOTO_RAW
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.SEARCH
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.Columns.WIDTH
import ru.khozyainov.splashun.data.db.entities.PhotoEntityContract.TABLE_NAME
import ru.khozyainov.splashun.ui.models.Author
import ru.khozyainov.splashun.ui.models.Photo

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(CREATED_AT)
    ]
)
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = PHOTO_RAW) val image: String,
    @ColumnInfo(name = LIKE) val like: Boolean,
    @ColumnInfo(name = LIKES) val likes: Long,
    @ColumnInfo(name = AUTHOR) val author: String,
    @ColumnInfo(name = AUTHOR_FULL_NAME) val authorFullName: String,
    @ColumnInfo(name = AUTHOR_AVATAR) val authorImage: String,
    @ColumnInfo(name = SEARCH) val search: String?,
    @ColumnInfo(name = CREATED_AT) val createdAt: Long,
    @ColumnInfo(name = HEIGHT) val height: Int,
    @ColumnInfo(name = WIDTH) val width: Int
) {

    fun toPhoto(): Photo =
        Photo(
            id = this.id,
            image = this.image,
            like = this.like,
            likes = this.likes,
            author = Author(
                name = "@${this.author}",
                fullName = this.authorFullName,
                image = this.authorImage
            ),
            width = width,
            height = height
        )

}
