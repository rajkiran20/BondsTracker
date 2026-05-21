package app.le.bondstracker.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import app.le.bondstracker.R
import app.le.bondstracker.domain.model.Bond
import app.le.bondstracker.ui.theme.*

import app.le.bondstracker.ui.MainViewModel

@Composable
fun HomeScreen(
    onAddBondClick: () -> Unit,
    onBondClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isDarkMode by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
    val isFiltered = state.selectedPlatforms.isNotEmpty() || state.selectedInvestors.isNotEmpty() || state.selectedStatuses.isNotEmpty()
    var filterPanelOpen by remember { mutableStateOf(false) }
    val hasFiltersAvailable = state.availablePlatforms.size > 1 || state.availableInvestors.size > 1 || state.availableStatuses.size > 1

    Scaffold(
        containerColor = NavyDeep,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBondClick,
                containerColor = GoldPrimary,
                contentColor = NavyDeep
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Investment", modifier = Modifier.size(28.dp))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // App Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(NavySurface, NavyDeep)))
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Bonds Tracker",
                                style = MaterialTheme.typography.headlineLarge,
                                color = TextPrimary
                            )
                        }

                        // 3-dots menu
                        var topMenuExpanded by remember { mutableStateOf(false) }
                        val context = LocalContext.current
                        
                        Box {
                            IconButton(onClick = { topMenuExpanded = true }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "Menu",
                                    tint = TextPrimary
                                )
                            }
                            DropdownMenu(
                                expanded = topMenuExpanded,
                                onDismissRequest = { topMenuExpanded = false },
                                containerColor = NavyElevated
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Toggle dark mode", color = TextPrimary) },
                                    onClick = {
                                        mainViewModel.toggleDarkMode()
                                        topMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Export investments", color = TextPrimary) },
                                    onClick = {
                                        val json = viewModel.exportBondsToJson()
                                        try {
                                            var folder = java.io.File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS), "Bond Investments")
                                            if (!folder.exists()) {
                                                val created = folder.mkdirs()
                                                if (!created) {
                                                    // Fallback to app-specific directory if no permission
                                                    folder = java.io.File(context.getExternalFilesDir(null), "Bond Investments")
                                                    folder.mkdirs()
                                                }
                                            }
                                            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                                            val timeFormat = SimpleDateFormat("HH-mm-ss", Locale.US)
                                            val dateStr = dateFormat.format(java.util.Date())
                                            val timeStr = timeFormat.format(java.util.Date())
                                            val fileName = "AllBonds-$dateStr-$timeStr.json"
                                            val file = java.io.File(folder, fileName)
                                            file.writeText(json)
                                            android.widget.Toast.makeText(context, "Saved to ${file.absolutePath}", android.widget.Toast.LENGTH_LONG).show()
                                        } catch (e: Exception) {
                                            android.widget.Toast.makeText(context, "Failed to save: ${e.localizedMessage}", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                        topMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Corporate Bonds Portfolio",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Portfolio Summary Card
            item {
                PortfolioSummaryCard(
                    summaries = listOf(state.overallSummary, state.activeSummary, state.closedSummary),
                    onPageChanged = { page ->
                        when(page) {
                            0 -> viewModel.setStatusFilter(null)           // Overall
                            1 -> viewModel.setStatusFilter("Active")       // Active
                            2 -> viewModel.setStatusFilter("Matured")      // Closed/Matured
                        }
                    }
                )
            }

            // Bond list header row with inline filter button
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            buildString {
                                append("Bonds (${state.filteredBonds.size}")
                                if (isFiltered) {
                                    append(" of ${state.bonds.size})")
                                    val total = state.filteredBonds.sumOf { it.investmentAmount }
                                    append(" worth ₹${formatPortfolioAmount(total)}")
                                } else {
                                    append(")")
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary,
                            modifier = Modifier.weight(1f)
                        )

                        // Clear filters badge
                        AnimatedVisibility(visible = isFiltered && filterPanelOpen, enter = fadeIn(), exit = fadeOut()) {
                            TextButton(
                                onClick = {
                                    viewModel.clearFilters()
                                    filterPanelOpen = false
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear filters",
                                    tint = RedError,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(2.dp))
                                Text("Clear", style = MaterialTheme.typography.labelSmall, color = RedError)
                            }
                        }

                        // Filter toggle button
                        if (hasFiltersAvailable) {
                            IconButton(onClick = { filterPanelOpen = !filterPanelOpen }) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = "Filter bonds",
                                    tint = if (isFiltered || filterPanelOpen) GoldPrimary else TextSecondary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Sort toggle button
                        var sortMenuExpanded by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { sortMenuExpanded = true }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = "Sort bonds",
                                    tint = if (state.selectedSortOption != SortOption.EARLIEST_PAYOUT) GoldPrimary else TextSecondary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = sortMenuExpanded,
                                onDismissRequest = { sortMenuExpanded = false },
                                containerColor = NavyCard
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Earliest Payout", color = if (state.selectedSortOption == SortOption.EARLIEST_PAYOUT) GoldPrimary else TextPrimary) },
                                    onClick = { 
                                        viewModel.selectSortOption(SortOption.EARLIEST_PAYOUT)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Recently Added", color = if (state.selectedSortOption == SortOption.CHRONOLOGICAL) GoldPrimary else TextPrimary) },
                                    onClick = { 
                                        viewModel.selectSortOption(SortOption.CHRONOLOGICAL)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Highest Interest", color = if (state.selectedSortOption == SortOption.INTEREST_RATE) GoldPrimary else TextPrimary) },
                                    onClick = { 
                                        viewModel.selectSortOption(SortOption.INTEREST_RATE)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Highest Amount", color = if (state.selectedSortOption == SortOption.INVESTMENT_AMOUNT) GoldPrimary else TextPrimary) },
                                    onClick = { 
                                        viewModel.selectSortOption(SortOption.INVESTMENT_AMOUNT)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Earliest Maturity", color = if (state.selectedSortOption == SortOption.EARLIEST_MATURITY) GoldPrimary else TextPrimary) },
                                    onClick = { 
                                        viewModel.selectSortOption(SortOption.EARLIEST_MATURITY)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Shortest Tenure", color = if (state.selectedSortOption == SortOption.SHORTEST_TENURE) GoldPrimary else TextPrimary) },
                                    onClick = { 
                                        viewModel.selectSortOption(SortOption.SHORTEST_TENURE)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Expandable filter chip panel
                    AnimatedVisibility(
                        visible = filterPanelOpen && hasFiltersAvailable,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NavySurface)
                                .padding(bottom = 8.dp)
                        ) {
                            if (state.availablePlatforms.size > 1) {
                                ChipGroup(
                                    label = "Platform",
                                    options = state.availablePlatforms,
                                    selected = state.selectedPlatforms,
                                    onSelect = { viewModel.selectPlatform(it) }
                                )
                            }
                            if (state.availableInvestors.size > 1) {
                                ChipGroup(
                                    label = "Investor",
                                    options = state.availableInvestors,
                                    selected = state.selectedInvestors,
                                    onSelect = { viewModel.selectInvestor(it) }
                                )
                            }
                            if (state.availableStatuses.size > 1) {
                                ChipGroup(
                                    label = "Status",
                                    options = state.availableStatuses,
                                    selected = state.selectedStatuses,
                                    onSelect = { viewModel.selectStatus(it) }
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = DividerColor
                    )
                }
            }

            // Loading / empty / list
            if (state.isLoading) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GoldPrimary)
                    }
                }
            } else if (state.filteredBonds.isEmpty()) {
                item {
                    EmptyState(
                        isFiltered = isFiltered,
                        onClearFilters = { viewModel.clearFilters() }
                    )
                }
            } else {
                items(state.filteredBonds, key = { it.investmentId }) { bond ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        BondCard(bond = bond, onClick = { onBondClick(bond.investmentId) })
                    }
                }
            }
        }
    }
}



@Composable
private fun ChipGroup(
    label: String,
    options: List<String>,
    selected: Set<String>,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "$label:",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            modifier = Modifier.padding(end = 2.dp)
        )
        options.forEach { option ->
            val isSelected = selected.contains(option)
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(option) },
                label = {
                    Text(
                        option,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = NavyCard,
                    labelColor = TextSecondary,
                    selectedContainerColor = GoldDark,
                    selectedLabelColor = TextPrimary,
                    iconColor = GoldPrimary,
                    selectedLeadingIconColor = TextPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = if (isSelected) GoldPrimary else DividerColor,
                    selectedBorderColor = GoldPrimary,
                    borderWidth = if (isSelected) 1.5.dp else 1.dp
                )
            )
        }
    }
}

