package app.le.bondstracker.data.dto

import com.google.gson.annotations.SerializedName

data class BondDto(
    @SerializedName("investment_id") val investmentId: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("platform") val platform: String,
    @SerializedName("investor") val investor: String,
    @SerializedName("company_name") val companyName: String,
    @SerializedName("bond_category") val bondCategory: String,
    @SerializedName("bond_type") val bondType: List<String>,
    @SerializedName("status") val status: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("investment_amount") val investmentAmount: Double,
    @SerializedName("face_value_per_unit") val faceValuePerUnit: Double,
    @SerializedName("units") val units: Int,
    @SerializedName("current_value") val currentValue: Double?,
    @SerializedName("outstanding_principal") val outstandingPrincipal: Double,
    @SerializedName("returns_received") val returnsReceived: Double,
    @SerializedName("gains") val gains: Double?,
    @SerializedName("total_principal_repaid") val totalPrincipalRepaid: Double,
    @SerializedName("interest_rate") val interestRate: Double,
    @SerializedName("coupon_rate") val couponRate: Double,
    @SerializedName("payout_frequency") val payoutFrequency: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("order_date") val orderDate: String,
    @SerializedName("maturity_date") val maturityDate: String,
    @SerializedName("tenure_months") val tenureMonths: Int,
    @SerializedName("interest_paid") val interestPaid: Double,
    @SerializedName("next_payout_date") val nextPayoutDate: String?,
    @SerializedName("notes") val notes: String?,
    @SerializedName("payouts") val payouts: List<PayoutDto>
)

data class PayoutDto(
    @SerializedName("payout_id") val payoutId: String,
    @SerializedName("date") val date: String,
    @SerializedName("payout_type") val payoutType: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("principal_component") val principalComponent: Double,
    @SerializedName("interest_component") val interestComponent: Double,
    @SerializedName("status") val status: String
)
