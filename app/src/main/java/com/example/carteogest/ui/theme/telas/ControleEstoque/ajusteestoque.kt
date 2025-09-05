package com.example.carteogest.ui.telas.ControleEstoque

import StockAdjustmentViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch


/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockAdjustmentScreen(openDrawer: () -> Unit) {
    var selectedProduct by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var isPositiveAdjustment by remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Lista de exemplo para produtos (pode ser substituída por dados reais de um ViewModel ou API)
    val products = listOf("Produto A", "Produto B", "Produto C", "Produto D")

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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dropdown para seleção de produto
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { /* Gerenciar estado de expansão, se necessário */ }
            ) {
                OutlinedTextField(
                    value = selectedProduct,
                    onValueChange = { selectedProduct = it },
                    label = { Text("Produto") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = false,
                    onDismissRequest = { /* Fechar menu */ }
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product) },
                            onClick = { selectedProduct = product }
                        )
                    }
                }
            }

            // Campo para quantidade
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Seleção de tipo de ajuste (entrada ou saída)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isPositiveAdjustment,
                        onClick = { isPositiveAdjustment = true }
                    )
                    Text("Entrada")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = !isPositiveAdjustment,
                        onClick = { isPositiveAdjustment = false }
                    )
                    Text("Saída")
                }
            }

            // Campo para motivo do ajuste
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Motivo do Ajuste") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Botão para confirmar ajuste
            Button(
                onClick = {
                    // Aqui você pode adicionar a lógica para processar o ajuste de estoque
                    // Por exemplo: chamar uma ViewModel ou API com os dados
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Confirmar Ajuste", fontSize = 18.sp)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockAdjustmentScreen(
    productViewModel: ProductViewModel,
    openDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val viewModel = remember { StockAdjustmentViewModel(productViewModel) }

    var selectedProduct by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var isPositiveAdjustment by remember { mutableStateOf(true) }

    val lastAdjustment by viewModel.lastAdjustment.collectAsState()

    val products = productViewModel.products.value.map { it.name }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = { scope.launch { drawerState.open() } },
                openDrawer = openDrawer
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
            // Dropdown de produto
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {}
            ) {
                OutlinedTextField(
                    value = selectedProduct,
                    onValueChange = {},
                    label = { Text("Produto") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = false,
                    onDismissRequest = {}
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product) },
                            onClick = { selectedProduct = product }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

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



            Button(
                onClick = {
                    val qtd = quantity.toIntOrNull() ?: 0
                    if (selectedProduct.isNotBlank() && qtd > 0) {
                        viewModel.adjustStock(selectedProduct, qtd, isPositiveAdjustment, )
                        quantity = ""

                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Ajuste")
            }

            // Histórico / Último ajuste
            lastAdjustment?.let { adjustment ->
                Spacer(Modifier.height(16.dp))
                Text(
                    "Último ajuste:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Produto: ${adjustment.productName}")
                Text("Quantidade: ${if (adjustment.isPositive) "+" else "-"}${adjustment.quantity}")
            }
        }
    }
}
*/

import com.example.carteogest.datadb.data_db.products.Products


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

    // estados de UI
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Products?>(null) }
    var quantity by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var isPositiveAdjustment by remember { mutableStateOf(true) }

    // último ajuste vindo do ViewModel
    val lastAdjustment by viewModel.lastAdjustment.collectAsState()

    // garantir que produtos sejam carregados
    LaunchedEffect(Unit) {
        productViewModel.getAll()
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
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedProduct?.name ?: "",
                    onValueChange = {},
                    label = { Text("Produto") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    produtosBanco.forEach { produto ->
                        DropdownMenuItem(
                            text = { Text(produto.name) },
                            onClick = {
                                selectedProduct = produto
                                expanded = false
                            }
                        )
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

            // Motivo
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Motivo do Ajuste") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

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
                        reason = ""
                        selectedProduct = null
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Ajuste")
            }

            // Histórico / Último ajuste
            lastAdjustment?.let { adjustment ->
                Spacer(Modifier.height(16.dp))
                Text("Último ajuste:", style = MaterialTheme.typography.titleMedium)
                Text("Produto: ${adjustment.productName}")
                Text("Quantidade: ${if (adjustment.isPositive) "+" else "-"}${adjustment.quantity}")
            }
        }
    }
}

