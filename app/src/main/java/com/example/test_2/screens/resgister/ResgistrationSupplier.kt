package com.example.test_2.screens.resgister




import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.supplier.SupplierViewModel
import com.example.test_2.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SupplierRegistration(openDrawer: () -> Unit,
                         navController: NavController) {


    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { SupplierViewModel(it) } }
    var name by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showExistSupplier  by remember{mutableStateOf(false)}
    var showSucessResgistration by remember{mutableStateOf(false)}


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
            }

           else{

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

                Button(
                    onClick = {
                        if (name.isNotBlank() &&
                            cnpj.isNotBlank() &&
                            viewModel != null){
                            scope.launch {
                                var existSupplier = viewModel.findByName(name)
                                if(existSupplier){
                                    showExistSupplier = true
                                }
                                else{
                                    scope.launch {
                                        var uid = Random.nextInt(0, 100)
                                        viewModel.insertSupplier(
                                            uid = uid,
                                            name = name,
                                            cnpj  =  cnpj,
                                            adress = address,
                                            phone = phone,
                                            email = email
                                        )
                                        showSucessResgistration = true

                                    }
                                }
                            }


                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Salvar", fontSize = 18.sp)
                }
            }
           }
        }

        if (showExistSupplier) {
            AlertDialog(
                onDismissRequest = { showExistSupplier = false },
                title = { Text("fornacedor ja cadastrado") },
                text = { Text("Falha ao cadastrar Fornecedor!") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExistSupplier = false

                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        if(showSucessResgistration){
            AlertDialog(
                onDismissRequest = { showSucessResgistration = false },
                title = { Text("fornacedor Cadastrado com Sucesso") },

                confirmButton = {
                    TextButton(
                        onClick = {
                            showExistSupplier = false
                            navController.popBackStack()

                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }


