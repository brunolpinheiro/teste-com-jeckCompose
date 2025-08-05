package com.example.test_2


import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf





@Composable
fun ConectPrinters() {

    var showPrinnters by remember{mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Fundo branco para a nova tela
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "Encontrar impressoras:",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            modifier =Modifier
            .padding(bottom = 50.dp)
        )

        if(showPrinnters){
            Text(
                text = "PTO-260",
                color =
            )
        }

        Button(
            onClick = { showPrinnters = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(top = 50.dp, bottom = 50.dp)
        ) {
            Text(
                text = "Descobrir",
                Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelLarge,

                )
        }
    }
}