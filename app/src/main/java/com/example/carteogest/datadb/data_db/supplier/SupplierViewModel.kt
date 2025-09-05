package com.example.carteogest.datadb.data_db.supplier




import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.Products


class SupplierViewModel(private val dao: SupplierDao) : ViewModel() {
    private val _supplier = mutableStateOf<List<Supplier>>(emptyList())
    val supplier: State<List<Supplier>> get() = _supplier

    fun loadSupplier(cnpj: String) {
        viewModelScope.launch {
            try {
                dao.getSupplierByCnpj(cnpj).collectLatest { supplier ->
                    _supplier.value = supplier ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao carregar fornecedores: ${e.message}")
                _supplier.value = emptyList()
            }
        }
    }

    suspend  fun findByName(name: String): Boolean {
        return try {
            val exists = dao.findByName(name)
            Log.d("SupplierModelView", "Verificação do nome do fornecedor  '$name': $exists")
            exists

        } catch (e: Exception) {
            Log.e("SupplierModelView", "Não foi possível verificar o nome do produto: ${e.message}")
            false
        }
    }


    fun insertSupplier(
        uid: Int = 0, // Valor padrão para suportar auto-incremento
        name: String,
        cnpj: String,
        adress: String,
        phone: String,
        email: String

    ) {
        viewModelScope.launch {
            try {
                dao.insertSuppllier(
                    Supplier(
                        uid = uid,
                        name = name,
                       cnpj  = cnpj,
                        adress = adress,
                        phone = phone,
                        email = email

                    )
                )
                loadSupplier(cnpj)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao inserir fornecedor: ${e.message}")
            }
        }
    }

    fun updateSupplier(supplier: Supplier) {
        viewModelScope.launch {
            try {
                dao.updateSupplier(supplier)
                loadSupplier(supplier.cnpj)
            } catch (e: Exception) {
                Log.e("SupplierModelView", "Falha ao atulizar o Fornecedor: ${e.message}")
            }
        }
    }

    fun deleteSupplier(supplier: Supplier) {
        viewModelScope.launch {
            try {
               dao.deleteSupplier(supplier)
                loadSupplier(supplier.cnpj)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Falha ao deletar Fornecedor: ${e.message}")
            }
        }
    }

    fun getAll() {
        viewModelScope.launch {
            try {
               dao.getAll().collectLatest { supplier ->
                    _supplier.value = supplier ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Falha ao carregar os fonecedores: ${e.message}")
                _supplier.value = emptyList()
            }
        }
    }
    suspend fun getById(id: Int): Supplier? {
        return dao.getById(id)
    }

}