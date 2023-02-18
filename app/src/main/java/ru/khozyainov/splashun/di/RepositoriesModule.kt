package ru.khozyainov.splashun.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.khozyainov.splashun.data.repository.auth.AuthRepository
//import ru.khozyainov.splashun.data.repository.auth.AuthRepositoryImpl
import ru.khozyainov.splashun.data.repository.onboarding.OnBoardingRepository
import ru.khozyainov.splashun.data.repository.onboarding.OnBoardingRepositoryImpl
import ru.khozyainov.splashun.data.repository.photo.PhotoRepository
import ru.khozyainov.splashun.data.repository.photo.PhotoRepositoryImpl
import ru.khozyainov.splashun.data.repository.workmanager.WorkManagerRepository
import ru.khozyainov.splashun.data.repository.workmanager.WorkManagerRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule {

//    @Binds
//    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun providePhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    abstract fun provideOnBoardingRepository(onBoardingRepositoryImpl: OnBoardingRepositoryImpl): OnBoardingRepository

    @Binds
    abstract fun provideWorkManagerRepository(workManagerRepositoryImpl: WorkManagerRepositoryImpl): WorkManagerRepository

}