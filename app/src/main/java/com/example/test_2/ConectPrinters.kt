package com.example.test_2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import kotlinx.coroutines.delay

@Composable
fun ConectPrinters(navController: NavController) {
    var showPrinters by remember { mutableStateOf(false) }
    var selectedPrinter by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    // Lista de impressoras de exemplo
    val printers = listOf("HP LaserJet", "Epson EcoTank", "Canon Pixma")

    fun searchForPrinters() {
        if (!showPrinters) {
            // Inicia a busca de impressoras
            loading = true
        } else if (selectedPrinter != null) {
            // Navega para a tela de detalhes quando uma impressora está selecionada
            navController.navigate("printer_details")
        }
    }

    // Simula o carregamento com um atraso de 2 segundos
    LaunchedEffect(loading) {
        if (loading) {
            delay(2000L) // Atraso de 2 segundos
            loading = false
            showPrinters = true
        }
    }

    ComposeTutorialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Encontrar impressoras:",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 50.dp)
            )

            // Mostra a animação de carregamento ou a lista de impressoras
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 16.dp)
                        .semantics { contentDescription = "Buscando impressoras" },
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (showPrinters) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(printers) { printer ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPrinter = printer }
                                .padding(horizontal = 8.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (selectedPrinter == printer) {
                                            MaterialTheme.colorScheme.secondary // #343A40
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        }
                                    )
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = printer,
                                    fontSize = 18.sp,
                                    color = if (selectedPrinter == printer) {
                                        MaterialTheme.colorScheme.onSecondary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }


                Button(
                    onClick = { searchForPrinters() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                    enabled = !loading // Habilita o botão se não estiver carregando
                ) {
                    if (showPrinters && selectedPrinter != null) {
                        Text(
                            text = "Conectar",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    } else {
                        Text(
                            text = "Descobrir",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
            }
        }
    }
}