/*package com.example.carteogest.ui.theme.telas.cadastro.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteogest.datadb.dbrom.User
import com.example.carteogest.datadb.dbrom.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AuthState { LOADING, UNAUTHENTICATED, AUTHENTICATED }

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    // Lista de usuários
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    // Usuário logado
    private val _usuarioLogado = MutableStateFlow<User?>(null)
    val usuarioLogado: StateFlow<User?> = _usuarioLogado

    // Estado de autenticação
    private val _authState = MutableStateFlow(AuthState.LOADING)
    val authState: StateFlow<AuthState> = _authState

    init {
        loadUsers()
        checkLoggedUser()
    }

    /*** CRUD de usuários ***/
    fun addUser(nome: String, login: String, email: String, senha: String, roleId: Int) {
        val hashed = hashSenha(senha)
        val user = User(
            nome = nome,
            login = login,
            email = email,
            senhaHash = hashed,
            roleId = roleId,
            roleName = getRoleName(roleId)
        )
        repo.addUser(user)
        loadUsers()
    }

    fun updateUser(user: User) {
        repo.updateUser(user)
        loadUsers()
    }

    fun removeUser(userId: Int) {
        repo.deleteUser(userId)
        loadUsers()
    }

    fun getRoleName(roleId: Int): String {
        return repo.getRoleName(roleId)
    }

    private fun loadUsers() {
        _users.value = repo.getUsers()
    }

    /*** Login / Logout ***/
    fun login(login: String, senha: String, onResult: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            val hashed = hashSenha(senha)
            val user = repo.autenticarUsuario(login, hashed)
            _usuarioLogado.value = user
            _authState.value = if (user != null) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
            onResult?.invoke(user != null)
        }
    }

    fun logout() {
        _usuarioLogado.value = null
        _authState.value = AuthState.UNAUTHENTICATED
    }

    private fun checkLoggedUser() {
        // Se quiser, aqui pode verificar persistência (SharedPreferences) para auto-login
        _authState.value = if (_usuarioLogado.value != null) AuthState.AUTHENTICATED
        else AuthState.UNAUTHENTICATED
    }

    private fun hashSenha(password: String): String {
        return password.reversed() // mesmo esquema do DatabaseHelper
    }

    /*** Controle de permissões ***/
    fun usuarioPode(roleNecessario: String): Boolean {
        val user = _usuarioLogado.value
        return user?.roleName == roleNecessario || user?.roleName == "Administrador"
    }
}
*/