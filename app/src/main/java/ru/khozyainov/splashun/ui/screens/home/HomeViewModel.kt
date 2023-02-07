package ru.khozyainov.splashun.ui.screens.home

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.khozyainov.splashun.data.network.models.AbbreviatedPhotoRemote
import ru.khozyainov.splashun.data.repository.photo.PhotoRepository
import ru.khozyainov.splashun.ui.models.Photo
import ru.khozyainov.splashun.ui.screens.login.LoginViewModel
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    //val photoFlow: Flow<PagingData<Photo>>

    private val searchBy = MutableLiveData(String())

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        setErrorState(throwable)
    }

    init {
        refreshState()
    }

    fun refreshState() {
        val photoFlow = searchBy.asFlow()
            .debounce(500)
            .flatMapLatest { searchString ->
                photoRepository.getPhotos(searchString)
            }
            .cachedIn(viewModelScope)

        _uiState.value = _uiState.value.copy(
            photoFlow = photoFlow,
            errorMessage = String()
        )
    }

    fun setSearchBy(value: String) {
        if (this.searchBy.value == value) return
        this.searchBy.value = value
    }

    fun refresh() {
        this.searchBy.postValue(this.searchBy.value)
    }

    fun setLike(photo: Photo) =
        if (!photo.like) {
            photoRepository.setLike(
                photo = photo,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    setRefreshPhoto(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        } else {
            photoRepository.deleteLike(
                photo = photo,
                onCompleteCallback = { abbreviatedPhotoRemote ->
                    setRefreshPhoto(abbreviatedPhotoRemote)
                },
                onErrorCallback = { error ->
                    setErrorState(error)
                }
            )
        }

    private fun setRefreshPhoto(abbreviatedPhotoRemote: AbbreviatedPhotoRemote) =
        viewModelScope.launch(errorHandler) {
            photoRepository.setRefreshPhoto(abbreviatedPhotoRemote)
        }

    private fun setErrorState(throwable: Throwable) {
        _uiState.value = _uiState.value.copy(
            //loading = false,
            errorMessage = throwable.message.toString()
        )
    }

    data class HomeScreenState(
        val photoFlow: Flow<PagingData<Photo>>? = null,
        //val loading: Boolean = false,
        val errorMessage: String = String()
    )
}