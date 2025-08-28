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
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.products.ProductViewModel
import com.example.test_2.menu.TopBarWithLogo
import kotlinx.coroutines.launch
import com.example.test_2.data_db.products.Products
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
import com.example.test_2.data_db.supplier.Supplier
import com.example.test_2.data_db.supplier.SupplierViewModel
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption,
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
fun RegistrationProducts(
    productId:Int,
    openDrawer: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context, CoroutineScope(Dispatchers.IO)) }
    val viewModelSupplier = remember { database?.let { SupplierViewModel(it) } }
    val viewModelProducts = remember{database?.let{ ProductViewModel(it.productsDao()) }}

    var showSupplierDropdown by remember { mutableStateOf(false) }
    val suppliers by viewModelSupplier?.supplier ?: remember { mutableStateOf(emptyList<Supplier>()) } //
    var inputHeight by remember { mutableStateOf(0f) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showProductsExists by remember { mutableStateOf(false) }
    var notProductSucess by remember { mutableStateOf(false) }
    var showDeleteDatabase by remember { mutableStateOf(false) }
    var notShowDeleteDatabase by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var skuCode by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf<String?>("") }
    var price by remember { mutableStateOf("") }
    var promotionalPrice by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unitOfMeasure by remember { mutableStateOf<String?>("") }
    var brand by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>("") }
    var barcode by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var selectedSupplier by remember { mutableStateOf<String?>(null) }
    var fabrication by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var imagens by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val statusOptions = listOf("Ativo", "Inativo", "Esgotado")
    val unidades = listOf("Unidade", "Caixa", "Kg", "Litro", "Metro")
    val opcoes = listOf("Cozinha", "Sushi", "Copa")



    var product by remember {
        mutableStateOf(
            Products(
                uid = 0,
                name = "",
                sector = "",
                skuCode = "",
                price = 0f,
                quantity = 0,
                brand = "",
                promotionalPrice = null,
                unitOfMeasure = null,
                status = null,
                barcode = null,
                height = null,
                width = null,
                length = null,
                weight = null,
                color = null,
                size = null,
                cost = null,
                tags = null,
                supplier = null,


            ))
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris != null) {
                imagens = imagens + uris
            }
        }

    val scope = rememberCoroutineScope()


    fun validateRequiredFields(): Boolean {
       return name.isNotBlank() && price!=null && sector!=null && status!=null

    }




    LaunchedEffect(Unit) {  // Carrega automaticamente ao entrar na tela
        viewModelSupplier?.let { vm ->
            scope.launch {
                try {
                    vm.getAll()  // Ou o nome da função equivalente no seu ViewModel
                } catch (e: Exception) {
                    Log.e("ResgistrationProducts", "Erro ao carregar fornecedores: ${e.message}")
                }
            }
        }
    }


    LaunchedEffect(productId) {
        if (productId != -1) {
            val searchOfProducts = viewModelProducts?.getById(productId)
            if (searchOfProducts != null) {
                product = searchOfProducts
            } else {
                // continua com o produto vazio, pronto para cadastro
                Log.w("ProdutoCadastroScreen", "Produto não encontrado, iniciando cadastro")
            }
        }
    }



    Scaffold(
        topBar = {
            TopBarWithLogo(
                userName = "Natanael Almeida",
                onMenuClick = {
                    scope.launch {  }
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
                text = "Cadastrar Produto",
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
                                .clickable { },
                            contentScale = ContentScale.Crop
                        )
                    }

                    item {
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
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do Produto*") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = skuCode,
                    onValueChange = { skuCode = it },
                    label = { Text("Código/SKU") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                dropdownMenuField(
                    label = "Setor*",
                    options = opcoes,
                    selectedOption = sector ?: "",
                    onOptionSelected = { sector = it }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Preço*") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = promotionalPrice,
                    onValueChange = { promotionalPrice = it },
                    label = { Text("Preço Promocional (Opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Estoque / Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                dropdownMenuField(
                    label = "Unidade de Medida*",
                    options = unidades,
                    selectedOption = unitOfMeasure ?: "",
                    onOptionSelected = { unitOfMeasure = it }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca / Fabricante") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                dropdownMenuField(
                    label = "Status*",
                    options = statusOptions,
                    selectedOption = status ?: "",
                    onOptionSelected = { status = it }
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = { Text("Código de Barras") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text("Custo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Altura (opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = width,
                    onValueChange = { width = it },
                    label = { Text("Largura (opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = length,
                    onValueChange = { length = it },
                    label = { Text("Comprimento (opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Peso (opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Cor (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = size,
                    onValueChange = { size = it },
                    label = { Text("Tamanho (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Text("Número de fornecedores: ${suppliers.size}")

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
                            suppliers.forEach { supplier ->
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

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = fabrication,
                    onValueChange = { fabrication = it },
                    label = { Text("Data de Fabricação (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = expirationDate,
                    onValueChange = { expirationDate = it },
                    label = { Text("Data de Validade (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (name.isNotBlank() &&
                                sector != null &&
                                skuCode.isNotBlank() &&
                                price.isNotBlank() &&
                                quantity.isNotBlank() &&
                                brand.isNotBlank() &&
                                viewModelProducts != null
                            ) {
                                scope.launch {
                                    try {
                                        val productExists = viewModelProducts.findByName(name.trim())
                                        if (productExists && productId != -1) {
                                            showProductsExists = true
                                        } else {
                                            viewModelProducts.insertProduct(
                                                uid = Random.nextInt(0, 100),
                                                name = name,
                                                sector = sector!!,
                                                skuCode = skuCode,
                                                price = price.toFloatOrNull() ?: 0f,
                                                promotionalPrice = promotionalPrice.toFloatOrNull(),
                                                quantity = quantity.toIntOrNull() ?: 0,
                                                unitOfMeasure = unitOfMeasure?.ifEmpty { null },
                                                brand = brand,
                                                status = status?.ifEmpty { null },
                                                barcode = barcode.ifEmpty { null },
                                                height = height.toFloatOrNull(),
                                                width = width.toFloatOrNull(),
                                                length = length.toFloatOrNull(),
                                                weight = weight.toFloatOrNull(),
                                                color = color.ifEmpty { null },
                                                size = size.ifEmpty { null },
                                                cost = cost.toFloatOrNull(),
                                                tags = tags.ifEmpty { null },
                                                supplier = selectedSupplier?.ifEmpty { null },
                                            )
                                            name = ""
                                            sector = null
                                            skuCode = ""
                                            price = ""
                                            promotionalPrice = ""
                                            quantity = ""
                                            unitOfMeasure = null
                                            brand = ""
                                            status = null
                                            barcode = ""
                                            height = ""
                                            width = ""
                                            length = ""
                                            weight = ""
                                            color = ""
                                            size = ""
                                            cost = ""
                                            tags = ""
                                            selectedSupplier = null
                                            expirationDate = ""
                                            showSuccessDialog = true

                                        }
                                    } catch (e: Exception) {
                                        Log.e("RegistrationProducts", "Erro ao cadastrar produto: ${e.message}")
                                        notProductSucess = true
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Salvar")
                    }

                    Button(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Voltar")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        try {
                            AppDatabase.resetDatabase(context, scope)
                            showDeleteDatabase = true
                        } catch (e: Exception) {
                            Log.e("RegistrationProducts", "Falha ao apagar ${e.message}")
                            notShowDeleteDatabase = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Resetar banco de dados", color = Color.White)
                }
            }
        }
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Sucesso") },
                text = { Text("Produto cadastrado com sucesso!") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            navController.popBackStack()
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
        if (showDeleteDatabase) {
            AlertDialog(
                onDismissRequest = { showDeleteDatabase = false },
                title = { Text("Base de dados apagado com sucesso") },
                text = { Text("") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDatabase = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
        if (notShowDeleteDatabase) {
            AlertDialog(
                onDismissRequest = { notShowDeleteDatabase = false },
                title = { Text("Falha ao apagar o banco de dados") },
                text = { Text("") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            notShowDeleteDatabase = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
        if (notProductSucess) {
            AlertDialog(
                onDismissRequest = { notProductSucess = false },
                title = { Text("Falha ao cadastrar o produto") },
                text = { Text("") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            notProductSucess = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }}