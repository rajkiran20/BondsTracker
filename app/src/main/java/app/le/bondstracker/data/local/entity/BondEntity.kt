package app.le.bondstracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bonds")
data class BondEntity(
    @PrimaryKey
    val investmentId: String,
    val createdAt: String,
    val platform: String,
    val investor: String,
    val companyName: String,
    val bondCategory: String,
    val bondTypeJson: String, // JSON array stored as string
    val status: String,
    val currency: String,
    val investmentAmount: Double,
    val faceValuePerUnit: Double,
    val units: Int,
    val currentValue: Double?,
    val outstandingPrincipal: Double,
    val returnsReceived: Double,
    val gains: Double?,
    val totalPrincipalRepaid: Double,
    val interestRate: Double,
    val couponRate: Double,
    val payoutFrequency: String,
    val startDate: String,
    val orderDate: String,
    val maturityDate: String,
    val tenureMonths: Int,
    val interestPaid: Double,
    val nextPayoutDate: String?,
    val notes: String?
)
