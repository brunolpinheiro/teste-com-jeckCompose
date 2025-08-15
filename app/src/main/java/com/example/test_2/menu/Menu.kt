package com.example.test_2.menu


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test_2.bluetooth.model.BluetoothViewModel

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    viewModel: BluetoothViewModel,
    navController: NavController,
    onDestinationClicked: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    var subMenu1 by remember { mutableStateOf(false) }
    var subMenu2 by remember { mutableStateOf(false) }
    var subMenu3 by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
                ) {
                    @Composable
                    fun drawerItem(
                        icon: @Composable () -> Unit,
                        labelText: String,
                        selected: Boolean,
                        onClick: () -> Unit,
                        modifier: Modifier = Modifier
                    ) {
                        NavigationDrawerItem(
                            icon = icon,
                            label = { Text(labelText) },
                            selected = selected,
                            onClick = onClick,
                            modifier = modifier
                                .padding(8.dp)
                                .background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color.Transparent,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = Color.Blue,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Blue
                            )
                        )
                    }

                    drawerItem(
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        labelText = "Home",
                        selected = currentRoute == "dash",
                        onClick = { onDestinationClicked("dash") }
                    )

                    drawerItem(
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        labelText = "Controle de Estoque",
                        selected = false,
                        onClick = { subMenu1 = !subMenu1 }
                    )

                    if (subMenu1) {
                        drawerItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = null) },
                            labelText = "Recebimento",
                            selected = currentRoute == "RecebimentoScreen",
                            onClick = { onDestinationClicked("RecebimentoScreen") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        drawerItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = null) },
                            labelText = "Contagem",
                            selected = currentRoute == "ShowProducts",
                            onClick = { onDestinationClicked("ShowProducts") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        drawerItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = null) },
                            labelText = "Validades",
                            selected = currentRoute == "ValidadesScreen",
                            onClick = { onDestinationClicked("ValidadesScreen") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        drawerItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = null) },
                            labelText = "Etiquetas",
                            selected = currentRoute == "Printers",
                            onClick = { onDestinationClicked("Printers") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        drawerItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = null) },
                            labelText = "Relatorios",
                            selected = currentRoute == "RelatoriosEstoqueScreen",
                            onClick = { onDestinationClicked("RelatoriosEstoqueScreen") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }

                    drawerItem(
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        labelText = "Cadastros",
                        selected = false,
                        onClick = { subMenu2 = !subMenu2 }
                    )

                    if (subMenu2) {
                        drawerItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = null) },
                            labelText = "Produtos",
                            selected = currentRoute == "RegistrationProducts",
                            onClick = { onDestinationClicked("RegistrationProducts") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }

                    drawerItem(
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        labelText = "Configurações",
                        selected = false,
                        onClick = { subMenu3 = !subMenu3 }
                    )

                    if (subMenu3) {
                        drawerItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = null) },
                            labelText = "Impressão",
                            selected = currentRoute == "ConectPrinters",
                            onClick = {
                                viewModel.requestPermissions(
                                    onDenied = {
                                        viewModel.onToastMessage?.invoke("Permissões negadas.")
                                    },
                                    onGranted = {
                                        viewModel.enableBluetooth()
                                        navController.navigate("connect_printers")
                                    }
                                )
                                onDestinationClicked("connect_printers")
                            },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }
                }
            }
        }
    ) {
        content()
    }
}