/*// RecebimentoScreen.kt
package com.example.carteogest.ui.telas.ControleEstoque

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
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import com.example.carteogest.ui.telas.ControleEstoque.model.RecebimentoViewModel

data class ProdutoRecebido(
    val codigo: String,
    val quantidade: Int,
    val observacao: String,

)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecebimentoScreen(
    onFinalizar: (List<ProdutoRecebido>) -> Unit,
    viewModel: RecebimentoViewModel = viewModel(),
    openDrawer: () -> Unit,
) {
    val produtos by viewModel.produtos.collectAsState()

    var codigoProduto by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var observacao by remember { mutableStateOf("") }
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
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Recebimento de Produtos", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = codigoProduto,
                    onValueChange = { codigoProduto = it },
                    label = { Text("Nome do Fornecedor") },
                    modifier = Modifier.fillMaxWidth()
                )
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
                                ProdutoRecebido(
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
*/