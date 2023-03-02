package ru.khozyainov.splashun.ui.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.data.repository.photo.PhotoRepository
import ru.khozyainov.splashun.ui.models.Photo
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _uiHomeState = MutableStateFlow(HomeScreenState())
    val uiHomeState: StateFlow<HomeScreenState> = _uiHomeState.asStateFlow()

    private val searchBy = MutableLiveData(String())

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        setErrorState(throwable)
    }

    init {
        refreshState()
    }

    fun refreshState() {

        val pagingPhotoFlow = searchBy.asFlow()
            .debounce(500)
            .flatMapLatest { searchString ->
                photoRepository.getPhotos(searchString)
            }
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)

        _uiHomeState.value = _uiHomeState.value.copy(
            pagingPhotoFlow = pagingPhotoFlow,
            errorMessage = String()
        )
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

    fun setSearchBy(searchString: String) {
        if (this.searchBy.value == searchString) return
        this.searchBy.value = searchString
    }

    fun refresh() {
        this.searchBy.postValue(this.searchBy.value)
    }

    private fun setRefreshPhoto(abbreviatedPhotoRemote: AbbreviatedPhotoRemote) {
        viewModelScope.launch(errorHandler) {
            photoRepository.setRefreshPhotoToDataBase(abbreviatedPhotoRemote)
        }
    }


    private fun setErrorState(throwable: Throwable) {
        _uiHomeState.value = _uiHomeState.value.copy(
            errorMessage = throwable.message.toString()
        )
    }

    data class HomeScreenState(
        val pagingPhotoFlow: Flow<PagingData<Photo>> = emptyFlow(),
        val errorMessage: String = String()
    )

}