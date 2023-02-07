package ru.khozyainov.splashun.data.repository.photo

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.ui.models.Photo

interface PhotoRepository {

    fun getPhotos(query: String): Flow<PagingData<Photo>>

    fun setLike(
        photo: Photo,
        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
        onErrorCallback: (error: Throwable) -> Unit
    )

    fun deleteLike(
        photo: Photo,
        onCompleteCallback: (abbreviatedPhotoRemote: AbbreviatedPhotoRemote) -> Unit,
        onErrorCallback: (error: Throwable) -> Unit
    )

    suspend fun setRefreshPhoto(abbreviatedPhotoRemote: AbbreviatedPhotoRemote)

    //fun getPhotoById(id: String): Flow<PhotoDetails>
}