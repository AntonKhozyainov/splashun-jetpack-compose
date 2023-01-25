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

    //Получаем признак просмотрел ли онбординг
    private val onBoardingCompleted = onboardingRepository.readOnBoardingState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    //Проверяем есть ли токен, если есть то логиниться не нужно
    private val loginCompleted = authRepository.getTokenFlow()
        .map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val startDestination = combine(
        onBoardingCompleted,
        loginCompleted
    ){ onBoarding, login ->
        onBoarding to login
    }.map { (onBoardingCompleted, loginCompleted) ->
        when{
            (onBoardingCompleted && loginCompleted) -> HomeDestination.route
            (onBoardingCompleted && !loginCompleted) -> LoginDestination.route
            else -> OnBoardingDestination.route
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, OnBoardingDestination.route)


}