package com.example.gest.ui.telas.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gest.datadb.data_db.login.AuthState
import com.example.gest.datadb.data_db.login.UserViewModel
import com.example.Gest.R

@Composable
fun LoginScreen(
    viewModel: UserViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val usuarioLogado by viewModel.usuarioLogado.collectAsState()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState == AuthState.AUTHENTICATED) {
            onLoginSuccess() // navega para a dashboard
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icone),
            contentDescription = "Imagem do projeto",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "Etiqueta",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 8.dp)
        )
        Text(
            text = "Rápida",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Usuário") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
                viewModel.login(nome, senha)
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        // Feedback se login falhar
        usuarioLogado?.let {
            if (it != null) {
                onLoginSuccess()
            }
        } ?: run {
            if (nome.isNotBlank() && senha.isNotBlank()) {
                Text("Usuário ou senha inválidos", color = MaterialTheme.colorScheme.error)
            }
        }

    }
}
