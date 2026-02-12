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
class ClearWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: AppRepository,
): CoroutineWorker(context,workerParams) {


    override suspend fun doWork(): Result {
        return try {
            repository.apply {
                clearUsers()
                clearDataPlans()
                clearCablePlans()
                clearBanks()
                clearAirtime()
                clearData()
                clearCable()
                clearMeter()
                clearResultChecker()
                clearWalletSummary()
                clearPrintCard()
                clearBulkSMS()
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }


    companion object {

        fun engage(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<ClearWorker>()
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