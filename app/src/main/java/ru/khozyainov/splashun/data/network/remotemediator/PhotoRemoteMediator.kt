package ru.khozyainov.splashun.data.network.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.khozyainov.splashun.data.db.dao.PhotoDao
import ru.khozyainov.splashun.data.db.entities.PhotoEntity
import ru.khozyainov.splashun.data.network.api.PhotoApi
import kotlin.random.Random

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator @AssistedInject constructor(
    private val photoDao: PhotoDao,
    private val photoAPI: PhotoApi,
    @Assisted private val query: String
) : RemoteMediator<Int, PhotoEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {

        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            //todo
//            val listItemPhoto =
//                if (query.isEmpty()) photoAPI.getPhotos(pageIndex, state.config.pageSize)
//                else photoAPI.searchPhotos(query, pageIndex, state.config.pageSize).listPhotoEntity
//                    .map { it.copy(search = query) }

            val listItemPhoto = getTestList()

            if (loadType == LoadType.REFRESH) {
                photoDao.refresh(query = query, collectionId = null, photos =  listItemPhoto)
            } else {
                photoDao.save(listItemPhoto)
            }
            MediatorResult.Success(
                endOfPaginationReached = listItemPhoto.size < state.config.pageSize
            )

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }

    //TODO
    private fun getTestList(): List<PhotoEntity> {
        val list = mutableListOf<PhotoEntity>()
        repeat(1) {
            list.add(
                PhotoEntity(
                    id = "id_${Random.nextInt()}",
                    image = "this.image",
                    like = Random.nextBoolean(),
                    likes = Random.nextInt(0, 10000000).toLong(),
                    author = "@author",
                    authorFullName = "Ivan Petrov",
                    authorImage = "this.authorImage",
                    search = null,
                    width = 300,
                    height = 450,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
        return list
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    @AssistedFactory
    interface Factory {
        fun create(query: String?): PhotoRemoteMediator
    }
}