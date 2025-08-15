package com.example.test_2.screens.invetory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.test_2.bluetooth.model.BluetoothViewModel
import com.example.test_2.screens.invetory.EditableDateInputField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Printers(
    navController: NavController,
    viewModel: BluetoothViewModel,
    openDrawer: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var responsible by remember { mutableStateOf("") }
    var manufactureDate by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    ComposeTutorialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Imprimir Etiqueta") },
                    navigationIcon = {
                        IconButton(onClick = openDrawer) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Detalhes da Etiqueta",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nome do Produto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = responsible,
                    onValueChange = { responsible = it },
                    label = { Text("Responsável") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                EditableDateInputField(
                    label = "Data de Fabricação",
                    initialDate = manufactureDate
                ) { selected ->
                    manufactureDate = selected
                }

                EditableDateInputField(
                    label = "Validade",
                    initialDate = expirationDate
                ) { selected ->
                    expirationDate = selected
                }

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantidade de Etiquetas") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (productName.isNotBlank() && quantity.isNotBlank()) {
                            viewModel.printLabel(
                                text1 = productName,
                                text2 = responsible,
                                text3 = manufactureDate,
                                text4 = expirationDate,
                                text5 = quantity
                            )
                        }
                    },
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
                        text = "Imprimir",
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
}
