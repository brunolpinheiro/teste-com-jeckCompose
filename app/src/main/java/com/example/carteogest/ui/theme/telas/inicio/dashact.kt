/*
package com.example.carteogest.ui.telas.inicio
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carteogest.menu.ProdutoViewModel

class DashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val produtoViewModel: ProdutoViewModel = viewModel()

            // Carrega produtos fictícios
            LaunchedEffect(Unit) {
                produtoViewModel.carregarProdutos()
            }

            DashboardScreenum(
                viewModel = produtoViewModel,
                onAjustarEstoque = { produto ->
                    println("Ajustar estoque de ${produto.nome}")
                },
                onInserirValidade = { produto ->
                    println("Inserir validade para ${produto.nome}")
                },
                openDrawer = {}
            )
        }
    }
}
*/