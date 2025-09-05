package com.example.carteogest.ui.telas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.CarteoGest.R

@Composable
fun UserPanelScreen(
    userViewModel: UserViewModel,
    onPasswordChanged: () -> Unit,
    onLogout: () -> Unit
) {
    val usuarioLogado by userViewModel.usuarioLogado.collectAsState()

    var expandChangePassword by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fundo branco
    ) {
        // Marca d’água
        Image(
            painter = painterResource(id = R.drawable.icone), // coloque seu logo
            contentDescription = "Logo Marca d'Água",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.5f), // tamanho da logo (50% da tela)
            alpha = 0.08f, // transparência para parecer marca d’água
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Painel do Usuário", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(24.dp))

            // Nome do usuário
            OutlinedTextField(
                value = usuarioLogado ?: "",
                onValueChange = {},
                label = { Text("Nome do Usuário") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Botão para expandir/ocultar alterar senha
            OutlinedButton(
                onClick = { expandChangePassword = !expandChangePassword },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (expandChangePassword) "Cancelar Alteração de Senha" else "Alterar Senha")
            }

            // Conteúdo expansível de alteração de senha
            AnimatedVisibility(
                visible = expandChangePassword,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Senha Atual") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nova Senha") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar Nova Senha") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Mensagem de erro ou sucesso
                    errorMessage?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                    }
                    successMessage?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                    }

                    // Botão confirmar alteração
                    Button(
                        onClick = {
                            if (newPassword != confirmPassword) {
                                errorMessage = "As senhas não coincidem!"
                                successMessage = null
                            } else {
                                userViewModel.changePassword(oldPassword, newPassword) { success ->
                                    if (success) {
                                        errorMessage = null
                                        successMessage = "Senha alterada com sucesso!"
                                        expandChangePassword = false
                                        oldPassword = ""
                                        newPassword = ""
                                        confirmPassword = ""
                                        onPasswordChanged()
                                    } else {
                                        errorMessage =
                                            "Falha ao alterar senha. Verifique sua senha atual."
                                        successMessage = null
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar Nova Senha")
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Botão de logout
            Button(
                onClick = { userViewModel.logout() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Sair")
            }
        }
    }
}