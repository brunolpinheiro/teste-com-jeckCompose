package com.example.carteogest.ui.telas.ControleEstoque

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class TipoRelatorio {
    PRODUTO, FORNECEDOR, FORNECEDOR_AGRUPADO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatoriosEstoqueScreen(
    produtos: List<Any>, // Lista de produtos recebida do backend
    fornecedores: List<String>, // Lista de fornecedores disponíveis
    onVoltar: () -> Unit,
    onExportarPDF: (List<Any>, List<String>) -> Unit,
    onExportarXLS: (List<Any>, List<String>) -> Unit,
    openDrawer: () -> Unit
) {
    var tipoRelatorio by remember { mutableStateOf(TipoRelatorio.PRODUTO) }
    var categoria by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var produto by remember { mutableStateOf("") }
    var fornecedor by remember { mutableStateOf("") }
    var dataInicio by remember { mutableStateOf(LocalDate.now().minusMonths(1)) }
    var dataFim by remember { mutableStateOf(LocalDate.now()) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val camposDisponiveis = listOf(
        "Nome", "SKU", "Descrição", "Categoria", "Preço", "Preço Promocional",
        "Quantidade", "Unidade Medida", "Marca", "Status", "Código de Barras",
        "Dimensões", "Peso", "Cor", "Tamanho", "Custo", "Fornecedor",
        "Data Validade", "Tags"
    )
    var camposSelecionados by remember { mutableStateOf(camposDisponiveis.take(5)) }

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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Text("Relatórios de Estoque", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            // ===== TIPO DE RELATÓRIO =====
            Text("Tipo de Relatório", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TipoRelatorio.values().forEach { tipo ->
                    Column(
                        modifier = Modifier.clickable { tipoRelatorio = tipo }
                    ) {
                        RadioButton(selected = tipoRelatorio == tipo, onClick = { tipoRelatorio = tipo })
                        Spacer(Modifier.width(4.dp))
                        Text(tipo.name.replace("_", " ").capitalize())
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ===== FILTROS COMUNS =====
            Text("Filtros", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Setor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth()
            )

            if (tipoRelatorio == TipoRelatorio.PRODUTO) {
                OutlinedTextField(
                    value = produto,
                    onValueChange = { produto = it },
                    label = { Text("Produto (Nome ou Código)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (tipoRelatorio != TipoRelatorio.PRODUTO) {
                OutlinedTextField(
                    value = fornecedor,
                    onValueChange = { fornecedor = it },
                    label = { Text("Fornecedor") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(8.dp))

            // ===== PERÍODO =====
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = dataInicio.toString(),
                    onValueChange = { /* Implementar picker se necessário */ },
                    label = { Text("Data Início") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = dataFim.toString(),
                    onValueChange = { /* Implementar picker se necessário */ },
                    label = { Text("Data Fim") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // ===== SELETOR DE CAMPOS =====
            Text("Campos do Relatório", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Column {
                camposDisponiveis.forEach { campo ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                camposSelecionados = if (camposSelecionados.contains(campo)) {
                                    camposSelecionados - campo
                                } else {
                                    camposSelecionados + campo
                                }
                            }
                            .padding(4.dp)
                    ) {
                        Checkbox(
                            checked = camposSelecionados.contains(campo),
                            onCheckedChange = {
                                camposSelecionados = if (it) camposSelecionados + campo else camposSelecionados - campo
                            }
                        )
                        Text(campo)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ===== TABELA DINÂMICA =====
            Text("Resultados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            EstoqueDynamicTable(produtos, camposSelecionados)

            Spacer(Modifier.height(20.dp))

            // ===== BOTÕES =====
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onVoltar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Voltar")
                }
                Button(
                    onClick = { onExportarPDF(produtos, camposSelecionados) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Exportar PDF")
                }
                Button(
                    onClick = { onExportarXLS(produtos, camposSelecionados) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Exportar XLS")
                }
            }
        }
    }
}

@Composable
fun EstoqueDynamicTable(produtos: List<Any>, camposSelecionados: List<String>) {
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
            camposSelecionados.forEach { campo ->
                Text(campo, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
        }
        produtos.forEach { _ ->
            Row(Modifier.padding(8.dp)) {
                camposSelecionados.forEach { _ ->
                    Text("-", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
