package com.example.carteogest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carteogest.datadb.data_db.supplier.SupplierDao
import com.example.carteogest.datadb.data_db.supplier.SupplierViewModel


class supplierViewModelFactory(
    private val dao: SupplierDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SupplierViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SupplierViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
