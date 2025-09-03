package com.example.carteogest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.login.UserDao
import com.example.carteogest.datadb.data_db.login.UserRepository
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.datadb.data_db.products.ProdutsDao
import com.example.carteogest.datadb.data_db.validity.ValidityDao
import com.example.carteogest.datadb.data_db.login.UserViewModel


class UsersViewModelFactory(
    private val dao: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
