package ru.khozyainov.splashun.data.repository.photo

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.khozyainov.splashun.data.db.dao.PhotoDao
import ru.khozyainov.splashun.data.network.PhotoRemoteMediator
import ru.khozyainov.splashun.data.network.api.PhotoApi
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoParentRemote
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.ui.models.Photo
import ru.khozyainov.splashun.ui.models.PhotoDetail
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao,
    private val photoAPI: PhotoApi,
    private val remoteMediatorFactory: PhotoRemoteMediator.Factory
): PhotoRepository {

    override fun getPhotos(query: String): Flow<PagingData<Photo>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE

            ),
            remoteMediator = remoteMediatorFactory.create(query),
            pagingSourceFactory = { photoDao.getPagingSource(query = if (query == "") null else query) }
        ).flow
            .map { pagingData ->
                pagingData.map { photoEntity ->
                    photoEntity.toPhoto()
                }
            }

    override fun getPhotoById(id: String): Flow<PhotoDetail> = flow {
            emit(photoAPI.getPhotoByID(id))
        }


    override suspend fun setRefreshPhotoToDataBase(abbreviatedPhotoRemote: AbbreviatedPhotoRemote) =
        withContext(Dispatchers.IO) {
            val itemPhotoEntity = photoDao.getPhotoByID(abbreviatedPhotoRemote.id)
                ?: throw Exception("Отсутствует элемент с id = ${abbreviatedPhotoRemote.id}") //TODO

            photoDao.updatePhoto(
                itemPhotoEntity.copy(
                    likes = abbreviatedPhotoRemote.likes,
                    like = abbreviatedPhotoRemote.like
                )
            )
        }

    override fun setLike(
        photoId: String,
        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
        onErrorCallback: (error: Throwable) -> Unit
    ) = photoAPI.likePhoto(photoId).enqueue(object : Callback<AbbreviatedPhotoParentRemote> {
        override fun onResponse(
            call: Call<AbbreviatedPhotoParentRemote>,
            response: Response<AbbreviatedPhotoParentRemote>
        ) {
            if (response.isSuccessful)
                onCompleteCallback(response.body()?.photo ?: throw Exception("TODO")) //TODO
        }

        override fun onFailure(call: Call<AbbreviatedPhotoParentRemote>, t: Throwable) {
            onErrorCallback(t)
        }
    })

    override fun deleteLike(
        photoId: String,
        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
        onErrorCallback: (error: Throwable) -> Unit
    ) = photoAPI.deleteLikePhoto(photoId).enqueue(object : Callback<AbbreviatedPhotoParentRemote> {
        override fun onResponse(
            call: Call<AbbreviatedPhotoParentRemote>,
            response: Response<AbbreviatedPhotoParentRemote>
        ) {
            if (response.isSuccessful)
                onCompleteCallback(response.body()?.photo ?: throw Exception("TODO")) //TODO
        }

        override fun onFailure(call: Call<AbbreviatedPhotoParentRemote>, t: Throwable) {
            onErrorCallback(t)
        }
    })

    private companion object {
        const val PAGE_SIZE = 5
    }
}