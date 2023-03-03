package ru.khozyainov.splashun.ui.screens.collections

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.data.repository.collections.CollectionsRepository
import ru.khozyainov.splashun.data.repository.photo.PhotoRepository
import ru.khozyainov.splashun.ui.models.Photo
import ru.khozyainov.splashun.ui.models.PhotoCollection
import ru.khozyainov.splashun.ui.models.PhotoDetail
import ru.khozyainov.splashun.ui.screens.home.HomeViewModel
import ru.khozyainov.splashun.ui.screens.home.PhotoDetailDestination
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    private val collectionsRepository: CollectionsRepository,
    private val photoRepository: PhotoRepository,
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiCollectionDetailState = MutableStateFlow(CollectionDetailScreenState())
    val uiCollectionDetailState: StateFlow<CollectionDetailScreenState> = _uiCollectionDetailState.asStateFlow()

    private var currentJob: Job? = null

    private val collectionId: String

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        setErrorState(throwable)
    }

    init {

        collectionId = saveStateHandle[CollectionDetailDestination.argName]
            ?: throw Exception("Incorrect argument = ${CollectionDetailDestination.argName}")

        refreshState()
    }

    fun refreshState() {
        currentJob = viewModelScope.launch(errorHandler) {
            collectionsRepository.getCollectionById(collectionId)
                .collect { photoCollection ->
                    _uiCollectionDetailState.value = _uiCollectionDetailState.value.copy(
                        photoCollection = photoCollection,
                        pagingPhotoFlow = photoRepository.getPhotosByCollectionId(collectionId),
                        errorMessage = String()
                    )
                }
        }
    }

    fun setLike(photo: Photo) =
        if (!photo.like) {
            photoRepository.setLike(
                photoId = photo.id,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    setRefreshPhoto(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        } else {
            photoRepository.deleteLike(
                photoId = photo.id,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    setRefreshPhoto(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        }

    private fun setRefreshPhoto(abbreviatedPhotoRemote: AbbreviatedPhotoRemote) {
        viewModelScope.launch(errorHandler) {
            photoRepository.setRefreshPhotoToDataBase(abbreviatedPhotoRemote)
        }
    }

    private fun setErrorState(throwable: Throwable) {
        _uiCollectionDetailState.value = _uiCollectionDetailState.value.copy(
            errorMessage = throwable.message.toString()
        )
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }

    data class CollectionDetailScreenState(
        val photoCollection: PhotoCollection? = null,
        val pagingPhotoFlow: Flow<PagingData<Photo>> = emptyFlow(),
        val errorMessage: String = String()
    )
}