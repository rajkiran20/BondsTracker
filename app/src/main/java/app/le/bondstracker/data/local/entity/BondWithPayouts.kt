package app.le.bondstracker.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BondWithPayouts(
    @Embedded val bond: BondEntity,
    @Relation(
        parentColumn = "investmentId",
        entityColumn = "bondId"
    )
    val payouts: List<PayoutEntity>
)
