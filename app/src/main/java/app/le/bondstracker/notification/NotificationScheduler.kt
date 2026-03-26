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

    private fun formatPayoutDate(targetDate: String): String {
        return try {
            val date = LocalDate.parse(targetDate, dateFormatter)
            val month = date.month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())
            val day = date.dayOfMonth
            "$day $month"
        } catch (e: Exception) {
            targetDate
        }
    }

    private fun formatAmount(amount: Double): String {
        val format = java.text.NumberFormat.getNumberInstance(java.util.Locale("en", "IN"))
        format.maximumFractionDigits = 0
        return "₹${format.format(amount)}"
    }

    fun schedulePayoutReminder(
        bondId: String,
        companyName: String,
        investorName: String,
        payoutDate: String,
        amount: Double
    ) {
        scheduleReminder(
            tag = "payout_${bondId}_${payoutDate}",
            targetDate = payoutDate,
            title = "Payout Due — $companyName",
            body = "of ${formatAmount(amount)} for $investorName on ${formatPayoutDate(payoutDate)}",
            notificationId = "payout_${bondId}".hashCode(),
            bondId = bondId
        )
    }

    fun scheduleMaturityReminder(
        bondId: String,
        companyName: String,
        investorName: String,
        maturityDate: String
    ) {
        scheduleReminder(
            tag = "maturity_${bondId}",
            targetDate = maturityDate,
            title = "Bond Maturity — $companyName",
            body = "Your bond for $investorName matures on ${formatPayoutDate(maturityDate)}! Check your account for the final payout.",
            notificationId = "maturity_${bondId}".hashCode(),
            bondId = bondId
        )
    }

    private fun scheduleReminder(
        tag: String,
        targetDate: String,
        title: String,
        body: String,
        notificationId: Int,
        bondId: String
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
                .putString(KEY_BOND_ID, bondId)
                .putString("tag", tag)
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
