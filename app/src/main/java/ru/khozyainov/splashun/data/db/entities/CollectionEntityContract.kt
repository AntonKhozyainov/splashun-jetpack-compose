package ru.khozyainov.splashun.data.db.entities

object CollectionEntityContract {

    const val TABLE_NAME = "collection"

    object Columns {
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val CREATED_AT = "created_at"
        const val PHOTOS_COUNT = "photos_count"
        const val COVER_PHOTO = "cover_photo"
        const val AUTHOR = "author"
        const val AUTHOR_FULL_NAME = "author_full_name"
        const val AUTHOR_AVATAR = "avatar"
        const val COVER_PHOTO_WIDTH = "image_width"
        const val COVER_PHOTO_HEIGHT = "image_height"
    }
}