package ru.khozyainov.splashun

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.khozyainov.splashun.notifications.SplashUnNotifications
import javax.inject.Inject

@HiltAndroidApp
class SplashUnApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        SplashUnNotifications.createChannels(this)
    }

}