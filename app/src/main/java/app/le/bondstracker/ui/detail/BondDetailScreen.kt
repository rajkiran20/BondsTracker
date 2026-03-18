package app.le.bondstracker.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.le.bondstracker.domain.model.Bond
import app.le.bondstracker.domain.model.Payout
import app.le.bondstracker.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BondDetailScreen(
    bondId: String,
    onNavigateBack: () -> Unit,
    viewModel: BondDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Investment") },
            text = { Text("Are you sure you want to delete this bond investment? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteBond { onNavigateBack() }
                }) {
                    Text("Delete", color = RedError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = TextPrimary)
                }
            },
            containerColor = NavyCard,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }

    Scaffold(
        containerColor = NavyDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        state.bond?.companyName ?: "Bond Details",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GoldPrimary)
                    }
                },
                actions = {
                    if (state.bond != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Bond", tint = RedError)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavySurface)
            )
        }
    ) { padding ->
        when {
            state.isLoading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GoldPrimary)
            }

            state.bond == null -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Bond not found", color = TextSecondary)
            }

            else -> BondDetailContent(bond = state.bond!!, paddingValues = padding)
        }
    }
}

@Composable
private fun BondDetailContent(bond: Bond, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero card
        item { BondHeroCard(bond) }

        // Financial metrics
        item {
            Spacer(Modifier.height(16.dp))
            SectionHeader("Financial Details")
        }
        item { FinancialDetailsCard(bond) }

        // Bond types
        if (bond.bondType.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bond.bondType.forEach { type ->
                        TypeChip(type)
                    }
                }
            }
        }

        // Payout schedule
        item {
            Spacer(Modifier.height(16.dp))
            SectionHeader("Payout Schedule (${bond.payouts.size})")
        }
        items(bond.payouts.sortedBy { it.date }, key = { it.payoutId }) { payout ->
            PayoutItem(payout)
        }

        // Notes
        if (!bond.notes.isNullOrBlank()) {
            item {
                Spacer(Modifier.height(8.dp))
                SectionHeader("Notes")
            }
            item {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        bond.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BondHeroCard(bond: Bond) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1E3A5F), NavyDeep))
            )
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(GoldDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.BusinessCenter,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        bond.platform,
                        style = MaterialTheme.typography.labelMedium,
                        color = GoldPrimary
                    )
                    Text(
                        bond.investor,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                "₹${formatAmount(bond.investmentAmount)}",
                style = MaterialTheme.typography.displayLarge,
                color = TextPrimary
            )
            Text(
                "${bond.units} units",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeroMetric("Start Date", formatDateString(bond.startDate))
                HeroMetric("Maturity", formatDateString(bond.maturityDate))
                HeroMetric("Tenure", calculateTenure(bond.startDate, bond.maturityDate))
            }
        }
    }
}

@Composable
private fun HeroMetric(label: String, value: String) {
    Column {
        Text(value, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}

@Composable
private fun FinancialDetailsCard(bond: Bond) {
    val displayReturnsReceived = if (bond.returnsReceived == 0.0) {
        bond.payouts.filter { it.status.lowercase() == "received" }.sumOf { it.amount }
    } else {
        bond.returnsReceived
    }

    val displayOutstandingPrincipal = if (bond.outstandingPrincipal == 0.0) {
        bond.investmentAmount - bond.payouts.filter { it.status.lowercase() == "received" }.sumOf { it.principalComponent }
    } else {
        bond.outstandingPrincipal
    }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            DetailRow("Interest Rate", "${bond.interestRate}%", isHighlight = true)
            DetailRow("Coupon Rate", "${bond.couponRate}%")
            DetailRow("Payout Frequency", bond.payoutFrequency.replaceFirstChar { it.uppercase() })
            DetailRow("Outstanding Principal", "₹${formatAmount(displayOutstandingPrincipal)}")
            DetailRow("Returns Received", "₹${formatAmount(displayReturnsReceived)}", isHighlight = true)
            DetailRow("Interest Paid", "₹${formatAmount(bond.interestPaid)}")
            DetailRow("Total Principal Repaid", "₹${formatAmount(bond.totalPrincipalRepaid)}")
            if (bond.nextPayoutDate != null) {
                DetailRow("Next Payout Date", formatDateString(bond.nextPayoutDate), isHighlight = true)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isHighlight) GoldPrimary else TextPrimary,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
    }
    HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
}

@Composable
private fun PayoutItem(payout: Payout) {
    val isReceived = payout.status.lowercase() == "received"
    val dotColor = if (isReceived) GreenSuccess else AmberWarning
    val statusIcon: ImageVector = if (isReceived) Icons.Default.CheckCircle else Icons.Default.Schedule

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline dot + line
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(statusIcon, contentDescription = null, tint = dotColor, modifier = Modifier.size(24.dp))
            if (!isReceived) {
                Box(
                    Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(DividerColor)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatDateString(payout.date),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "₹${formatAmount(payout.amount)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isReceived) GreenSuccess else GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        payout.payoutType.replace("_", " ").replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Text(
                        payout.status.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = dotColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Principal: ₹${formatAmount(payout.principalComponent)}  •  Interest: ₹${formatAmount(payout.interestComponent)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
    )
}

@Composable
private fun TypeChip(type: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(NavyElevated)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(type, style = MaterialTheme.typography.labelSmall, color = GoldPrimary)
    }
}

private fun formatAmount(amount: Double): String {
    val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
    format.maximumFractionDigits = 0
    return format.format(amount)
}

private fun formatDateString(dateStr: String?): String {
    if (dateStr.isNullOrBlank()) return ""
    return try {
        val parsedDate = try {
            if (dateStr.contains("T")) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(dateStr)
            } else {
                SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr)
            }
        } catch (e: Exception) { null }

        if (parsedDate != null) {
            SimpleDateFormat("dd/MM/yyyy", Locale.US).format(parsedDate)
        } else dateStr
    } catch (e: Exception) {
        dateStr
    }
}

private fun calculateTenure(startDateStr: String?, maturityDateStr: String?): String {
    if (startDateStr.isNullOrBlank() || maturityDateStr.isNullOrBlank()) return "0W"
    try {
        val format1 = if (startDateStr.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val format2 = if (maturityDateStr.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
        
        val start = format1.parse(startDateStr)
        val end = format2.parse(maturityDateStr)
        
        if (start == null || end == null) return "0W"

        val startCal = Calendar.getInstance().apply { time = start }
        val endCal = Calendar.getInstance().apply { time = end }

        var years = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)
        var months = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH)
        var days = endCal.get(Calendar.DAY_OF_MONTH) - startCal.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            months -= 1
            val cal = Calendar.getInstance().apply { time = endCal.time }
            cal.add(Calendar.MONTH, -1)
            days += cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        if (months < 0) {
            years -= 1
            months += 12
        }

        val weeks = days / 7

        val parts = mutableListOf<String>()
        if (years > 0) parts.add("${years}Y")
        if (months > 0) parts.add("${months}M")
        if (weeks > 0) parts.add("${weeks}W")

        return if (parts.isEmpty()) "0W" else parts.joinToString("")
    } catch (e: Exception) {
        return "0W"
    }
}
