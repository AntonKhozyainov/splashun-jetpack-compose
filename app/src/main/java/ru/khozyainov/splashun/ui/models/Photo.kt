package ru.khozyainov.splashun.ui.models

data class Photo(
    val id: String,
    val width: Int,
    val height: Int,
    val image: String,
    val placeholder: String,
    val like: Boolean,
    val likes: Long,
    val author: Author
)
