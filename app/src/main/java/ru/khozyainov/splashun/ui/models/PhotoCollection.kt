package ru.khozyainov.splashun.ui.models

data class PhotoCollection(
    val id: String,
    val description: String,
    val width: Int,
    val height: Int,
    val image: String,
    val photosCount: Int,
    val title: String,
    val author: Author
)
