package com.example.test_2.screens.invetory






import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test_2.menu.TopBarWithLogo
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
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