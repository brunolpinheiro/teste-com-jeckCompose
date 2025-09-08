package com.example.gest.ui.telas.login



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gest.datadb.data_db.login.AuthState
import com.example.gest.datadb.data_db.login.UserViewModel
import com.example.Gest.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: UserViewModel = viewModel(),
    openDrawer: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var isInitialLoading by remember { mutableStateOf(true) }
    var loginError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        delay(4000L) // Delay de 1,5 segundos para simular carregamento (ajustável)
        isInitialLoading = false
        if (authState == AuthState.AUTHENTICATED) {
            onLoginSuccess()
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
            text = "Gest",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(all = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = isInitialLoading,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (!isInitialLoading && authState == AuthState.UNAUTHENTICATED) {
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
                    if (nome.isNotBlank() && senha.isNotBlank()) {
                        viewModel.login(nome, senha)
                        loginError = false
                    } else {
                        loginError = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Preencha usuário e senha")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }

            if (loginError) {
                Text(
                    text = "Preencha todos os campos",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else if (nome.isNotBlank() && senha.isNotBlank() && authState == AuthState.UNAUTHENTICATED) {
                Text(
                    text = "Usuário ou senha inválidos",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
        }

