package com.example.test_2.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.test_2.data.AppDatabase
import com.example.test_2.data_db.ProductViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ValidityOfProducts(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentDate = Date()
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel?.let { vm ->
            scope.launch {
                try {
                    vm.getAll()
                } catch (e: Exception) {
                    Log.e("ShowProducts", "Erro ao carregar produtos: ${e.message}")
                }
            }
        }
    }

    ComposeTutorialTheme {
        Scaffold(
            containerColor = Color.Black
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
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Produtos com Validade",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(products) { product ->
                                    val validityColor = product.validity?.let { validity ->
                                        try {
                                            val validityDate = dateFormat.parse(validity)
                                            if (validityDate != null && validityDate.after(currentDate)) {
                                                Color.Green // Within validity
                                            } else {
                                                Color.Red // Expired
                                            }
                                        } catch (e: Exception) {
                                            Color.Black // Parsing error, fallback to black
                                        }
                                    } ?: Color.Black // No validity, fallback to black

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = product.name,
                                            color = Color.Black,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = product.validity ?: "Sem validade",
                                            color = validityColor,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                            if (products.isEmpty()) {
                                Text(
                                    text = "Nenhum produto cadastrado",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { navController.navigate("resgistrationProducts") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "Tela de cadastrado",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Button(
                            onClick = { navController.navigate("showProducts") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "Visualizar produtos",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Button(
                            onClick = { navController.navigate("connect_printers") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = "Conectar impressora",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}