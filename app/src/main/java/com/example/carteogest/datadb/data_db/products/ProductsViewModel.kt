package com.example.carteogest.datadb.data_db.products

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.Products
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class ProductViewModel(private val dao: ProdutsDao) : ViewModel() {
    private val _products = mutableStateOf<List<Products>>(emptyList())
    val products: State<List<Products>> get() = _products
    val sector: StateFlow<List<String>> =
        dao.getSetor()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadProducts(sector: String) {
        viewModelScope.launch {
            try {
                dao.getProductsBySector(sector).collectLatest { products ->
                    _products.value = products ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao carregar produtos: ${e.message}")
                _products.value = emptyList()
            }
        }
    }

    suspend  fun findByName(name: String): Boolean {
        return try {
            val exists = dao.findByName(name)
            Log.d("ProductViewModel", "Verificação de produto com nome '$name': $exists")
            exists

        } catch (e: Exception) {
            Log.e("ProductViewModel", "Não foi possível verificar o nome do produto: ${e.message}")
            false
        }
    }


    fun insertProduct( product: Products) {
        viewModelScope.launch {
            try {
                dao.insertProduct(product)
                loadProducts(product.sector)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao inserir produto: ${e.message}")
            }
        }
    }

    fun updateProduct(product: Products) {
        viewModelScope.launch {
            try {
                dao.updateProduct(product)
                loadProducts(product.sector)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao atualizar produto: ${e.message}")
            }
        }
    }

    fun deleteProduct(product: Products) {
        viewModelScope.launch {
            try {
              dao.deleteProduct(product)
                loadProducts(product.sector)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao deletar produto: ${e.message}")
            }
        }
    }

    fun getAll() {
        viewModelScope.launch {
            try {
                dao.getAll().collectLatest { products ->
                    _products.value = products ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao carregar todos os produtos: ${e.message}")
                _products.value = emptyList()
            }
        }
    }

    suspend fun getById(id: Int): Products? {
        return dao.getById(id)
    }



}