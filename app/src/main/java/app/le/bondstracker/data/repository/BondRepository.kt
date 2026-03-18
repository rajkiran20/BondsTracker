package app.le.bondstracker.data.repository

import app.le.bondstracker.data.dto.BondDto
import app.le.bondstracker.data.local.dao.BondDao
import app.le.bondstracker.data.local.entity.BondEntity
import app.le.bondstracker.data.local.entity.PayoutEntity
import app.le.bondstracker.data.mapper.toDomain
import app.le.bondstracker.domain.model.Bond
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BondRepository @Inject constructor(
    private val bondDao: BondDao,
    private val gson: Gson
) {

    fun getAllBonds(): Flow<List<Bond>> =
        bondDao.getAllBondsWithPayouts().map { list ->
            list.map { it.toDomain() }
        }

    suspend fun getBondById(id: String): Bond? =
        bondDao.getBondWithPayoutsById(id)?.toDomain()

    suspend fun deleteBondById(id: String) {
        val bondEntity = bondDao.getBondById(id)
        if (bondEntity != null) {
            bondDao.deleteBond(bondEntity)
        }
    }

    /**
     * Parse a JSON string array of bonds and insert them into the database.
     * Throws an exception if JSON is invalid.
     */
    suspend fun addBondsFromJson(json: String) {
        val type = object : TypeToken<List<BondDto>>() {}.type
        val dtos: List<BondDto> = gson.fromJson(json, type)

        val allBonds = mutableListOf<BondEntity>()
        val allPayouts = mutableListOf<PayoutEntity>()

        for (dto in dtos) {
            val existingBond = bondDao.getBondById(dto.investmentId)
            if (existingBond != null) {
                throw Exception("Bond with ID ${dto.investmentId} already exists")
            }

            val bondTypeJson = gson.toJson(dto.bondType)

            val bondEntity = try {
                BondEntity(
                    investmentId = dto.investmentId,
                    createdAt = dto.createdAt,
                    platform = dto.platform,
                    investor = dto.investor,
                    companyName = dto.companyName,
                    bondCategory = dto.bondCategory,
                    bondTypeJson = bondTypeJson,
                    status = if (dto.status.equals("closed", ignoreCase = true)) "matured" else dto.status,
                    currency = dto.currency,
                    investmentAmount = dto.investmentAmount,
                    faceValuePerUnit = dto.faceValuePerUnit,
                    units = dto.units,
                    currentValue = dto.currentValue,
                    outstandingPrincipal = dto.outstandingPrincipal,
                    returnsReceived = dto.returnsReceived,
                    gains = dto.gains,
                    totalPrincipalRepaid = dto.totalPrincipalRepaid,
                    interestRate = dto.interestRate,
                    couponRate = dto.couponRate,
                    payoutFrequency = dto.payoutFrequency,
                    startDate = dto.startDate,
                    orderDate = dto.orderDate,
                    maturityDate = dto.maturityDate,
                    tenureMonths = dto.tenureMonths,
                    interestPaid = dto.interestPaid,
                    nextPayoutDate = dto.nextPayoutDate,
                    notes = dto.notes
                )
            } catch (e: Exception) {
                throw Exception("Error in bond '${dto.companyName}' (ID: ${dto.investmentId}): missing or invalid field. Details: ${e.message}")
            }
            allBonds.add(bondEntity)

            val payoutEntities = try {
                dto.payouts.map { p ->
                    PayoutEntity(
                        payoutId = p.payoutId,
                        bondId = dto.investmentId,
                        date = p.date,
                        payoutType = p.payoutType,
                        amount = p.amount,
                        principalComponent = p.principalComponent,
                        interestComponent = p.interestComponent,
                        status = p.status
                    )
                }
            } catch (e: Exception) {
                throw Exception("Error in payouts for bond '${dto.companyName}' (ID: ${dto.investmentId}): missing or invalid field. Details: ${e.message}")
            }
            allPayouts.addAll(payoutEntities)
        }

        bondDao.insertBondsWithPayouts(allBonds, allPayouts)
    }
}
