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
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.carteogest.datadb.data_db.login.User
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import kotlin.random.Random

/*
@Composable
fun UserRegistrationScreen(
    viewModel: UserViewModel,
    openDrawer: () -> Unit,
    usuarioId: Int,
    navController: NavController,
    userViewModel: UserViewModel
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
                userViewModel = userViewModel,
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer,
                navController = navController

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
                                navController.popBackStack()

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
*/

// Enum para os cargos
enum class UserRole(val label: String) {
    ADMIN("Administrador"),
    ESTOQUISTA("Estoquista"),
    USUARIO("Usuário")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegistrationScreen(
    viewModel: UserViewModel,
    openDrawer: () -> Unit,
    usuarioId: Int,
    navController: NavController,
    userViewModel: UserViewModel
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.USUARIO) } // padrão
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var showExistUser by remember { mutableStateOf(false) }
    var showSucessResgistration by remember { mutableStateOf(false) }

    // Carregar usuário se for edição
    LaunchedEffect(usuarioId) {
        if (usuarioId != -1) {
            val usuarioEncontrado = viewModel.getById(usuarioId)
            if (usuarioEncontrado != null) {
                nome = usuarioEncontrado.nome.orEmpty()
                senha = usuarioEncontrado.senha.orEmpty()
                usuarioEncontrado.permissao?.let {
                    try {
                        selectedRole = UserRole.valueOf(it)
                    } catch (e: Exception) {
                        Log.w("UserRegistrationScreen", "Permissão inválida: $it, default USUARIO")
                        selectedRole = UserRole.USUARIO
                    }
                }
            } else {
                Log.w("UserRegistrationScreen", "Usuário não encontrado, iniciando cadastro")
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer,
                navController = navController
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

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") }
            )

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation()
            )

            // Dropdown de papéis (permissão)
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedRole.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Permissão") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    UserRole.values().forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role.label) },
                            onClick = {
                                selectedRole = role
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botões de ação
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (nome.isNotBlank() && senha.isNotBlank()) {
                            scope.launch {
                                if (usuarioId == -1) {
                                    // Novo usuário
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
                                                permissao = selectedRole.name // salvando como String
                                            )
                                        )
                                        showSucessResgistration = true
                                    }
                                } else {
                                    // Atualizar usuário existente
                                    viewModel.updateUser(
                                        User(
                                            uid = usuarioId,
                                            nome = nome,
                                            senha = senha,
                                            permissao = selectedRole.name // salvando como String
                                        )
                                    )
                                    showSucessResgistration = true
                                }
                                navController.popBackStack()
                            }
                        }
                    }
                ) {
                    Text("Salvar", fontSize = 18.sp)
                }

                if (usuarioId != -1) {
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.deleteUser(usuarioId)
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
