package ru.khozyainov.splashun.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import ru.khozyainov.splashun.data.auth.AuthRepository
import ru.khozyainov.splashun.data.onboarding.OnBoardingRepository
import ru.khozyainov.splashun.ui.screens.onbording.OnBoardingDestination
import ru.khozyainov.splashun.ui.screens.home.HomeDestination
import ru.khozyainov.splashun.ui.screens.login.LoginDestination
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    onboardingRepository: OnBoardingRepository,
    authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LauncherState>(LauncherState.Loading(loading = true))
    val uiState: StateFlow<LauncherState> = _uiState

    private val onBoardingCompleted = onboardingRepository.readOnBoardingState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val loginCompleted = authRepository.getTokenFlow()
        .map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        combine(
            onBoardingCompleted,
            loginCompleted
        ) { onBoarding, login ->
            onBoarding to login
        }.map { (onBoardingCompleted, loginCompleted) ->
            if (onBoardingCompleted != null && loginCompleted != null) {
                when {
                    (onBoardingCompleted && loginCompleted) -> {
                        _uiState.value = LauncherState.Success(HomeDestination.route)
                    }
                    (onBoardingCompleted && !loginCompleted) -> {
                        _uiState.value = LauncherState.Success(LoginDestination.route)
                    }
                    else -> {
                        _uiState.value = LauncherState.Success(OnBoardingDestination.route)
                    }
                }
                LauncherState.Loading(false)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, LauncherState.Loading(true))
    }


    sealed class LauncherState {
        data class Success(val route: String) : LauncherState()
        data class Loading(val loading: Boolean) : LauncherState()
    }
}