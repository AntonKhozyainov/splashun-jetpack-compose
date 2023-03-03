package ru.khozyainov.splashun.ui.models

data class Photo(
    val id: String,
    override val width: Int,
    override val height: Int,
    override val image: String,
    val like: Boolean,
    val likes: Long,
    val author: Author
): ImageWithSize
