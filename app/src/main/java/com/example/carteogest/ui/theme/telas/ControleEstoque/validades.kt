package com.example.carteogest.ui.telas.ControleEstoque

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.datadb.data_db.products.Products
import com.example.carteogest.datadb.data_db.validity.ValidityAndFabrication
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import com.example.carteogest.datadb.data_db.login.UserViewModel

import androidx.navigation.NavController



@Composable
fun TelaValidades(
    viewModel: ProductViewModel,
    openDrawer: () -> Unit,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val scope = rememberCoroutineScope()

    //val validities by viewModel.produtosComValidades.collectAsState(initial = emptyList())

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var query by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val produtosComValidades by viewModel.produtosComValidades.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.getAll()
    }
    Scaffold(
        topBar = {
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer,
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
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
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(produtosComValidades) { item ->
                    ProdutoItem(
                        produto = item.product,
                        validades = item.validades,
                        dateFormatter = dateFormatter,
                        onExcluir = { validade -> scope.launch { viewModel.excluirValidade(validade) } },
                        onIncluirValidade = { produtoId, novaData ->
                            scope.launch {
                                val validade = ValidityAndFabrication(
                                    produtoId = produtoId,
                                    fabrication = LocalDate.now().format(dateFormatter),
                                    validity = novaData.format(dateFormatter)
                                )
                                viewModel.adicionarValidade(validade)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ProdutoItem(
    produto: Products,
    validades: List<ValidityAndFabrication>,
    dateFormatter: DateTimeFormatter,
    onExcluir: (ValidityAndFabrication) -> Unit,
    onIncluirValidade: (Int, LocalDate) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val validadeMaisProxima = validades.minByOrNull {
        LocalDate.parse(it.validity, dateFormatter)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .shadow(2.dp, shape = RoundedCornerShape(8.dp))
            .padding(18.dp)
            .clickable { expandido = !expandido }
    ) {
        // Nome do produto
        Text(
            text = produto.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (validadeMaisProxima == null) {
            Text("Sem validade cadastrada", color = Color.Gray)
        } else {
            val validade = LocalDate.parse(validadeMaisProxima.validity, dateFormatter)
            val fabricacao = LocalDate.parse(validadeMaisProxima.fabrication, dateFormatter)
            val diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), validade)
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = "Fab: ${fabricacao.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(18.dp))

                Text(
                    text = "Val: ${validade.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (diasRestantes <= 7) Color.Red else Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Divider(color = Color.White.copy(alpha = 0.3f))

        if (expandido) {
            validades.forEach { validadeItem ->
                val validade = LocalDate.parse(validadeItem.validity, dateFormatter)
                val fabricacao = LocalDate.parse(validadeItem.fabrication, dateFormatter)
                val diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), validade)
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    ) {

                    Text(
                        text = "Fab: ${fabricacao.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Val: ${validade.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (diasRestantes <= 7) Color.Red else Color.Black
                    )
                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { onExcluir(validadeItem) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Red)
                        }
                        Divider(color = Color.White.copy(alpha = 0.3f))
                     }



            }
            }


            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val hoje = LocalDate.now()

                    // Primeiro escolhe a FABRICAÇÃO
                    val datePickerFab = DatePickerDialog(
                        context,
                        { _, yearFab, monthFab, dayFab ->
                            val dataFabricacao = LocalDate.of(yearFab, monthFab + 1, dayFab)

                            // Depois de escolher a fabricação, abre outro para VALIDADE
                            val datePickerVal = DatePickerDialog(
                                context,
                                { _, yearVal, monthVal, dayVal ->
                                    val dataValidade = LocalDate.of(yearVal, monthVal + 1, dayVal)

                                    // Cria objeto e salva
                                    val validade = ValidityAndFabrication(
                                        produtoId = produto.uid,
                                        fabrication = dataFabricacao.format(dateFormatter),
                                        validity = dataValidade.format(dateFormatter)
                                    )
                                    onIncluirValidade(produto.uid, dataValidade)
                                },
                                hoje.year,
                                hoje.monthValue - 1,
                                hoje.dayOfMonth
                            )
                            datePickerVal.setTitle("Selecione a validade")
                            datePickerVal.show()

                        },
                        hoje.year,
                        hoje.monthValue - 1,
                        hoje.dayOfMonth
                    )
                    datePickerFab.setTitle("Selecione a fabricação")
                    datePickerFab.show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Incluir Nova Validade", color = Color.White)
            }
        }
    }

}
