package com.example.carteogest.ui.theme.telas.usuarios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carteogest.login.UserViewModel




@Composable
fun UserListScreen(userViewModel: UserViewModel = viewModel()) {
    val users by userViewModel.users.collectAsState()

    // Atualiza a lista de usuários ao entrar na tela
    LaunchedEffect(Unit) {
        userViewModel.loadUsers()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Usuários cadastrados",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(users) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Nome: ${user.nome}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Ativo: ${if(user.ativo) "Sim" else "Não"}", style = MaterialTheme.typography.bodyMedium)
                        }
                        // Aqui você pode colocar botões de ação (editar, excluir)
                        Column {
                            IconButton(onClick = { /* TODO: Editar usuário */ }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { userViewModel.deleteUser(user.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                    }
                }
            }
        }
    }
}
