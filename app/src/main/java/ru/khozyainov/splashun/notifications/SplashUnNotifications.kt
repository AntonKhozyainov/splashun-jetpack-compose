package ru.khozyainov.splashun.notifications

import android.app.*
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
            DOWNLOAD_PHOTO_CHANNEL_ID
        )
            .setContentTitle(context.getString(R.string.download))
            .setContentText(context.getString(R.string.notification_downloading))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.drawable.ic_download)
            .setVibrate(longArrayOf(100))
            .setProgress(0,0,true)
            .build()

        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT

        NotificationManagerCompat.from(context)
            .notify(tag, DOWNLOAD_NOTIFICATION_ID, notification)
    }

    fun makeDownloadCompleteNotification(context: Context, tag: String, photo: Bitmap, photoUri: Uri) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(photoUri, context.contentResolver.getType(photoUri))

        val notification = NotificationCompat.Builder(
            context,
            DOWNLOAD_PHOTO_CHANNEL_ID
        )
            .setContentTitle(context.getString(R.string.download))
            .setContentText(context.getString(R.string.photo_downloaded))
            .setShowWhen(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.drawable.ic_download)
            .setLargeIcon(photo)
            .setVibrate(longArrayOf(100))
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    1,
                    intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PendingIntent.FLAG_IMMUTABLE or FLAG_ONE_SHOT
                    } else{
                        FLAG_ONE_SHOT
                    }
                )
            )
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(tag, DOWNLOAD_NOTIFICATION_ID, notification)
    }

    fun cancelProgressNotification(context: Context, tag: String) {
        NotificationManagerCompat.from(context)
            .cancel(tag, DOWNLOAD_NOTIFICATION_ID)
    }

    //Вызываем это метод с версии api android 26
    //Метод с такой аннотацией можно вызвать только после проверки версии как в fun create()
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelDownloadPhoto(context: Context){
        val nameChannel = context.getString(R.string.notification_channel_name)

        val notificationDescription = context.getString(R.string.notification_channel_description)
        val priority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(DOWNLOAD_PHOTO_CHANNEL_ID,nameChannel,priority).apply {
            description = notificationDescription
            enableVibration(true)
            vibrationPattern = longArrayOf(100)
        }

        NotificationManagerCompat.from(context)
            .createNotificationChannel(channel)
    }

}
