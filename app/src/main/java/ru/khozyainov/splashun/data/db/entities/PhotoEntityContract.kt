package ru.khozyainov.splashun.data.db.entities

object PhotoEntityContract {

    const val TABLE_NAME = "photo"

    object Columns {
        const val ID = "id"
        const val COLLECTION_ID = "collection_id"
        const val PHOTO_RAW = "photo_raw"
        const val LIKE = "photo_like"
        const val LIKES = "likes"
        const val AUTHOR = "author"
        const val AUTHOR_FULL_NAME = "author_full_name"
        const val AUTHOR_AVATAR = "avatar"
        const val SEARCH = "search_for_rm"
        const val WIDTH = "image_width"
        const val HEIGHT = "image_height"
        const val CREATED_AT = "created_at"
    }
}