package ru.khozyainov.splashun.data.network.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ru.khozyainov.splashun.data.db.dao.CollectionsDao
import ru.khozyainov.splashun.data.db.entities.CollectionEntity
import ru.khozyainov.splashun.data.network.api.CollectionsApi
import javax.inject.Inject
import kotlin.random.Random

@OptIn(ExperimentalPagingApi::class)
class CollectionsRemoteMediator @Inject constructor(
    private val collectionsDao: CollectionsDao,
    private val collectionsAPI: CollectionsApi
) : RemoteMediator<Int, CollectionEntity>() {

    private var pageIndex = 0

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CollectionEntity>
    ): MediatorResult {
        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            //todo
            val collections = collectionsAPI.getCollections(pageIndex, state.config.pageSize)
            //val collections = getTestList()

            if (loadType == LoadType.REFRESH) {
                collectionsDao.refresh(collections)
            } else {
                collectionsDao.save(collections)
            }
            MediatorResult.Success(
                endOfPaginationReached = collections.size < state.config.pageSize
            )

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getTestList(): List<CollectionEntity> {
        val list = mutableListOf<CollectionEntity>()
        repeat(1) {
            list.add(
                CollectionEntity(
                    id = "id_${Random.nextInt()}",
                    description = "some description",
                    title = "Collection title",
                    photosCount = Random.nextInt(1,200),
                    coverPhoto = "",
                    author = "@author",
                    authorFullName = "Ivan Petrov",
                    authorImage = "this.authorImage",
                    coverPhotoWidth = 300,
                    coverPhotoHeight = 450,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
        return list
    }
}