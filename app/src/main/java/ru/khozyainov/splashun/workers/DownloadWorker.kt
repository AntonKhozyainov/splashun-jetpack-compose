package ru.khozyainov.splashun.workers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.*
import androidx.core.content.FileProvider
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.khozyainov.splashun.R
import ru.khozyainov.splashun.data.network.api.PhotoApi
import ru.khozyainov.splashun.notifications.SplashUnNotifications.makeProgressNotification
import java.io.File
import java.io.FileOutputStream
import java.util.*

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val photoApi: PhotoApi
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        //if (isStopped) return Result.failure()
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return Result.failure(
            workDataOf(TAG_OUTPUT_FAILURE to applicationContext.getString(R.string.external_not_available))
        )

        return withContext(Dispatchers.IO) {

            return@withContext try {
                val imageUri =
                    inputData.getString(DOWNLOAD_URL_KEY) ?: return@withContext Result.failure()

                val fileName =
                    inputData.getString(DOWNLOAD_FILE_NAME_KEY) ?: return@withContext Result.failure()

                makeProgressNotification(applicationContext, fileName) //TODO сделать оповещениче что фото загрузилось

                val fileUri = downloadAndSavePhoto("$fileName.png", imageUri)

                if (fileUri.isEmpty()){
                    Result.failure(
                        workDataOf(TAG_OUTPUT_FAILURE to "File uri = null") //TODO
                    )
                }else{
                    Result.success(
                        workDataOf(TAG_OUTPUT_SUCCESS to fileUri)
                    )
                }

            } catch (exception: Exception) {
                Result.failure(
                    workDataOf(TAG_OUTPUT_FAILURE to exception.toString())
                )
            }
        }
    }

    private suspend fun downloadAndSavePhoto(
        fileName: String,
        imageUri: String
    ): String {

        val bitmap = BitmapFactory.decodeStream(
            photoApi.downloadPhoto(imageUri).byteStream()
        )

        //showProgressNotification

        return if (SDK_INT < Q) {
            @Suppress("DEPRECATION")
            val file = File(applicationContext.getExternalFilesDir(DIRECTORY_PICTURES), fileName)
            withContext(Dispatchers.IO) {
                FileOutputStream(file).use {
                    bitmap.compress(PNG, 100, it)
                }
            }
            MediaScannerConnection.scanFile(applicationContext,
                arrayOf(file.absolutePath), null, null)
            FileProvider.getUriForFile(applicationContext,
                "${ applicationContext.packageName }.provider", file)
        } else {
            val values = ContentValues().apply {
                put(DISPLAY_NAME, fileName)
                put(MIME_TYPE, "image/png")
                put(RELATIVE_PATH, DIRECTORY_DCIM)
                put(IS_PENDING, 1)
            }

            val resolver = applicationContext.contentResolver
            val uri = resolver.insert(EXTERNAL_CONTENT_URI, values)
            uri?.let { resolver.openOutputStream(it) }
                ?.use {
                    bitmap.compress(PNG, 100, it)
                }

            values.clear()
            values.put(IS_PENDING, 0)
            uri?.also {
                resolver.update(it, values, null, null) }
        }.toString()
    }

    companion object {
        const val TAG_OUTPUT_SUCCESS = "OUTPUT_SUCCESS"
        const val TAG_OUTPUT_FAILURE = "OUTPUT_FAILURE"
        const val DOWNLOAD_URL_KEY = "data_uri_key"
        const val DOWNLOAD_FILE_NAME_KEY = "data_name_key"
    }
}