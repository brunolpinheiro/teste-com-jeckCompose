package com.example.carteogest.ui.telas.cadastro

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.carteogest.login.UserViewModel
import com.example.carteogest.login.permissoes.Permissao
import com.example.carteogest.login.User
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch


@Composable
fun UserRegistrationScreen(
    viewModel: UserViewModel,
    openDrawer: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var permissao by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
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
            TextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Tipo de usuario") })
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

            Button(
                onClick = {
                    val user = User(nome = nome, senha = senha.reversed(), ativo = true)
                    viewModel.addUser(user)
                    val permissao = Permissao(permissao = permissao)

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar")
            }
        }
    }
}
