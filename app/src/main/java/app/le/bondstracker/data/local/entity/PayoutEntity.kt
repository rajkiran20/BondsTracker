package app.le.bondstracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payouts",
    foreignKeys = [
        ForeignKey(
            entity = BondEntity::class,
            parentColumns = ["investmentId"],
            childColumns = ["bondId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bondId"])]
)
data class PayoutEntity(
    @PrimaryKey
    val payoutId: String,
    val bondId: String,
    val date: String,
    val payoutType: String,
    val amount: Double,
    val principalComponent: Double,
    val interestComponent: Double,
    val status: String
)
