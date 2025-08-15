package com.example.test_2.screens.invetory



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.test_2.bluetooth.model.BluetoothViewModel
import android.app.DatePickerDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
