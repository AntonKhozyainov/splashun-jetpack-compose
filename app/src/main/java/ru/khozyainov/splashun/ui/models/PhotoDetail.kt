package ru.khozyainov.splashun.ui.models

data class PhotoDetail(
    val id: String,
    override val image: String,
    override val width: Int,
    override val height: Int,
    val placeholder: String,
    val imageForDownload: String,
    val like: Boolean,
    val likes: Long,
    val author: Author,
    val location: Location,
    val exif: Exif,
    val tags: List<String>,
    val downloads: Int
): ImageWithSize
