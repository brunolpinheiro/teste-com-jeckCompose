package com.example.test_2.screens.invetory



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import java.time.LocalDate
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Report(
    onVoltar: () -> Unit,
    onGerarRelatorio: () -> Unit,
    onExportar: () -> Unit,
    openDrawer: () -> Unit
) {
    var periodo by remember { mutableStateOf("Últimos 7 dias") }
    var categoria by remember { mutableStateOf("") }
    var localizacao by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var produto by remember { mutableStateOf("") }
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
            Text(
                text = "Relatórios de Estoque",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            // ======== FILTROS ========
            Text("Filtros", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = periodo,
                onValueChange = { periodo = it },
                label = { Text("Período") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria do Produto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = localizacao,
                onValueChange = { localizacao = it },
                label = { Text("Localização") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status do Estoque") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = produto,
                onValueChange = { produto = it },
                label = { Text("Produto (Nome, SKU, Código)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Botões de ações
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onGerarRelatorio,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Gerar Relatório")
                }
                OutlinedButton(
                    onClick = { produto = ""; categoria = ""; localizacao = ""; status = "" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpar Filtros")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ======== RESUMO GERAL ========
            Text("Resumo Geral", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))

            ResumoEstoqueCard(
                totalItens = 1240,
                valorTotal = 58740.50,
                baixoEstoque = 14,
                excessoEstoque = 5
            )

            Spacer(Modifier.height(20.dp))

            // ======== TABELA DE RESULTADOS ========
            Text("Resultados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))

            EstoqueTable(
                produtos = listOf(
                    ProdutoEstoque("SKU-001", "Arroz Tipo 1", 150, 50, 300, "Depósito A", 750.0),
                    ProdutoEstoque("SKU-002", "Feijão Carioca", 80, 100, 400, "Depósito B", 560.0),
                    ProdutoEstoque(
                        "SKU-003",
                        "Macarrão Espaguete",
                        20,
                        50,
                        200,
                        "Prateleira 4",
                        140.0
                    )
                )
            )

            Spacer(Modifier.height(24.dp))

            // ======== BOTÕES FINAIS ========
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onVoltar,
                    modifier = Modifier.weight(1f)
                ) { Text("Voltar") }
                Button(
                    onClick = onExportar,
                    modifier = Modifier.weight(1f)
                ) { Text("Exportar") }
            }
        }
    }
}

@Composable
fun ResumoEstoqueCard(
    totalItens: Int,
    valorTotal: Double,
    baixoEstoque: Int,
    excessoEstoque: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF4FA))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Total de Itens: $totalItens")
            Text("Valor Total: R$ ${"%.2f".format(valorTotal)}")
            Text("Produtos com Estoque Baixo: $baixoEstoque", color = Color.Red)
            Text("Produtos com Estoque Excedente: $excessoEstoque", color = Color(0xFF006400))
        }
    }
}

data class ProdutoEstoque(
    val codigo: String,
    val nome: String,
    val quantidade: Int,
    val minimo: Int?,
    val maximo: Int?,
    val localizacao: String,
    val valorTotal: Double
)

@Composable
fun EstoqueTable(produtos: List<ProdutoEstoque>) {
    Column(
        Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
    ) {
        Row(
            Modifier
                .background(Color(0xFFEEEEEE))
                .padding(8.dp)
        ) {
            Text("Código", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Produto", fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
            Text("Qtd", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Local", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Valor", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        produtos.forEach { p ->
            Row(
                Modifier
                    .padding(8.dp)
                    .clickable { /* abrir detalhes */ }
            ) {
                Text(p.codigo, modifier = Modifier.weight(1f))
                Text(p.nome, modifier = Modifier.weight(2f))
                Text(p.quantidade.toString(), modifier = Modifier.weight(1f))
                Text(p.localizacao, modifier = Modifier.weight(1f))
                Text("R$ ${"%.2f".format(p.valorTotal)}", modifier = Modifier.weight(1f))
            }
        }
    }
}