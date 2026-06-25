package app.le.bondstracker.ui.addbond

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.le.bondstracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBondScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddBondViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        object : ActivityResultContract<Unit, Uri?>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/json", "text/plain", "*/*"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val downloadsUri = Uri.parse(
                            "content://com.android.externalstorage.documents/document/primary:${Environment.DIRECTORY_DOWNLOADS}"
                        )
                        putExtra(DocumentsContract.EXTRA_INITIAL_URI, downloadsUri)
                    }
                }
            }
            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return intent?.data.takeIf { resultCode == android.app.Activity.RESULT_OK }
            }
        }
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.bufferedReader()?.use { reader ->
                    val text = reader.readText()
                    viewModel.importFromFile(text)
                }
            } catch (e: Exception) {
                // Ignore or handle
            }
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.clearSuccess()
            onNavigateBack()
        }
    }

    Scaffold(
        containerColor = NavyDeep,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Investment",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GeminiBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavySurface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Instructions card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = GeminiBlue, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            "Paste Bond JSON",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Paste the complete JSON for your bond investment. Include the investment_id, company_name, payouts array, and all required fields.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // JSON Input field
            OutlinedTextField(
                value = state.jsonInput,
                onValueChange = viewModel::onJsonChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                placeholder = {
                    Text(
                        "{\n  \"investment_id\": \"...\",\n  \"company_name\": \"...\",\n  ...\n}",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GeminiBlue,
                    unfocusedBorderColor = DividerColor,
                    cursorColor = GeminiBlue,
                    focusedContainerColor = NavyCard,
                    unfocusedContainerColor = NavyCard,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorContainerColor = NavyCard
                ),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                isError = state.error != null,
                supportingText = state.error?.let { err ->
                    {
                        Text(err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                }
            )

            // File Picker button
            Button(
                onClick = { launcher.launch(Unit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyElevated,
                    contentColor = GeminiBlue
                ),
                enabled = !state.isLoading
            ) {
                Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Select JSON File",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Save button
            Button(
                onClick = viewModel::addBond,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GeminiBlue,
                    contentColor = NavyDeep
                ),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = NavyDeep,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Save Investment",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
