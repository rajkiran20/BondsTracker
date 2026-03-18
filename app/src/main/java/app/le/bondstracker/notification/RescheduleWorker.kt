package app.le.bondstracker.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.le.bondstracker.data.repository.BondRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

@HiltWorker
class RescheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: BondRepository,
    private val notificationScheduler: NotificationScheduler
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val bonds = repository.getAllBonds().firstOrNull() ?: emptyList()

            for (bond in bonds) {
                // Schedule or immediate trigger for upcoming payouts
                bond.payouts.filter { it.status.lowercase() == "upcoming" }.forEach { payout ->
                    try {
                        notificationScheduler.schedulePayoutReminder(
                            bondId = bond.investmentId,
                            companyName = bond.companyName,
                            payoutDate = payout.date,
                            amount = payout.amount
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // Schedule or immediate trigger for active bonds maturity
                if (bond.status.lowercase() == "active") {
                    try {
                        notificationScheduler.scheduleMaturityReminder(
                            bondId = bond.investmentId,
                            companyName = bond.companyName,
                            maturityDate = bond.maturityDate
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
    }
}
