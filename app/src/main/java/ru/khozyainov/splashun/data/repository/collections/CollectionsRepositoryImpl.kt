package ru.khozyainov.splashun.data.repository.collections

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.khozyainov.splashun.data.db.dao.CollectionsDao
import ru.khozyainov.splashun.data.network.api.CollectionsApi
import ru.khozyainov.splashun.data.network.remotemediator.CollectionsRemoteMediator
import ru.khozyainov.splashun.ui.models.PhotoCollection
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CollectionsRepositoryImpl @Inject constructor(
    private val collectionsApi: CollectionsApi,
    private val collectionsDao: CollectionsDao,
    private val collectionsRemoteMediator: CollectionsRemoteMediator
) : CollectionsRepository {

    override fun getCollections(): Flow<PagingData<PhotoCollection>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE

            ),
            remoteMediator = collectionsRemoteMediator,
            pagingSourceFactory = { collectionsDao.getCollectionsPagingSource() }
        ).flow
            .map { pagingData ->
                pagingData.map { collectionEntity ->
                    collectionEntity.toPhotoCollection()
                }
            }

    override fun getCollectionById(id: String): Flow<PhotoCollection> = flow {
        withContext(Dispatchers.IO){
            emit(collectionsApi.getCollectionById(id))
        }
    }

    private companion object {
        const val PAGE_SIZE = 7
    }
}