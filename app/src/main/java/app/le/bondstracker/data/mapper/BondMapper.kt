package app.le.bondstracker.data.mapper

import app.le.bondstracker.data.dto.BondDto
import app.le.bondstracker.data.dto.PayoutDto
import app.le.bondstracker.data.local.entity.BondEntity
import app.le.bondstracker.data.local.entity.BondWithPayouts
import app.le.bondstracker.data.local.entity.PayoutEntity
import app.le.bondstracker.domain.model.Bond
import app.le.bondstracker.domain.model.Payout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun BondWithPayouts.toDomain(): Bond {
    val gson = Gson()
    val bondTypeList: List<String> = try {
        val type = object : TypeToken<List<String>>() {}.type
        gson.fromJson(bond.bondTypeJson, type) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    return Bond(
        investmentId = bond.investmentId,
        createdAt = bond.createdAt,
        platform = bond.platform,
        investor = bond.investor,
        companyName = bond.companyName,
        bondCategory = bond.bondCategory,
        bondType = bondTypeList,
        status = bond.status,
        currency = bond.currency,
        investmentAmount = bond.investmentAmount,
        faceValuePerUnit = bond.faceValuePerUnit,
        units = bond.units,
        currentValue = bond.currentValue,
        outstandingPrincipal = bond.outstandingPrincipal,
        returnsReceived = bond.returnsReceived,
        gains = bond.gains,
        totalPrincipalRepaid = bond.totalPrincipalRepaid,
        interestRate = bond.interestRate,
        couponRate = bond.couponRate,
        payoutFrequency = bond.payoutFrequency,
        startDate = bond.startDate,
        orderDate = bond.orderDate,
        maturityDate = bond.maturityDate,
        tenureMonths = bond.tenureMonths,
        interestPaid = bond.interestPaid,
        nextPayoutDate = bond.nextPayoutDate,
        notes = bond.notes,
        payouts = payouts.map { it.toDomain() }
    )
}

fun PayoutEntity.toDomain(): Payout = Payout(
    payoutId = payoutId,
    date = date,
    payoutType = payoutType,
    amount = amount,
    principalComponent = principalComponent,
    interestComponent = interestComponent,
    status = status
)

fun Bond.toDto(): BondDto = BondDto(
    investmentId = investmentId,
    createdAt = createdAt,
    platform = platform,
    investor = investor,
    companyName = companyName,
    bondCategory = bondCategory,
    bondType = bondType,
    status = status,
    currency = currency,
    investmentAmount = investmentAmount,
    faceValuePerUnit = faceValuePerUnit,
    units = units,
    currentValue = currentValue,
    outstandingPrincipal = outstandingPrincipal,
    returnsReceived = returnsReceived,
    gains = gains,
    totalPrincipalRepaid = totalPrincipalRepaid,
    interestRate = interestRate,
    couponRate = couponRate,
    payoutFrequency = payoutFrequency,
    startDate = startDate,
    orderDate = orderDate,
    maturityDate = maturityDate,
    tenureMonths = tenureMonths,
    interestPaid = interestPaid,
    nextPayoutDate = nextPayoutDate,
    notes = notes,
    payouts = payouts.map { it.toDto() }
)

fun Payout.toDto(): PayoutDto = PayoutDto(
    payoutId = payoutId,
    date = date,
    payoutType = payoutType,
    amount = amount,
    principalComponent = principalComponent,
    interestComponent = interestComponent,
    status = status
)
