package com.example.test_2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun Printers() {
    var productName by remember { mutableStateOf("") }
    var responsible by remember { mutableStateOf("") }
    var manufactureDate by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }

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
                text = "Imprimir Etiqueta",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Nome do Produto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo para nome do produto" },
                singleLine = true
            )

            OutlinedTextField(
                value = responsible,
                onValueChange = { responsible = it },
                label = { Text("Responsável") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo para responsável" },
                singleLine = true
            )

            OutlinedTextField(
                value = manufactureDate,
                onValueChange = { manufactureDate = it },
                label = { Text("Data de Fabricação") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo para data de fabricação" },
                singleLine = true
            )

            OutlinedTextField(
                value = expirationDate,
                onValueChange = { expirationDate = it },
                label = { Text("Validade") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo para validade" },
                singleLine = true
            )

            Button(
                onClick = { /* Ação para imprimir etiqueta */ },
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
                onClick = { },
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