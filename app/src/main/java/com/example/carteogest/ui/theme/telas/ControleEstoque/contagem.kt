package com.example.carteogest.ui.telas.ControleEstoque

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ProdutoContagem(
    val codigo: String,
    val nome: String,
    val quantidadeEsperada: Int,
    val quantidadeContada: Int,
    val localizacao: String,
    val status: String,
    val observacoes: String,
    val dataHoraContagem: LocalDateTime = LocalDateTime.now()
) {
    val diferenca get() = quantidadeContada - quantidadeEsperada
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContagemScreen(
    produtosDisponiveis: List<ProdutoContagem> = emptyList(),
    locais: List<String> = listOf("Depósito A", "Prateleira 1", "Prateleira 2"),
    statusOptions: List<String> = listOf("OK", "Danificado", "Faltando"),
    onFinalizarContagem: (List<ProdutoContagem>) -> Unit = {},
    onCancelar: () -> Unit = {},
    openDrawer: () -> Unit
) {
    var buscaTexto by remember { mutableStateOf("") }
    var produtoSelecionado by remember { mutableStateOf<ProdutoContagem?>(null) }

    var quantidadeContada by remember { mutableStateOf("") }
    var localizacaoSelecionada by remember { mutableStateOf<String?>(null) }
    var statusSelecionado by remember { mutableStateOf<String?>(null) }
    var observacoes by remember { mutableStateOf("") }

    var listaContagens by remember { mutableStateOf<List<ProdutoContagem>>(emptyList()) }

    // Filtro simples por texto
    val produtosFiltrados = if (buscaTexto.isBlank()) produtosDisponiveis
    else produtosDisponiveis.filter {
        it.nome.contains(buscaTexto, ignoreCase = true) || it.codigo.contains(buscaTexto)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Conectar Impressoras") },
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
                .padding(paddingValues)  // aplica padding correto do scaffold
                .padding(16.dp)
        ) {
            Text("Contagem de Produtos", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = buscaTexto,
                onValueChange = { buscaTexto = it },
                label = { Text("Buscar produto por nome ou código") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 150.dp)
            ) {
                itemsIndexed(produtosFiltrados) { idx, produto ->
                    ListItem(
                        headlineContent = { Text(produto.nome) },
                        supportingContent = { Text("Código: ${produto.codigo} | Estoque: ${produto.quantidadeEsperada}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                produtoSelecionado = produto
                                quantidadeContada = produto.quantidadeEsperada.toString()
                                localizacaoSelecionada = locais.firstOrNull()
                                statusSelecionado = statusOptions.firstOrNull()
                                observacoes = ""
                                buscaTexto = ""
                            }
                    )
                    if (idx < produtosFiltrados.lastIndex) Divider()
                }
            }

            Spacer(Modifier.height(16.dp))

            produtoSelecionado?.let { produto ->
                Text("Produto selecionado:", style = MaterialTheme.typography.titleMedium)
                Text("Nome: ${produto.nome}")
                Text("Código: ${produto.codigo}")
                Text("Quantidade Esperada: ${produto.quantidadeEsperada}")

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = quantidadeContada,
                    onValueChange = { if (it.all { c -> c.isDigit() }) quantidadeContada = it },
                    label = { Text("Quantidade Contada*") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                DropdownMenuField(
                    label = "Localização*",
                    options = locais,
                    selectedOption = localizacaoSelecionada,
                    onOptionSelected = { localizacaoSelecionada = it }
                )

                Spacer(Modifier.height(8.dp))

                DropdownMenuField(
                    label = "Status*",
                    options = statusOptions,
                    selectedOption = statusSelecionado,
                    onOptionSelected = { statusSelecionado = it }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = observacoes,
                    onValueChange = { observacoes = it },
                    label = { Text("Observações") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val qtd = quantidadeContada.toIntOrNull() ?: 0
                        if (qtd > 0 && localizacaoSelecionada != null && statusSelecionado != null) {
                            val novaContagem = ProdutoContagem(
                                codigo = produto.codigo,
                                nome = produto.nome,
                                quantidadeEsperada = produto.quantidadeEsperada,
                                quantidadeContada = qtd,
                                localizacao = localizacaoSelecionada!!,
                                status = statusSelecionado!!,
                                observacoes = observacoes
                            )
                            listaContagens = listaContagens + novaContagem
                            // Reset campos
                            produtoSelecionado = null
                            quantidadeContada = ""
                            localizacaoSelecionada = null
                            statusSelecionado = null
                            observacoes = ""
                        } else {
                            // Aqui você pode mostrar um snackbar ou alerta para erro de validação
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Adicionar Contagem")
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Produtos Contados", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            if (listaContagens.isEmpty()) {
                Text("Nenhum produto contado ainda.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(listaContagens) { idx, contagem ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(contagem.nome, style = MaterialTheme.typography.titleSmall)
                                    Text("Código: ${contagem.codigo}")
                                    Text("Local: ${contagem.localizacao}")
                                    Text("Status: ${contagem.status}")
                                    Text("Obs: ${contagem.observacoes}")
                                    Text(
                                        "Data: ${
                                            contagem.dataHoraContagem.format(
                                                DateTimeFormatter.ofPattern(
                                                    "dd/MM/yyyy HH:mm"
                                                )
                                            )
                                        }"
                                    )
                                }
                                Column(
                                    modifier = Modifier.width(120.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("Esperada: ${contagem.quantidadeEsperada}")
                                    Text("Contada: ${contagem.quantidadeContada}")
                                    Text("Diferença: ${contagem.diferenca}")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { onCancelar() }, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { onFinalizarContagem(listaContagens) },
                    enabled = listaContagens.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Finalizar Contagem")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption ?: "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
