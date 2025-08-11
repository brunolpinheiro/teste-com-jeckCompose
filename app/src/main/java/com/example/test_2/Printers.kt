package com.example.test_2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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

@Composable
fun Printers(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } }

    var printerNameInput by remember { mutableStateOf("") }
    var ipAddress by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var showProductDropdown by remember { mutableStateOf(false) }
    var showSectorDropdown by remember { mutableStateOf(false) }
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }
    var selectedSector by remember { mutableStateOf("Todos") }

    // Carregar todos os produtos ao iniciar
    LaunchedEffect(Unit) {
        try {
            viewModel?.getAll()
        } catch (e: Exception) {
            Log.e("Printers", "Falha ao carregar os produtos: ${e.message}")
        }
    }

    ComposeTutorialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Detalhes da Impressora",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium
            )

            // Campo para selecionar setor
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
                    listOf("Cozinha", "Copa", "Sushi").forEach { sector ->
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

            // Campo para selecionar produto
            Box {
                OutlinedTextField(
                    value = printerNameInput,
                    onValueChange = {},
                    label = { Text("Nome da Impressora") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_more),
                            contentDescription = "Selecionar produto",
                            modifier = Modifier.clickable {
                                showProductDropdown = true
                            }
                        )
                    }
                )

                DropdownMenu(
                    expanded = showProductDropdown,
                    onDismissRequest = { showProductDropdown = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = with(LocalDensity.current) { 56.dp.toPx().toDp() })
                ) {
                    if (products.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Nenhum produto disponível") },
                            onClick = { showProductDropdown = false }
                        )
                    } else {
                        products.forEach { product ->
                            DropdownMenuItem(
                                text = { Text(product.name) },
                                onClick = {
                                    printerNameInput = product.name
                                    showProductDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
                label = { Text("Endereço IP") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = port,
                onValueChange = { port = it },
                label = { Text("Porta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentário") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { /* Ação do botão, ex: salvar ou conectar */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Salvar",
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
        }
    }
}