package com.example.carteogest.iu.telas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteogest.datadb.data_db.login.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch



class PUserViewModel : ViewModel() {

    // Estado do usuário logado
    private val _loggedUser = MutableStateFlow<User?>(null)
    val loggedUser: StateFlow<User?> = _loggedUser

    fun loadUser(user: User) {
        _loggedUser.value = user
    }

    fun changePassword(oldPassword: String, newPassword: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = _loggedUser.value
            if (user != null && user.senha == oldPassword) {
                // Aqui faria update no banco (Room ou API)
                _loggedUser.value = user.copy(senha = newPassword)
                callback(true)
            } else {
                callback(false)
            }
        }
    }
}
