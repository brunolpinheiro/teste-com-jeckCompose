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
import com.example.carteogest.login.AuthState
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
import com.example.carteogest.ui.telas.ControleEstoque.SupplierRegistrationScreen
import com.example.carteogest.ui.telas.ControleEstoque.TelaValidades
import com.example.carteogest.ui.telas.cadastro.UserRegistrationScreen
import com.example.carteogest.login.UserViewModel
import com.example.carteogest.ui.telas.ControleEstoque.model.fornecedoresViewModel
import com.example.carteogest.ui.theme.telas.usuarios.UserListScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.carteogest.datadb.AppDatabase
import com.example.carteogest.login.UserPrefs
import com.example.carteogest.login.UserRepository
import com.example.carteogest.ui.theme.telas.cadastro.modal.UserViewModelFactory
import kotlinx.coroutines.launch


@Composable
fun MainApp() {

    val context = LocalContext.current.applicationContext

    // Cria UserViewModel

    val db = AppDatabase.getDatabase(context)
    val userPrefs = UserPrefs(context)
    val userRepo = UserRepository(db.userDao(), userPrefs)

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepo)
    )

    // Outros ViewModels
    val bluetoothViewModel: BluetoothViewModel = viewModel(
        factory = BluetoothViewModelFactory(context as android.app.Application)
    )
    val produtoViewModel: ProdutoViewModel = viewModel()
    val fornecedoresViewModel: fornecedoresViewModel = viewModel()

    // Inicializa dados
    LaunchedEffect(Unit) {
        produtoViewModel.carregarProdutos()
        fornecedoresViewModel.carregarfornecedores()
    }

    // Observa autenticação
    val authState by userViewModel.authState.collectAsState()
    val usuarioLogado by userViewModel.usuarioLogado.collectAsState()

    // NavHost / Drawer
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    when (authState) {

        AuthState.UNAUTHENTICATED -> LoginScreen(
            viewModel = userViewModel,
            onLoginSuccess = {
                navController.navigate("dash") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )

        AuthState.AUTHENTICATED -> AppDrawer(
            drawerState = drawerState,
            navController = navController,
            viewModel = bluetoothViewModel,
            userViewModel = userViewModel,
            onDestinationClicked = { route ->
                scope.launch { drawerState.close() }
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },

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
                                    produtos = emptyList(),
                                    fornecedores = emptyList(),
                                    onVoltar = { },
                                    onExportarPDF = { _, _ -> },
                                    onExportarXLS = { _, _ -> },
                                    openDrawer = { scope.launch { drawerState.open() } }
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
                            composable("TelaValidades") {
                                TelaValidades(
                                    openDrawer = { scope.launch { drawerState.open() } }
                                )
                            }
                            composable("UserListScreen") {
                                UserListScreen(
                                    viewModel = userViewModel,
                                    onDestinationClicked = { route ->
                                        scope.launch { drawerState.close() }
                                        navController.navigate(route)
                                    },
                                    openDrawer = { scope.launch { drawerState.open() } }

                                )

                            }

                            composable("UserRegistrationScreen") {
                                UserRegistrationScreen(
                                    viewModel = userViewModel,
                                    openDrawer = { scope.launch { drawerState.open() } }


                                )
                            }

                        }
                    }
                }
            }



