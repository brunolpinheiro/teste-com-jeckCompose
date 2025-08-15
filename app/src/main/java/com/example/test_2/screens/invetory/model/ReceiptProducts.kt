package com.example.test_2.screens.invetory.model





import androidx.lifecycle.ViewModel
import com.example.test_2.screens.invetory.ReceiptProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReceiptProducts : ViewModel() {
    private val _produtos = MutableStateFlow<List<ReceiptProduct>>(emptyList())
    val produtos = _produtos.asStateFlow()

    fun adicionarProduto(produto: ReceiptProduct) {
        _produtos.value = _produtos.value + produto
    }
}