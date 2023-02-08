package ru.khozyainov.splashun.ui.models

abstract class AbstractPhoto(
    open val id: String,
    open val likes: Long
) {
    fun getLikeCountString(): String = when {
        likes >= 1000000 -> "${
            likes.toString().substring(0, likes.toString().length - 6)
        }kk"
        likes >= 1000 -> "${
            likes.toString().substring(0, likes.toString().length - 3)
        }k"
        else -> likes.toString()
    }

}