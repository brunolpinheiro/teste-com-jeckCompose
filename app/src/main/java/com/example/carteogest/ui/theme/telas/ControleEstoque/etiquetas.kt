package com.example.carteogest.ui.telas.ControleEstoque

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun EditableDateInputField(
    label: String = "Data",
    initialDate: String = "",
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var textValue by remember { mutableStateOf(initialDate) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Função para abrir o calendário
    fun openDatePicker() {
        val currentDate = try {
            LocalDate.parse(textValue, dateFormatter)
        } catch (e: Exception) {
            LocalDate.now()
        }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                textValue = newDate.format(dateFormatter)
                onDateSelected(textValue)
            },
            currentDate.year,
            currentDate.monthValue - 1,
            currentDate.dayOfMonth
        ).show()
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onDateSelected(it)
        },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Campo para $label" },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { openDatePicker() }) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Selecionar data")
            }
        }
    )
}
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
    var quantit by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            // Aqui você pode usar seu EditableDateInputField, exemplo:
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
                value = quantit,
                onValueChange = { quantit = it },
                label = { Text("Quantidade de Etiquetas") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            val quantit = quantit.toIntOrNull() ?: 1
            Button(
                onClick = {
                    viewModel.printLabel(
                        text1 = productName,
                        text2 = responsible,
                        text3 = manufactureDate,
                        text4 = expirationDate,
                        text5 = quantit
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Imprimir")
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar")
            }
        }
    }
}