package com.kxtdev.bukkydatasup.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.kxtdev.bukkydatasup.domain.repository.AppRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class RefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: AppRepository,
): CoroutineWorker(context,workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.apply {
                getUserProfile()
                saveUserOrUpdate()
                saveDataPlansOrUpdate()
                saveCablePlansOrUpdate()
                saveBanksOrUpdate()
                saveAirtimeHistoryOrUpdate()
                saveDataHistoryOrUpdate()
                saveCableHistoryOrUpdate()
                saveMeterHistoryOrUpdate()
                saveResultCheckerHistoryOrUpdate()
                saveWalletSummaryOrUpdate()
                savePrintCardHistoryOrUpdate()
                saveBulkSMSHistoryOrUpdate()
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }


    companion object {

        fun engage(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<RefreshWorker>()
                .setConstraints(internetConstraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    15L,
                    TimeUnit.SECONDS
                )
                .build()

            WorkManager.getInstance(context.applicationContext).enqueue(workRequest)
        }
    }

}
