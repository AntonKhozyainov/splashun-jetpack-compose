package ru.khozyainov.splashun.ui.screens.collections

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import ru.khozyainov.splashun.data.repository.collections.CollectionsRepository
import ru.khozyainov.splashun.ui.models.PhotoCollection
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val collectionsRepository: CollectionsRepository
): ViewModel() {

    private val _uiCollectionsState = MutableStateFlow(CollectionsScreenState())
    val uiCollectionsState: StateFlow<CollectionsScreenState> = _uiCollectionsState.asStateFlow()

    init {
        refreshState()
    }

    fun refreshState() {
        _uiCollectionsState.value = _uiCollectionsState.value.copy(
            pagingCollectionsFlow = collectionsRepository.getCollections(),
            errorMessage = String()
        )
    }

    data class CollectionsScreenState(
        val pagingCollectionsFlow: Flow<PagingData<PhotoCollection>> = emptyFlow(),
        val errorMessage: String = String()
    )
}