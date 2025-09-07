package com.example.gest.ui.theme.telas.cadastro.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gest.datadb.data_db.login.UserRepository
import com.example.gest.datadb.data_db.login.UserViewModel

class UserViewModelFactory(
    private val repo: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}