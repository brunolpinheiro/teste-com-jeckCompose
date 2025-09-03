/*package com.example.carteogest.ui.telas.ControleEstoque

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.carteogest.datadb.data_db.login.User
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.datadb.data_db.products.Products
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch

// Estrutura para controlar seleção de produtos
data class ProdutoSelecionado(
    val produtoId: Int,
    val nome: String,
    var responsavelId: Int? = null,
    var fabricacao: String = "",
    var validade: String = "",
    var quantidade: Int = 1
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImpressaoAgrupadaScreen(
    navController: NavController,
    viewModel: BluetoothViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    openDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // Produtos e usuários vindos do banco
    val produtosBanco = productViewModel.products.value
    val usuarios by userViewModel.users.collectAsState()

    var produtosSelecionados by remember { mutableStateOf(listOf<ProdutoSelecionado>()) }
    var produtoSelecionado by remember { mutableStateOf<ProdutoSelecionado?>(null) }

    // Switch de validade/responsável global
    var usarValidadeUnica by remember { mutableStateOf(true) }
    var usarResponsavelUnico by remember { mutableStateOf(true) }

    // Campos globais
    var validadeGlobal by remember { mutableStateOf("") }
    var fabricacaoGlobal by remember { mutableStateOf("") }
    var responsavelGlobalId by remember { mutableStateOf<Int?>(null) }
    var expandedProdutos by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    LaunchedEffect(Unit) {
        userViewModel.loadUsers()
        productViewModel.getAll()
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Configuração Global
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Validade Global?")
                Switch(checked = usarValidadeUnica, onCheckedChange = { usarValidadeUnica = it })
            }
            if (usarValidadeUnica) {
                EditableDateInputField(
                    label = "Data de Fabricação (todos os produtos)",
                    initialDate = fabricacaoGlobal
                ) { fabricacaoGlobal = it }
                EditableDateInputField(
                    label = "Validade (todos os produtos)",
                    initialDate = validadeGlobal
                ) { validadeGlobal = it }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Responsável Global?")
                Switch(checked = usarResponsavelUnico, onCheckedChange = { usarResponsavelUnico = it })
            }
            if (usarResponsavelUnico) {
                DropdownUsuario(
                    usuarios = usuarios,
                    selectedId = responsavelGlobalId,
                    onSelect = { responsavelGlobalId = it },
                    label = "Responsável (todos os produtos)"
                )
            }

           /* // Lista de produtos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(produtosBanco) { produto ->
                    val selecionado = produtosSelecionados.find { it.produtoId == produto.uid }

                    if (selecionado == null) {
                        // Produto ainda não selecionado
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(produto.name)
                                Button(onClick = {
                                    produtosSelecionados = produtosSelecionados + ProdutoSelecionado(
                                        produtoId = produto.uid,
                                        nome = produto.name
                                    )
                                }) {
                                    Text("Adicionar")
                                }
                            }
                        }
                    } else {
                        // Produto já selecionado -> Card editável
                        ProdutoSelecionadoCard(
                            selecionado = selecionado,
                            usuarios = usuarios,
                            usarValidadeUnica = usarValidadeUnica,
                            usarResponsavelUnico = usarResponsavelUnico,
                            onRemove = { produtosSelecionados = produtosSelecionados - selecionado }
                        )
                    }
                }
            }*/
            SearchableProductSelector(
                produtosBanco = produtosBanco,
                produtosSelecionados = produtosSelecionados,
                onProdutoSelected = { novoProduto ->
                    produtosSelecionados = produtosSelecionados + novoProduto
                }
            )
            // Botão para selecionar produto
            /*OutlinedButton(onClick = { expandedProdutos = true }) {
                Text("Adicionar Produto")
            }

            DropdownMenu(
                expanded = expandedProdutos,
                onDismissRequest = { expandedProdutos = false }
            ) {
                produtosBanco.forEach { produto ->
                    // evitar duplicatas
                    if (produtosSelecionados.none { it.produtoId == produto.uid }) {
                        DropdownMenuItem(
                            text = { Text(produto.name) },
                            onClick = {
                                produtosSelecionados = produtosSelecionados + ProdutoSelecionado(
                                    produtoId = produto.uid,
                                    nome = produto.name
                                )
                                expandedProdutos = false
                            }
                        )
                    }
                }
            }
            */
             

// Exibir apenas os produtos selecionados
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(produtosSelecionados) { selecionado ->
                    ProdutoSelecionadoCard(
                        selecionado = selecionado,
                        usuarios = usuarios,
                        usarValidadeUnica = usarValidadeUnica,
                        usarResponsavelUnico = usarResponsavelUnico,
                        onRemove = { produtosSelecionados = produtosSelecionados - selecionado }
                    )
                }
            }

            // Botão imprimir todos
            Button(
                onClick = {
                    produtosSelecionados.forEach { p ->
                        val validadeFinal = if (usarValidadeUnica) validadeGlobal else p.validade
                        val fabricacaoFinal = if (usarValidadeUnica) fabricacaoGlobal else p.fabricacao
                        val responsavelIdFinal = if (usarResponsavelUnico) responsavelGlobalId else p.responsavelId
                        val responsavelNome = usuarios.find { it.uid == responsavelIdFinal }?.nome ?: ""

                        viewModel.printLabel(
                            text1 = p.nome,
                            text2 = responsavelNome,
                            text3 = fabricacaoFinal,
                            text4 = validadeFinal,
                            text5 = p.quantidade
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = produtosSelecionados.isNotEmpty()
            ) {
                Text("Imprimir Todos")
            }
        }
    }
}

