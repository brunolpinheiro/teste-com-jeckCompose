package com.example.test_2.data_db

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_2.data.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log

class ProductViewModel(private val database: AppDatabase) : ViewModel() {
    private val _products = mutableStateOf<List<Products>>(emptyList())
    val products: State<List<Products>> get() = _products

    fun loadProducts(sector: String) {
        viewModelScope.launch {
            try {
                database.productDao().getProductsBySector(sector).collectLatest { products ->
                    _products.value = products ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao carregar produtos: ${e.message}")
                _products.value = emptyList()
            }
        }
    }

    fun findByName(name: String): Boolean {
        return try {
            database.productDao().findByName(name)
        } catch (e: Exception) {
            Log.e("ProductViewModel", "Não foi possível verificar o nome do produto: ${e.message}")
            false
        }
    }


    fun insertProduct(uid: Int, name: String, sector: String) {
        viewModelScope.launch {
            try {
                database.productDao().insertProduct(Products(uid = uid, name = name, sector = sector))
                loadProducts(sector)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao inserir produto: ${e.message}")
            }
        }
    }

    fun updateProduct(product: Products) {
        viewModelScope.launch {
            try {
                database.productDao().updateProduct(product)
                loadProducts(product.sector)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao atualizar produto: ${e.message}")
            }
        }
    }

    fun deleteProduct(product: Products) {
        viewModelScope.launch {
            try {
                database.productDao().deleteProduct(product)
                loadProducts(product.sector)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao deletar produto: ${e.message}")
            }
        }
    }

    fun getAll() {
        viewModelScope.launch {
            try {
                database.productDao().getAll().collectLatest { products ->
                    _products.value = products ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao carregar todos os produtos: ${e.message}")
                _products.value = emptyList()
            }
        }
    }
}