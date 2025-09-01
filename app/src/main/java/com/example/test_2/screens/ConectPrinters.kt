package com.example.test_2.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test_2.bluetooth.model.BluetoothViewModel
import com.example.test_2.bluetooth.utils.BluetoothViewModelFactory
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.products.ProductViewModel
import com.example.test_2.data_db.supplier.SupplierViewModel
import com.example.test_2.menu.TopBarWithLogo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableDateInputField(
    label: String = "Data",
    initialDate: String = "",
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var textValue by remember { mutableStateOf(initialDate) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun openDatePicker() {
        val currentDate = try {
            LocalDate.parse(textValue, dateFormatter)
        } catch (e: Exception) {
            LocalDate.now()
        }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                textValue = newDate.format(dateFormatter)
                onDateSelected(textValue)
            },
            currentDate.year,
            currentDate.monthValue - 1,
            currentDate.dayOfMonth
        ).show()
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onDateSelected(it)
        },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Campo para $label" },
        singleLine = true,
        textStyle = TextStyle(color = Color.Black),
        trailingIcon = {
            IconButton(onClick = { openDatePicker() }) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Selecionar data")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConectPrinters(
    navController: NavController,
    openDrawer: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var responsible by remember { mutableStateOf("") }
    var manufactureDate by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var quantit by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current.applicationContext
    val bluetoothViewModel: BluetoothViewModel = viewModel(
        factory = BluetoothViewModelFactory(context as Application)
    )

    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it.productsDao()) } }
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }
    var showProductsDropdown by remember { mutableStateOf(false) }
    var inputHeight by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        viewModel?.let { vm ->
            scope.launch {
                try {
                    vm.getAll()
                } catch (e: Exception) {
                    Log.e("ConectPrinters", "Erro ao carregar produtos: ${e.message}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = productName ?: "",
                onValueChange = {},
                label = { Text("Produtos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .onGloballyPositioned { coordinates ->
                        inputHeight = coordinates.size.height.toFloat()
                    },
                readOnly = true,
                textStyle = TextStyle(color = Color.Black),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Selecionar produto",
                        modifier = Modifier.clickable { showProductsDropdown = true }
                    )
                }
            )
            DropdownMenu(
                expanded = showProductsDropdown,
                onDismissRequest = { showProductsDropdown = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp), // Altura máxima do menu
                offset = DpOffset(
                    x = 0.dp,
                    y = with(LocalDensity.current) { inputHeight.toDp() }
                )
            ) {
                if (products.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Nenhum produto cadastrado") },
                        onClick = { },
                        enabled = false
                    )
                } else {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.name) },
                            onClick = {
                                productName = product.name
                                showProductsDropdown = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = responsible,
                onValueChange = { responsible = it },
                label = { Text("Responsável") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black)
            )

            EditableDateInputField(
                label = "Data de Fabricação",
                initialDate = manufactureDate
            ) { selected ->
                manufactureDate = selected
            }

            EditableDateInputField(
                label = "Validade",
                initialDate = expirationDate
            ) { selected ->
                expirationDate = selected
            }

            OutlinedTextField(
                value = quantit,
                onValueChange = { quantit = it },
                label = { Text("Quantidade de Etiquetas") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black)
            )

            val quantitValue = quantit.toIntOrNull() ?: 1
            Button(
                onClick = {
                    bluetoothViewModel.printLabel(
                        text1 = productName,
                        text2 = responsible,
                        text3 = manufactureDate,
                        text4 = expirationDate,
                        text5 = quantitValue.toString()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Imprimir")
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar")
            }
        }
    }
}