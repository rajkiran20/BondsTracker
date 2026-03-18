package app.le.bondstracker.notification

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    private val context: Context
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun schedulePayoutReminder(
        bondId: String,
        companyName: String,
        payoutDate: String,
        amount: Double
    ) {
        scheduleReminder(
            tag = "payout_${bondId}_${payoutDate}",
            targetDate = payoutDate,
            title = "Payout Due — $companyName",
            body = "Your payout of ₹${"%.2f".format(amount)} is scheduled today.",
            notificationId = "payout_${bondId}".hashCode()
        )
    }

    fun scheduleMaturityReminder(
        bondId: String,
        companyName: String,
        maturityDate: String
    ) {
        scheduleReminder(
            tag = "maturity_${bondId}",
            targetDate = maturityDate,
            title = "Bond Maturity — $companyName",
            body = "Your bond matures today! Check your account for the final payout.",
            notificationId = "maturity_${bondId}".hashCode()
        )
    }

    private fun scheduleReminder(
        tag: String,
        targetDate: String,
        title: String,
        body: String,
        notificationId: Int
    ) {
        try {
            val date = LocalDate.parse(targetDate, dateFormatter)
            val targetDateTime = date.atTime(10, 0) // 10 AM on the day

            var delayMillis = targetDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - System.currentTimeMillis()

            if (delayMillis < 0) {
                // If past due (missed while off), execute immediately
                delayMillis = 0L
            }

            val data = Data.Builder()
                .putString(KEY_NOTIFICATION_TITLE, title)
                .putString(KEY_NOTIFICATION_BODY, body)
                .putInt(KEY_NOTIFICATION_ID, notificationId)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<BondReminderWorker>()
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(tag)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(tag, androidx.work.ExistingWorkPolicy.REPLACE, workRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelReminders(bondId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag("payout_$bondId")
        WorkManager.getInstance(context).cancelAllWorkByTag("maturity_$bondId")
    }
}
