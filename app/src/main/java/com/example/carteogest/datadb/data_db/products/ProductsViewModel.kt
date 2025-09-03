package com.example.carteogest.datadb.data_db.products

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import com.example.carteogest.datadb.data_db.ProductWithValidities
import com.example.carteogest.datadb.data_db.supplier.Supplier
import com.example.carteogest.datadb.data_db.validity.ValidityAndFabrication
import com.example.carteogest.datadb.data_db.validity.ValidityDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProductViewModel(
    private val dao: ProdutsDao,
    private val validadeDao: ValidityDao
) : ViewModel() {

    private val _products = mutableStateOf<List<Products>>(emptyList())
    val products: State<List<Products>> get() = _products

    // Lista de setores únicos
    val sector: StateFlow<List<String>> =
        dao.getSetor()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /*Produtos com validades (relacionamento JOIN)
    val produtosComValidades: StateFlow<List<ValidityAndFabrication>> =
        validadeDao.getAll()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    */

    val produtosComValidades: Flow<List<ProductWithValidities>> = dao.getProdutosComValidades()

    // ==========================
    // 🔹 Produtos
    // ==========================
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

    suspend fun findByName(name: String): Boolean {
        return try {
            val exists = dao.findByName(name)
            Log.d("ProductViewModel", "Produto com nome '$name' existe? $exists")
            exists
        } catch (e: Exception) {
            Log.e("ProductViewModel", "Erro ao verificar produto: ${e.message}")
            false
        }
    }

    fun insertProduct(product: Products) {
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

    // ==========================
    // 🔹 Validades
    // ==========================






    fun adicionarValidade(validade: ValidityAndFabrication) {
        viewModelScope.launch {
            try {
                validadeDao.inserirValidade(validade)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao adicionar validade: ${e.message}")
            }
        }
    }

    fun atualizarValidade(validade: ValidityAndFabrication) {
        viewModelScope.launch {
            try {
                validadeDao.atualizarValidade(validade)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao atualizar validade: ${e.message}")
            }
        }
    }

    fun excluirValidade(validade : ValidityAndFabrication) {
        viewModelScope.launch {
            try {
                validadeDao.excluirValidade(validade)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao excluir validade: ${e.message}")
            }
        }
    }

    fun listarValidadesDoProduto(produtoId: Int, onResult: (List<ValidityAndFabrication>) -> Unit) {
        viewModelScope.launch {
            try {
                val lista = validadeDao.listarValidadesDoProduto(produtoId)
                onResult(lista)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao listar validades: ${e.message}")
                onResult(emptyList())
            }
        }
    }
    fun carregarProdutos(supplierId: Long) {
        viewModelScope.launch {
            val products = dao.getProductsBySupplier(supplierId)
        }
    }

}
