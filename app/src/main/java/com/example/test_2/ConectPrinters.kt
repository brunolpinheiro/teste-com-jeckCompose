package com.example.test_2

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConectPrinters() {
    // State for showing printers
    var showPrinters by remember { mutableStateOf(false) }
    // State for selected printer
    var selectedPrinter by remember { mutableStateOf<String?>(null) }
    // Sample list of printers (replace with actual printer discovery logic)
    val printers = listOf("PTO-260", "PTO-300", "PTO-400", "PTO-500")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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

        if (showPrinters) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(printers) { printer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPrinter = printer }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPrinter == printer,
                            onClick = { selectedPrinter = printer },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = printer,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Button(
            onClick = { showPrinters = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(top = 50.dp, bottom = 50.dp)
        ) {
            Text(
                text = "Descobrir",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Optional: Display selected printer (for feedback)
        if (selectedPrinter != null) {
            Text(
                text = "Impressora selecionada: $selectedPrinter",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}