package com.example.test_2.data_db.validity



import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import com.example.test_2.data_db.AppDatabase
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate


class ValidityViewModel(private val database: AppDatabase) : ViewModel() {
    private val _validity = mutableStateOf<List<ValidityAndFabrication>>(emptyList())
    val validity: State<List<ValidityAndFabrication>> get() = _validity

    fun loadValidity() {
        viewModelScope.launch {
            try {
                database.validityDao().getAll().collectLatest { validitys ->
                    _validity.value = validitys ?: emptyList()
                }

            } catch (e: Exception) {
                Log.e("ValidityViewModal", "Erro ao carregar as validades : ${e.message}")
                _validity.value = emptyList()
            }
        }
    }







    fun addValidityToProduct(productId: Int, newDateFabrication: String,newDateValidity: String) {
        viewModelScope.launch {
            try {
                // Assumindo que ValidityDao tem getByProductName(name: String): Flow<ValidityAndFabrication?>

                    database.validityDao().addValidityToProduct(
                        ValidityAndFabrication(
                            productId = productId,
                            fabrication = newDateFabrication,
                            validity = newDateValidity
                        )
                    )

                loadValidity() // Recarrega para atualizar a UI
            } catch (e: Exception) {
                Log.e("ValidityViewModel", "Falha ao adicionar validade: ${e.message}")
            }
        }
    }

    fun updateValidity( newDateFabrication: String,newDateValidity: String) {
        viewModelScope.launch {
            try {
                database.validityDao().updateValidity(ValidityAndFabrication(
                    productId = 0,
                    fabrication = newDateFabrication,
                    validity = newDateValidity
                ))

            } catch (e: Exception) {
                Log.e("ProductViewModel", "Erro ao atualizar produto: ${e.message}")
            }
        }
    }

    fun deleteValidity(validity: ValidityAndFabrication) {
        viewModelScope.launch {
            try {
                database.validityDao().deleteValidity(validity)
                loadValidity()
            } catch (e: Exception) {
                Log.e("ValidityViewModel", "Falha ao deletar a validade: ${e.message}")
            }
        }
    }



}