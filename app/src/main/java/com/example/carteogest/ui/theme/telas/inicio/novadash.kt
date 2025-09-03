package com.example.carteogest.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/* ---------------- MODELOS ---------------- */

data class SideMenuItem(
    val name: String,
    val action: () -> Unit
)

/* ---------------- DASHBOARD ACTIVITY ----------------

class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Simulação de permissao e nav
            val userPermissao = "ADMIN" // pode vir do UserViewModel
            val viewModel = BluetoothViewModel()
            DashboardFromSideMenu(
                viewModel = viewModel,
                userPermissao = userPermissao,
                onDestinationClicked = { route ->
                    // Aqui você faria navController.navigate(route)
                    println("Navegando para $route")
                }
            )
        }
    }
}
*/

/* ---------------- FUNÇÕES AUXILIARES ---------------- */

// Retorna todos os itens do menu lateral, incluindo submenus e permissões
fun getAllMenuItems(
    onDestinationClicked: (String) -> Unit,
    viewModel: BluetoothViewModel,
    permissao: String?
): List<SideMenuItem> {
    val list = mutableListOf<SideMenuItem>()

    list.add(SideMenuItem("Inicio") { onDestinationClicked("dash") })

    // Controle de Estoque
    list.add(SideMenuItem("Produtos") { onDestinationClicked("dash1") })
    list.add(SideMenuItem("Fornecedores") { onDestinationClicked("Fornecedores") })
    list.add(SideMenuItem("Ajuste de Estoque") { onDestinationClicked("StockAdjustmentScreen") })
    list.add(SideMenuItem("Validades") { onDestinationClicked("TelaValidades") })
    list.add(SideMenuItem("Etiquetas") { onDestinationClicked("ImpressaoAgrupadaScreen") })
    list.add(SideMenuItem("Relatorios") { onDestinationClicked("RelatoriosEstoqueScreen") })

    // Configurações
    list.add(SideMenuItem("Impressoras") {
        viewModel.requestPermissions(
            onDenied = { viewModel.onToastMessage?.invoke("Permissões negadas.") },
            onGranted = {
                viewModel.enableBluetooth()
                onDestinationClicked("ConectPrinters")
            }
        )
    })

    if (permissao == "ADMIN") {
        list.add(SideMenuItem("Import") { onDestinationClicked("DatabaseImport") })
        list.add(SideMenuItem("Export") { onDestinationClicked("DatabaseExport") })
        list.add(SideMenuItem("Usuarios") { onDestinationClicked("UserListScreen") })
    }

    return list
}

/* ---------------- DASHBOARD COMPOSE ---------------- */

@Composable
fun DashboardFromSideMenu(
    viewModel: BluetoothViewModel,
    userPermissao: String?,
    onDestinationClicked: (String) -> Unit
) {
    var dashboardBlocks by remember { mutableStateOf(listOf<SideMenuItem>()) }

    val sideMenuItems = getAllMenuItems(onDestinationClicked, viewModel, userPermissao)

    Column(modifier = Modifier.fillMaxSize()) {
        // Topo com data e hora
        TimeHeader()
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botões para adicionar itens do menu lateral
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(
                        "Adicionar opção do menu lateral",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    sideMenuItems.forEach { item ->
                        Button(
                            onClick = {
                                if (dashboardBlocks.none { it.name == item.name }) {
                                    dashboardBlocks = dashboardBlocks + item
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(item.name)
                        }
                    }
                }
            }

            // Blocos do dashboard
            items(dashboardBlocks) { block ->
                MenuBlock(menu = block)
            }
        }
    }
}

/* ---------------- COMPONENTES ---------------- */

@Composable
fun TimeHeader() {
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getCurrentTime()
            delay(1000L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF004AAD))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = currentTime,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

@Composable
fun MenuBlock(menu: SideMenuItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(80.dp)
            .padding(vertical = 8.dp)
            .background(Color(0xFF0077CC), RoundedCornerShape(12.dp))
            .clickable { menu.action() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = menu.name, color = Color.White, fontSize = 18.sp)
    }
}
