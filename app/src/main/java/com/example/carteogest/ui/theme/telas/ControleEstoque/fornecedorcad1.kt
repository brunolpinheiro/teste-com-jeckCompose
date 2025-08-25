package com.example.carteogest.iu.telas.ControleEstoque

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.supplier.Supplier
import com.example.carteogest.datadb.data_db.supplier.SupplierViewModel
import com.example.carteogest.factory.supplierViewModelFactory
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SupplierRegistration(
    supplierViewModel: SupplierViewModel,
    openDrawer: () -> Unit,
    navController: NavController,
    fornecedoresid: Int
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }

    var name by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showExistSupplier by remember { mutableStateOf(false) }
    var showSucessResgistration by remember { mutableStateOf(false) }

    // Carregar fornecedor se for edição
    LaunchedEffect(fornecedoresid) {
        if (fornecedoresid != -1) {
            val fornecedorEncontrado = supplierViewModel.getById(fornecedoresid)
            if (fornecedorEncontrado != null) {
                name = fornecedorEncontrado.name.orEmpty()
                cnpj = fornecedorEncontrado.cnpj.orEmpty()
                address = fornecedorEncontrado.adress.orEmpty()
                phone = fornecedorEncontrado.phone.orEmpty()
                email = fornecedorEncontrado.email.orEmpty()
            } else {
                Log.w("FornecedorCadastroScreen", "Fornecedor não encontrado, iniciando cadastro")
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Bruno",
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (database == null) {
                Text(
                    text = "Erro: Não foi possível conectar ao banco de dados",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp
                )
            } else {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Fornecedor") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cnpj,
                    onValueChange = { cnpj = it },
                    label = { Text("CNPJ") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Endereço") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Button(
                        onClick = {
                            if (name.isNotBlank() && cnpj.isNotBlank()) {
                                scope.launch {
                                    if (fornecedoresid == -1) {
                                        // Novo fornecedor
                                        val existSupplier = supplierViewModel.findByName(name)
                                        if (existSupplier) {
                                            showExistSupplier = true
                                        } else {
                                            val uid = Random.nextInt(0, 1000)
                                            supplierViewModel.insertSupplier(
                                                uid = uid,
                                                name = name,
                                                cnpj = cnpj,
                                                adress = address,
                                                phone = phone,
                                                email = email
                                            )
                                            showSucessResgistration = true
                                        }
                                    } else {
                                        // Atualizar fornecedor existente
                                        supplierViewModel.updateSupplier(
                                            Supplier(
                                                uid = fornecedoresid,
                                                name = name,
                                                cnpj = cnpj,
                                                adress = address,
                                                phone = phone,
                                                email = email
                                            )
                                        )
                                        showSucessResgistration = true
                                    }

                                }
                            }
                        },

                    ) {
                        Text("Salvar", fontSize = 18.sp)
                    }
                    if (fornecedoresid != -1) {
                        Button(
                            onClick = {
                                scope.launch {
                                    // Cria um objeto Supplier com os dados atuais do formulário
                                    val fornecedorParaExcluir = Supplier(
                                        uid = fornecedoresid,
                                        name = name,
                                        cnpj = cnpj,
                                        adress = address,
                                        phone = phone,
                                        email = email
                                    )
                                    supplierViewModel.deleteSupplier(
                                        fornecedorParaExcluir
                                    )
                                    navController.popBackStack()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        ) {
                            Text("Excluir", color = Color.White)
                        }
                    }
                }

            }
        }
    }

    if (showExistSupplier) {
        AlertDialog(
            onDismissRequest = { showExistSupplier = false },
            title = { Text("Fornecedor já cadastrado") },
            text = { Text("Falha ao cadastrar Fornecedor!") },
            confirmButton = {
                TextButton(onClick = { showExistSupplier = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showSucessResgistration) {
        AlertDialog(
            onDismissRequest = { showSucessResgistration = false },
            title = { Text("Fornecedor cadastrado com sucesso") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSucessResgistration = false
                        navController.popBackStack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
