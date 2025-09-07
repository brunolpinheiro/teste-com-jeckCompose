// RecebimentoViewModel.kt
package com.example.gest.ui.telas.ControleEstoque.model

import androidx.lifecycle.ViewModel
import com.example.gest.ui.telas.ControleEstoque.ProdutoRecebido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecebimentoViewModel : ViewModel() {
    private val _produtos = MutableStateFlow<List<ProdutoRecebido>>(emptyList())
    val produtos = _produtos.asStateFlow()

    fun adicionarProduto(produto: ProdutoRecebido) {
        _produtos.value = _produtos.value + produto
    }
}
