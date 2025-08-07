package com.example.test_2.data_db




import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_2.data.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.test_2.data_db.ProductViewModel

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

    fun insertProduct(uid: Int, name: String, sector: String) {
        viewModelScope.launch {
            try{
                database.productDao().insertProduct(Products(uid = uid,name = name, sector = sector))
            }
            catch (e: Exception){
                Log.e("ProducViewModel","Falha ao inserir produtos:${e.message}")
            }
            loadProducts(sector) // Atualiza a lista após inserção
        }
    }
}