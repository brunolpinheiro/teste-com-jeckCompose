package com.example.carteogest.ui.telas.cadastro

import android.util.Log
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carteogest.login.UserViewModel
import com.example.carteogest.login.permissoes.Permissao
import com.example.carteogest.login.User
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun UserRegistrationScreen(
    viewModel: UserViewModel,
    openDrawer: () -> Unit,
    usuarioId: Int,
    navController: NavController
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var permissao by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var showExistUser by remember { mutableStateOf(false) }
    var showSucessResgistration by remember { mutableStateOf(false) }

    // Carregar fornecedor se for edição
    LaunchedEffect(usuarioId) {
        if (usuarioId != -1) {
            val usuarioEncontrado = viewModel.getById(usuarioId)
            if (usuarioEncontrado != null) {
                nome = usuarioEncontrado.nome.orEmpty()
                senha = usuarioEncontrado.senha.orEmpty()

            } else {
                Log.w("FornecedorCadastroScreen", "usuario não encontrado, iniciando cadastro")
            }
        }
    }

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
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Cadastro de Usuário", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Estado: ")
                Switch(
                    checked = status,
                    onCheckedChange = { status = it }
                )
            }

            // Dropdown de papéis

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),

                modifier = Modifier.fillMaxWidth()

            ) {
                Button(
                    onClick = {
                        if (nome.isNotBlank() && senha.isNotBlank()) {
                            scope.launch {
                                if (usuarioId == -1) {
                                    // Novo fornecedor
                                    val existUser = viewModel.findByName(nome)
                                    if (existUser) {
                                        showExistUser = true
                                    } else {
                                        val uid = Random.nextInt(0, 1000)
                                        viewModel.addUser(
                                            User(
                                                uid = uid,
                                                nome = nome,
                                                senha = senha,
                                            )
                                        )
                                        showSucessResgistration = true
                                    }
                                } else {
                                    // Atualizar fornecedor existente
                                    viewModel.updateUser(
                                        User(
                                            uid = usuarioId,
                                            nome = nome,
                                            senha = senha,
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
                if (usuarioId != -1) {
                    Button(
                        onClick = {
                            scope.launch {
                                // Cria um objeto Supplier com os dados atuais do formulário
                                val usuarioparaExcluir = usuarioId
                                viewModel.deleteUser(
                                    usuarioparaExcluir
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
