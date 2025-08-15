package com.example.carteogest.ui.telas.inicio
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpOffset
import androidx.navigation.NavController
import com.example.carteogest.ui.telas.ControleEstoque.model.fornecedoresViewModel


data class fornecedores(
    val nome: String)

@Composable
fun Fornecedores(
    viewModel: fornecedoresViewModel = viewModel(),
    openDrawer: () -> Unit,
    navController: NavController
) {
    val fornecedores by viewModel.fornecedores.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var query by remember { mutableStateOf("") }
    val fornecedoresFiltrados = fornecedores
        .filter { it.nome.contains(query, ignoreCase = true) }


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var expandedItem by remember { mutableStateOf<String?>(null) }
    var isExpanded by remember { mutableStateOf(false) }
    var fabMenuExpanded by remember { mutableStateOf(false) }


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
                    onClick = { fabMenuExpanded = !fabMenuExpanded }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = fabMenuExpanded,
                    onDismissRequest = { fabMenuExpanded = false },
                    offset = DpOffset(x = 0.dp, y = (-56).dp) // aparece acima do FAB
                ) {
                    DropdownMenuItem(
                        text = { Text("Cadastrar Fornecedor") },
                        onClick = { navController.navigate("SupplierRegistrationScreen")// ação: abrir tela de cadastro
                        }
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
            /*
            if (showFilters) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botão para mostrar todas as categorias
                    item {
                        FilterChip(
                            selected = true,
                            onClick = { /*...*/ },
                            label = { Text("Todas") }) // ✅ Composable
                    }

                    // Criar chips para cada categoria única
                    items(fornecedores.map { it.categoria }.distinct()) { categoria ->
                        FilterChip(
                            selected = true,
                            onClick = { /*...*/ },
                            label = { Text(categoria) } // ✅ Composable
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))*/


            LazyColumn(
                modifier = Modifier.fillMaxSize() .padding(vertical = 2.dp)

            ) {
                items(fornecedoresFiltrados) { fornecedores ->
                    val isExpanded = expandedItem == fornecedores.nome

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(8.dp),
                                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // sombra azul
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                            .clickable { expandedItem = if (isExpanded) null else fornecedores.nome }
                            .padding(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                                .padding(12.dp)
                        ) {

                            Text(fornecedores.nome, style = MaterialTheme.typography.titleMedium)
                            Divider(
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                        }

                        }
                    }
                }

            }

        }
    }

/*
@Composable
fun FornecedorItem(
    fornecedor: fornecedor
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = true },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = fornecedores.nome, style = MaterialTheme.typography.titleMedium)
            }

        }
    }
}*/
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

