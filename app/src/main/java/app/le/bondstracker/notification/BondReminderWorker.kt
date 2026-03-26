package app.le.bondstracker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

const val NOTIFICATION_CHANNEL_ID = "bond_reminders"
const val KEY_NOTIFICATION_TITLE = "notification_title"
const val KEY_NOTIFICATION_BODY = "notification_body"
const val KEY_NOTIFICATION_ID = "notification_id"
const val KEY_BOND_ID = "bond_id"

@HiltWorker
class BondReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_NOTIFICATION_TITLE) ?: "Bond Reminder"
        val body = inputData.getString(KEY_NOTIFICATION_BODY) ?: "You have an upcoming bond event."
        val notificationId = inputData.getInt(KEY_NOTIFICATION_ID, System.currentTimeMillis().toInt())

        val tag = inputData.getString("tag")
        val prefs = context.getSharedPreferences("NotificationsPrefs", Context.MODE_PRIVATE)
        if (tag != null && prefs.getBoolean(tag, false)) {
            return Result.success() // Already shown
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            context.getString(app.le.bondstracker.R.string.channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(app.le.bondstracker.R.string.channel_description)
        }
        notificationManager.createNotificationChannel(channel)

        val bondId = inputData.getString(KEY_BOND_ID)
        val pendingIntent = if (bondId != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://bonds/detail/$bondId")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            null
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)

        if (tag != null) {
            prefs.edit().putBoolean(tag, true).apply()
        }

        return Result.success()
    }
}
