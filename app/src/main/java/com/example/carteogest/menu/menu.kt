package com.example.carteogest.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.CarteoGest.R
import com.example.carteogest.login.UserViewModel

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    viewModel: BluetoothViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
    onDestinationClicked: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val usuario = userViewModel.usuarioLogado.collectAsState().value

    val currentRoute = navController.currentBackStackEntry?.destination?.route
    var subMenu1 by remember { mutableStateOf(false) }
    var subMenu2 by remember { mutableStateOf(false) }
    var subMenu3 by remember { mutableStateOf(false) }
    var subMenu4 by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier .background(Color.Green),
        scrimColor = Color.Transparent,


        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
                modifier = Modifier
                    //.fillMaxHeight()
                    .width(220.dp)
                    .background(Color.Transparent),

            // Cor de fundo do menu
            )  {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .width(220.dp)
                        //.padding(26.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()

                            .padding(vertical = 0.dp, horizontal = 0.dp)
                            .background(Color(0xFF008C4A),shape = RoundedCornerShape(0.dp)),
                            horizontalArrangement = Arrangement.Start

                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp) // altura da row
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.image),
                                contentDescription = "Fundo da Row",
                                contentScale = ContentScale.Crop, // preenche a área
                                modifier = Modifier.matchParentSize() // ocupa todo o Box
                            )

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .background(Color.Transparent),
                            horizontalArrangement = Arrangement.Start) {
                            Icon(
                                painter = painterResource(id = R.drawable.icone), // substitua pela logo real
                                contentDescription = "Logo do app",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(82.dp)
                                    //.padding(all = 12.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 8.dp)
                                    .background(Color.Transparent),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "userName",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Bem-vindo(a)!",
                                    color = Color.White.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                    }

                    }

                    Divider(color = Color.White.copy(alpha = 0.3f))
@Composable
                    fun drawerItem(

                        labelText: String,
                        selected: Boolean,
                        onClick: () -> Unit,
                        modifier: Modifier = Modifier
                    ) {
                        NavigationDrawerItem(
                            label = { Text(labelText) },
                            selected = selected,

                            onClick = onClick,
                            modifier = modifier
                                .padding(2.dp)
                                .padding(start = 8.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(0.dp)),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary ,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = Color.White,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.White
                            )
                        )
                    }

                    drawerItem(
                        labelText = "Inicio",
                        selected = currentRoute == "dash",
                        onClick = { onDestinationClicked("dash") }
                    )

                    Divider(color = Color.White.copy(alpha = 0.3f))
                    drawerItem(
                        labelText = "Controle de Estoque",
                        selected = false,
                        onClick = { subMenu1 = !subMenu1 }
                    )
                    Divider(color = Color.White.copy(alpha = 0.3f))
                    if (subMenu1) {
                        drawerItem(

                            labelText = "Produtos",
                            selected = currentRoute == "dash1",
                            onClick = { onDestinationClicked("dash1") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Recebimento",
                            selected = currentRoute == "RecebimentoScreen",
                            onClick = { onDestinationClicked("RecebimentoScreen") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Fornecedores",
                            selected = currentRoute == "Fornecedores",
                            onClick = { onDestinationClicked("Fornecedores") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Ajuste de Estoque",
                            selected = currentRoute == "StockAdjustmentScreen",
                            onClick = { onDestinationClicked("StockAdjustmentScreen") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Validades",
                            selected = currentRoute == "TelaValidades",
                            onClick = { onDestinationClicked("TelaValidades") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Etiquetas",
                            selected = currentRoute == "Printers",
                            onClick = { onDestinationClicked("Printers") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Relatorios",
                            selected = currentRoute == "RelatoriosEstoqueScreen",
                            onClick = { onDestinationClicked("RelatoriosEstoqueScreen") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }
                    Divider(color = Color.White.copy(alpha = 0.3f))
                    drawerItem(

                        labelText = "Cadastros",
                        selected = false,
                        onClick = { subMenu2 = !subMenu2 }
                    )
                    Divider(color = Color.White.copy(alpha = 0.3f))
                    if (subMenu2) {
                        drawerItem(

                            labelText = "Produtos",
                            selected = currentRoute == "ProdutoCadastroScreen/-1",
                            onClick = { onDestinationClicked("ProdutoCadastroScreen/-1") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Fornecedores",
                            selected = currentRoute == "SupplierRegistration/-1",
                            onClick = { onDestinationClicked("SupplierRegistration/-1") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Usuarios",
                            selected = currentRoute == "UserRegistrationScreen/-1",
                            onClick = { onDestinationClicked("UserRegistrationScreen/-1") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }
                    Divider(color = Color.White.copy(alpha = 0.3f))
                    drawerItem(

                        labelText = "Configurações",
                        selected = false,
                        onClick = { subMenu3 = !subMenu3 }
                    )
                    Divider(color = Color.White.copy(alpha = 0.3f))

                    if (subMenu3) {
                        drawerItem(

                            labelText = "Impressoras",
                            selected = currentRoute == "ConectPrinters",
                            onClick = {
                                viewModel.requestPermissions(
                                    onDenied = {
                                        viewModel.onToastMessage?.invoke("Permissões negadas.")
                                    },
                                    onGranted = {
                                        viewModel.enableBluetooth()
                                        onDestinationClicked("ConectPrinters")
                                    }
                                )

                            },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "mandar arquivo db",
                            selected = currentRoute == "DatabaseImport",
                            onClick = { onDestinationClicked("DatabaseImport") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Export",
                            selected = currentRoute == "DatabaseExport",
                            onClick = { onDestinationClicked("DatabaseExport") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Divider(color = Color.White.copy(alpha = 0.3f))
                    }
                    drawerItem(

                        labelText = "Usuários e Permissões",
                        selected = false,
                        onClick = { subMenu4 = !subMenu4 }
                    )
                    Divider(color = Color.White.copy(alpha = 0.3f))
                    if (subMenu4) {
                            drawerItem(

                                labelText = "Usuarios",
                                selected = currentRoute == "UserListScreen",
                                onClick = { onDestinationClicked("UserListScreen") },
                                modifier = Modifier.padding(
                                    start = 32.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                            )

                        Divider(color = Color.White.copy(alpha = 0.3f))
                        drawerItem(

                            labelText = "Editas Usuarios",
                            selected = currentRoute == "UserEditScreen",
                            onClick = { onDestinationClicked("UserEditScreen") },
                            modifier = Modifier.padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }

                }
            }
        },
    ) {
        content()
    }
}
