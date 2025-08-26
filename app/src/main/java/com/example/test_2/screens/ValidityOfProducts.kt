package com.example.test_2.screens

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn  // Adicionado para animação suave
import androidx.compose.animation.fadeOut  // Adicionado para animação suave
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check  // Adicionado para ícone no botão
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.products.ProductViewModel
import com.example.test_2.data_db.validity.ValidityAndFabrication
import com.example.test_2.data_db.validity.ValidityViewModel
import com.example.test_2.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

// ==== MODELO DE DADOS ====
data class ValidityGroup(
    val productName: String,
    val expirationDates: List<LocalDate>,
    val fabricationDates: List<LocalDate>
)

@Composable
fun ValidityOfProducts(
    navController: NavController,
    openDrawer: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val productViewModel = remember { database?.let { ProductViewModel(it) } }
    val validityViewModel = remember { database?.let { ValidityViewModel(it) } }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val validities by validityViewModel?.validity ?: remember { mutableStateOf(emptyList<ValidityAndFabrication>()) }
    var newDateFabrication by remember { mutableStateOf<LocalDate?>(null) }
    var newDateValidity by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        validityViewModel?.loadValidity()
        Log.d("ValidityOfProducts", "Carregando validades. Tamanho da lista: ${validities.size}")
    }

    fun parseDateString(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, dateFormatter)
        } catch (e: Exception) {
            Log.e("DateConversion", "Erro ao converter string para LocalDate: ${e.message}")
            null
        }
    }

    // Agrupar validades por produto
    val groupedValidities = validities.groupBy { it.nameOfProduct }
    val validityGroups = groupedValidities.map { (productName, items) ->
        val datesValidity = items.mapNotNull { item ->
            item.validity?.let { parseDateString(it) }
        }.sorted()
        val datesFabrication = items.mapNotNull { item ->
            item.fabrication?.let { parseDateString(it) }
        }.sorted()

        ValidityGroup(productName, datesValidity, datesFabrication)
    }

    // Ordena produtos pelo vencimento mais próximo
    val sortedValidityGroups = validityGroups.sortedBy { it.expirationDates.minOrNull() }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = { scope.launch { openDrawer() } },
                openDrawer = openDrawer
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (validities.isEmpty()) {
                Text(
                    text = "Nenhuma validade cadastrada",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Validades cadastradas: ${validities.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(sortedValidityGroups) { group ->
                        ProductItem(
                            productName = group.productName,
                            expirationDates = group.expirationDates,
                            fabrication = group.fabricationDates,
                            dateFormatter = dateFormatter
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate("resgistrationProducts") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Tela de cadastrado",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Button(
                    onClick = { navController.navigate("showProducts") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Visualizar produtos",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Button(
                    onClick = { navController.navigate("connect_printers") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Conectar impressora",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

// ==== ITEM DO PRODUTO ====
@Composable
fun ProductItem(
    productName: String,
    expirationDates: List<LocalDate>,
    fabrication: List<LocalDate>,
    dateFormatter: DateTimeFormatter
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val productViewModel = remember { database?.let { ProductViewModel(it) } }
    val validityViewModel = remember { database?.let { ValidityViewModel(it) } }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val validities by validityViewModel?.validity ?: remember { mutableStateOf(emptyList<ValidityAndFabrication>()) }
    var newDateFabrication by remember { mutableStateOf<LocalDate?>(null) }
    var newDateValidity by remember { mutableStateOf<LocalDate?>(null) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF008C4A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = productName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            val nearestExpiration = expirationDates.minOrNull()
            nearestExpiration?.let {
                val colorProxima = if (it.isAfter(LocalDate.now())) Color.Green else Color.Red
                Text(
                    text = "Próx. vencimento: ${it.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorProxima
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally  // Centraliza o conteúdo horizontalmente
                ) {
                    // Seção para datas de validade
                    Text(
                        text = "Datas de Validade:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    expirationDates.forEach { expirationDate ->
                        val colorValidity = if (expirationDate.isAfter(LocalDate.now())) Color.Green else Color.Red
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = expirationDate.format(dateFormatter),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorValidity
                            )
                            IconButton(
                                onClick = {
                                    Log.d("RegistrationValidity", "Clicou no campo de validade")
                                    val calendar = Calendar.getInstance()
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            newDateValidity = LocalDate.of(year, month + 1, day)
                                            Log.d(
                                                "RegistrationValidity",
                                                "Data de validade selecionada: $newDateValidity"
                                            )
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar data de validade",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }

                    // Seção para datas de fabricação
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Datas de Fabricação:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    fabrication.forEach { fabricationDate ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = fabricationDate.format(dateFormatter),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Blue
                            )
                            IconButton(
                                onClick = {
                                    Log.d("RegistrationValidity", "Clicou no campo de validade")
                                    val calendar = Calendar.getInstance()
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            newDateFabrication = LocalDate.of(year, month + 1, day)
                                            Log.d(
                                                "RegistrationValidity",
                                                "Data de fabricacao  selecionada: $newDateFabrication"
                                            )
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar data de fabricação",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }

                    // Animação para o botão aparecer suavemente
                    Spacer(modifier = Modifier.height(12.dp))  // Espaçamento extra para beleza
                    AnimatedVisibility(
                        visible = newDateFabrication != null && newDateValidity != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Button(
                            onClick = {
                                try {
                                    if (newDateFabrication != null && newDateValidity != null) {
                                        val dateForStringFabrication = newDateFabrication!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        val dateForStringValidation = newDateValidity!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        scope.launch {
                                            validityViewModel?.addValidityToProduct(
                                                productName,
                                                dateForStringFabrication,
                                                dateForStringValidation
                                            )

                                            newDateFabrication = null
                                            newDateValidity = null
                                            Log.d("RegistrationValidity", "Validade adicionada com sucesso")
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("RegistrationValidity", "Falha ao cadastrar a validade: ${e.message}")
                                }
                            },
                            shape = RoundedCornerShape(20.dp),  // Forma mais arredondada para visual bonito
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008C4A)),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)  // Botão mais largo, mas não full, para centralizar
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirmar",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Adicionar Validade", color = Color.White)
                        }
                    }
                }
            }
        }
    }}