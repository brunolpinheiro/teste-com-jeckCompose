package com.example.carteogest.ui.telas.ControleEstoque

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
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.focus.focusRequester
import com.example.carteogest.datadb.data_db.login.User
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.datadb.data_db.products.Products


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

    // Função para abrir o calendário
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
        trailingIcon = {
            IconButton(onClick = { openDatePicker() }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Selecionar data"
                )
            }
        }
    )
}
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Printers(
    navController: NavController,
    viewModel: BluetoothViewModel,
    openDrawer: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var responsible by remember { mutableStateOf("") }
    var manufactureDate by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var quantit by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Nome do Produto") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = responsible,
                onValueChange = { responsible = it },
                label = { Text("Responsável") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Aqui você pode usar seu EditableDateInputField, exemplo:
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
                modifier = Modifier.fillMaxWidth()
            )
            val quantit = quantit.toIntOrNull() ?: 1
            Button(
                onClick = {
                    viewModel.printLabel(
                        text1 = productName,
                        text2 = responsible,
                        text3 = manufactureDate,
                        text4 = expirationDate,
                        text5 = quantit
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Printers(
    navController: NavController,
    viewModel: BluetoothViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    openDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Dados do banco
    val produtosBanco = productViewModel.products.value
    val usuarios = userViewModel.users.collectAsState(initial = emptyList()).value

    var selectedProduct by remember { mutableStateOf<Products?>(null) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var manufactureDate by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }

    // Para dropdowns
    var expandedUser by remember { mutableStateOf(false) }
    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    var searchText by remember { mutableStateOf("") }
    var expandedProduct by remember { mutableStateOf(false) }
    val produtosFiltrados = produtosBanco.filter {
        it.name.contains(searchText, ignoreCase = true)}

    LaunchedEffect(Unit) {
        productViewModel.getAll()
        userViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = { scope.launch { drawerState.open() } },
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
            // Dropdown Produto
            ExposedDropdownMenuBox(
                expanded = expandedProduct,
                onExpandedChange = { expandedProduct = it }
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        expandedProduct = true
                    },
                    label = { Text("Produto") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester), // adiciona o FocusRequester
                    readOnly = false
                )

                ExposedDropdownMenu(
                    expanded = expandedProduct && produtosFiltrados.isNotEmpty(),
                    onDismissRequest = { expandedProduct = false }
                ) {
                    produtosFiltrados.forEach { produto ->
                        DropdownMenuItem(
                            text = { Text(produto.name) },
                            onClick = {
                                searchText = produto.name
                                selectedProduct = produto
                                expandedProduct = false
                            }
                        )
                    }
                    if (produtosFiltrados.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Nenhum produto encontrado") },
                            onClick = {}
                        )
                    }
                }
            }

            // Dropdown Responsável
            ExposedDropdownMenuBox(
                expanded = expandedUser,
                onExpandedChange = { expandedUser = it }
            ) {
                OutlinedTextField(
                    value = selectedUser?.nome ?: "",
                    onValueChange = {},
                    label = { Text("Responsável") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedUser,
                    onDismissRequest = { expandedUser = false }
                ) {
                    usuarios.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.nome) },
                            onClick = {
                                selectedUser = user
                                expandedUser = false
                            }
                        )
                    }
                }
            }

            // Datas
            EditableDateInputField(
                label = "Data de Fabricação",
                initialDate = manufactureDate
            ) { manufactureDate = it }

            EditableDateInputField(
                label = "Validade",
                initialDate = expirationDate
            ) { expirationDate = it }

            // Quantidade
            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it },
                label = { Text("Quantidade de Etiquetas") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            val quantity = quantityText.toIntOrNull() ?: 1

            // Botão imprimir
            Button(
                onClick = {
                    if (selectedProduct != null && selectedUser != null) {
                        viewModel.printLabel(
                            text1 = selectedProduct!!.name,
                            text2 = selectedUser!!.nome,
                            text3 = manufactureDate,
                            text4 = expirationDate,
                            text5 = quantity
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Imprimir")
            }

            // Botão voltar
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar")
            }
        }
    }
}
*/