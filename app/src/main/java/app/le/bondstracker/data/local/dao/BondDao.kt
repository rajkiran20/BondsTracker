package app.le.bondstracker.data.local.dao

import androidx.room.*
import app.le.bondstracker.data.local.entity.BondEntity
import app.le.bondstracker.data.local.entity.BondWithPayouts
import app.le.bondstracker.data.local.entity.PayoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BondDao {

    @Transaction
    @Query("SELECT * FROM bonds ORDER BY maturityDate ASC")
    fun getAllBondsWithPayouts(): Flow<List<BondWithPayouts>>

    @Transaction
    @Query("SELECT * FROM bonds WHERE investmentId = :id")
    suspend fun getBondWithPayoutsById(id: String): BondWithPayouts?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBond(bond: BondEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBonds(bonds: List<BondEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayouts(payouts: List<PayoutEntity>)

    @Transaction
    suspend fun insertBondWithPayouts(bond: BondEntity, payouts: List<PayoutEntity>) {
        insertBond(bond)
        insertPayouts(payouts)
    }

    @Transaction
    suspend fun insertBondsWithPayouts(bonds: List<BondEntity>, payouts: List<PayoutEntity>) {
        insertBonds(bonds)
        insertPayouts(payouts)
    }

    @Delete
    suspend fun deleteBond(bond: BondEntity)

    @Query("SELECT * FROM bonds WHERE investmentId = :id")
    suspend fun getBondById(id: String): BondEntity?
}
