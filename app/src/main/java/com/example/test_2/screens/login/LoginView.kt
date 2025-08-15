
package com.example.carteogest.ui.theme.telas.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class AuthState {
    LOADING, // Splash ou carregando
    UNAUTHENTICATED, // Tela de login
    AUTHENTICATED // Usuário logado
}
class AuthViewModel : ViewModel() {
    private val _authState = mutableStateOf(AuthState.LOADING)
    val authState: State<AuthState> = _authState

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            // Simula carregamento
            delay(1500)

            // Aqui você verificaria se existe um token salvo, sessão válida etc.
            val usuarioLogado = false // substitua pela verificação real

            _authState.value = if (usuarioLogado) {
                AuthState.AUTHENTICATED
            } else {
                AuthState.UNAUTHENTICATED
            }
        }
    }

    fun login(usuario: String, senha: String) {
        viewModelScope.launch {
            // Verifica login real
            val sucesso = usuario == "admin" && senha == "1234"
            _authState.value = if (sucesso) {
                AuthState.AUTHENTICATED
            } else {
                AuthState.UNAUTHENTICATED
            }
        }
    }
}
