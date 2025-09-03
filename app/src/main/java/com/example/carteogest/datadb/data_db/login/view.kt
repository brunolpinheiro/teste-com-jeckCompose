// UserViewModel.kt
package com.example.carteogest.datadb.data_db.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



enum class AuthState {
    AUTHENTICATED,
    UNAUTHENTICATED
}
class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users
    private val _loggedUser = MutableStateFlow<String?>(null)
    val loggedUser: StateFlow<String?> = _loggedUser

    private val _authState = MutableStateFlow<AuthState>(AuthState.UNAUTHENTICATED)
    val authState: StateFlow<AuthState> = _authState

    private val _usuarioLogado = MutableStateFlow<String?>(null)
    val usuarioLogado: StateFlow<String?> = _usuarioLogado
    fun loadUsers() {
        viewModelScope.launch {
            _users.value = repo.getUsers()
        }
        observeLoggedUser()
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            repo.addUser(user)
            loadUsers()
        }
    }
    fun updateUser(user: User) {
        viewModelScope.launch {
            repo.updateUser(user)
            loadUsers()
        }
    }
    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            val userToDelete = _users.value.find { it.uid == userId }
            userToDelete?.let {
                repo.deleteUser(it)
                loadUsers()
            }
        }
    }
    private fun observeLoggedUser() {
        viewModelScope.launch {
            repo.loggedUserFlow().collect { username ->
                _usuarioLogado.value = username
                _authState.value =
                    if (username != null) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
            }
        }
    }

    fun login(nome: String, senha: String) {
        viewModelScope.launch {
            val user = repo.autenticarUsuario(nome, senha)
            if (user != null) {
                repo.saveLoggedUser(user.nome)
                _authState.value = AuthState.AUTHENTICATED
            } else {
                _authState.value = AuthState.UNAUTHENTICATED
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.clearLoggedUser()
        }
    }

    suspend  fun findByName(nome: String): Boolean {
        return try {
            val exists = repo.findByName(nome)
            Log.d("SupplierModelView", "Verificação do nome do fornecedor  '$nome': $exists")
            exists

        } catch (e: Exception) {
            Log.e("SupplierModelView", "Não foi possível verificar o nome do produto: ${e.message}")
            false
        }
    }
    suspend fun getById(id: Int): User? {
        return repo.getById(id)
    }
    fun getAllUsers() {
        viewModelScope.launch {
            _users.value = repo.getUsers()
        }
    }
    fun changePassword(oldPassword: String, newPassword: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val nomeUsuario = _usuarioLogado.value
            if (nomeUsuario == null) {
                callback(false)
                return@launch
            }

            val user = repo.findUserByName(nomeUsuario) // busca objeto User pelo nome
            if (user != null && user.senha == oldPassword) {
                val updatedUser = user.copy(senha = newPassword)
                repo.updateUser(updatedUser) // atualiza no banco
                callback(true)
            } else {
                callback(false)
            }
        }
    }
    suspend fun getPermissao(nome: String): String? {
        return repo.getPermissao(nome)
    }


}
