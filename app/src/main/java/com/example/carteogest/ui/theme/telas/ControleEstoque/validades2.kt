/*package com.example.carteogest.ui.telas.ControleEstoque

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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

    var query by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(paddingValues)
        )
        {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Pesquisar Produtos") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filtrar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Cabeçalho
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 6.dp, horizontal = 12.dp)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Validades",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Ordena produtos pelo vencimento mais próximo
            val produtosOrdenados = produtosFicticios.sortedBy { it.validades.minOrNull() }

            LazyColumn(
                modifier = Modifier.fillMaxSize() .padding(vertical = 2.dp)
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

    Column(
        //shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // sombra azul
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            .padding(18 .dp)
            .clickable { expandido = !expandido },
        //colors = CardDefaults.cardColors(containerColor = Color(0xFF008C4A))
    ) {
            Text(
                text = produto.nome,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            val validadeMaisProxima = produto.validades.minOrNull()
            validadeMaisProxima?.let {
                Text(
                    text = "Próx. vencimento: ${it.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.8f)
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
                            Text(
                                text = validade.format(dateFormatter),
                                style = MaterialTheme.typography.bodyMedium
                            )

                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { viewModel.deleteUser(user.uid!!) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                        Button(
                            onClick ={ navController.navigate("SupplierRegistration/${fornecedores.uid}") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Editar Fornecedor", color = Color.White)
                        }
                    }
                }
            }
        }
    }
*/