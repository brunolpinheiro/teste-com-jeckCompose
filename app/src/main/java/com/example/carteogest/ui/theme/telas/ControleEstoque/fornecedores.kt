package com.example.carteogest.ui.telas.inicio
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpOffset
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.supplier.SupplierViewModel
import com.example.carteogest.factory.supplierViewModelFactory


data class fornecedores(
    val nome: String)

@Composable
fun Fornecedores(
    openDrawer: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Obtem o DB e o DAO
    val db = AppDatabase.getDatabase(context, scope)
    val supllierViewModel: SupplierViewModel = viewModel(
        factory = supplierViewModelFactory(db.supplierDao()))

    val fornecedores by supllierViewModel.supplier

    var query by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val fornecedoresFiltrados = fornecedores.filter { fornecedor ->
        fornecedor.name.contains(query, ignoreCase = true)
    }



    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var expandedItem by remember { mutableStateOf<String?>(null) }
    var isExpanded by remember { mutableStateOf(false) }
    var fabMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        supllierViewModel.getAll()
    }
    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                openDrawer = openDrawer

            )
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { fabMenuExpanded = !fabMenuExpanded },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Menu", tint = Color.White)

                }

                DropdownMenu(
                    expanded = fabMenuExpanded,
                    onDismissRequest = { fabMenuExpanded = false },
                    offset = DpOffset(x = 0.dp, y = (-56).dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Cadastrar Fornecedor",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)

                            )
                        },
                        onClick = { navController.navigate("SupplierRegistration/-1") },
                        modifier = Modifier
                            .background(Color(0xFF004AAD), shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

            }
        }
    ) {  paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(paddingValues))
        {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Pesquisar Fornecedores") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar",tint = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize() .padding(vertical = 2.dp)

            ) {
                items(fornecedoresFiltrados) { fornecedores ->
                    val isExpanded = expandedItem == fornecedores.name

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(8.dp),
                                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // sombra azul
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                            .clickable { expandedItem = if (isExpanded) null else fornecedores.name }
                            .padding(18 .dp)
                    ) {
                        Row(

                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(fornecedores.name, style = MaterialTheme.typography.titleMedium)
                            Divider(color = Color.Gray, modifier = Modifier.fillMaxHeight().width(1.dp))
                            Text(fornecedores.cnpj, style = MaterialTheme.typography.titleMedium)

                        }
                        AnimatedVisibility(visible = isExpanded) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Button(
                                    onClick ={ navController.navigate("SupplierRegistration/${fornecedores.uid}") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Editar Fornecedor", color = Color.White)
                                }
                            }

                        }
                    }
                }

            }

        }
    }


@Composable
fun FilterChipfor(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary else Color.Gray
        ),
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
}

