package com.example.carteogest.menu
import androidx.lifecycle.ViewModel
import com.example.carteogest.ui.telas.inicio.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ProdutoViewModel : ViewModel() {
    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    fun carregarProdutos() {
        _produtos.value = listOf(
            Produto("Arroz", 10, "2025/12/01","graus"),
            Produto("Feijão", 5, "2025/10/15","graus"),
            Produto("Macarrão", 8, "2025/09/20","graus"),
            Produto("agua", 10, "2025/12/01","bebidas"),
            Produto("coca", 5, "2025/10/15","bebidas"),
            Produto("pepsi", 8, "2025/09/20","bebidas")
        )
    }
}

