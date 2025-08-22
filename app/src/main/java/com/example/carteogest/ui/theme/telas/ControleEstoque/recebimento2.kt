package com.example.carteogest.ui.telas.ControleEstoque

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.carteogest.menu.TopBarWithLogo

// Modelo de produto
data class Produto(
    val codigo: String,
    val nome: String,
    val observacao: String = ""
)

// Modelo de produto recebido
data class ProdutoRecebido(
    val fornecedor: String,
    val cnpj: String,
    val codigo: String,
    val nome: String,
    val quantidade: Int,
    val observacao: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecebimentoScreen(
    onFinalizar: (List<ProdutoRecebido>) -> Unit,
    openDrawer: () -> Unit
) {
    var fornecedor by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var produtosListados by remember { mutableStateOf<List<Produto>>(emptyList()) }
    var produtosRecebidos by remember { mutableStateOf<List<ProdutoRecebido>>(emptyList()) }

    // Produtos fictícios por fornecedor
    val produtosFicticios = mapOf(
        "Fornecedor A" to listOf(
            Produto("001", "Arroz 5kg"),
            Produto("002", "Feijão 1kg"),
            Produto("003", "Óleo 900ml")
        ),
        "Fornecedor B" to listOf(
            Produto("101", "Macarrão 500g"),
            Produto("102", "Molho de Tomate 340g"),
            Produto("103", "Açúcar 2kg")
        )
    )

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = { /* Abrir drawer */ },
                openDrawer = openDrawer
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            Text("Recebimento de Produtos", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            // Seleção do fornecedor
            OutlinedTextField(
                value = fornecedor,
                onValueChange = { fornecedor = it },
                label = { Text("Fornecedor") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = cnpj,
                onValueChange = { cnpj = it },
                label = { Text("CNPJ") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    produtosListados = produtosFicticios[fornecedor] ?: emptyList()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Listar Produtos")
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f, fill = true)
            ) {
                items(produtosListados) { produto ->
                    ProdutoRecebimentoItem(
                        produto = produto,
                        fornecedor = fornecedor,
                        cnpj = cnpj,
                        onInserir = { qtd ->
                            if (qtd > 0) {
                                produtosRecebidos = produtosRecebidos + ProdutoRecebido(
                                    fornecedor = fornecedor,
                                    cnpj = cnpj,
                                    codigo = produto.codigo,
                                    nome = produto.nome,
                                    quantidade = qtd,
                                    observacao = produto.observacao
                                )
                            }
                        }
                    )
                }
            }

            Button(
                onClick = { onFinalizar(produtosRecebidos) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar Recebimento")
            }
        }
    }
}

@Composable
fun ProdutoRecebimentoItem(
    produto: Produto,
    fornecedor: String,
    cnpj: String,
    onInserir: (Int) -> Unit
) {
    var quantidade by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("${produto.nome} (Código: ${produto.codigo})", style = MaterialTheme.typography.bodyLarge)
            if (produto.observacao.isNotBlank()) {
                Text("Obs: ${produto.observacao}")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    label = { Text("Qtd recebida") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        val qtd = quantidade.toIntOrNull() ?: 0
                        onInserir(qtd)
                        quantidade = ""
                    }
                ) {
                    Text("Inserir")
                }
            }
        }
    }
}
