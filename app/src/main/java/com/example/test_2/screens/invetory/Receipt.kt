package com.example.test_2.screens.invetory

// RecebimentoScreen.kt


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test_2.screens.invetory.model.ReceiptProducts

data class ReceiptProduct(
    val codigo: String,
    val quantidade: Int,
    val observacao: String,

    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Receipt(
    onFinalizar: (List<ReceiptProduct>) -> Unit,
    viewModel: ReceiptProducts = viewModel(),
    openDrawer: () -> Unit,
) {
    val produtos by viewModel.produtos.collectAsState()

    var codigoProduto by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var observacao by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conectar Impressoras") }, // Note que o título parece incorreto; deveria ser "Recebimento de Produtos"?
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
                .padding(paddingValues) // Aplica o paddingValues do Scaffold
                .padding(16.dp), // Adiciona padding extra, se desejar
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Recebimento de Produtos", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = codigoProduto,
                    onValueChange = { codigoProduto = it },
                    label = { Text("Código do Produto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it },
                    label = { Text("Observações (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (codigoProduto.isNotBlank() && quantidade.isNotBlank()) {
                            viewModel.adicionarProduto(
                                ReceiptProduct(
                                    codigo = codigoProduto,
                                    quantidade = quantidade.toInt(),
                                    observacao = observacao
                                )
                            )
                            codigoProduto = ""
                            quantidade = ""
                            observacao = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Adicionar Produto")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Produtos Adicionados:", style = MaterialTheme.typography.titleMedium)

                LazyColumn {
                    items(produtos) { produto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(Modifier.padding(8.dp)) {
                                Text("Código: ${produto.codigo}")
                                Text("Quantidade: ${produto.quantidade}")
                                if (produto.observacao.isNotBlank()) {
                                    Text("Obs: ${produto.observacao}")
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { onFinalizar(produtos) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar Recebimento")
            }
        }
    }
}