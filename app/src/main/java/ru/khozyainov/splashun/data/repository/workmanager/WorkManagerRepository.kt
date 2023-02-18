package ru.khozyainov.splashun.data.repository.workmanager

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface WorkManagerRepository {

    //val outputWorkInfo: Flow<WorkInfo>
    fun downloadPhoto(url: String, photoId: String)
    fun cancelWork(photoId: String)
}