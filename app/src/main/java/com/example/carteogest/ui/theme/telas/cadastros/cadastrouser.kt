// UserRegistrationScreen.kt
package com.example.carteogest.ui.theme.telas.cadastros


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carteogest.login.permissoes.Permissao
import com.example.carteogest.login.User
import com.example.carteogest.login.UserViewModel

@Composable
fun UserRegistrationScreen(userViewModel: UserViewModel = viewModel()) {

    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var roleId by remember { mutableStateOf(1) } // por enquanto fixo
    var permissao by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
        TextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") })
        TextField(value = permissao, onValueChange = { permissao = it }, label = { Text("Tipo de usuario") })

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



        Button(onClick = {
            val user = User(nome = nome, senha = senha.reversed(), ativo = status)
            userViewModel.addUser(user)
            val permissao = Permissao(permissao = permissao)
        }) {
            Text("Cadastrar")
        }
    }
}
