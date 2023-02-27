package ru.khozyainov.splashun.ui.models

data class Author(
    val name: String,
    val fullName: String,
    val image: String,
    val about: String = ""
)
