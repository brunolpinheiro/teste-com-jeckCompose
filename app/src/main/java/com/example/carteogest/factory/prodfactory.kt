package com.example.carteogest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.datadb.data_db.products.ProdutsDao
import com.example.carteogest.datadb.data_db.validity.ValidityDao


class ProductViewModelFactory(
    private val dao: ProdutsDao,
    private val validade : ValidityDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(dao,validade) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
