package com.example.test_2

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResgistrationProducts(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } }

    var newProductName by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf("Todos") }
    var showSuccessDialog by remember { mutableStateOf(false) } // Estado para o diálogo de sucesso
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
            }
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
                }

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

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Cozinha", "Sushi", "Copa").forEach { sector ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSector = sector
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedSector == sector,
                                onClick = {
                                    selectedSector = sector
                                },
                                modifier = Modifier.semantics { contentDescription = "Selecionar setor $sector" }
                            )
                            Text(
                                text = sector,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (newProductName.isNotBlank() && selectedSector.isNotBlank() && selectedSector != "Todos" && viewModel != null) {
                            scope.launch {
                                try {
                                    // Verifica se o produto já existe
                                    val productExists = viewModel.findByName(newProductName)
                                    if (productExists) {
                                        showSuccessDialog = false
                                    } else {
                                        val uid = Random.nextInt(1, 100)
                                        viewModel.insertProduct(uid, newProductName, selectedSector)
                                        newProductName = ""
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
                    enabled = newProductName.isNotBlank() && selectedSector.isNotBlank() && selectedSector != "Todos" && viewModel != null
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
                                    navController.popBackStack() // Redireciona após fechar o diálogo
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