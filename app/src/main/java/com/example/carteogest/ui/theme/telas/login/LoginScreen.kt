package com.example.carteogest.ui.telas.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.CarteoGest.R
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.carteogest.ui.telas.login.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Text("Login", style = MaterialTheme.typography.headlineMedium)
        Image(
            painter = painterResource(id = R.drawable.icone),
            contentDescription = "Imagem do projeto",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "Etiqueta",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onSurface,
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



        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        if (erro != null) {
            Text(text = erro!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.login(usuario, senha)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }

    val authState by authViewModel.authState
    LaunchedEffect(authState) {
        erro = if (authState == com.example.carteogest.ui.telas.login.AuthState.UNAUTHENTICATED) {
            "Entre com Usuário ou senha"
        } else {
            null
        }
    }
}
