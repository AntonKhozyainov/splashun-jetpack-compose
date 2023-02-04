package ru.khozyainov.splashun.data.repository.photo

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.khozyainov.splashun.data.db.dao.PhotoDao
import ru.khozyainov.splashun.data.network.PhotoRemoteMediator
import ru.khozyainov.splashun.data.network.api.PhotoApi
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoParentRemote
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.ui.models.Photo
import javax.inject.Inject
import javax.inject.Singleton

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

//    fun getPhotoById(id: String): Flow<PhotoDetails> = flow {
//            emit(photoAPI.getPhotoByID(id))
//        }


//    suspend fun setRefreshPhoto(abbreviatedPhotoRemote: AbbreviatedPhotoRemote) =
//        withContext(Dispatchers.IO) {
//            val itemPhotoEntity = itemPhotoDao.getItemPhotoByID(abbreviatedPhotoRemote.id)
//                ?: throw Exception("Отсутствует элемент с id = ${abbreviatedPhotoRemote.id}") //TODO
//
//            itemPhotoDao.updatePhoto(
//                itemPhotoEntity.copy(
//                    likes = abbreviatedPhotoRemote.likes,
//                    like = abbreviatedPhotoRemote.like
//                )
//            )
//        }

//    override fun setLike(
//        photo: Photo,
//        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
//        onErrorCallback: (error: Throwable) -> Unit
//    ) = photoAPI.likePhoto(photo.id).enqueue(object : Callback<AbbreviatedPhotoParentRemote> {
//        override fun onResponse(
//            call: Call<AbbreviatedPhotoParentRemote>,
//            response: Response<AbbreviatedPhotoParentRemote>
//        ) {
//            if (response.isSuccessful)
//                onCompleteCallback(response.body()?.photo ?: throw Exception("TODO")) //TODO
//        }
//
//        override fun onFailure(call: Call<AbbreviatedPhotoParentRemote>, t: Throwable) {
//            onErrorCallback(t)
//        }
//    })
//
//    override fun deleteLike(
//        photo: Photo,
//        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
//        onErrorCallback: (error: Throwable) -> Unit
//    ) = photoAPI.deleteLikePhoto(photo.id).enqueue(object : Callback<AbbreviatedPhotoParentRemote> {
//        override fun onResponse(
//            call: Call<AbbreviatedPhotoParentRemote>,
//            response: Response<AbbreviatedPhotoParentRemote>
//        ) {
//            if (response.isSuccessful)
//                onCompleteCallback(response.body()?.photo ?: throw Exception("TODO")) //TODO
//        }
//
//        override fun onFailure(call: Call<AbbreviatedPhotoParentRemote>, t: Throwable) {
//            onErrorCallback(t)
//        }
//    })

    private companion object {
        const val PAGE_SIZE = 10
    }
}