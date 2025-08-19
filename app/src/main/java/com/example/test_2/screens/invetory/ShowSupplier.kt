package com.example.test_2.screens.inventory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.supplier.Supplier
import com.example.test_2.data_db.supplier.SupplierViewModel
import com.example.test_2.menu.TopBarWithLogo
import kotlinx.coroutines.launch

@Composable
fun ShowSupplier(
    openDrawer: () -> Unit,
    navController: NavController
) {
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel: SupplierViewModel = viewModel { SupplierViewModel(database!!) } // Use viewModel() para gerenciar lifecycle
    val suppliers by viewModel.supplier // Renomeei para suppliers para clareza

    // Filtragem baseada na query (pesquisa por nome) e categoria
    val filteredSuppliers = suppliers.filter {
        (query.isEmpty() || it.name.contains(query, ignoreCase = true)) &&
                (selectedCategory == null || it.name == selectedCategory) // Assumindo que Supplier tem 'category'; ajuste se não
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var expandedItem by remember { mutableStateOf<String?>(null) }
    var fabMenuExpanded by remember { mutableStateOf(false) }

    // Carregue a lista ao entrar na tela (se não carregada)
    LaunchedEffect(Unit) {
        viewModel.getAll()
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = { scope.launch { drawerState.open() } },
                openDrawer = openDrawer
            )
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(onClick = { fabMenuExpanded = !fabMenuExpanded }) {
                    Icon(Icons.Default.Add, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = fabMenuExpanded,
                    onDismissRequest = { fabMenuExpanded = false },
                    offset = DpOffset(x = 0.dp, y = (-56).dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Cadastrar Fornecedor") },
                        onClick = {
                            navController.navigate("SupplierRegistrationScreen")
                            fabMenuExpanded = false
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Pesquisar Fornecedores") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (showFilters) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Chip para "Todas"
                    item {
                        FilterChipfor(
                            label = "Todas",
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null }
                        )
                    }
                    // Chips para categorias únicas (assumindo Supplier tem 'category')
                    val categories = suppliers.map { it.name }.distinct()
                    items(categories) { category ->
                        FilterChipfor(
                            label = category ?: "Sem Cnpj",
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredSuppliers) { supplier ->
                    val isExpanded = expandedItem == supplier.name
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .clickable { expandedItem = if (isExpanded) null else supplier.name }
                            .padding(16.dp)
                    ) {
                        Text(supplier.name, style = MaterialTheme.typography.titleMedium)
                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("CNPJ: ${supplier.cnpj}")
                            Text("Endereço: ${supplier.adress}") // Ajuste se o campo for 'adress'
                            Text("Telefone: ${supplier.phone}")
                            Text("Email: ${supplier.email}")
                            // Adicione mais campos se necessário
                        }
                    }
                    Divider(color = Color.LightGray, thickness = 1.dp)
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
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else Color.Gray),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(text = label, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
    }
}
