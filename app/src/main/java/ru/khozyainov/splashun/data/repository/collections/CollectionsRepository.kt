package ru.khozyainov.splashun.data.repository.collections

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.splashun.ui.models.PhotoCollection

interface CollectionsRepository {
    fun getCollections(): Flow<PagingData<PhotoCollection>>
}