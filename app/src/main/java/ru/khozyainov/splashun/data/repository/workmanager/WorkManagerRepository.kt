package ru.khozyainov.splashun.data.repository.workmanager

interface WorkManagerRepository {

    fun downloadPhoto(url: String, photoId: String)
    fun cancelWork(photoId: String)
}