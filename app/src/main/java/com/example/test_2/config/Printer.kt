package com.example.test_2.config





import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.test_2.bluetooth.model.BluetoothViewModel
import kotlinx.coroutines.delay
import kotlin.let


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConectPrinters(
    navController: NavController,
    viewModel: BluetoothViewModel,
    openDrawer: () -> Unit,
) {
    var showPrinters by remember { mutableStateOf(false) }
    var selectedPrinter by remember { mutableStateOf<BluetoothDevice?>(null) }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val hasPermission = remember { viewModel.hasBluetoothPermission(context) }
    val printers by viewModel.deviceList.collectAsState()

    fun searchForPrinters() {
        if (!showPrinters) {
            loading = true
            viewModel.startDiscovery()
            showPrinters = true
        } else if (selectedPrinter != null) {
            selectedPrinter?.let {
                viewModel.connectToDevice(it.address)
                navController.navigate("printer_details")
            }
        }
    }

    LaunchedEffect(loading) {
        if (loading) {
            delay(2000L)
            loading = false
            showPrinters = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conectar Impressoras") },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Encontrar impressoras:",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 50.dp)
            )

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 16.dp)
                        .semantics { contentDescription = "Buscando impressoras" },
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (showPrinters) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(printers) { printer ->
                        val name = if (hasPermission) printer.name
                            ?: "Desconhecida" else "Permissão negada"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPrinter = printer }
                                .padding(horizontal = 8.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = if (selectedPrinter == printer)
                                CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
                            else
                                CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = name,
                                    fontSize = 18.sp,
                                    color = if (selectedPrinter == printer)
                                        MaterialTheme.colorScheme.onSecondary
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { searchForPrinters() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                enabled = !loading && (selectedPrinter != null || !showPrinters)
            ) {
                Text(
                    text = if (showPrinters && selectedPrinter != null) "Conectar" else "Descobrir",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
