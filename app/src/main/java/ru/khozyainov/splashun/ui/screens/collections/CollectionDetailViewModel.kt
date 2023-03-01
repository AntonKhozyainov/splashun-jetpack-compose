package ru.khozyainov.splashun.ui.screens.collections

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class CollectionDetailViewModel @Inject constructor(

): ViewModel() {

//    fun getPhotoById(photoId: String?, refresh: Boolean = false) {
//        if ((this.photoId.value == photoId || photoId == null) && !refresh) return
//        this.photoId.value = photoId!!
//    }
}