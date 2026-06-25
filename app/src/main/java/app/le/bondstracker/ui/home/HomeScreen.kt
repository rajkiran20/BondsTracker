package app.le.bondstracker.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.le.bondstracker.R
import app.le.bondstracker.domain.model.Bond
import app.le.bondstracker.ui.theme.*

import app.le.bondstracker.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddBondClick: () -> Unit,
    onBondClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var exportSuccessPath by remember { mutableStateOf<String?>(null) }
    var exportError by remember { mutableStateOf<String?>(null) }

    HomeScreenContent(
        state = state,
        onAddBondClick = onAddBondClick,
        onBondClick = onBondClick,
        onToggleDarkMode = { mainViewModel.toggleDarkMode() },
        onExportBonds = {
            val json = viewModel.exportBondsToJson()
            try {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val timeFormat = SimpleDateFormat("HH-mm-ss", Locale.US)
                val dateStr = dateFormat.format(java.util.Date())
                val timeStr = timeFormat.format(java.util.Date())
                val fileName = "AllBonds-$dateStr-$timeStr.json"
                
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    val resolver = context.contentResolver
                    val contentValues = android.content.ContentValues().apply {
                        put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/json")
                        put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
                    }
                    val uri = resolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    if (uri != null) {
                        resolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(json.toByteArray())
                        }
                        exportSuccessPath = "Download/$fileName"
                    } else {
                        exportError = "Failed to create file in Downloads"
                    }
                } else {
                    val folder = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                    val file = java.io.File(folder, fileName)
                    file.writeText(json)
                    exportSuccessPath = file.absolutePath
                }
            } catch (e: Exception) {
                exportError = "Failed to save: ${e.localizedMessage}"
            }
        },
        onSetStatusFilter = { viewModel.setStatusFilter(it) },
        onClearFilters = { viewModel.clearFilters() },
        onSelectPlatform = { viewModel.selectPlatform(it) },
        onSelectInvestor = { viewModel.selectInvestor(it) },
        onSelectStatus = { viewModel.selectStatus(it) },
        onSelectSortOption = { viewModel.selectSortOption(it) }
    )

    if (exportSuccessPath != null) {
        ModalBottomSheet(
            onDismissRequest = { exportSuccessPath = null },
            containerColor = NavySurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = GeminiBlue
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Export Successful",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "File saved to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    exportSuccessPath ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        try {
                            val intent = Intent(android.app.DownloadManager.ACTION_VIEW_DOWNLOADS)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.widget.Toast.makeText(context, "No file manager found", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        exportSuccessPath = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GeminiBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Locate File", color = Color.White)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (exportError != null) {
        android.widget.Toast.makeText(context, exportError, android.widget.Toast.LENGTH_SHORT).show()
        exportError = null
    }
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onAddBondClick: () -> Unit,
    onBondClick: (String) -> Unit,
    onToggleDarkMode: () -> Unit,
    onExportBonds: () -> Unit,
    onSetStatusFilter: (String?) -> Unit,
    onClearFilters: () -> Unit,
    onSelectPlatform: (String) -> Unit,
    onSelectInvestor: (String) -> Unit,
    onSelectStatus: (String) -> Unit,
    onSelectSortOption: (SortOption) -> Unit
) {
    val isFiltered = state.selectedPlatforms.isNotEmpty() || state.selectedInvestors.isNotEmpty() || state.selectedStatuses.isNotEmpty()
    var filterPanelOpen by remember { mutableStateOf(false) }
    val hasFiltersAvailable = state.availablePlatforms.size > 1 || state.availableInvestors.size > 1 || state.availableStatuses.size > 1

    Scaffold(
        containerColor = NavyDeep,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBondClick,
                containerColor = GeminiBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Investment",
                    modifier = Modifier.size(28.dp)
                )
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
                        .background(NavyDeep)
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFF9C4).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = null,
                                    tint = GeminiAmber,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Bonds Tracker",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "Corporate Bonds Portfolio",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }



                        // 3-dots menu
                        var topMenuExpanded by remember { mutableStateOf(false) }
                        
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
                                        onToggleDarkMode()
                                        topMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Export investments", color = TextPrimary) },
                                    onClick = {
                                        onExportBonds()
                                        topMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Portfolio Summary Card
            item {
                PortfolioSummaryCard(
                    state = state,
                    onPageChanged = { page ->
                        when(page) {
                            0 -> onSetStatusFilter(null)           // Overall
                            1 -> onSetStatusFilter("Active")       // Active
                            2 -> onSetStatusFilter("Matured")      // Closed/Matured
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
                                val activeCount = state.bonds.count { it.status.lowercase() == "active" }
                                val totalCount = state.bonds.size
                                val totalWorth = state.bonds.filter { it.status.lowercase() == "active" }.sumOf { it.investmentAmount }
                                
                                if (isFiltered) {
                                    append("Bonds (${state.filteredBonds.size} of $totalCount)")
                                    val filteredWorth = state.filteredBonds.sumOf { it.investmentAmount }
                                    append(" worth ₹${formatPortfolioAmount(filteredWorth)}")
                                } else {
                                    append("Bonds ($activeCount of $totalCount) worth ₹${formatPortfolioAmount(totalWorth)}")
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
                                    onClearFilters()
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

                        // Filter chip/button
                        if (hasFiltersAvailable) {
                            Surface(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable { filterPanelOpen = !filterPanelOpen },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isFiltered || filterPanelOpen) GeminiBlue else DividerColor.copy(alpha = 0.5f)
                                ),
                                color = Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.FilterList,
                                        contentDescription = null,
                                        tint = if (isFiltered || filterPanelOpen) GeminiBlue else TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        "Filter",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isFiltered || filterPanelOpen) GeminiBlue else TextSecondary
                                    )
                                }
                            }
                        }

                        // Sort chip/button
                        var sortMenuExpanded by remember { mutableStateOf(false) }
                        Box {
                            Surface(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .clickable { sortMenuExpanded = true },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (state.selectedSortOption != SortOption.EARLIEST_PAYOUT) GeminiBlue else DividerColor.copy(alpha = 0.5f)
                                ),
                                color = Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Sort,
                                        contentDescription = null,
                                        tint = if (state.selectedSortOption != SortOption.EARLIEST_PAYOUT) GeminiBlue else TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        "Sort",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (state.selectedSortOption != SortOption.EARLIEST_PAYOUT) GeminiBlue else TextSecondary
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = sortMenuExpanded,
                                onDismissRequest = { sortMenuExpanded = false },
                                containerColor = NavyCard
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Earliest Payout", color = if (state.selectedSortOption == SortOption.EARLIEST_PAYOUT) GeminiBlue else TextPrimary) },
                                    onClick = { 
                                        onSelectSortOption(SortOption.EARLIEST_PAYOUT)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Recent Payout", color = if (state.selectedSortOption == SortOption.RECENT_PAYOUT) GeminiBlue else TextPrimary) },
                                    onClick = { 
                                        onSelectSortOption(SortOption.RECENT_PAYOUT)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Recently Added", color = if (state.selectedSortOption == SortOption.CHRONOLOGICAL) GeminiBlue else TextPrimary) },
                                    onClick = { 
                                        onSelectSortOption(SortOption.CHRONOLOGICAL)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Highest Interest", color = if (state.selectedSortOption == SortOption.INTEREST_RATE) GeminiBlue else TextPrimary) },
                                    onClick = { 
                                        onSelectSortOption(SortOption.INTEREST_RATE)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Highest Amount", color = if (state.selectedSortOption == SortOption.INVESTMENT_AMOUNT) GeminiBlue else TextPrimary) },
                                    onClick = { 
                                        onSelectSortOption(SortOption.INVESTMENT_AMOUNT)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Earliest Maturity", color = if (state.selectedSortOption == SortOption.EARLIEST_MATURITY) GeminiBlue else TextPrimary) },
                                    onClick = { 
                                        onSelectSortOption(SortOption.EARLIEST_MATURITY)
                                        sortMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Shortest Tenure", color = if (state.selectedSortOption == SortOption.SHORTEST_TENURE) GeminiBlue else TextPrimary) },
                                    onClick = { 
                                        onSelectSortOption(SortOption.SHORTEST_TENURE)
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
                                    onSelect = { onSelectPlatform(it) }
                                )
                            }
                            if (state.availableInvestors.size > 1) {
                                ChipGroup(
                                    label = "Investor",
                                    options = state.availableInvestors,
                                    selected = state.selectedInvestors,
                                    onSelect = { onSelectInvestor(it) }
                                )
                            }
                            if (state.availableStatuses.size > 1) {
                                ChipGroup(
                                    label = "Status",
                                    options = state.availableStatuses,
                                    selected = state.selectedStatuses,
                                    onSelect = { onSelectStatus(it) }
                                )
                            }
                        }
                    }

                    // HorizontalDivider
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = DividerColor.copy(alpha = 0.5f)
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
                        CircularProgressIndicator(color = GeminiBlue)
                    }
                }
            } else if (state.filteredBonds.isEmpty()) {
                item {
                    EmptyState(
                        isFiltered = isFiltered,
                        onClearFilters = { onClearFilters() }
                    )
                }
            } else {
                items(state.filteredBonds, key = { it.investmentId }) { bond ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        BondCard(bond = bond, selectedSortOption = state.selectedSortOption, onClick = { onBondClick(bond.investmentId) })
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
                    selectedContainerColor = GeminiBlueDark,
                    selectedLabelColor = TextPrimary,
                    iconColor = GeminiBlue,
                    selectedLeadingIconColor = TextPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = if (isSelected) GeminiBlue else DividerColor,
                    selectedBorderColor = GeminiBlue,
                    borderWidth = if (isSelected) 1.5.dp else 1.dp
                )
        )
    }
}
}

@Composable
private fun SummaryMetric(
    label: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    circleBgColor: Color,
    modifier: Modifier = Modifier,
    labelAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier,
        horizontalAlignment = labelAlignment
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(circleBgColor)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = when (labelAlignment) {
                Alignment.Start -> TextAlign.Start
                Alignment.End -> TextAlign.End
                else -> TextAlign.Center
            },
            modifier = Modifier.align(labelAlignment)
        )
    }
}

