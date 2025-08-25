// ProdutoCadastroScreen.kt
package com.example.carteogest.ui.telas.ControleEstoque

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import com.example.carteogest.datadb.data_db.products.Products
import com.example.carteogest.datadb.data_db.supplier.SupplierViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import android.util.Log
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carteogest.datadb.data_db.supplier.Supplier
import com.example.carteogest.factory.ProductViewModelFactory
import com.example.carteogest.factory.supplierViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption ?: "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutoCadastroScreen(
    openDrawer: () -> Unit,
    productViewModel: ProductViewModel,
    produtoId: Int,
    navController : NavController

) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context, CoroutineScope(Dispatchers.IO)) }
    val supllierViewModel: SupplierViewModel = viewModel(
        factory = supplierViewModelFactory(database.supplierDao()))

    val suppliers by supllierViewModel?.supplier
        ?: remember { mutableStateOf(emptyList<Supplier>()) }
    // Corrigido: "supplier" -> "suppliers"; adicionei tipo genérico para clareza
    var showUnitOfMeasureDropdown by remember { mutableStateOf(false) }
    var showStatusDropdown by remember { mutableStateOf(false) }
    var showSupplierDropdown by remember { mutableStateOf(false) }
    var showProductsExists by remember { mutableStateOf(false) }
    var inputHeight by remember { mutableStateOf(0f) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    //Seleçãod e setor
    val setor by productViewModel.sector.collectAsState()
    val opcoes = if (setor.isEmpty()) {
        listOf("Nenhuma categoria disponível")
    } else {
        setor
    }
    //estado do usuario
    val statusOptions = listOf("Ativo", "Inativo")
    //unidades de medidas
    val unidades = listOf("Unidade", "Caixa", "Kg", "Litro", "Metro")


    //val de menu
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
//fornecedores
    var selectedSupplier by remember { mutableStateOf<String?>(null) }
    // Estados dos campos obrigatórios
    LaunchedEffect(Unit) {  // Carrega automaticamente ao entrar na tela
        supllierViewModel?.let { vm ->
            scope.launch {
                try {
                    vm.getAll()  // Ou o nome da função equivalente no seu ViewModel
                } catch (e: Exception) {
                    Log.e("ResgistrationProducts", "Erro ao carregar fornecedores: ${e.message}")
                }
            }
        }
    }

    var produto by remember {
        mutableStateOf(
            Products(
                uid = 0,
                name = "",
                skuCode = "",
                sector = "",
                price = 0f,
                promotionalPrice = 0f,
                quantity = 0,
                brand = "",
                supplier = "",
                status = true,
                barcode = "",
                cost = 0f,
                unitOfMeasure = ""
            )
        )
    }

    // Imagens

    var imagens by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris != null) {
                imagens = imagens + uris
            }
        }
