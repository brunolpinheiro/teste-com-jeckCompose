package com.example.carteogest.menu


import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.carteogest.bluetooth.utils.BluetoothViewModelFactory
import com.example.carteogest.menu.AppDrawer
import com.example.carteogest.ui.telas.login.AuthViewModel
import com.example.carteogest.ui.telas.login.LoginScreen
import com.example.carteogest.ui.telas.login.SplashScreen
import com.example.carteogest.ui.telas.ControleEstoque.RecebimentoScreen
import com.example.carteogest.ui.telas.config.ConectPrinters
import com.example.carteogest.ui.telas.ControleEstoque.StockAdjustmentScreen
import com.example.carteogest.ui.telas.ControleEstoque.Printers
import com.example.carteogest.ui.telas.ControleEstoque.RelatoriosEstoqueScreen
import com.example.carteogest.ui.telas.ControleEstoque.ValidadesScreen
import com.example.carteogest.ui.telas.ControleEstoque.ProdutoCadastroScreen
import com.example.carteogest.ui.telas.inicio.DashboardScreen
import com.example.carteogest.ui.telas.inicio.DashboardScreenum
import com.example.carteogest.ui.telas.inicio.Fornecedores
import com.example.carteogest.ui.telas.ControleEstoque.model.fornecedoresViewModel
import com.example.carteogest.ui.telas.ControleEstoque.SupplierRegistrationScreen





import kotlinx.coroutines.launch
import kotlin.String


@Composable
fun MainApp() {
    val context = LocalContext.current.applicationContext
    val authViewModel: AuthViewModel = viewModel()
    val bluetoothViewModel: BluetoothViewModel = viewModel(
        factory = BluetoothViewModelFactory(context as android.app.Application)
    )
    val produtoViewModel: ProdutoViewModel = viewModel()
    LaunchedEffect(Unit) {
        produtoViewModel.carregarProdutos()
    }
    val fornecedoresViewModel: fornecedoresViewModel = viewModel()
    LaunchedEffect(Unit) {
        fornecedoresViewModel.carregarfornecedores()
    }

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val authState by authViewModel.authState

    when (authState) {
        com.example.carteogest.ui.telas.login.AuthState.LOADING -> {
            SplashScreen(authViewModel, navController, bluetoothViewModel)
        }

        com.example.carteogest.ui.telas.login.AuthState.UNAUTHENTICATED -> {
            LoginScreen(authViewModel)
        }

        com.example.carteogest.ui.telas.login.AuthState.AUTHENTICATED -> {
            AppDrawer(
                drawerState = drawerState,
                navController = navController,
                viewModel = bluetoothViewModel,
                onDestinationClicked = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                NavHost(navController = navController, startDestination = "dash") {
                    composable("dash") {
                        DashboardScreen(

                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }

                    composable("dash1") {
                        val produtoViewModel: ProdutoViewModel = viewModel()

                        // Garante que os produtos fictícios sejam carregados
                        LaunchedEffect(Unit) {
                            produtoViewModel.carregarProdutos()
                        }
                        DashboardScreenum(
                            viewModel = viewModel(),
                            onAjustarEstoque = { },
                            onInserirValidade = { },
                            openDrawer = { scope.launch { drawerState.open() } },
                            navController = navController,
                        )

                    }

                    composable("Fornecedores") {
                        Fornecedores(
                            openDrawer = { scope.launch { drawerState.open() } },
                            navController = navController,
                            )
                    }
                    composable("RecebimentoScreen") {
                        RecebimentoScreen(
                            onFinalizar = { },
                            viewModel(),
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                    composable("ConectPrinters") {
                        ConectPrinters(
                            navController = navController,
                            viewModel = bluetoothViewModel,
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                    composable("StockAdjustmentScreen") {
                        StockAdjustmentScreen(
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                    composable("RelatoriosEstoqueScreen") {
                        RelatoriosEstoqueScreen(
                            onVoltar = { },
                            onGerarRelatorio = { },
                            onExportar = { },
                            openDrawer = { scope.launch { drawerState.open() } },
                        )
                    }
                    composable("ValidadesScreen") {
                        ValidadesScreen(
                            navController = navController,
                            produtos = emptyList(),
                            onVoltar = { },
                            onGerarRelatorio = { },
                            openDrawer = { scope.launch { drawerState.open() } },
                        )
                    }
                    composable("ProdutoCadastroScreen") {
                        ProdutoCadastroScreen(
                            categorias = emptyList(),
                            unidadesMedida = emptyList(),
                            statusOptions = emptyList(),
                            onSalvar = { },
                            openDrawer = { scope.launch { drawerState.open() } },
                        )



                    }
                    composable("Printers") {
                        Printers(
                            navController = navController,
                            viewModel = bluetoothViewModel,
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                    composable("SupplierRegistrationScreen") {
                        SupplierRegistrationScreen(
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                    composable("StockAdjustmentScreen") {
                        StockAdjustmentScreen(
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                }
            }
        }
    }
}


