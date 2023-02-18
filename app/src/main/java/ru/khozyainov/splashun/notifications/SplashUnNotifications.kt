package ru.khozyainov.splashun.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.khozyainov.splashun.R

object SplashUnNotifications {

    private const val DOWNLOAD_PHOTO_CHANNEL_ID = "download_photo"
    private const val DOWNLOAD_NOTIFICATION_ID = 1

    fun createChannels(context: Context){
        //Если версия android меньше 8, каналы не нужны
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannelDownloadPhoto(context)
        }
    }

    fun makeProgressNotification(context: Context, tag: String) {

        val notification = NotificationCompat.Builder(
            context,
            SplashUnNotifications.DOWNLOAD_PHOTO_CHANNEL_ID
        )
            .setContentTitle(context.getString(R.string.download))
            .setContentText(context.getString(R.string.notification_downloading))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_download)
            .build()

        NotificationManagerCompat.from(context)
            .notify(tag, DOWNLOAD_NOTIFICATION_ID, notification)
    }

    //Вызываем это метод с версии api android 26
    //Метод с такой аннотацией можно вызвать только после проверки версии как в fun create()
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelDownloadPhoto(context: Context){
        val nameChannel = context.getString(R.string.notification_channel_name)

        val notificationDescription = context.getString(R.string.notification_channel_description)
        val priority = NotificationManager.IMPORTANCE_LOW

        val channel = NotificationChannel(DOWNLOAD_PHOTO_CHANNEL_ID,nameChannel,priority).apply {
            description = notificationDescription
        }

        NotificationManagerCompat.from(context)
            .createNotificationChannel(channel)
    }

}
