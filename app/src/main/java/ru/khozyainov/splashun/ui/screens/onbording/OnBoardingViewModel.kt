package ru.khozyainov.splashun.ui.screens.onbording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.khozyainov.splashun.data.onboarding.OnBoardingRepository
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {
    fun saveOnBoardingState(completed: Boolean) = viewModelScope.launch {
        onBoardingRepository.saveOnBoardingState(completed = completed)
    }
}