@Composable
private fun PortfolioSummaryCard(
    summaries: List<PortfolioSummary>,
    onPageChanged: (Int) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { summaries.size })

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val summary = summaries[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF1E3A5F), NavyCard)),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            summary.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = GoldPrimary
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SummaryMetric("Total Invested", "₹${formatPortfolioAmount(summary.totalInvested)}", Modifier.weight(1f))
                            SummaryMetric("Principal Repaid", "₹${formatPortfolioAmount(summary.totalPrincipalReceived)}", Modifier.weight(1f))
                            SummaryMetric("Interest Paid", "₹${formatPortfolioAmount(summary.totalInterestReceived)}", Modifier.weight(1f))
                            SummaryMetric(summary.bondCountLabel, summary.bondCount.toString(), Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        
        // Setup pager indicators
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(summaries.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) GoldPrimary else DividerColor
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                )
            }
        }
    }
}

@Composable
private fun SummaryMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary, textAlign = TextAlign.Center)
    }
}

@Composable
private fun BondCard(bond: Bond, onClick: () -> Unit) {
    val statusColor = when (bond.status.lowercase()) {
        "active" -> GreenSuccess
        "matured" -> AmberWarning
        "defaulted" -> RedError
        else -> TextSecondary
    }
    val statusBg = when (bond.status.lowercase()) {
        "active" -> GreenLight
        "matured" -> AmberLight
        "defaulted" -> RedLight
        else -> NavyElevated
    }

    val isMatured = bond.status.lowercase() in listOf("matured", "defaulted")
    val alpha = if (isMatured) 0.6f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onClick() }
            .alpha(alpha),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        bond.companyName,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${bond.investor} • ",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        val iconRes = getPlatformIcon(bond.platform)
                        if (iconRes != null) {
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = bond.platform,
                                modifier = Modifier
                                    .height(20.dp)
                                    .widthIn(max = 80.dp)
                            )
                        } else {
                            Text(
                                text = bond.platform,
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(statusBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            bond.status.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                BondMetric(label = "Invested", value = "₹${formatAmount(bond.investmentAmount)}")
                BondMetric(label = "Interest Rate", value = "${bond.interestRate}%")
                BondMetric(label = "Matures", value = formatDateString(bond.maturityDate))
            }

            if (!bond.nextPayoutDate.isNullOrBlank()) {
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(GoldPrimary)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Next Payout: ${formatDateString(bond.nextPayoutDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun BondMetric(label: String, value: String) {
    Column {
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}

@Composable
private fun EmptyState(isFiltered: Boolean, onClearFilters: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.AutoMirrored.Filled.TrendingUp,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            if (isFiltered) "No bonds match these filters" else "No Bonds Added Yet",
            style = MaterialTheme.typography.headlineSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        if (isFiltered) {
            TextButton(onClick = onClearFilters) {
                Text("Clear Filters", color = GoldPrimary)
            }
        } else {
            Text(
                "Tap \"Add Investment\" to import your first bond via JSON",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatPortfolioAmount(amount: Double): String {
    return when {
        amount >= 1_00_000 -> {
            val formatted = "%.2f".format(amount / 1_00_000)
            "${formatted.removeSuffix(".00")}L"
        }
        else -> {
            val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
            format.maximumFractionDigits = 0
            format.format(amount)
        }
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
            val calendarDate = java.util.Calendar.getInstance().apply { time = parsedDate }
            val now = java.util.Calendar.getInstance()
            
            val isToday = calendarDate.get(java.util.Calendar.YEAR) == now.get(java.util.Calendar.YEAR) &&
                          calendarDate.get(java.util.Calendar.DAY_OF_YEAR) == now.get(java.util.Calendar.DAY_OF_YEAR)
            
            now.add(java.util.Calendar.DAY_OF_YEAR, 1)
            val isTomorrow = calendarDate.get(java.util.Calendar.YEAR) == now.get(java.util.Calendar.YEAR) &&
                             calendarDate.get(java.util.Calendar.DAY_OF_YEAR) == now.get(java.util.Calendar.DAY_OF_YEAR)
            
            if (isToday) "Today"
            else if (isTomorrow) "Tomorrow"
            else SimpleDateFormat("dd/MM/yyyy", Locale.US).format(parsedDate)
        } else dateStr
    } catch (e: Exception) {
        dateStr
    }
}

private fun getPlatformIcon(platform: String): Int? {
    return when (platform.lowercase(Locale.ROOT).trim()) {
        "jiraaf" -> R.drawable.ic_jiraaf
        "stable money", "stablemoney" -> R.drawable.ic_stablemoney
        else -> null
    }
}
