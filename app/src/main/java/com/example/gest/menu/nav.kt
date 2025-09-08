package com.example.gest.menu


import android.app.Application
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gest.bluetooth.model.BluetoothViewModel
import com.example.gest.bluetooth.utils.BluetoothViewModelFactory
import com.example.gest.datadb.data_db.AppDatabase
import com.example.gest.datadb.data_db.login.AuthState
import com.example.gest.datadb.data_db.login.UserPrefs
import com.example.gest.datadb.data_db.login.UserRepository
import com.example.gest.datadb.data_db.login.UserViewModel
import com.example.gest.datadb.data_db.products.ProductViewModel
import com.example.gest.datadb.data_db.supplier.SupplierViewModel
import com.example.gest.factory.ProductViewModelFactory
import com.example.gest.factory.supplierViewModelFactory
import com.example.gest.iu.telas.ControleEstoque.SupplierRegistration
import com.example.gest.menu.AppDrawer
import com.example.gest.ui.telas.ControleEstoque.DashboardScreenum
import com.example.gest.factory.UsersViewModelFactory
import com.example.gest.ui.telas.ControleEstoque.ImpressaoAgrupadaScreen
import com.example.gest.ui.telas.ControleEstoque.RelatoriosEstoqueScreen
import com.example.gest.ui.telas.ControleEstoque.StockAdjustmentScreen
import com.example.gest.ui.telas.ControleEstoque.TelaValidades
import com.example.gest.ui.telas.UserPanelScreen
import com.example.gest.ui.telas.cadastro.UserRegistrationScreen
import com.example.gest.ui.telas.config.ConectPrinters
import com.example.gest.ui.telas.inicio.DashboardScreen
import com.example.gest.ui.telas.ControleEstoque.ProdutoCadastroScreen
import com.example.gest.ui.telas.inicio.Fornecedores
import com.example.gest.ui.telas.login.LoginScreen
import com.example.gest.ui.telas.roomBackup.DataBaseExport
import com.example.gest.ui.telas.roomBackup.DataBaseImport
import com.example.gest.ui.theme.telas.usuarios.UserListScreen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MainApp() {

    val context = LocalContext.current.applicationContext

    // Cria UserViewModel

    val db = AppDatabase.getDatabase(context, CoroutineScope(Dispatchers.IO))
    val userPrefs = UserPrefs(context)
    val userRepo = UserRepository(db.userDao(), userPrefs)

    val userViewModel: UserViewModel = viewModel(
        factory = UsersViewModelFactory(userRepo)
    )

    // Outros ViewModels
    val bluetoothViewModel: BluetoothViewModel = viewModel(
        factory = BluetoothViewModelFactory(context as Application)
    )

    val produtoViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(db.productsDao(), db.validityDao())
    )
    val supllierViewModel: SupplierViewModel = viewModel(
        factory = supplierViewModelFactory(db.supplierDao())
    )


    // Inicializa dados
    LaunchedEffect(Unit) {
        produtoViewModel.getAll()
        supllierViewModel.getAll()
        userViewModel.loadUsers()

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
            openDrawer = { scope.launch { drawerState.open() } },
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
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel,
                        navController = navController
                    )


                }



                composable("dash1") {

                    DashboardScreenum(
                        onAjustarEstoque = { },
                        onInserirValidade = { },
                        openDrawer = { scope.launch { drawerState.open() } },
                        navController = navController,
                        userViewModel = userViewModel
                    )

                }

                composable("Fornecedores") {
                    Fornecedores(
                        openDrawer = { scope.launch { drawerState.open() } },
                        navController = navController,
                        userViewModel = userViewModel
                    )
                }

                composable("ConectPrinters") {
                    ConectPrinters(
                        navController = navController,
                        viewModel = bluetoothViewModel,
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel
                    )
                }
                composable("StockAdjustmentScreen") {
                    StockAdjustmentScreen(
                        productViewModel = produtoViewModel,
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel,
                        navController = navController
                    )
                }
                composable("RelatoriosEstoqueScreen") {
                    RelatoriosEstoqueScreen(
                        produtos = emptyList(),
                        fornecedores = emptyList(),
                        onVoltar = { },
                        onExportarPDF = { _, _ -> },
                        onExportarXLS = { _, _ -> },
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel,
                        navController = navController

                    )

                }

                composable(
                    "ProdutoCadastroScreen/{produtoId}",
                    arguments = listOf(navArgument("produtoId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val produtoId = backStackEntry.arguments?.getInt("produtoId") ?: -1
                    ProdutoCadastroScreen(
                        openDrawer = { scope.launch { drawerState.open() } },
                        productViewModel = produtoViewModel,
                        produtoId = produtoId,
                        navController = navController,
                        userViewModel = userViewModel
                    )
                }
                /* composable("Printers") {
                     Printers(
                         navController = navController,
                         viewModel = bluetoothViewModel,
                         productViewModel = produtoViewModel,
                         userViewModel = userViewModel,
                         openDrawer = { scope.launch { drawerState.open() } }
                     )
                 }*/

                composable(
                    "SupplierRegistration/{fornecedoresId}",
                    arguments = listOf(navArgument("fornecedoresId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val fornecedoresId = backStackEntry.arguments?.getInt("fornecedoresId") ?: -1
                    SupplierRegistration(
                        supplierViewModel = supllierViewModel,
                        openDrawer = { scope.launch { drawerState.open() } },
                        navController = navController,
                        fornecedoresid = fornecedoresId,
                        userViewModel = userViewModel
                    )
                }
                composable("StockAdjustmentScreen") {
                    StockAdjustmentScreen(
                        productViewModel = produtoViewModel,
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel,
                        navController = navController
                    )
                }
                composable("TelaValidades") {
                    TelaValidades(
                        viewModel = produtoViewModel,
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel,
                        navController = navController
                    )
                }


                composable(
                    "UserRegistrationScreen/{usuarioId}",
                    arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: -1
                    UserRegistrationScreen(
                        viewModel = userViewModel,
                        openDrawer = { scope.launch { drawerState.open() } },
                        usuarioId = usuarioId,
                        navController = navController,
                        userViewModel = userViewModel

                    )
                }
                composable("DatabaseExport") {
                    DataBaseExport(
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel,
                        navController = navController)

                }

                composable("DatabaseImport"){
                    DataBaseImport (
                        openDrawer = { scope.launch { drawerState.open() } },
                        userViewModel = userViewModel,
                        navController = navController                    )
                }
                composable("UserListScreen"){
                    UserListScreen (
                        viewModel = userViewModel,
                        onDestinationClicked = {},
                        openDrawer = { scope.launch { drawerState.open() }},
                        navController = navController,
                        userViewModel = userViewModel

                    )
                }
                composable("ImpressaoAgrupadaScreen"){
                    ImpressaoAgrupadaScreen (
                        navController = navController,
                        viewModel = bluetoothViewModel,
                        productViewModel = produtoViewModel,
                        userViewModel = userViewModel,
                        openDrawer = { scope.launch { drawerState.open() }}
                    )
                }
                composable("UserPanelScreen"){
                    UserPanelScreen (
                        userViewModel = userViewModel,
                        onPasswordChanged = {},
                        onLogout = {}
                    )
                }


            }
        }
    }
}