package ru.khozyainov.splashun.data.repository.photo

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.ui.models.Photo
import ru.khozyainov.splashun.ui.models.PhotoDetail

interface PhotoRepository {

    fun getPhotos(query: String): Flow<PagingData<Photo>>

    fun getPhotosByCollectionId(id: String): Flow<PagingData<Photo>>

    fun setLike(
        photoId: String,
        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
        onErrorCallback: (error: Throwable) -> Unit
    )

    fun deleteLike(
        photoId: String,
        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
        onErrorCallback: (error: Throwable) -> Unit
    )

    suspend fun setRefreshPhotoToDataBase(abbreviatedPhotoRemote: AbbreviatedPhotoRemote)

    fun getPhotoById(id: String): Flow<PhotoDetail>
}