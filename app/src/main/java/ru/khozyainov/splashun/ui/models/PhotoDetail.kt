package ru.khozyainov.splashun.ui.models

data class PhotoDetail(
    val id: String,
    val width: Int,
    val height: Int,
    val placeholder: String,
    val image: String,
    val imageForDownload: String,
    val like: Boolean,
    val likes: Long,
    val author: Author,
    val location: Location,
    val exif: Exif,
    val tags: List<String>,
    val downloads: Int
)
