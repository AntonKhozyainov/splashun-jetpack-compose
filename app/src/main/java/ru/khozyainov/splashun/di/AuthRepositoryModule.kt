package ru.khozyainov.splashun.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.khozyainov.splashun.data.repository.auth.AuthRepository
import ru.khozyainov.splashun.data.repository.auth.AuthRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    abstract fun provideRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository
}