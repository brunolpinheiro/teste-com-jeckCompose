package com.example.test_2.screens.register

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.validity.ValidityViewModel
import com.example.test_2.data_db.products.ProductViewModel
import com.example.test_2.data_db.products.Products
import com.example.test_2.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun RegistrationValidity(
    navController: NavController,
    openDrawer: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } }
    val viewModelValidity = remember { database?.let { ValidityViewModel(it) } }
    var showValiditySuccess by remember { mutableStateOf(false) }
    var notValiditySuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var selectedProduct by remember { mutableStateOf<Products?>(null) }
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }
    val validity by viewModelValidity?.validity ?: remember { mutableStateOf(emptyList()) }
    var newDateFabrication by remember { mutableStateOf<LocalDate?>(null) }
    var newDateValidity by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        viewModel?.getAll()
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = { scope.launch { openDrawer() } },
                openDrawer = openDrawer
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onBack) {
                Text("Voltar")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Cadastro de Validade",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            item {
                Text(
                    text = "Selecione um produto para adicionar validade:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (products.isEmpty()) {
                item {
                    Text(
                        text = "Nenhum produto disponível",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                items(products) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedProduct = product
                                Log.d("RegistrationValidity", "Produto selecionado: ${product.name}")
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(
                            text = product.name,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Feedback para depuração
            item {
                Text(
                    text = if (selectedProduct != null) "Produto selecionado: ${selectedProduct?.name}" else "Nenhum produto selecionado",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Campos de entrada e botão
            if (selectedProduct != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color(0xFFF5F5F5)), // Fundo para depuração
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Campo de data de fabricação usando Box + Text
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .clickable {
                                    Log.d("RegistrationValidity", "Clicou no campo de fabricação")
                                    val calendar = Calendar.getInstance()
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            newDateFabrication = LocalDate.of(year, month + 1, day)
                                            Log.d("RegistrationValidity", "Data de fabricação selecionada: $newDateFabrication")
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                                .padding(16.dp)
                                .background(Color.White, shape = MaterialTheme.shapes.small)

                        ) {
                            Text(
                                text = newDateFabrication?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Selecionar Data de Fabricação",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (newDateFabrication == null) Color.Gray else Color.Black
                            )
                        }

                        // Campo de data de validade usando Box + Text
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .clickable {
                                    Log.d("RegistrationValidity", "Clicou no campo de validade")
                                    val calendar = Calendar.getInstance()
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            newDateValidity = LocalDate.of(year, month + 1, day)
                                            Log.d("RegistrationValidity", "Data de validade selecionada: $newDateValidity")
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                                .padding(16.dp)
                                .background(Color.White, shape = MaterialTheme.shapes.small)

                        ) {
                            Text(
                                text = newDateValidity?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Selecionar Data de Validade",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (newDateValidity == null) Color.Gray else Color.Black
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    selectedProduct = null
                                    newDateFabrication = null
                                    newDateValidity = null
                                    Log.d("RegistrationValidity", "Cancelado")
                                },
                                modifier = Modifier.weight(1f).padding(end = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text("Cancelar", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    try {
                                        if (newDateFabrication != null && newDateValidity != null) {
                                            val dateForStringFabrication = newDateFabrication!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            val dateForStringValidation = newDateValidity!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            scope.launch {
                                                viewModelValidity?.addValidityToProduct(
                                                    selectedProduct?.name ?: "",
                                                    dateForStringFabrication,
                                                    dateForStringValidation
                                                )
                                                showValiditySuccess = true
                                                newDateFabrication = null
                                                newDateValidity = null
                                                selectedProduct = null
                                                Log.d("RegistrationValidity", "Validade adicionada com sucesso")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("RegistrationValidity", "Falha ao cadastrar a validade: ${e.message}")
                                        errorMessage = e.message ?: "Erro desconhecido"
                                        notValiditySuccess = true
                                    }
                                },
                                enabled = newDateFabrication != null && newDateValidity != null,
                                modifier = Modifier.weight(1f).padding(start = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008C4A))
                            ) {
                                Text("Adicionar Validade", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        if (showValiditySuccess) {
            AlertDialog(
                onDismissRequest = { showValiditySuccess = false },
                title = { Text("Sucesso") },
                text = { Text("Validade cadastrada com sucesso") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showValiditySuccess = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        if (notValiditySuccess) {
            AlertDialog(
                onDismissRequest = { notValiditySuccess = false },
                title = { Text("Falha ao cadastrar a validade") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(
                        onClick = { notValiditySuccess = false }
                    ) {
                        Text("OK")
                    }
                }
            )}}}