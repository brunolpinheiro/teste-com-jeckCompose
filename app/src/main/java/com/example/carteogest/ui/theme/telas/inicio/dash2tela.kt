package com.example.carteogest.ui.telas.inicio

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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carteogest.menu.ProdutoViewModel
import com.example.carteogest.menu.TopBarWithLogo
import com.example.carteogest.ui.telas.inicio.Produto
import kotlinx.coroutines.launch
import com.example.CarteoGest.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.unit.DpOffset
import androidx.navigation.NavController


@Composable
fun DashboardScreenum(
    viewModel: ProdutoViewModel = viewModel(),
    onAjustarEstoque: (Produto) -> Unit,
    onInserirValidade: (Produto) -> Unit,
    openDrawer: () -> Unit,
    navController: NavController
) {
    val produtos by viewModel.produtos.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var query by remember { mutableStateOf("") }
    val produtosFiltrados = produtos
        .filter { it.nome.contains(query, ignoreCase = true) }
        .sortedBy { it.validade }


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
                    onClick = { fabMenuExpanded = !fabMenuExpanded },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "cadastro")
                }

                DropdownMenu(
                    expanded = fabMenuExpanded,
                    onDismissRequest = { fabMenuExpanded = false },
                    offset = DpOffset(x = 0.dp, y = (-36).dp) // aparece acima do FAB
                ) {
                    DropdownMenuItem(
                        text = { Text("Cadastrar Produtos") },
                        onClick = { navController.navigate("ProdutoCadastroScreen")// ação: abrir tela de cadastro
                        },
                        modifier = Modifier.background(Color(0xFF004AAD))
                            .padding(12.dp),
                        

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
                label = { Text("Pesquisar produtos") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar",tint = MaterialTheme.colorScheme.primary)
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
                    // Botão para mostrar todas as categorias
                    item {
                        FilterChip(
                            selected = true,
                            onClick = { /*...*/ },
                            label = { Text("Todas") }) // ✅ Composable
                    }

                    // Criar chips para cada categoria única
                    items(produtos.map { it.categoria }.distinct()) { categoria ->
                        FilterChip(
                            selected = true,
                            onClick = { /*...*/ },
                            label = { Text(categoria) } // ✅ Composable
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
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Produto", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text("Quantidade", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text("Validade", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize() .padding(vertical = 2.dp)

            ) {
                items(produtosFiltrados) { produto ->
                    val isExpanded = expandedItem == produto.nome

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
                            .clickable { expandedItem = if (isExpanded) null else produto.nome }
                            .padding(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = produto.nome,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary, // cor azul primária
                                    shape = RoundedCornerShape(8.dp)
                                )
                            )

                            Spacer(modifier = Modifier.width(1.dp))

                            Text(produto.nome, style = MaterialTheme.typography.titleMedium)
                            Divider(
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            Text(" ${produto.quantidade}", style = MaterialTheme.typography.bodyLarge)
                            dividerPro()
                            Text(" ${produto.validade}", style = MaterialTheme.typography.bodyLarge)
                        }

                        AnimatedVisibility(visible = isExpanded,) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { onAjustarEstoque(produto) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Ajustar Estoque", color = Color.White,style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { onInserirValidade(produto) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008C4A)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Inserir Validade", color = Color.White,style = MaterialTheme.typography.bodySmall)
                                }
                                Button(
                                    onClick = { navController.navigate("ProdutoCadastroScreen") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Editar Produto", color = Color.White,style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ProdutoItem(
    produto: Produto,
    onAjustarEstoque: () -> Unit,
    onInserirValidade: () -> Unit
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
                Text(text = produto.nome, style = MaterialTheme.typography.titleMedium)

                Text(text = "Qtd: ${produto.quantidade}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Validade: ${produto.validade}", style = MaterialTheme.typography.bodySmall)
            }

            // Ícone para abrir menu contextual
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Ajustar Estoque") },
                        onClick = {
                            onAjustarEstoque()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Inserir Nova Validade") },
                        onClick = {
                            onInserirValidade()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
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
@Composable
fun dividerPro(){
    Divider(
        color = Color.Gray,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}


