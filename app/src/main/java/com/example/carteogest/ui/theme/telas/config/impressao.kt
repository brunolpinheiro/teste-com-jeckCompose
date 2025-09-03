package com.example.carteogest.ui.telas.config


import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.let


@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
/*
fun ConectPrinters(
    navController: NavController,
    viewModel: BluetoothViewModel,
    openDrawer: () -> Unit,
    userViewModel: UserViewModel
) {

    var showPrinters by remember { mutableStateOf(false) }
    var selectedPrinter by remember { mutableStateOf<BluetoothDevice?>(null) }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val hasPermission = remember { viewModel.hasBluetoothPermission(context) }
    val printers by viewModel.deviceList.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.registerDisconnectReceiver(context)
    }

    /*fun searchForPrinters() {
        if (!showPrinters) {
            loading = true
            viewModel.startDiscovery()
            showPrinters = true
        } else if (selectedPrinter != null) {
            selectedPrinter?.let {
                viewModel.connectToDevice(it.address)
                navController.navigate("ImpressaoAgrupadaScreen")
            }
        }
    }*/
    fun searchForPrinters() {
        if (!showPrinters) {
            loading = true
            viewModel.startDiscovery()
            showPrinters = true
        } else if (selectedPrinter != null) {
            scope.launch {
                val success = viewModel.connectToDevice(selectedPrinter!!)
                if (success) {
                    navController.navigate("ImpressaoAgrupadaScreen")
                } else {
                    // Mostra alerta de falha
                    Toast.makeText(context, "Falha ao conectar. Verifique a impressora.", Toast.LENGTH_SHORT).show()
                }
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
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer,
                navController = navController

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
                        val name = if (hasPermission) printer.name ?: "Desconhecida" else "Permissão negada"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPrinter = printer }
                                .padding(horizontal = 8.dp)
                                .background( Color(0xFF004AAD)),
                            shape = MaterialTheme.shapes.medium,
                            colors = if (selectedPrinter == printer)
                                CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
                            else
                                CardDefaults.cardColors(Color(0xFF004AAD))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
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

*/

fun ConectPrinters(
    navController: NavController,
    viewModel: BluetoothViewModel,
    openDrawer: () -> Unit,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Estado de UI
    var showPrinters by remember { mutableStateOf(false) }
    var selectedPrinter by remember { mutableStateOf<BluetoothDevice?>(null) }
    var loading by remember { mutableStateOf(false) }

    // Lista de dispositivos do ViewModel
    val printers by viewModel.deviceList.collectAsState()
    val connectedDevice by viewModel.connectedDevice.collectAsState()
    val hasPermission = remember { viewModel.hasBluetoothPermission(context) }

    // Registrar BroadcastReceiver de desconexão
    LaunchedEffect(Unit) {
        viewModel.registerDisconnectReceiver(context)
    }

    // Função para buscar ou conectar impressora
    fun searchOrConnect() {
        if (!showPrinters) {
            loading = true
            viewModel.startDiscovery()
            showPrinters = true
        } else if (selectedPrinter != null) {
            scope.launch {
                val success = viewModel.connectToDevice(selectedPrinter!!)
                if (success) {
                    Toast.makeText(context, "Conectado a ${selectedPrinter?.name}", Toast.LENGTH_SHORT).show()
                    navController.navigate("ImpressaoAgrupadaScreen")
                } else {
                    Toast.makeText(context, "Falha ao conectar. Verifique a impressora.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Simula loading da descoberta
    LaunchedEffect(loading) {
        if (loading) {
            delay(2000)
            loading = false
            showPrinters = true
        }
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = { scope.launch { drawerState.open() } },
                openDrawer = openDrawer,
                navController = navController
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
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Encontrar impressoras:",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Loading
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Lista de impressoras
            if (showPrinters) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(printers) { printer ->
                        val name = if (hasPermission) printer.name ?: "Desconhecida" else "Permissão negada"
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
                                    .padding(12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = name,
                                    fontSize = 18.sp,
                                    color = if (selectedPrinter == printer)
                                        MaterialTheme.colorScheme.onSecondary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Descobrir/Conectar
            Button(
                onClick = { searchOrConnect() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && (selectedPrinter != null || !showPrinters)
            ) {
                Text(
                    text = if (showPrinters && selectedPrinter != null) "Conectar" else "Descobrir",
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Exibe impressora conectada
            connectedDevice?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Conectado a: ${it.name ?: "Desconhecida"}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
        }
    }
}
