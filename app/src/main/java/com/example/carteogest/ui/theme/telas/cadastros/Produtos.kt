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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.navigation.NavController
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.launch


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
    categorias: List<String> = listOf("Eletrônicos", "Roupas", "Alimentos", "Outros"),
    unidadesMedida: List<String> = listOf("kg", "litro", "unidade", "pacote"),
    statusOptions: List<String> = listOf("Ativo", "Inativo", "Rascunho"),
    onSalvar: (ProdutoCadastro) -> Unit = {},
    openDrawer: () -> Unit,
) {
    //val de menu
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // Estados dos campos obrigatórios
    var nome by remember { mutableStateOf("") }
    var codigoSKU by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf<String?>(null) }
    var preco by remember { mutableStateOf("") }
    var precoPromocional by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var unidadeMedida by remember { mutableStateOf<String?>(null) }
    var marca by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }

    // Campos opcionais
    var codigoBarras by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var largura by remember { mutableStateOf("") }
    var comprimento by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var cor by remember { mutableStateOf("") }
    var tamanho by remember { mutableStateOf("") }
    var custo by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var fornecedor by remember { mutableStateOf("") }
    var dataValidade by remember { mutableStateOf("") } // Simplificado como texto (ex: "dd/MM/yyyy")

    // Imagens
    val context = LocalContext.current
    var imagens by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris != null) {
            imagens = imagens + uris
        }
    }

    fun validarCamposObrigatorios(): Boolean {
        return nome.isNotBlank() && preco.toDoubleOrNull() != null
                && categoria != null && status != null
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
            Text("Cadastro de Produto", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(imagens) { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context).data(uri).size(Size.ORIGINAL).build()
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
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Produto*") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = codigoSKU,
                onValueChange = { codigoSKU = it },
                label = { Text("Código/SKU") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 5
            )

            Spacer(Modifier.height(8.dp))

            // Dropdown Categoria
            dropdownMenuField(
                label = "Categoria*",
                options = categorias,
                selectedOption = categoria,
                onOptionSelected = { categoria = it }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = preco,
                onValueChange = { preco = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Preço*") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = precoPromocional,
                onValueChange = { precoPromocional = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Preço Promocional (Opcional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = quantidade,
                onValueChange = { quantidade = it.filter { c -> c.isDigit() } },
                label = { Text("Estoque / Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            dropdownMenuField(
                label = "Unidade de Medida",
                options = unidadesMedida,
                selectedOption = unidadeMedida,
                onOptionSelected = { unidadeMedida = it }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca / Fabricante") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            dropdownMenuField(
                label = "Status*",
                options = statusOptions,
                selectedOption = status,
                onOptionSelected = { status = it }
            )

            Spacer(Modifier.height(12.dp))
            Text("Campos Opcionais", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = codigoBarras,
                onValueChange = { codigoBarras = it },
                label = { Text("Código de Barras") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = altura,
                    onValueChange = { altura = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Altura (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = largura,
                    onValueChange = { largura = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Largura (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = comprimento,
                    onValueChange = { comprimento = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Comprimento (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = cor,
                    onValueChange = { cor = it },
                    label = { Text("Cor") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = tamanho,
                    onValueChange = { tamanho = it },
                    label = { Text("Tamanho") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = custo,
                onValueChange = { custo = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Custo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags / Palavras-chave (separadas por vírgula)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = fornecedor,
                onValueChange = { fornecedor = it },
                label = { Text("Fornecedor") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = dataValidade,
                onValueChange = { dataValidade = it },
                label = { Text("Data de Validade (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Upload e preview de imagens
            Text("Imagens do Produto", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))


            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validarCamposObrigatorios()) {
                        val produto = ProdutoCadastro(
                            nome = nome,
                            codigoSKU = codigoSKU,
                            descricao = descricao,
                            categoria = categoria ?: "",
                            preco = preco.toDoubleOrNull() ?: 0.0,
                            precoPromocional = precoPromocional.toDoubleOrNull(),
                            quantidade = quantidade.toIntOrNull() ?: 0,
                            unidadeMedida = unidadeMedida,
                            marca = marca,
                            status = status ?: "",
                            codigoBarras = codigoBarras,
                            altura = altura.toDoubleOrNull(),
                            largura = largura.toDoubleOrNull(),
                            comprimento = comprimento.toDoubleOrNull(),
                            peso = peso.toDoubleOrNull(),
                            cor = cor,
                            tamanho = tamanho,
                            custo = custo.toDoubleOrNull(),
                            tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            fornecedor = fornecedor,
                            dataValidade = dataValidade,
                            imagens = imagens
                        )
                        onSalvar(produto)
                    } else {
                        // Tratar erro de validação
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Produto")
            }

            Spacer(Modifier.height(16.dp))
        }
    }

// Modelo de dados para cadastro
data class ProdutoCadastro(
    val nome: String,
    val codigoSKU: String,
    val descricao: String,
    val categoria: String,
    val preco: Double,
    val precoPromocional: Double? = null,
    val quantidade: Int,
    val unidadeMedida: String?,
    val marca: String,
    val status: String,
    val codigoBarras: String,
    val altura: Double?,
    val largura: Double?,
    val comprimento: Double?,
    val peso: Double?,
    val cor: String,
    val tamanho: String,
    val custo: Double?,
    val tags: List<String>,
    val fornecedor: String,
    val dataValidade: String,
    val imagens: List<Uri>
)
