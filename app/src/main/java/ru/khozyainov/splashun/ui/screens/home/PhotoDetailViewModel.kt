package ru.khozyainov.splashun.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.data.repository.photo.PhotoRepository
import ru.khozyainov.splashun.data.repository.workmanager.WorkManagerRepository
import ru.khozyainov.splashun.ui.models.PhotoDetail
import ru.khozyainov.splashun.workers.DownloadWorker.Companion.TAG_OUTPUT_FAILURE
import ru.khozyainov.splashun.workers.DownloadWorker.Companion.TAG_OUTPUT_SUCCESS
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val workManagerRepository: WorkManagerRepository,
    private val workManager: WorkManager,
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiPhotoDetailState = MutableStateFlow(PhotoDetailScreenState())
    val uiPhotoDetailState: StateFlow<PhotoDetailScreenState> = _uiPhotoDetailState.asStateFlow()

    private var currentJob: Job? = null

    private val photoId: String

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        setErrorState(throwable)
    }

    init {
        photoId = saveStateHandle[PhotoDetailDestination.argName] ?: throw Exception("Incorrect argument = ${PhotoDetailDestination.argName}")
        refreshState()
    }

    fun refreshState() {
        currentJob = viewModelScope.launch(errorHandler) {
            photoRepository.getPhotoById(photoId)
                .collect { photoDetail ->
                _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
                    photoDetail = photoDetail,
                    photoIsDownloading = false,
                    like = photoDetail.like,
                    likes = photoDetail.likes,
                    errorMessage = String()
                )
            }
        }
    }

    private fun setErrorState(throwable: Throwable) {
        _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
            errorMessage = throwable.message.toString()
        )
    }

    private fun updateLikeState(abbreviatedPhotoRemote: AbbreviatedPhotoRemote) {
        _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
            like = abbreviatedPhotoRemote.like,
            likes = abbreviatedPhotoRemote.likes,
        )
        viewModelScope.launch(errorHandler) {
            photoRepository.setRefreshPhotoToDataBase(abbreviatedPhotoRemote)
        }
    }

    fun setLike() {
        if (!_uiPhotoDetailState.value.like) {
            photoRepository.setLike(
                photoId = photoId,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    updateLikeState(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        } else {
            photoRepository.deleteLike(
                photoId = photoId,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    updateLikeState(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        }
    }

    fun downloadPhoto() {
        val photoDetail = _uiPhotoDetailState.value.photoDetail ?: return

        _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
            photoIsDownloading = true
        )

        workManagerRepository.downloadPhoto(
            photoDetail.imageForDownload,
            photoDetail.id
        )

        viewModelScope.launch(errorHandler) {
            workManager.getWorkInfosForUniqueWorkLiveData(photoDetail.id)
                .asFlow()
                .mapNotNull {
                    if (it.isNotEmpty()) it.first() else null
                }.collect { info ->
                val photoUri = info.outputData.getString(TAG_OUTPUT_SUCCESS)
                val errorMessage = info.outputData.getString(TAG_OUTPUT_FAILURE)
                if (info.state.isFinished && !photoUri.isNullOrEmpty() && errorMessage.isNullOrEmpty()) {
                    _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
                        downloadPhotoUri = photoUri,
                        photoIsDownloading = false
                    )
                }else{
                    _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
                        photoIsDownloading = true
                    )
                }
            }
        }
    }

    fun cancelWork(){
        _uiPhotoDetailState.value.photoDetail?.let { photoDetail->
            workManagerRepository.cancelWork(photoDetail.id)
        }
        _uiPhotoDetailState.value = _uiPhotoDetailState.value.copy(
            downloadPhotoUri = String()
        )
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }

    data class PhotoDetailScreenState(
        val photoDetail: PhotoDetail? = null,
        val downloadPhotoUri: String = String(),
        val photoIsDownloading: Boolean = false,
        val like: Boolean = false,
        val likes: Long = 0,
        val errorMessage: String = String()
    )
}