package com.example.carteogest.ui.theme.telas.usuarios

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carteogest.login.User
import com.example.carteogest.login.UserViewModel
import com.example.carteogest.menu.TopBarWithLogo
import com.example.carteogest.ui.telas.ControleEstoque.FilterChip
import kotlinx.coroutines.launch


@Composable
fun UserListScreen(
    viewModel: UserViewModel,
    onDestinationClicked: (String) -> Unit,
    openDrawer: () -> Unit,
    navController: NavController
) {
    val users by viewModel.users.collectAsState(initial = emptyList())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val produtosFiltrados = users.filter { usuarios ->
        usuarios.nome.contains(query, ignoreCase = true)
    }

    var expandedItem by remember { mutableStateOf<String?>(null) }
    var fabMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
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
                        onClick = { navController.navigate("UserRegistrationScreen/-1") },
                        modifier = Modifier
                            .background(Color(0xFF004AAD), shape = RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

            }
        }
    ) {  paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).padding(paddingValues)) {
            // Campo de pesquisa
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Pesquisar Usuarios") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filtros por setor
            AnimatedVisibility(visible = showFilters) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            label = "Todos",
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null }
                        )
                    }
                    /*
                    items(users.map { it.sector }.distinct()) { categoria ->
                        FilterChip(
                            label = categoria,
                            selected = selectedCategory == categoria,
                            onClick = { selectedCategory = if (selectedCategory == categoria) null else categoria }
                        )
                    }*/
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cabeçalho
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp, horizontal = 12.dp)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Usuario", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text("Status", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(users) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(4.dp))
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(6.dp),
                                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // sombra azul
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(user.nome, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Nome: ${user.nome}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = { onDestinationClicked("UserRegistrationScreen/${user.uid}") }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = { viewModel.deleteUser(user.uid!!) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

