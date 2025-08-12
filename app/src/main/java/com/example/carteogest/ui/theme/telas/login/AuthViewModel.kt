package com.example.carteogest.ui.telas.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class AuthState {
    LOADING,
    UNAUTHENTICATED,
    AUTHENTICATED
}

class AuthViewModel : ViewModel() {
    private val _authState = mutableStateOf(AuthState.UNAUTHENTICATED)
    val authState: State<AuthState> = _authState

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            delay(1500) // Simula carregamento/splash

            val usuarioLogado = false // TODO: verificar token/sessão real

            _authState.value = if (usuarioLogado) {
                AuthState.AUTHENTICATED
            } else {
                AuthState.UNAUTHENTICATED
            }
        }
    }

    fun login(usuario: String, senha: String) {
        viewModelScope.launch {
            // Simulação de autenticação
            val sucesso = usuario == "admin" && senha == "1234"
            _authState.value = if (sucesso) {
                AuthState.AUTHENTICATED
            } else {
                AuthState.UNAUTHENTICATED
            }
        }
    }
}
