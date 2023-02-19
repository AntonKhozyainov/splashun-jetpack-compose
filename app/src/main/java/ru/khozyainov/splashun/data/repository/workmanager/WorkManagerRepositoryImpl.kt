package ru.khozyainov.splashun.data.repository.workmanager

import androidx.work.*
import ru.khozyainov.splashun.workers.DownloadWorker
import ru.khozyainov.splashun.workers.DownloadWorker.Companion.DOWNLOAD_PHOTO_ID_KEY
import ru.khozyainov.splashun.workers.DownloadWorker.Companion.DOWNLOAD_URL_KEY
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerRepositoryImpl @Inject constructor(
    private val workManager: WorkManager
) : WorkManagerRepository {

//    override val outputWorkInfo: Flow<WorkInfo> =
//        workManager.getWorkInfosForUniqueWorkLiveData(DOWNLOAD_WORK_ID)
//            .asFlow()
//            .mapNotNull {
//                if (it.isNotEmpty()) it.last() else null
//            }
//

    override fun downloadPhoto(url: String, photoId: String) {
        //Для передачи в worker аргументов используем Data, аналог Bundle для воркера
        val workData = workDataOf(DOWNLOAD_URL_KEY to url, DOWNLOAD_PHOTO_ID_KEY to photoId)


        //Указываем условия выполнения работы
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .build()

        //OneTimeWorkRequestBuilder - выполняет работу только один раз
        //в WorkRequest описываем как должна быть выполненоа работа
        val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(constraints)
            //setBackoffCriteria - установим время отсрочки повторения задачи
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            //.addTag(TAG_OUTPUT)
            .setInputData(workData)
            .build()

        //Ставим в очередь на выполнение WorkRequest
        //ExistingWorkPolicy - указываес что делать если Work с таким id уже существует
        workManager.enqueueUniqueWork(
            photoId,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest
        )

    }

    override fun cancelWork(photoId: String) {
        workManager.cancelUniqueWork(photoId)
    }

//    companion object {
//        const val DOWNLOAD_WORK_ID = "download work id"
//    }

}