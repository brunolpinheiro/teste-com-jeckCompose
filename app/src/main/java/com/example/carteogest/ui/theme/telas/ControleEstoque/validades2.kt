package com.example.carteogest.ui.telas.ControleEstoque

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// ==== MODELO DE DADOS ====
data class Produto1(
    val nome: String,
    val validades: MutableList<LocalDate>
)

// ==== TELA PRINCIPAL FICTÍCIA ====
@Composable
fun TelaValidades(openDrawer: () -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Produtos fictícios
    val produtosFicticios = remember {
        mutableStateListOf(
            Produto1(
                nome = "Arroz 5kg",
                validades = mutableListOf(
                    LocalDate.now().plusDays(10),
                    LocalDate.now().plusMonths(3)
                )
            ),
            Produto1(
                nome = "Feijão 1kg",
                validades = mutableListOf(
                    LocalDate.now().plusDays(5),
                    LocalDate.now().plusMonths(2)
                )
            ),
            Produto1(
                nome = "Óleo 900ml",
                validades = mutableListOf(
                    LocalDate.now().plusDays(20),
                    LocalDate.now().plusMonths(1)
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = { scope.launch { drawerState.open() } },
                openDrawer = openDrawer
            )
        }
    ) { paddingValues ->

        // Ordena produtos pelo vencimento mais próximo
        val produtosOrdenados = produtosFicticios.sortedBy { it.validades.minOrNull() }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            items(produtosOrdenados) { produto ->
                ProdutoItem(
                    produto = produto,
                    dateFormatter = dateFormatter,
                    onEditarValidade = { p, index, novaData ->
                        // Atualiza a validade no produto
                        p.validades[index] = novaData
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ==== ITEM DO PRODUTO COM DATEPICKER ====
@Composable
fun ProdutoItem(
    produto: Produto1,
    dateFormatter: DateTimeFormatter,
    onEditarValidade: (produto: Produto1, index: Int, novaData: LocalDate) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expandido = !expandido },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF008C4A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = produto.nome,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            val validadeMaisProxima = produto.validades.minOrNull()
            validadeMaisProxima?.let {
                Text(
                    text = "Próx. vencimento: ${it.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            AnimatedVisibility(
                visible = expandido,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    produto.validades.sorted().forEachIndexed { index, validade ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = validade.format(dateFormatter),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Button(
                                onClick = {
                                    // DatePickerDialog para editar a validade
                                    val calendar = Calendar.getInstance()
                                    calendar.set(
                                        validade.year,
                                        validade.monthValue - 1,
                                        validade.dayOfMonth
                                    )
                                    DatePickerDialog(
                                        context,
                                        { _, ano, mes, dia ->
                                            val novaData = LocalDate.of(ano, mes + 1, dia)
                                            onEditarValidade(produto, index, novaData)
                                        },
                                        validade.year,
                                        validade.monthValue - 1,
                                        validade.dayOfMonth
                                    ).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008C4A))
                            ) {
                                Text("Editar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
