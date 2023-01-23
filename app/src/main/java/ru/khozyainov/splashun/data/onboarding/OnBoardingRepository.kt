package ru.khozyainov.splashun.data.onboarding

import kotlinx.coroutines.flow.Flow

interface OnBoardingRepository {
    suspend fun saveOnBoardingState(completed: Boolean)
    fun readOnBoardingState(): Flow<Boolean>
}