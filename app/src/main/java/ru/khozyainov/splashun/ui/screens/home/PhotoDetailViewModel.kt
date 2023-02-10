package ru.khozyainov.splashun.ui.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.data.repository.photo.PhotoRepository
import ru.khozyainov.splashun.ui.models.PhotoDetail
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _uiPhotoDetailState = MutableStateFlow(PhotoDetailScreenState())
    val uiPhotoDetailState: StateFlow<PhotoDetailScreenState> = _uiPhotoDetailState.asStateFlow()

    private val photoId = MutableLiveData(String())

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        setErrorState(throwable)
    }

    init {
        refreshState()
    }

    fun refreshState() {

        val photoDetailFlow = photoId.asFlow()
            .flatMapLatest { photoId ->
                photoRepository.getPhotoById(photoId)
            }

        viewModelScope.launch(errorHandler) {
            val photoDetail = photoDetailFlow.first()
            _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
                photoDetailFlow = photoDetailFlow,
                like = photoDetail.like,
                likes = photoDetail.likes,
                errorMessage = String()
            )
        }
    }

    fun getPhotoById(photoId: String?, refresh: Boolean = false) {
        if ((this.photoId.value == photoId || photoId == null) && !refresh) return
        this.photoId.value = photoId
    }

    private fun setErrorState(throwable: Throwable) {
        _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
            errorMessage = throwable.message.toString()
        )
    }

    private fun updateLakeState(abbreviatedPhotoRemote: AbbreviatedPhotoRemote) {
        _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
            like = abbreviatedPhotoRemote.like,
            likes = abbreviatedPhotoRemote.likes,
        )
        viewModelScope.launch(errorHandler) {
            photoRepository.setRefreshPhotoToDataBase(abbreviatedPhotoRemote)
        }
    }

    fun setLike() {
        if (photoId.value == null) {
            setErrorState(Exception("Отсутствует значение photoId"))
            return
        } //TODO

        if (!_uiPhotoDetailState.value.like) {
            photoRepository.setLike(
                photoId = photoId.value!!,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    updateLakeState(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        } else {
            photoRepository.deleteLike(
                photoId = photoId.value!!,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    updateLakeState(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        }
    }

    data class PhotoDetailScreenState(
        val photoDetailFlow: Flow<PhotoDetail?> = emptyFlow(),
        val like: Boolean = false,
        val likes: Long = 0,
        val errorMessage: String = String()
    )
}