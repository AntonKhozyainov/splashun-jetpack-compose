package ru.khozyainov.splashun.ui.models

data class PhotoCollection(
    val id: String,
    val description: String,
    override val width: Int,
    override val height: Int,
    override val image: String,
    val photosCount: Int,
    val title: String,
    val author: Author
): ImageWithSize
