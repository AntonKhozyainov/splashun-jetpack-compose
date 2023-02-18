package ru.khozyainov.splashun.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PROGRESS_NOTIFICATION_ID = 321123

suspend fun showProgressNotification(
    context: Context,
    builder: NotificationCompat.Builder,
    maxProgress: Int,
    currentProgress: Int,
    tag: String
) {
    val notification = builder
        .setProgress(maxProgress, currentProgress, false)
        .build()

    NotificationManagerCompat.from(context)
        .notify(tag, PROGRESS_NOTIFICATION_ID, notification)
}