@Composable
private fun PortfolioSummaryCard(
    state: HomeUiState,
    onPageChanged: (Int) -> Unit
) {
    val summaries = listOf(state.overallSummary, state.activeSummary, state.closedSummary)
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { summaries.size })

    val totalCount = state.bonds.size
    val activeCount = state.bonds.count { it.status.lowercase() == "active" }
    val closedCount = state.bonds.count { it.status.lowercase() in listOf("matured", "defaulted") }

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val summary = summaries[page]
            val formattedTitle = when (page) {
                0 -> "Overall Portfolio ($totalCount/$totalCount)"
                1 -> "Active Portfolio ($activeCount/$totalCount)"
                else -> "Closed Portfolio ($closedCount/$totalCount)"
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard.copy(alpha = 0.3f)),
                border = BorderStroke(1.dp, GeminiBlue.copy(alpha = 0.2f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formattedTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = GeminiBlue,
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SummaryMetric(
                                label = "Total Invested",
                                value = "₹${formatPortfolioAmount(summary.totalInvested)}",
                                icon = Icons.Outlined.AccountBalanceWallet,
                                iconColor = GeminiBlue,
                                circleBgColor = GeminiBlue.copy(alpha = 0.15f),
                                labelAlignment = Alignment.Start
                            )
                            SummaryMetric(
                                label = "Returns Received",
                                value = "₹${formatPortfolioAmount(summary.totalReturns)}",
                                icon = Icons.AutoMirrored.Outlined.TrendingUp,
                                iconColor = Color(0xFF81C784),
                                circleBgColor = Color(0xFF4CAF50).copy(alpha = 0.15f),
                                labelAlignment = Alignment.CenterHorizontally
                            )
                            SummaryMetric(
                                label = "Interest Received",
                                value = "₹${formatPortfolioAmount(summary.totalInterestReceived)}",
                                icon = Icons.Outlined.Percent,
                                iconColor = Color(0xFFFFD54F),
                                circleBgColor = Color(0xFFFFB74D).copy(alpha = 0.15f),
                                labelAlignment = Alignment.End
                            )
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
                val color = if (pagerState.currentPage == iteration) GeminiBlue else DividerColor
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
private fun BondCard(bond: Bond, selectedSortOption: SortOption = SortOption.EARLIEST_PAYOUT, onClick: () -> Unit) {
    val statusColor = when (bond.status.lowercase()) {
        "active" -> GreenSuccess
        "matured" -> Color.Black
        "defaulted" -> RedError
        else -> TextSecondary
    }
    val statusBg = when (bond.status.lowercase()) {
        "active" -> GreenLight
        "matured" -> TextMuted
        "defaulted" -> RedLight
        else -> NavyElevated
    }

    val isMatured = bond.status.lowercase() in listOf("matured", "defaulted")
    val alpha = if (isMatured) 0.8f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onClick() }
            .alpha(alpha),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard.copy(alpha = if (isDarkTheme) 0.1f else 0.3f)),
        border = BorderStroke(0.5.dp, DividerColor.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                // Top Section: Platform Icon + Titles (end padding to avoid ribbon overlap)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val iconRes = getPlatformIcon(bond.platform)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GeminiBlue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (iconRes != null) {
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = bond.platform,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = bond.platform.take(1).uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = GeminiBlue
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = bond.companyName,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = bond.investor,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = DividerColor.copy(alpha = 0.3f), thickness = 0.5.dp)
                Spacer(Modifier.height(12.dp))

                // Metrics Row (3 columns separated by thin vertical dividers)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BondMetric(
                        label = "Invested",
                        value = "₹${formatAmount(bond.investmentAmount)}",
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        textAlign = TextAlign.Start
                    )
                    
                    VerticalDivider(
                        color = DividerColor.copy(alpha = 0.3f),
                        thickness = 0.5.dp,
                        modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp)
                    )

                    BondMetric(
                        label = "Interest Rate",
                        value = "${bond.interestRate}%",
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        textAlign = TextAlign.Center
                    )

                    VerticalDivider(
                        color = DividerColor.copy(alpha = 0.3f),
                        thickness = 0.5.dp,
                        modifier = Modifier.fillMaxHeight().padding(vertical = 4.dp)
                    )

                    BondMetric(
                        label = "Matures",
                        value = formatDateString(bond.maturityDate),
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End,
                        textAlign = TextAlign.End
                    )
                }

                // Bottom payout warning section
                val isRecentPayoutSort = selectedSortOption == SortOption.RECENT_PAYOUT
                
                val recentPayout = if (isRecentPayoutSort) {
                    val today = java.util.Calendar.getInstance().apply {
                        set(java.util.Calendar.HOUR_OF_DAY, 0)
                        set(java.util.Calendar.MINUTE, 0)
                        set(java.util.Calendar.SECOND, 0)
                        set(java.util.Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    
                    bond.payouts.filter { p ->
                        try {
                            val format = if (p.date.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
                            val t = format.parse(p.date)?.time
                            t != null && t <= today
                        } catch (e: Exception) { false }
                    }.maxByOrNull { p ->
                        try {
                            val format = if (p.date.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
                            format.parse(p.date)?.time ?: 0L
                        } catch (e: Exception) { 0L }
                    }
                } else null

                val showBottomSection = (!bond.nextPayoutDate.isNullOrBlank() && !isRecentPayoutSort) || (isRecentPayoutSort && recentPayout != null)

                if (showBottomSection) {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = GeminiBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        
                        val amountStr = if (isRecentPayoutSort && recentPayout != null) {
                            "₹${formatAmount(recentPayout.amount)}"
                        } else {
                            val nextPayoutAmount = bond.payouts
                                .filter { it.date.take(10) == bond.nextPayoutDate?.take(10) }
                                .sumOf { it.amount }
                            if (nextPayoutAmount > 0) "₹${formatAmount(nextPayoutAmount)}" else ""
                        }
                            
                        val dateText = if (isRecentPayoutSort && recentPayout != null) {
                            formatNextPayoutDateString(recentPayout.date)
                        } else {
                            formatNextPayoutDateString(bond.nextPayoutDate)
                        }
                        
                        val isTodayOrTomorrow = dateText.equals("Today", ignoreCase = true) || dateText.equals("Tomorrow", ignoreCase = true)
                        
                        Text(
                            text = buildAnnotatedString {
                                if (isRecentPayoutSort) {
                                    append("Last payment")
                                } else {
                                    append("Next payout")
                                }
                                
                                if (amountStr.isNotEmpty()) {
                                    append(" of ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = GreenSuccess)) {
                                        append(amountStr)
                                    }
                                }
                                
                                if (isRecentPayoutSort) {
                                    if (dateText.equals("Today", ignoreCase = true)) {
                                        append(" was ")
                                    } else {
                                        append(" on ")
                                    }
                                } else {
                                    if (isTodayOrTomorrow) {
                                        append(" is ")
                                    } else {
                                        append(" on ")
                                    }
                                }
                                
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = GeminiBlue)) {
                                    append(dateText)
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Ribbon Overlay
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = statusBg,
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = bond.status.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BondMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    textAlign: TextAlign = TextAlign.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary,
            textAlign = textAlign
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = textAlign
        )
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
                Text("Clear Filters", color = GeminiBlue)
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

private fun formatNextPayoutDateString(dateStr: String?): String {
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
            else SimpleDateFormat("d MMMM", Locale.US).format(parsedDate)
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

private fun getCompanyInitials(name: String): String {
    val words = name.split(" ").filter { it.isNotEmpty() }
    return if (words.size >= 2) {
        (words[0].take(1) + words[1].take(1)).uppercase()
    } else {
        name.take(2).uppercase()
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
fun HomeScreenPreview() {
    val sampleBond = Bond(
        investmentId = "bond_123",
        createdAt = "2023-12-15",
        platform = "Jiraaf",
        investor = "John Doe",
        companyName = "Green Energy Corp",
        bondCategory = "Corporate",
        bondType = listOf("Senior Secured"),
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
        maturityDate = "2024-06-15",
        tenureMonths = 12,
        interestPaid = 1200.0,
        nextPayoutDate = "2024-03-15",
        notes = null,
        payouts = emptyList()
    )

    val sampleState = HomeUiState(
        bonds = listOf(sampleBond),
        filteredBonds = listOf(sampleBond),
        overallSummary = PortfolioSummary("Overall Portfolio", 100000.0, 1200.0, 0.0, 1200.0, 1, "Total Bonds"),
        activeSummary = PortfolioSummary("Active Bonds", 100000.0, 1200.0, 0.0, 1200.0, 1, "Active Bonds"),
        closedSummary = PortfolioSummary("Closed/Matured", 0.0, 0.0, 0.0, 0.0, 0, "Closed Bonds"),
        isLoading = false,
        availablePlatforms = listOf("Jiraaf"),
        availableInvestors = listOf("John Doe"),
        availableStatuses = listOf("Active")
    )

    BondsTrackerTheme {
        HomeScreenContent(
            state = sampleState,
            onAddBondClick = {},
            onBondClick = {},
            onToggleDarkMode = {},
            onExportBonds = {},
            onSetStatusFilter = {},
            onClearFilters = {},
            onSelectPlatform = {},
            onSelectInvestor = {},
            onSelectStatus = {},
            onSelectSortOption = {}
        )
    }
}
