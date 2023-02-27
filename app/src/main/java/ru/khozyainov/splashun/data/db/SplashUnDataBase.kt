package ru.khozyainov.splashun.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.khozyainov.splashun.data.db.SplashUnDataBase.Companion.DB_VERSION
import ru.khozyainov.splashun.data.db.dao.CollectionsDao
import ru.khozyainov.splashun.data.db.dao.PhotoDao
import ru.khozyainov.splashun.data.db.entities.CollectionEntity
import ru.khozyainov.splashun.data.db.entities.PhotoEntity

@Database(
    entities =[
        PhotoEntity::class,
        CollectionEntity::class
    ],
    version = DB_VERSION
)
abstract class SplashUnDataBase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun collectionsDao(): CollectionsDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "splashun-database"
    }
}