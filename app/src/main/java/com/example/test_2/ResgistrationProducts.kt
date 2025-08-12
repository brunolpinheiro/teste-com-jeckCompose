package com.example.test_2

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.test_2.data.AppDatabase
import com.example.test_2.data_db.ProductViewModel
import com.example.test_2.data_db.Products
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.layout.offset
import kotlin.random.Random
import kotlin.random.nextInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResgistrationProducts(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } }
    var showSectorDropdown by remember { mutableStateOf(false) }
    var fabrication by remember { mutableStateOf("") }
    var validity by remember { mutableStateOf("") }
    var newProductName by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf("Cozinha") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }

    ComposeTutorialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cadastrar Produtos") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },

        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (database == null) {
                    Text(
                        text = "Erro: Não foi possível conectar ao banco de dados",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                } else {
                    // Lista de produtos
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { product ->
                            Text(
                                text = "${product.name} - ${product.sector}",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }

                    // Formulário de cadastro
                    Text(
                        text = "Cadastrar Novo Produto",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    OutlinedTextField(
                        value = newProductName,
                        onValueChange = { newProductName = it },
                        label = { Text("Nome do Novo Produto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Campo para nome do novo produto" },
                        singleLine = true
                    )

                    Text(
                        text = "Selecione o Setor",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Box {
                        OutlinedTextField(
                            value = selectedSector,
                            onValueChange = {},
                            label = { Text("Setor") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.ic_menu_more),
                                    contentDescription = "Selecionar setor",
                                    modifier = Modifier.clickable {
                                        showSectorDropdown = true
                                    }
                                )
                            }
                        )

                        DropdownMenu(
                            expanded = showSectorDropdown,
                            onDismissRequest = { showSectorDropdown = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = with(LocalDensity.current) { 56.dp.toPx().toDp() })
                        ) {
                            listOf("Cozinha", "Sushi", "Copa").forEach { sector ->
                                DropdownMenuItem(
                                    text = { Text(sector) },
                                    onClick = {
                                        selectedSector = sector
                                        showSectorDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = "Fabricação do Produto",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    OutlinedTextField(
                        value = fabrication,
                        onValueChange = { fabrication = it },
                        label = { Text("ex: 11/08/2025") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Campo para data de fabricação" },
                        singleLine = true
                    )

                    Text(
                        text = "Validade do Produto",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    OutlinedTextField(
                        value = validity,
                        onValueChange = { validity = it },
                        label = { Text("ex: 11/08/2025") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Campo para data de validade" },
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (newProductName.isNotBlank() && selectedSector.isNotBlank() && viewModel != null) {
                                scope.launch {
                                    try {
                                        // Verifica se o produto já existe
                                        val productExists = viewModel.findByName(newProductName)
                                        if (productExists) {
                                            showSuccessDialog = false
                                        } else {
                                            var uid = Random.nextInt(0,100)
                                            viewModel.insertProduct(

                                                    uid= uid,
                                                    name = newProductName,
                                                    sector = selectedSector,
                                                    fabrication = fabrication,
                                                    validity = validity

                                            )
                                            newProductName = ""
                                            fabrication = ""
                                            validity = ""
                                            showSuccessDialog = true
                                        }
                                    } catch (e: Exception) {
                                        Log.e("ResgistrationProducts", "Erro ao cadastrar produto: ${e.message}")
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = newProductName.isNotBlank() && selectedSector.isNotBlank() && viewModel != null
                    ) {
                        Text(
                            text = "Cadastrar Produto",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Voltar",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Diálogo de sucesso
                    if (showSuccessDialog) {
                        AlertDialog(
                            onDismissRequest = { showSuccessDialog = false },
                            title = { Text("Sucesso") },
                            text = { Text("Produto cadastrado com sucesso!") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showSuccessDialog = false
                                        navController.popBackStack()
                                    }
                                ) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}