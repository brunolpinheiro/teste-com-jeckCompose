package com.example.test_2.screens

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
    val productId: Int,
    val expirationDates: List<LocalDate>,
    val fabricationDates: List<LocalDate>,
    val validities: List<ValidityAndFabrication>
)

@Composable
fun ValidityOfProducts(
    navController: NavController,
    openDrawer: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val productViewModel = remember { database?.let { ProductViewModel(it.productsDao()) } }
    val validityViewModel = remember { database?.let { ValidityViewModel(it) } }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val validities by validityViewModel?.validity ?: remember { mutableStateOf(emptyList<ValidityAndFabrication>()) }
    val products by productViewModel?.products ?: remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        try{
            productViewModel?.getAll()
            validityViewModel?.loadValidity()
            Log.d("ValidityOfProducts", "Carregando validades. Tamanho da lista: ${validities.size}")
        }catch(e: Exception){
            Log.e("ValidityOfProducts", "falha ao carregar os produtos")
        }
    }

    fun parseDateString(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, dateFormatter)
        } catch (e: Exception) {
            Log.e("DateConversion", "Erro ao converter string para LocalDate: ${e.message}")
            null
        }
    }

    // Agrupar validades por productId
    val groupedValidities = validities.groupBy { it.productId }

    // Criar grupos para TODOS os produtos, mesmo sem validades
    val validityGroups = products.map { product ->
        val productId = product.uid
        val items = groupedValidities[productId] ?: emptyList()
        val datesValidity = items.mapNotNull { item ->
            item.validity?.let { parseDateString(it) }
        }.sorted()
        val datesFabrication = items.mapNotNull { item ->
            item.fabrication?.let { parseDateString(it) }
        }.sorted()

        ValidityGroup(
            productName = product.name,
            productId = productId,
            expirationDates = datesValidity,
            fabricationDates = datesFabrication,
            validities = items
        )
    }

    // Ordena produtos pelo vencimento mais próximo; produtos sem datas vão para o final
    val sortedValidityGroups = validityGroups.sortedBy { it.expirationDates.minOrNull() ?: LocalDate.MAX }

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
            if (products.isEmpty()) {
                Text(
                    text = "Nenhum produto cadastrado",
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
                            productId = group.productId,
                            expirationDates = group.expirationDates,
                            fabricationDates = group.fabricationDates,
                            validities = group.validities,
                            dateFormatter = dateFormatter,
                            validityViewModel = validityViewModel
                        )
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

@Composable
fun ProductItem(
    productName: String,
    productId: Int,
    expirationDates: List<LocalDate>,
    fabricationDates: List<LocalDate>,
    validities: List<ValidityAndFabrication>,
    dateFormatter: DateTimeFormatter,
    validityViewModel: ValidityViewModel?
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var editingValidityId by remember { mutableStateOf<Int?>(null) }
    var newDateFabrication by remember { mutableStateOf<LocalDate?>(null) }
    var newDateValidity by remember { mutableStateOf<LocalDate?>(null) }

    fun parseDateString(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString, dateFormatter)
        } catch (e: Exception) {
            Log.e("DateConversion", "Erro ao converter string para LocalDate: ${e.message}")
            null
        }
    }

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
            if (nearestExpiration != null) {
                val colorProxima = if (nearestExpiration.isAfter(LocalDate.now())) Color.Green else Color.Red
                Text(
                    text = "Próx. vencimento: ${nearestExpiration.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorProxima
                )
            } else {
                Text(
                    text = "Sem vencimentos cadastrados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Seção para datas de validade
                    Text(
                        text = "Datas de Validade:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    if (validities.isEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nenhuma data cadastrada",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            IconButton(
                                onClick = {
                                    editingValidityId = productId
                                    val calendar = Calendar.getInstance()
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            newDateValidity = LocalDate.of(year, month + 1, day)
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Adicionar data de validade",
                                    tint = Color.Gray
                                )
                            }
                        }
                    } else {
                        validities.forEach { validity ->
                            val expirationDate = validity.validity?.let { parseDateString(it) }
                            val colorValidity = if (expirationDate != null) {
                                if (expirationDate.isAfter(LocalDate.now())) Color.Green else Color.Red
                            } else {
                                Color.Gray
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = expirationDate?.format(dateFormatter) ?: "Nenhuma data cadastrada",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorValidity
                                )
                                IconButton(
                                    onClick = {
                                        editingValidityId = productId
                                        val calendar = Calendar.getInstance()
                                        DatePickerDialog(
                                            context,
                                            { _, year, month, day ->
                                                newDateValidity = LocalDate.of(year, month + 1, day)
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
                    if (validities.isEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nenhuma data cadastrada",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            IconButton(
                                onClick = {
                                    editingValidityId = productId
                                    val calendar = Calendar.getInstance()
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            newDateFabrication = LocalDate.of(year, month + 1, day)
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Adicionar data de fabricação",
                                    tint = Color.Gray
                                )
                            }
                        }
                    } else {
                        validities.forEach { validity ->
                            val fabricationDate = validity.fabrication?.let { parseDateString(it) }
                            val colorFabrication = if (fabricationDate != null) Color.Blue else Color.Gray
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = fabricationDate?.format(dateFormatter) ?: "Nenhuma data cadastrada",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorFabrication
                                )
                                IconButton(
                                    onClick = {
                                        editingValidityId = productId
                                        val calendar = Calendar.getInstance()
                                        DatePickerDialog(
                                            context,
                                            { _, year, month, day ->
                                                newDateFabrication = LocalDate.of(year, month + 1, day)
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
                    }

                    // Botão para confirmar alterações
                    Spacer(modifier = Modifier.height(12.dp))
                    AnimatedVisibility(
                        visible = newDateValidity != null || newDateFabrication != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Button(
                            onClick = {
                                if (editingValidityId != null) {
                                    scope.launch {
                                        try {
                                            validityViewModel?.updateValidity(

                                                newDateFabrication?.format(dateFormatter).toString(),
                                                newDateValidity?.format(dateFormatter).toString()
                                            )
                                            newDateValidity = null
                                            newDateFabrication = null
                                            editingValidityId = null
                                        } catch (e: Exception) {
                                            Log.e("ValidityUpdate", "Erro ao atualizar validade: ${e.message}")
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008C4A)),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirmar",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Atualizar Validade", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}