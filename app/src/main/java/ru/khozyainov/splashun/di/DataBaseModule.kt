package ru.khozyainov.splashun.di

//import android.app.Application
//import androidx.room.Room
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import ru.khozyainov.splashun.data.db.SplashUnDataBase
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataBaseModule {
//
//    @Provides
//    @Singleton
//    fun providesDatabase(context: Application): SplashUnDataBase = Room.databaseBuilder(
//        context,
//        SplashUnDataBase::class.java,
//        SplashUnDataBase.DB_NAME
//    ).build()
//
////    @Provides
////    fun providesItemPhotoDAO(db: SplashUnDataBase): ItemPhotoDao = db.itemPhotoDao()
//
//}