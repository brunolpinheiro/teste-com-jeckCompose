/*package com.example.carteogest.ui.telas.ControleEstoque.model

import androidx.lifecycle.ViewModel
import com.example.carteogest.ui.telas.inicio.fornecedores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class fornecedoresViewModel : ViewModel() {
    private val _fornecedores= MutableStateFlow<List<fornecedores>>(emptyList())
    val fornecedores: StateFlow<List<fornecedores>> = _fornecedores

    fun carregarfornecedores() {
        _fornecedores.value = listOf(
            fornecedores("Distibuidora tal "),
            fornecedores("Distibuidora tal 1"),
            fornecedores("Distibuidora tal 2"),
            fornecedores("Distibuidora tal 3"),
            fornecedores("Distibuidora tal 4"),
            fornecedores("Distibuidora tal 5")
        )
    }
}

*/