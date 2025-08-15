import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.navigation.NavController
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.test_2.data.AppDatabase
import com.example.test_2.data_db.ProductViewModel
import com.example.test_2.data_db.Products
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyRow
import kotlin.random.Random
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.*
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.android.volley.toolbox.ImageRequest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.size.Size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.layout.onGloballyPositioned

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResgistrationProducts(
    navController: NavController,
    openDrawer: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } }
    var showSectorDropdown by remember { mutableStateOf(false) }
    var showUnitOfMeasureDropdown by remember { mutableStateOf(false) }
    var showStatusDropdown by remember { mutableStateOf(false) }
    var inputHeight by remember { mutableStateOf(0f) }
    var showProductsExists by remember{mutableStateOf(false)}

    var fabrication by remember { mutableStateOf("") }
    var validity by remember { mutableStateOf("") }
    var newProductName by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf("Cozinha") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var skuCode by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf<String?>(null) }
    var price by remember { mutableStateOf("") }
    var promotionalPrice by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unitOfMeasure by remember { mutableStateOf<String?>(null) }
    var brand by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }
    var barcode by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var supplier by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var imagens by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris != null) {
            imagens = imagens + uris
        }
    }




    ComposeTutorialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cadastrar Produtos") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (database == null) {
                    Text(
                        text = "Erro: Não foi possível conectar ao banco de dados",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Cadastrar Novo Produto",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Nome do Produto") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para nome do produto" },
                                singleLine = true
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showSectorDropdown = true }
                            ) {
                                OutlinedTextField(
                                    value = category ?: "",
                                    onValueChange = {},
                                    label = { Text("Setor") },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Selecionar setor",
                                            modifier = Modifier.clickable { showSectorDropdown = true }
                                        )
                                    }
                                )
                                DropdownMenu(
                                    expanded = showSectorDropdown,
                                    onDismissRequest = { showSectorDropdown = false },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    listOf("Cozinha", "Sushi", "Copa").forEach { sector ->
                                        DropdownMenuItem(
                                            text = { Text(sector) },
                                            onClick = {
                                                category = sector
                                                showSectorDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = skuCode,
                                onValueChange = { skuCode = it },
                                label = { Text("Código SKU") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para código SKU" },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = price,
                                onValueChange = { price = it },
                                label = { Text("Preço") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para preço" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = promotionalPrice,
                                onValueChange = { promotionalPrice = it },
                                label = { Text("Preço Promocional (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para preço promocional" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = quantity,
                                onValueChange = { quantity = it },
                                label = { Text("Quantidade") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para quantidade" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Box (  modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showUnitOfMeasureDropdown = true }){
                                OutlinedTextField(
                                    value = unitOfMeasure ?: "",
                                    onValueChange = {},
                                    label = { Text("Unidade de Medida (opcional)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onGloballyPositioned { coordinates ->
                                            inputHeight = coordinates.size.height.toFloat()
                                        },
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Selecionar unidade de medida",
                                            modifier = Modifier.clickable{showUnitOfMeasureDropdown = true}
                                        )
                                    }
                                )
                                DropdownMenu(
                                    expanded = showUnitOfMeasureDropdown,
                                    onDismissRequest = { showUnitOfMeasureDropdown = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = with(LocalDensity.current) { inputHeight.toDp()    })
                                )   {
                                    listOf("Unidade", "Kg", "Litro", "Metro").forEach { unit ->
                                        DropdownMenuItem(
                                            text = { Text(unit) },
                                            onClick = {
                                                unitOfMeasure = unit
                                                showUnitOfMeasureDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = brand,
                                onValueChange = { brand = it },
                                label = { Text("Marca") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para marca" },
                                singleLine = true
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {showStatusDropdown  = true }
                            ) {
                                OutlinedTextField(
                                    value = status ?: "",
                                    onValueChange = {},
                                    label = { Text("Status (opcional)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showStatusDropdown = true },
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Selecionar status",
                                                    modifier = Modifier.clickable{showStatusDropdown = true}

                                        )
                                    }
                                )
                                DropdownMenu(
                                    expanded = showStatusDropdown,
                                    onDismissRequest = { showStatusDropdown = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = with(LocalDensity.current) { 56.dp.toPx().toDp() })
                                ) {
                                    listOf("Ativo", "Inativo", "Esgotado").forEach { statusOption ->
                                        DropdownMenuItem(
                                            text = { Text(statusOption) },
                                            onClick = {
                                                status = statusOption
                                                showStatusDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = barcode,
                                onValueChange = { barcode = it },
                                label = { Text("Código de Barras (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para código de barras" },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = height,
                                onValueChange = { height = it },
                                label = { Text("Altura (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para altura" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = width,
                                onValueChange = { width = it },
                                label = { Text("Largura (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para largura" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = length,
                                onValueChange = { length = it },
                                label = { Text("Comprimento (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para comprimento" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("Peso (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para peso" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = color,
                                onValueChange = { color = it },
                                label = { Text("Cor (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para cor" },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = size,
                                onValueChange = { size = it },
                                label = { Text("Tamanho (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para tamanho" },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = cost,
                                onValueChange = { cost = it },
                                label = { Text("Custo (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para custo" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = tags,
                                onValueChange = { tags = it },
                                label = { Text("Tags (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para tags" },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = supplier,
                                onValueChange = { supplier = it },
                                label = { Text("Fornecedor (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para fornecedor" },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = fabrication,
                                onValueChange = { fabrication = it },
                                label = { Text("Data de Fabricação (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para data de fabricação" },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = expirationDate,
                                onValueChange = { expirationDate = it },
                                label = { Text("Data de Validade (opcional)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { contentDescription = "Campo para data de validade" },
                                singleLine = true
                            )

                            Spacer(Modifier.height(16.dp))

                            // Upload e preview de imagens
                            Text("Imagens do Produto", style = MaterialTheme.typography.titleMedium)

                            Spacer(Modifier.height(8.dp))



                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(imagens) { uri ->
                                    Image(
                                        painter = rememberAsyncImagePainter(model = uri),
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

                            Spacer(Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (description.isNotBlank() &&
                                        category != null &&
                                        skuCode.isNotBlank() &&
                                        price.isNotBlank() &&
                                        quantity.isNotBlank() &&
                                        brand.isNotBlank() &&
                                        viewModel != null
                                    ) {
                                        scope.launch {
                                            try {
                                                // Verifica se o produto já existe
                                                val productExists = viewModel.findByName(description.trim())
                                                if (productExists) {
                                                    showProductsExists = true
                                                } else {
                                                    viewModel.insertProduct(
                                                        uid = Random.nextInt(0, 100),
                                                        name = description,
                                                        sector = category!!,
                                                        fabrication = fabrication.ifEmpty { null },
                                                        validity = expirationDate.ifEmpty { null },
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
                                                        supplier = supplier.ifEmpty { null }
                                                    )
                                                    description = ""
                                                    category = null
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
                                                    supplier = ""
                                                    fabrication = ""
                                                    expirationDate = ""
                                                    imagens = emptyList()
                                                    showSuccessDialog = true
                                                }
                                            } catch (e: Exception) {
                                                Log.e("ResgistrationProducts", "Erro ao cadastrar produto: ${e.message}")
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Cadastrar Produto",
                                    modifier = Modifier.padding(10.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            Button(
                                onClick = { navController.popBackStack() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text(
                                    text = "Voltar",
                                    modifier = Modifier.padding(10.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
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
                            title = { Text("Produto ja cadastrado") },
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
        }
    }
}

