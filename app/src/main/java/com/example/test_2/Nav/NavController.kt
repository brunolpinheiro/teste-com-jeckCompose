package com.example.test_2.Nav



import DataBaseImport
import LoginScreen
import ResgistrationProducts
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test_2.bluetooth.model.BluetoothViewModel
import com.example.test_2.bluetooth.utils.BluetoothViewModelFactory
import com.example.test_2.screens.login.AuthViewModel
import com.example.test_2.screens.login.SplashScreen
import com.example.test_2.screens.invetory.Receipt
import com.example.test_2.config.ConectPrinters
import com.example.test_2.menu.AppDrawer
import com.example.test_2.screens.roomBackup.DataBaseExport
import com.example.test_2.screens.invetory.ShowProducts
import com.example.test_2.screens.invetory.Printers
import com.example.test_2.screens.invetory.Report
import com.example.test_2.screens.invetory.Validity
import com.example.test_2.screens.home.DashboardScreen
import com.example.test_2.screens.inventory.ShowSupplier
import kotlinx.coroutines.launch
import com.example.test_2.screens.resgister.SupplierRegistration


@Composable
fun MainApp() {
    val context = LocalContext.current.applicationContext
    val authViewModel: AuthViewModel = viewModel()
    val bluetoothViewModel: BluetoothViewModel = viewModel(
        factory = BluetoothViewModelFactory(context as android.app.Application)
    )

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val authState by authViewModel.authState

    when (authState) {
        com.example.test_2.screens.login.AuthState.LOADING -> {
            SplashScreen(authViewModel, navController, bluetoothViewModel)
        }

        com.example.test_2.screens.login.AuthState.UNAUTHENTICATED -> {
            LoginScreen(authViewModel)
        }

        com.example.test_2.screens.login.AuthState.AUTHENTICATED -> {
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
                    composable("RecebimentoScreen") {
                        Receipt(
                            onFinalizar = { /*...*/ },
                            viewModel = viewModel(),
                            openDrawer = { scope.launch { drawerState.open() } },

                            )
                    }
                    composable("ConectPrinters") {
                        ConectPrinters(
                            navController = navController,
                            viewModel = bluetoothViewModel,
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }

                    composable("ResgistrationProducts") {
                        ResgistrationProducts(navController = navController,
                         openDrawer = {scope.launch { drawerState.open()
                     }})
                    }
                    composable("RelatoriosEstoqueScreen") {
                        Report(
                            onVoltar = { },
                            onGerarRelatorio = { },
                            onExportar = { },
                            openDrawer = { scope.launch { drawerState.open() } },
                        )
                    }
                    composable("Validity") {
                        Validity(
                            navController = navController,
                            produtos = emptyList(),
                            onVoltar = { },
                            onGerarRelatorio = { },
                            openDrawer = { scope.launch { drawerState.open() } },
                        )
                    }
                    composable("RegistrationProducts") {
                        ResgistrationProducts(navController = navController,
                            openDrawer = {scope.launch { drawerState.open() }})


                    }
                    composable("ShowProducts") {
                        ShowProducts(navController = navController,
                            openDrawer = {scope.launch { drawerState.open() }})


                    }
                    composable("DatabaseExport") {
                        DataBaseExport(
                            openDrawer = {scope.launch { drawerState.open() }})


                    }

                    composable("DatabaseImport"){
                        DataBaseImport (
                            openDrawer = {scope.launch { drawerState.open() }}
                        )
                    }
                    composable("Printers") {
                        Printers(
                            navController = navController,
                            viewModel = bluetoothViewModel,
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }

                    composable ( "ResgistrationSupplier" ){
                        SupplierRegistration(
                            openDrawer = {scope.launch { drawerState.open() }},
                            navController = navController
                        )
                    }

                    composable("ShowSupplier"){
                        ShowSupplier(openDrawer = {scope.launch { drawerState.open() }}, navController = navController)

                    }
                }
            }
        }
    }
}