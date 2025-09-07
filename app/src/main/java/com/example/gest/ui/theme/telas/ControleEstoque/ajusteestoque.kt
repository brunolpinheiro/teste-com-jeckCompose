package com.example.gest.ui.telas.ControleEstoque

import StockAdjustmentViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gest.datadb.data_db.login.UserViewModel
import com.example.gest.datadb.data_db.products.ProductViewModel
import com.example.gest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem

import com.example.gest.datadb.data_db.products.Products


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockAdjustmentScreen(
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    openDrawer: () -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val viewModel = remember { StockAdjustmentViewModel(productViewModel) }

    // lista de produtos do banco
    val produtosBanco = productViewModel.products.value
    LaunchedEffect(Unit) {
        productViewModel.getAll()
    }

    // estados de UI
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Products?>(null) }
    var quantity by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var isPositiveAdjustment by remember { mutableStateOf(true) }

    // último ajuste vindo do ViewModel
    val lastAdjustment by viewModel.lastAdjustment.collectAsState()

    var searchText by remember { mutableStateOf("") }


    val produtosFiltrados = produtosBanco
        .filter { it.name.contains(searchText, ignoreCase = true) }
    // garantir que produtos sejam carregados


    Scaffold(
        topBar = {
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = { scope.launch { drawerState.open() } },
                openDrawer = openDrawer,
                navController = navController
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dropdown de produtos
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.background(Color.White)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        expanded = it.isNotBlank()
                    },
                    label = { Text("Pesquisar Produto") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .background(Color.White),
                    readOnly = false
                )

                val produtosFiltrados = produtosBanco
                    .filter { it.name.contains(searchText, ignoreCase = true) }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    if (produtosFiltrados.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Nenhum produto encontrado") },
                            onClick = {}
                        )
                    } else {
                        produtosFiltrados.forEach { produto ->
                            DropdownMenuItem(
                                text = { Text(produto.name) },
                                onClick = {
                                    selectedProduct = produto
                                    searchText = produto.name // limpa pesquisa
                                    expanded = false

                                }
                            )
                        }
                    }
                }
            }

            // Quantidade
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo de ajuste
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isPositiveAdjustment,
                        onClick = { isPositiveAdjustment = true }
                    )
                    Text("Entrada")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !isPositiveAdjustment,
                        onClick = { isPositiveAdjustment = false }
                    )
                    Text("Saída")
                }
            }


            // Botão confirmar
            Button(
                onClick = {
                    val qtd = quantity.toIntOrNull() ?: 0
                    if (selectedProduct != null && qtd > 0) {
                        viewModel.adjustStock(
                            productName = selectedProduct!!.name,
                            quantity = qtd,
                            isPositive = isPositiveAdjustment
                        )
                        quantity = ""
                        selectedProduct = null
                        searchText = ""
                        expanded = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Ajuste")
            }

            // Histórico / Último ajuste
            lastAdjustment?.let { adjustment ->
                Spacer(Modifier.height(16.dp))
                val product = produtosBanco.find { it.name == adjustment.productName }
                val quantidade = product?.quantity ?: 0
                Text("Último ajuste:", style = MaterialTheme.typography.titleMedium)
                Text("Produto: ${adjustment.productName}")
                Text("Quantidade: ${if (adjustment.isPositive) "+" else "-"}${adjustment.quantity}")
                Text("Estoque Atual: ${product?.quantity ?: "Desconhecido"}",color = if (quantidade < 0) Color.Red else Color.Black)
            }
        }
    }
}

