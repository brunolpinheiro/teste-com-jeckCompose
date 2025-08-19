package com.example.test_2.screens.invetory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.products.ProductViewModel
import com.example.test_2.data_db.products.Products
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowProducts(
    navController: NavController,
    openDrawer: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } }

    var selectedSector by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<Products?>(null) }
    var productToDelete by remember { mutableStateOf<Products?>(null) }
    var editProductName by remember { mutableStateOf("") }
    var editProductSector by remember { mutableStateOf("") }
    var editProductFabrication by remember { mutableStateOf("") }
    var editProductValidity by remember { mutableStateOf("") }
    var showEditSectorDropdown by remember { mutableStateOf(false) }
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }
    var loadingProducts by remember {mutableStateOf(false)}

    // Carregar todos os produtos ao iniciar
    LaunchedEffect(loadingProducts) {
        //   viewModel?.let { vm ->
        //       scope.launch {
        //         try {
        //           vm.getAll()
        //     } catch (e: Exception) {
        //        Log.e("ShowProducts", "Erro ao carregar produtos: ${e.message}")
        //   }
        //}
        //}
        if(loadingProducts){
            delay(2000L) // Atraso de 2 segundos
            loadingProducts= false

        }
    }

    ComposeTutorialTheme {
        Scaffold(
            containerColor = Color.Black // Fundo preto para o Scaffold
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary) // Fundo branco para a área de conteúdo
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
                    listOf("Todos", "Cozinha", "Sushi", "Copa").forEach { sector ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSector = sector
                                    viewModel?.let { vm ->
                                        scope.launch {
                                            try {
                                                if (selectedSector == "Todos") {
                                                    vm.getAll()
                                                } else {
                                                    vm.loadProducts(sector)
                                                }
                                            } catch (e: Exception) {
                                                Log.e("ShowProducts", "Erro ao carregar produtos: ${e.message}")
                                            }
                                        }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedSector == sector,
                                onClick = {
                                    selectedSector = sector
                                    loadingProducts = true
                                    viewModel?.let { vm ->
                                        scope.launch {
                                            try {
                                                if (selectedSector == "Todos") {
                                                    vm.getAll()
                                                } else {
                                                    vm.loadProducts(sector)
                                                }
                                            } catch (e: Exception) {
                                                Log.e("ShowProducts", "Erro ao carregar produtos: ${e.message}")
                                            }
                                        }
                                    }
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (loadingProducts) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .semantics { contentDescription = "Carregando produtos" },
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    else{
                        items(products) { product ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${product.name} - ${product.sector}",
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )

                                }
                                    Row {
                                        Button(
                                            onClick = {
                                                productToEdit = product
                                                editProductName = product.name
                                                editProductSector = product.sector ?: ""
                                                editProductFabrication = product.fabrication ?: ""
                                                editProductValidity = product.validity ?: ""
                                                showEditDialog = true
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            modifier = Modifier.padding(end = 8.dp)
                                        ) {
                                            Text("Editar", fontSize = 14.sp)
                                        }
                                        Button(
                                            onClick = {
                                                productToDelete = product
                                                showDeleteDialog = true
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error,
                                                contentColor = MaterialTheme.colorScheme.onError
                                            )
                                        ) {
                                            Text("Deletar", fontSize = 14.sp)
                                        }
                                    }
                                }


                            }
                        }
                    }
                }

                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Voltar", fontSize = 16.sp)
                }
            }

            // Diálogo de Exclusão
            if (showDeleteDialog && productToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Deletar Produto") },
                    text = {
                        Text(
                            text = "Tem certeza que deseja apagar o produto '${productToDelete!!.name}'?"
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel?.let {
                                    scope.launch {
                                        try {
                                            it.deleteProduct(productToDelete!!)
                                            selectedSector = ""
                                            showDeleteDialog = false
                                        } catch (e: Exception) {
                                            Log.e("ShowProducts", "Erro ao deletar produto: ${e.message}")
                                        }
                                    }
                                }
                            }
                        ) {
                            Text("Sim")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Não")
                        }
                    }
                )
            }

            // Diálogo de Edição
            if (showEditDialog && productToEdit != null) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text("Editar Produto") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = editProductName,
                                onValueChange = { editProductName = it },
                                label = { Text("Nome do Produto") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = editProductSector,
                                onValueChange = {},
                                label = { Text("Setor") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showEditSectorDropdown = true },
                                readOnly = true
                            )
                            DropdownMenu(
                                expanded = showEditSectorDropdown,
                                onDismissRequest = { showEditSectorDropdown = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                listOf("Cozinha", "Sushi", "Copa").forEach { sector ->
                                    DropdownMenuItem(
                                        text = { Text(sector) },
                                        onClick = {
                                            editProductSector = sector
                                            showEditSectorDropdown = false
                                        }
                                    )
                                }
                            }
                            OutlinedTextField(
                                value = editProductFabrication,
                                onValueChange = { editProductFabrication = it },
                                label = { Text("Data de Fabricação (opcional)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = editProductValidity,
                                onValueChange = { editProductValidity = it },
                                label = { Text("Validade (opcional)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel?.let {
                                    scope.launch {
                                        try {
                                            it.updateProduct(
                                                Products(
                                                    uid = productToEdit!!.uid,
                                                    name = editProductName,
                                                    sector = editProductSector,
                                                    fabrication = editProductFabrication.ifEmpty { null },
                                                    validity = editProductValidity.ifEmpty { null },
                                                    skuCode = "",
                                                    price = 0f,
                                                    promotionalPrice = null,
                                                    quantity = 0,
                                                    unitOfMeasure = null,
                                                    brand = "",
                                                    status = null,
                                                    barcode = null,
                                                    height = null,
                                                    width = null,
                                                    length = null,
                                                    weight = null,
                                                    color = null,
                                                    size = null,
                                                    cost = null,
                                                    tags = null,
                                                    supplier = null,

                                                 )
                                            )
                                            showEditDialog = false
                                        } catch (e: Exception) {
                                            Log.e("ShowProducts", "Erro ao atualizar produto: ${e.message}")
                                        }
                                    }
                                }
                            },
                            enabled = editProductName.isNotBlank()
                        ) {
                            Text("Salvar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEditDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