//carregar produto
    LaunchedEffect(produtoId) {
        if (produtoId != -1) {
            val produtoEncontrado = productViewModel.getById(produtoId)
            if (produtoEncontrado != null) {
                produto = produtoEncontrado
            } else {
                // continua com o produto vazio, pronto para cadastro
                Log.w("ProdutoCadastroScreen", "Produto não encontrado, iniciando cadastro")
            }
        }
    }
    fun validarCamposObrigatorios(): Boolean {
        return produto.name.isNotBlank() && produto.price != null
                && produto.sector != null && produto.status != null
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (produtoId == -1) "Cadastrar Produto" else "Editar Produto",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))
                if (database == null) {
                    Text(
                        text = "Erro: Não foi possível conectar ao banco de dados",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(imagens) { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(context).data(uri).size(Size.ORIGINAL)
                                        .build()
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        // Poderia implementar remover a imagem ao clicar
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }

                        item {
                            // Botão para adicionar imagens
                            OutlinedButton(
                                onClick = { launcher.launch("image/*") },
                                modifier = Modifier.size(100.dp)
                            ) {
                                Text("+")
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = produto.name,
                        onValueChange = { novoNome ->
                            produto = produto.copy(name = novoNome)
                        },
                        label = { Text("Nome do Produto*") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))
                    if (produto != null) {
                        Text("Código Interno: ${produto!!.uid}", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = produto.skuCode,
                        onValueChange = { novoCode ->
                            produto = produto.copy(skuCode = novoCode)
                        },
                        label = { Text("Código/SKU") },
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(Modifier.height(8.dp))

                    // Dropdown Categoria
                    setorInputField(
                        label = "Setor",
                        options = opcoes,
                        selectedOption = produto.sector,
                        onOptionSelected = { selecionadoOuNovo ->
                            produto = produto.copy(sector = selecionadoOuNovo)
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = produto.price.toString(),
                        onValueChange = { novoValor ->
                            // tenta converter para Double, se não conseguir mantém 0.0
                            val preco = novoValor.toFloatOrNull() ?: 0f
                            produto = produto.copy(price = preco) // 👈 atualiza o objeto inteiro
                        },
                        label = { Text("Preço*") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = produto.promotionalPrice.toString(),
                        onValueChange = { novoValor ->
                            val preco = novoValor.toFloatOrNull() ?: 0f
                            produto = produto.copy(promotionalPrice = preco)
                        },
                        label = { Text("Preço Promocional (Opcional)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = produto.quantity.toString(),
                        onValueChange = { novaquant ->
                            val quant = novaquant.toIntOrNull() ?: 0
                            produto = produto.copy(quantity = quant)
                        },
                        label = { Text("Estoque / Quantidade") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    dropdownMenuField(
                        label = "Unidade de Medida*",
                        options = unidades,
                        selectedOption = produto.unitOfMeasure,
                        onOptionSelected = { selected ->
                            produto = produto.copy(unitOfMeasure = selected)
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(

                        value = produto.brand,
                        onValueChange = { marca ->
                            produto = produto.copy(brand = marca)
                        },
                        label = { Text("Marca / Fabricante") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    dropdownMenuField(
                        label = "Status*",
                        options = statusOptions,
                        selectedOption = if (produto.status) "Ativo" else "Inativo",
                        onOptionSelected = { selected ->
                            produto = produto.copy(
                                status = selected == "Ativo"
                            )
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = produto.barcode.toString(),
                        onValueChange = { novocode ->
                            produto = produto.copy(barcode = novocode)
                        },
                        label = { Text("Código de Barras") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))


                    OutlinedTextField(
                        value = produto.cost.toString(),
                        onValueChange = { novoCusto ->
                            val custo = novoCusto.toFloatOrNull() ?: 0f
                            produto = produto.copy(cost = custo)
                        },
                        label = { Text("Custo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(Modifier.height(8.dp))

                    Text("Número de fornecedores: ${suppliers.size ?: "nenhum"}") // Corrigido para lidar com null ou vazio
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSupplierDropdown = true }
                    ) {
                        OutlinedTextField(
                            value = selectedSupplier ?: "",
                            onValueChange = {},
                            label = { Text("Fornecedor (opcional)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                                .onGloballyPositioned { coordinates ->
                                    inputHeight = coordinates.size.height.toFloat()
                                },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Selecionar fornecedor",
                                    modifier = Modifier.clickable { showSupplierDropdown = true }
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = showSupplierDropdown,
                            onDismissRequest = { showSupplierDropdown = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = with(LocalDensity.current) { inputHeight.toDp() })
                        ) {
                            if (suppliers.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Nenhum fornecedor cadastrado") },
                                    onClick = { },
                                    enabled = false
                                )
                            } else {
                                suppliers.forEach { supplier -> // Corrigido: supplie -> supplier
                                    DropdownMenuItem(
                                        text = { Text(supplier.name) },
                                        onClick = {
                                            selectedSupplier = supplier.name
                                            showSupplierDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))


                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    produto?.let {
                                        if (produtoId == -1) {
                                            // Novo produto
                                            productViewModel.insertProduct(it)
                                        } else {
                                            // Atualização
                                            productViewModel.updateProduct(it)
                                        }
                                        navController.popBackStack()
                                    }
                                }
                            }
                        ) {
                            Text(if (produtoId == -1) "Salvar" else "Atualizar")
                        }

                        if (produtoId != -1 && produto != null) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        productViewModel.deleteProduct(produto!!)
                                        navController.popBackStack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Excluir", color = Color.White)
                            }
                        }


                    Spacer(Modifier.height(16.dp))
                }
            }
    }
    // Diálogo de sucesso
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Sucesso") },
            text = { Text("Produto cadastrado com sucesso!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    if (showProductsExists) {
        AlertDialog(
            onDismissRequest = { showProductsExists = false },
            title = { Text("Produto já cadastrado") },
            text = { Text("Falha ao cadastrar") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showProductsExists = false

                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun setorInputField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(selectedOption) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { novoTexto ->
                text = novoTexto
                onOptionSelected(novoTexto) // já atualiza no produto
            },
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        text = selectionOption
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}
