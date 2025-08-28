package com.example.test_2.data_db




// UserViewModel.kt


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_2.data_db.users.User
import com.example.test_2.data_db.users.UserRepository
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
}
