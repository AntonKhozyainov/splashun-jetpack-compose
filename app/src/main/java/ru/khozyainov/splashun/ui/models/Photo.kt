package ru.khozyainov.splashun.ui.models

data class Photo(
    override val id: String,
    val width: Int,
    val height: Int,
    val image: String,
    val like: Boolean,
    override val likes: Long,
    val author: Author
): AbstractPhoto(id, likes)
