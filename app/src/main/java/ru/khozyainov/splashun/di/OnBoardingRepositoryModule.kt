package ru.khozyainov.splashun.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.khozyainov.splashun.data.onboarding.OnBoardingRepository
import ru.khozyainov.splashun.data.onboarding.OnBoardingRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class OnBoardingRepositoryModule {
    @Binds
    abstract fun provideRepository(repositoryImpl: OnBoardingRepositoryImpl): OnBoardingRepository
}