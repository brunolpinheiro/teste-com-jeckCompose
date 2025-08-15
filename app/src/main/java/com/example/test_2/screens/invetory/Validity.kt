package com.example.test_2.screens.invetory


// ValidadesScreen.kt


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// Modelo de dados
data class ProdutoValidade(
    val codigo: String,
    val nome: String,
    val validade: LocalDate,
    val quantidade: Int,
    val localizacao: String
) {
    val diasRestantes: Long
        get() = ChronoUnit.DAYS.between(LocalDate.now(), validade)

    val statusColor: Color
        get() = when {
            diasRestantes < 0 -> Color.Red
            diasRestantes <= 7 -> Color.Yellow
            else -> Color.Green
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Validity(
    navController: NavHostController,
    produtos: List<ProdutoValidade>,
    onVoltar: () -> Unit,
    onGerarRelatorio: () -> Unit,
    openDrawer: () -> Unit
) {
    var filtroProduto by remember { mutableStateOf("") }
    var filtroStatus by remember { mutableStateOf("Todos") }
    Scaffold(
        topBar = {
            TopAppBar(
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Consulta de Validades", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))

            // Resumo
            ResumoValidades(produtos)

            Spacer(Modifier.height(16.dp))

            // Filtros
            OutlinedTextField(
                value = filtroProduto,
                onValueChange = { filtroProduto = it },
                label = { Text("Buscar Produto / Código") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { }
            ) {
                OutlinedTextField(
                    value = filtroStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            // Lista
            LazyColumn(modifier = Modifier.height(400.dp)) {
                items(produtos.filter {
                    (filtroProduto.isEmpty() || it.nome.contains(
                        filtroProduto,
                        true
                    ) || it.codigo.contains(filtroProduto, true)) &&
                            (filtroStatus == "Todos" || (filtroStatus == "Vencido" && it.diasRestantes < 0) ||
                                    (filtroStatus == "A Vencer" && it.diasRestantes in 0..7) ||
                                    (filtroStatus == "Dentro da Validade" && it.diasRestantes > 7))
                }) { produto ->
                    ProdutoValidadeItem(produto)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botões
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onVoltar) { Text("Voltar") }
                Button(onClick = onGerarRelatorio) { Text("Gerar Relatório") }
            }
        }
    }
}

@Composable
fun ResumoValidades(produtos: List<ProdutoValidade>) {
    val vencidos = produtos.count { it.diasRestantes < 0 }
    val aVencer = produtos.count { it.diasRestantes in 0..7 }
    val total = produtos.size

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ResumoCard("Vencidos", vencidos.toString(), Color.Red)
        ResumoCard("A Vencer", aVencer.toString(), Color.Yellow)
        ResumoCard("Total", total.toString(), Color.Green)
    }
}

@Composable
fun ResumoCard(titulo: String, valor: String, cor: Color) {
    Card(
        modifier = Modifier

            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = cor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(titulo, fontWeight = FontWeight.Bold)
            Text(valor, fontSize = 18.sp)
        }
    }
}

@Composable
fun ProdutoValidadeItem(produto: ProdutoValidade) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = produto.statusColor.copy(alpha = 0.1f))
    ) {
        Column(Modifier.padding(8.dp)) {
            Text("${produto.codigo} - ${produto.nome}", fontWeight = FontWeight.Bold)
            Text("Validade: ${produto.validade}")
            Text("Estoque: ${produto.quantidade}")
            Text("Local: ${produto.localizacao}")
            Text("Dias Restantes: ${produto.diasRestantes}")
        }
    }
}