package com.example.carteogest.ui.telas.ControleEstoque

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.CarteoGest.R
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.factory.ProductViewModelFactory
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import com.example.carteogest.datadb.data_db.login.UserViewModel
import androidx.compose.ui.text.input.ImeAction


@Composable
fun DashboardScreenum(
    onAjustarEstoque: (produtoId: Int) -> Unit,
    onInserirValidade: (produtoId: Int) -> Unit,
    openDrawer: () -> Unit,
    navController: NavController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Obtem o DB e o DAO
    val db = AppDatabase.getDatabase(context, scope)
    val produtoViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(db.productsDao(), db.validityDao())
    )

    val produtos by produtoViewModel.products

    // Estado de busca e filtros
    var query by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val produtosFiltrados = produtos.filter { produto ->
        produto.name.contains(query, ignoreCase = true) &&
                (selectedCategory == null || produto.sector == selectedCategory)
    }

    var expandedItem by remember { mutableStateOf<String?>(null) }
    var fabMenuExpanded by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    LaunchedEffect(Unit) {
        produtoViewModel.getAll()
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = { coroutineScope.launch { drawerState.open() } },
                openDrawer = openDrawer,
                navController = navController

            )
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { fabMenuExpanded = !fabMenuExpanded },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.size(56.dp).background(Color.White)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Cadastrar")
                }

                DropdownMenu(
                    expanded = fabMenuExpanded,
                    onDismissRequest = { fabMenuExpanded = false },
                    offset = DpOffset(x = 0.dp, y = (-36).dp),
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Cadastrar Produtos") },
                        onClick = {
                            navController.navigate("ProdutoCadastroScreen/-1")
                            fabMenuExpanded = false
                        },
                        modifier = Modifier.background(Color.White)
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
            // Campo de pesquisa
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Pesquisar produtos") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // aqui aplica o filtro
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filtros por setor
            AnimatedVisibility(visible = showFilters,modifier = Modifier.fillMaxWidth()
                .background(Color.White)) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth() .background(Color.White)
                ) {
                    item{

                        FilterChip(
                            label =  { Text("Todas", color = Color.Black) },
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                selectedContainerColor = Color(0xFF004AAD)
                            )

                        )
                    }
                    items(produtos.map { it.sector }.distinct()) { categoria ->
                        FilterChip(
                            label = { Text(categoria, color = Color.Black) },
                            selected = selectedCategory == categoria,
                            onClick = { selectedCategory = if (selectedCategory == categoria) null else categoria },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                selectedContainerColor = Color(0xFF004AAD)
                            )
                        )
                    }
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
                Text("Produto", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text("Quantidade", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Lista de produtos
            LazyColumn(
                modifier = Modifier.fillMaxSize()

            ) {
                items(produtosFiltrados) { produto ->
                    val isExpanded = expandedItem == produto.name

                    ProdutoItem(
                        produto = produto,
                        expanded = isExpanded,
                        onClick = { expandedItem = if (isExpanded) null else produto.name },
                        onAjustarEstoque = { onAjustarEstoque(produto.uid) },
                        onInserirValidade = { onInserirValidade(produto.uid) },
                        onEditarProduto = { navController.navigate("ProdutoCadastroScreen/${produto.uid}") }
                    )
                }
            }

        }
    }
}

@Composable
fun ProdutoItem(
    produto: com.example.carteogest.datadb.data_db.products.Products,
    expanded: Boolean,
    onClick: () -> Unit,
    onAjustarEstoque: () -> Unit,
    onInserirValidade: () -> Unit,
    onEditarProduto: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(Color.White, shape = RoundedCornerShape(4.dp))
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(6.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // sombra azul
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            .clickable { onClick() }
            .padding(4.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = produto.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(produto.name, style = MaterialTheme.typography.titleMedium)
            Divider(color = Color.Gray, modifier = Modifier.fillMaxHeight().width(1.dp))
            Text("${produto.quantity}", style = MaterialTheme.typography.bodyLarge)
        }

        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                //horizontalArrangement = Arrangement.SpaceEvenly,
                horizontalArrangement = Arrangement.spacedBy(8.dp),

                //verticalAlignment = Alignment.CenterVertically

            ) {
                Button(
                    onClick = onAjustarEstoque,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ajustar Estoque", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onInserirValidade,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008C4A)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Inserir Validade", color = Color.White)
                }

                Button(
                    onClick = onEditarProduto,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar Produto", color = Color.White)
                }
            }
        }
    }
    Divider(color = Color.Gray, modifier = Modifier.fillMaxHeight().width(1.dp))
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
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
