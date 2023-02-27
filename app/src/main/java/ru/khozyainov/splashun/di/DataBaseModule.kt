package ru.khozyainov.splashun.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.khozyainov.splashun.data.db.SplashUnDataBase
import ru.khozyainov.splashun.data.db.dao.CollectionsDao
import ru.khozyainov.splashun.data.db.dao.PhotoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun providesDatabase(context: Application): SplashUnDataBase = Room.databaseBuilder(
        context,
        SplashUnDataBase::class.java,
        SplashUnDataBase.DB_NAME
    ).build()

    @Provides
    fun providesPhotoDAO(db: SplashUnDataBase): PhotoDao = db.photoDao()

    @Provides
    fun providesCollectionsDAO(db: SplashUnDataBase): CollectionsDao = db.collectionsDao()
}