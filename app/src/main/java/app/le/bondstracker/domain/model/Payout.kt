package app.le.bondstracker.domain.model

data class Payout(
    val payoutId: String,
    val date: String,
    val payoutType: String,
    val amount: Double,
    val principalComponent: Double,
    val interestComponent: Double,
    val status: String
)