@Composable
fun ProdutoSelecionadoCard(
    selecionado: ProdutoSelecionado,
    usuarios: List<User>, // seu model de usuário
    usarValidadeUnica: Boolean,
    usarResponsavelUnico: Boolean,
    onRemove: () -> Unit
) {
    var quantidadeText by remember { mutableStateOf(selecionado.quantidade.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Produto: ${selecionado.nome}")


            if (!usarValidadeUnica) {
                EditableDateInputField(
                    label = "fabricação",
                    initialDate = selecionado.fabricacao
                ) { selecionado.fabricacao = it }

                EditableDateInputField(
                    label = "Validade",
                    initialDate = selecionado.validade
                ) { selecionado.validade = it }
            }

            if (!usarResponsavelUnico) {
                DropdownUsuario(
                    usuarios = usuarios,
                    selectedId = selecionado.responsavelId,
                    onSelect = { selecionado.responsavelId = it },
                    label = "Responsável"
                )
            }

            OutlinedTextField(
                value = quantidadeText,
                onValueChange = { q ->
                    quantidadeText = q
                    selecionado.quantidade = q.toIntOrNull() ?: 1
                },
                label = { Text("Quantidade de Impressões") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remover Produto")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownUsuario(
    usuarios: List<User>,
    selectedId: Int?,
    onSelect: (Int) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedNome = usuarios.find { it.uid == selectedId }?.nome ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedNome,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            usuarios.forEach { usuario ->
                DropdownMenuItem(
                    text = { Text(usuario.nome) },
                    onClick = {
                        onSelect(usuario.uid)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Composable
fun SearchableProductSelector(
    produtosBanco: List<Products>,
    produtosSelecionados: List<ProdutoSelecionado>,
    onProdutoSelected: (ProdutoSelecionado) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                expanded = it.isNotBlank()
            },
            label = { Text("Pesquisar Produto") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            val produtosFiltrados = produtosBanco
                .filter { it.name.contains(searchText, ignoreCase = true) }
                .filter { produto -> produtosSelecionados.none { it.produtoId == produto.uid } }

            if (produtosFiltrados.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Nenhum produto encontrado") },
                    onClick = { /* nada */ }
                )
            } else {
                produtosFiltrados.forEach { produto ->
                    DropdownMenuItem(
                        text = { Text(produto.name) },
                        onClick = {
                            onProdutoSelected(
                                ProdutoSelecionado(
                                    produtoId = produto.uid,
                                    nome = produto.name
                                )
                            )
                            searchText = ""  // limpa pesquisa
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

*/
package com.example.carteogest.ui.telas.ControleEstoque

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carteogest.bluetooth.model.BluetoothViewModel
import com.example.carteogest.datadb.data_db.login.User
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.datadb.data_db.products.Products
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch

// Estrutura para controlar seleção de produtos
data class ProdutoSelecionado(
    val produtoId: Int,
    val nome: String,
    var responsavelId: Int? = null,
    var fabricacao: String = "",
    var validade: String = "",
    var quantidade: Int = 1
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImpressaoAgrupadaScreen(
    navController: NavController,
    viewModel: BluetoothViewModel,
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel,
    openDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // Produtos e usuários vindos do banco
    val produtosBanco = productViewModel.products.value
    val usuarios by userViewModel.users.collectAsState()

    var produtosSelecionados by remember { mutableStateOf(listOf<ProdutoSelecionado>()) }

    // Switch global
    var usarValidadeUnica by remember { mutableStateOf(true) }
    var usarResponsavelUnico by remember { mutableStateOf(true) }

    // Campos globais
    var validadeGlobal by remember { mutableStateOf("") }
    var fabricacaoGlobal by remember { mutableStateOf("") }
    var responsavelGlobalId by remember { mutableStateOf<Int?>(null) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)


    Scaffold(
        topBar = {
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = { scope.launch { drawerState.open() } },
                openDrawer = openDrawer,
                navController = navController
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Configuração global de validade
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Validade Global?")
                Switch(checked = usarValidadeUnica, onCheckedChange = { usarValidadeUnica = it })
            }
            if (usarValidadeUnica) {
                EditableDateInputField(
                    label = "Data de Fabricação (todos os produtos)",
                    initialDate = fabricacaoGlobal
                ) { fabricacaoGlobal = it }
                EditableDateInputField(
                    label = "Validade (todos os produtos)",
                    initialDate = validadeGlobal
                ) { validadeGlobal = it }
            }

            // Configuração global de responsável
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Responsável Global?")
                Switch(checked = usarResponsavelUnico, onCheckedChange = { usarResponsavelUnico = it })
            }
            if (usarResponsavelUnico) {
                DropdownUsuario(
                    usuarios = usuarios,
                    selectedId = responsavelGlobalId,
                    onSelect = { responsavelGlobalId = it },
                    label = "Responsável (todos os produtos)",
                )
            }

            // Campo de pesquisa de produtos
            SearchableProductSelector(
                produtosBanco = produtosBanco,
                produtosSelecionados = produtosSelecionados,
                onProdutoSelected = { novoProduto ->
                    produtosSelecionados = produtosSelecionados + novoProduto
                }
            )

            // Produtos selecionados
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f ).background(Color.White)
            ) {
                items(produtosSelecionados) { selecionado ->
                    ProdutoSelecionadoCard(
                        selecionado = selecionado,
                        usuarios = usuarios,
                        usarValidadeUnica = usarValidadeUnica,
                        usarResponsavelUnico = usarResponsavelUnico,
                        onRemove = { produtosSelecionados = produtosSelecionados - selecionado }
                    )
                }
            }

            // Botão imprimir todos
            Button(
                onClick = {
                    if (viewModel.isConnected()) {
                    produtosSelecionados.forEach { p ->
                        val validadeFinal = if (usarValidadeUnica) validadeGlobal else p.validade
                        val fabricacaoFinal = if (usarValidadeUnica) fabricacaoGlobal else p.fabricacao
                        val responsavelIdFinal = if (usarResponsavelUnico) responsavelGlobalId else p.responsavelId
                        val responsavelNome = usuarios.find { it.uid == responsavelIdFinal }?.nome ?: ""

                        // Imprimir N vezes baseado na quantidade
                            viewModel.printLabel(
                                text1 = p.nome,
                                text2 = responsavelNome,
                                text3 = fabricacaoFinal,
                                text4 = validadeFinal,
                                text5 = p.quantidade // Exemplo: etiqueta numerada
                            )

                    }
                    }else{ // Se não estiver conectado, navega para tela de conexão
                        navController.navigate("ConectPrinters")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = produtosSelecionados.isNotEmpty()
            ) {
                Text("Imprimir Todos")
            }
        }
    }
}

@Composable
fun ProdutoSelecionadoCard(
    selecionado: ProdutoSelecionado,
    usuarios: List<User>,
    usarValidadeUnica: Boolean,
    usarResponsavelUnico: Boolean,
    onRemove: () -> Unit
) {
    var quantidadeText by remember { mutableStateOf(selecionado.quantidade.toString()) }
    var selectedUserId by remember { mutableStateOf(selecionado.responsavelId) }

    Card(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Produto: ${selecionado.nome}")

            if (!usarValidadeUnica) {
                EditableDateInputField(
                    label = "Fabricação",
                    initialDate = selecionado.fabricacao
                ) { selecionado.fabricacao = it }

                EditableDateInputField(
                    label = "Validade",
                    initialDate = selecionado.validade
                ) { selecionado.validade = it }
            }

            if (!usarResponsavelUnico) {
                DropdownUsuario(
                    usuarios = usuarios,
                    selectedId = selectedUserId,
                    onSelect = {
                        selectedUserId = it
                        selecionado.responsavelId = it // atualiza o modelo também
                    },
                    label = "Responsável"
                )

            }

            OutlinedTextField(
                value = quantidadeText,
                onValueChange = { q ->
                    quantidadeText = q
                    selecionado.quantidade = q.toIntOrNull() ?: 1
                },
                label = { Text("Quantidade de Impressões") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Button(
                onClick = onRemove,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remover Produto")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownUsuario(
    usuarios: List<User>,
    selectedId: Int?,
    onSelect: (Int) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedNome = usuarios.find { it.uid == selectedId }?.nome ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedNome,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            usuarios.forEach { usuario ->
                DropdownMenuItem(
                    text = { Text(usuario.nome) },
                    onClick = {
                        onSelect(usuario.uid)
                        expanded = false
                    },
                    modifier = Modifier.background(Color.White)

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableProductSelector(
    produtosBanco: List<Products>,
    produtosSelecionados: List<ProdutoSelecionado>,
    onProdutoSelected: (ProdutoSelecionado) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.background(Color.White)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                expanded = it.isNotBlank()
            },
            label = { Text("Pesquisar Produto") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .background(Color.White),
            readOnly = false
        )

        val produtosFiltrados = produtosBanco
            .filter { it.name.contains(searchText, ignoreCase = true) }
            .filter { produto -> produtosSelecionados.none { it.produtoId == produto.uid } }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            if (produtosFiltrados.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Nenhum produto encontrado") },
                    onClick = { expanded = false },
                    modifier = Modifier.background(Color.White)
                )
            } else {
                produtosFiltrados.forEach { produto ->
                    DropdownMenuItem(
                        text = { Text(produto.name) },
                        onClick = {
                            onProdutoSelected(
                                ProdutoSelecionado(
                                    produtoId = produto.uid,
                                    nome = produto.name
                                )
                            )
                            searchText = ""
                            expanded = false
                        },
                        modifier = Modifier.background(Color.White)
                    )
                }
            }
        }
    }
}
