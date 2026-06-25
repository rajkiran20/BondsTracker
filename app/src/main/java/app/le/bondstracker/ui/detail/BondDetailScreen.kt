package app.le.bondstracker.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.le.bondstracker.R
import app.le.bondstracker.domain.model.Bond
import app.le.bondstracker.domain.model.Payout
import app.le.bondstracker.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

@Composable
fun BondDetailScreen(
    bondId: String,
    onNavigateBack: () -> Unit,
    viewModel: BondDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BondDetailScreen(
        state = state,
        onNavigateBack = onNavigateBack,
        onDeleteBond = { viewModel.deleteBond(onSuccess = onNavigateBack) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BondDetailScreen(
    state: DetailUiState,
    onNavigateBack: () -> Unit,
    onDeleteBond: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Investment") },
            text = { Text("Are you sure you want to delete this bond investment? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteBond()
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
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    if (state.bond != null) {
                        TextButton(onClick = { showDeleteDialog = true }) {
                            Text(
                                text = "DELETE",
                                color = RedError,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        when {
            state.isLoading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GeminiBlue)
            }

            state.bond == null -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Bond not found", color = TextSecondary)
            }

            else -> BondDetailContent(bond = state.bond, paddingValues = padding)
        }
    }
}
@Composable
private fun BondDetailContent(bond: Bond, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // --- FIXED TOP SECTION ---
        BondHeroHeader(bond)

        Spacer(Modifier.height(8.dp))

        // --- SCROLLABLE BOTTOM SECTION ---
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Takes up the remaining space below the fixed header
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Metrics card
            item {
                KeyMetricsCard(bond)
            }

            // Financial Snapshot Grid
            item {
                Spacer(Modifier.height(16.dp))
                SectionHeader("Financial Snapshot")
            }
            item { FinancialDetailsGrid(bond) }

            // Payout schedule
            item {
                Spacer(Modifier.height(16.dp))
                SectionHeader("Payout Schedule (${bond.payouts.size})")
            }

            val sortedPayouts = bond.payouts.sortedBy { it.date }
            itemsIndexed(sortedPayouts, key = { _, it -> it.payoutId }) { index, payout ->
                PayoutItem(
                    payout = payout,
                    isLast = index == sortedPayouts.size - 1
                )
            }

            // Bond types
            if (bond.bondType.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(24.dp))
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
                            text = bond.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BondHeroHeader(bond: Bond) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left Side: Details
            Column(
                modifier = Modifier.weight(1.3f)
            ) {
                // Company Name
                Text(
                    text = bond.companyName,
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = MaterialTheme.typography.titleLarge.fontSize * 1.25),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Investor Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GreenSuccess.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = bond.investor,
                            style = MaterialTheme.typography.labelSmall,
                            color = GreenSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    // Platform Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GeminiBlue.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = bond.platform,
                            style = MaterialTheme.typography.labelSmall,
                            color = GeminiBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // Right Side: Current Value Info
            Column(
                modifier = Modifier.weight(0.9f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Current Value",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "₹${formatAmount(bond.currentValue ?: bond.investmentAmount)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Invested: ₹${formatAmount(bond.investmentAmount)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = GreenSuccess,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun KeyMetricsCard(bond: Bond) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        HorizontalDivider(
            color = DividerColor.copy(alpha = 0.5f),
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val metrics = listOf(
                Triple("Issue Date", formatLongDateString(bond.startDate), Icons.Outlined.CalendarToday),
                Triple("Tenure", calculateTenure(bond.startDate, bond.maturityDate), Icons.Outlined.Timer),
                Triple("Maturity", formatLongDateString(bond.maturityDate), Icons.Outlined.CalendarToday)
            )

            metrics.forEachIndexed { index, metric ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val tintColor = when (index) {
                        0 -> Color(0xFF7E57C2) // Purple
                        1 -> Color(0xFF4CAF50) // Green
                        2 -> Color(0xFF2196F3) // Blue
                        else -> Color(0xFFFF9800) // Orange
                    }
                    Icon(
                        imageVector = metric.third,
                        contentDescription = null,
                        tint = tintColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = metric.first,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = metric.second,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (index < metrics.size - 1) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(48.dp)
                            .background(DividerColor.copy(alpha = 0.5f))
                    )
                }
            }
        }

        HorizontalDivider(
            color = DividerColor.copy(alpha = 0.5f),
            thickness = 0.5.dp
        )
    }
}

private data class FinancialSnapshotItem(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val iconColor: Color,
    val circleBgColor: Color,
    val valueTextColor: Color? = null
)

@Composable
private fun FinancialSnapshotCell(
    item: FinancialSnapshotItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(item.circleBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = item.valueTextColor ?: TextPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FinancialDetailsGrid(bond: Bond) {
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

    val items = listOf(
        FinancialSnapshotItem(
            label = "Interest Rate (p.a.)",
            value = "${bond.interestRate}%",
            icon = Icons.Outlined.Percent,
            iconColor = Color(0xFF7E57C2),
            circleBgColor = Color(0xFF7E57C2).copy(alpha = 0.12f)
        ),
        FinancialSnapshotItem(
            label = "Coupon Rate (p.a.)",
            value = "${bond.couponRate}%",
            icon = Icons.Outlined.LocalOffer,
            iconColor = Color(0xFFEF5350),
            circleBgColor = Color(0xFFEF5350).copy(alpha = 0.12f)
        ),
        FinancialSnapshotItem(
            label = "Payout Frequency",
            value = bond.payoutFrequency.replaceFirstChar { it.uppercase() },
            icon = Icons.Outlined.Timeline,
            iconColor = Color(0xFF4CAF50),
            circleBgColor = Color(0xFF4CAF50).copy(alpha = 0.12f)
        ),
        FinancialSnapshotItem(
            label = "Outstanding Principal",
            value = "₹${formatAmount(displayOutstandingPrincipal)}",
            icon = Icons.Outlined.AccountBalanceWallet,
            iconColor = Color(0xFF2196F3),
            circleBgColor = Color(0xFF2196F3).copy(alpha = 0.12f)
        ),
        FinancialSnapshotItem(
            label = "Returns Received",
            value = "+₹${formatAmount(displayReturnsReceived)}",
            icon = Icons.Default.CurrencyRupee,
            iconColor = Color(0xFF4CAF50),
            circleBgColor = Color(0xFF4CAF50).copy(alpha = 0.12f),
            valueTextColor = Color(0xFF4CAF50)
        ),
        FinancialSnapshotItem(
            label = "Interest Paid",
            value = "-₹${formatAmount(bond.interestPaid)}",
            icon = Icons.Default.CurrencyRupee,
            iconColor = Color(0xFF4CAF50),
            circleBgColor = Color(0xFF4CAF50).copy(alpha = 0.12f),
            valueTextColor = Color(0xFF4CAF50)
        ),
        FinancialSnapshotItem(
            label = "Total Principal Repaid",
            value = "₹${formatAmount(bond.totalPrincipalRepaid)}",
            icon = Icons.Outlined.AccountBalance,
            iconColor = Color(0xFF7E57C2),
            circleBgColor = Color(0xFF7E57C2).copy(alpha = 0.12f)
        ),
        FinancialSnapshotItem(
            label = "Next Payout Date",
            value = bond.nextPayoutDate?.let { formatLongDateString(it) } ?: "N/A",
            icon = Icons.Outlined.CalendarToday,
            iconColor = Color(0xFF2196F3),
            circleBgColor = Color(0xFF2196F3).copy(alpha = 0.12f),
            valueTextColor = Color(0xFF2196F3)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        val chunked = items.chunked(2)
        chunked.forEachIndexed { rowIndex, rowItems ->
            if (rowIndex > 0) {
                HorizontalDivider(
                    color = DividerColor.copy(alpha = 0.5f),
                    thickness = 0.5.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                rowItems.forEachIndexed { colIndex, item ->
                    if (colIndex > 0) {
                        VerticalDivider(
                            color = DividerColor.copy(alpha = 0.5f),
                            thickness = 0.5.dp,
                            modifier = Modifier
                                .fillMaxHeight()
                        )
                    }

                    FinancialSnapshotCell(
                        item = item,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                    )
                }

                if (rowItems.size < 3) {
                    repeat(2 - rowItems.size) { colIndex ->
                        val actualColIndex = rowItems.size + colIndex
                        if (actualColIndex > 0) {
                            VerticalDivider(
                                color = DividerColor.copy(alpha = 0.5f),
                                thickness = 0.5.dp,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
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
            color = if (isHighlight) GeminiBlue else TextPrimary,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
    }
    HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
}

@Composable
private fun PayoutItem(payout: Payout, isLast: Boolean) {
    val isReceived = payout.status.lowercase() == "received" || isPastDate(payout.date)
    val dotColor = if (isReceived) GreenSuccess else AmberWarning
    val statusIcon: ImageVector = if (isReceived) Icons.Default.CheckCircle else Icons.Default.Schedule

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically // Centers everything horizontally
    ) {
        // Timeline Column
        Box(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center // Centers the icon in the box
        ) {
            // The connecting line
            Box(
                modifier = Modifier
                    .width(2.dp)
                    // If it's the last item, only draw the line from the top down to the center.
                    // Otherwise, fill the entire height to connect to the next item.
                    .fillMaxHeight(if (isLast) 0.5f else 1f)
                    .align(Alignment.TopCenter)
                    .background(DividerColor)
            )

            // The Icon (drawn on top of the line)
            Icon(
                imageVector = statusIcon,
                contentDescription = null,
                tint = dotColor,
                modifier = Modifier
                    .size(24.dp)
                    .background(NavyDeep, CircleShape) // Masks the line underneath
            )
        }

        Spacer(Modifier.width(12.dp))

        // Payout Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isReceived) GreenSuccess.copy(alpha = 0.1f) else AmberWarning.copy(alpha = 0.1f)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (isReceived) GreenSuccess else AmberWarning
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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
                        color = if (isReceived) GreenSuccess else GeminiBlue,
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
                        if (isReceived) "Received" else "Upcoming",
                        style = MaterialTheme.typography.labelSmall,
                        color = dotColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                val textParts = mutableListOf<String>()
                if (payout.principalComponent > 0.0) {
                    textParts.add("Principal: ₹${formatAmount(payout.principalComponent)}")
                }
                if (payout.interestComponent > 0.0) {
                    textParts.add("Interest: ₹${formatAmount(payout.interestComponent)}")
                }
                if (textParts.isEmpty()) {
                    textParts.add("Interest: ₹${formatAmount(payout.amount)}")
                }
                if (textParts.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = textParts.joinToString("  •  "),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
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
        Text(type, style = MaterialTheme.typography.labelSmall, color = GeminiBlue)
    }
}

@Composable
private fun DottedDivider(modifier: Modifier = Modifier, color: Color = DividerColor) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
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

        val diffInMs = end.time - start.time
        val diffInDays = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffInMs)
        if (diffInDays < 120) {
            return "${maxOf(0L, diffInDays)}D"
        }

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

private fun getPlatformIcon(platform: String): Int? {
    return when (platform.lowercase(Locale.ROOT).trim()) {
        "jiraaf" -> R.drawable.ic_jiraaf
        "stable money", "stablemoney" -> R.drawable.ic_stablemoney
        else -> null
    }
}

private fun formatShortDateString(dateStr: String?): String {
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
            SimpleDateFormat("dd MMM", Locale.US).format(parsedDate)
        } else dateStr
    } catch (e: Exception) {
        dateStr
    }
}

private fun formatLongDateString(dateStr: String?): String {
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
            SimpleDateFormat("dd MMM yyyy", Locale.US).format(parsedDate)
        } else dateStr
    } catch (e: Exception) {
        dateStr
    }
}

private fun isPastDate(dateStr: String?): Boolean {
    if (dateStr.isNullOrBlank()) return false
    return try {
        val parsedDate = if (dateStr.contains("T")) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(dateStr)
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr)
        }
        parsedDate?.before(java.util.Date()) ?: false
    } catch (e: Exception) {
        false
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun BondDetailScreenPreview() {
    BondsTrackerTheme {
        val samplePayouts = listOf(
            Payout(
                payoutId = "1",
                date = "2024-03-15",
                payoutType = "Interest",
                amount = 1200.0,
                principalComponent = 0.0,
                interestComponent = 1200.0,
                status = "received"
            ),
            Payout(
                payoutId = "2",
                date = "2024-06-15",
                payoutType = "Interest",
                amount = 1200.0,
                principalComponent = 0.0,
                interestComponent = 1200.0,
                status = "pending"
            )
        )

        val sampleBond = Bond(
            investmentId = "bond_123",
            createdAt = "2023-12-15",
            platform = "Jiraaf",
            investor = "John Doe",
            companyName = "Green Energy Corp",
            bondCategory = "Corporate",
            bondType = listOf("Senior Secured", "Taxable"),
            status = "Active",
            currency = "INR",
            investmentAmount = 100000.0,
            faceValuePerUnit = 1000.0,
            units = 100,
            currentValue = 100000.0,
            outstandingPrincipal = 100000.0,
            returnsReceived = 1200.0,
            gains = 1200.0,
            totalPrincipalRepaid = 0.0,
            interestRate = 12.0,
            couponRate = 11.5,
            payoutFrequency = "Quarterly",
            startDate = "2023-06-15",
            orderDate = "2023-06-10",
            maturityDate = "2023-08-15",
            tenureMonths = 3,
            interestPaid = 1200.0,
            nextPayoutDate = "2024-06-15",
            notes = "This is a sample note for the bond investment.",
            payouts = samplePayouts
        )

        BondDetailScreen(
            state = DetailUiState(bond = sampleBond, isLoading = false),
            onNavigateBack = {},
            onDeleteBond = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BondDetailScreenLoadingPreview() {
    BondsTrackerTheme {
        BondDetailScreen(
            state = DetailUiState(isLoading = true),
            onNavigateBack = {},
            onDeleteBond = {}
        )
    }
}
