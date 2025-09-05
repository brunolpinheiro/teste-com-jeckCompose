package com.example.carteogest.ui.telas.inicio

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import android.Manifest
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.login.UserViewModel
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.menu.TopBarWithLogo
import kotlinx.coroutines.delay
import android.os.Build
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.carteogest.workers.ExpirationCheckWorker
import java.util.concurrent.TimeUnit
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import com.example.carteogest.MainActivity

data class ValidityGroup(
    val productName: String,
    val validity: String,
    val fabrication: String
)

enum class ValidityStatus {
    NEAR_EXPIRY, // Perto de vencer (amarelo)
    EXPIRED,     // Vencido (vermelho)
    VALID        // Válido (cor padrão)
}

fun checkValidityStatus(date: String): ValidityStatus {
    try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val validityDate = dateFormat.parse(date) ?: return ValidityStatus.VALID
        val currentDate = Date()
        val diffInMillis = validityDate.time - currentDate.time
        val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)

        return when {
            diffInDays < 0 -> ValidityStatus.EXPIRED // Já passou da validade
            diffInDays in 0..7 -> ValidityStatus.NEAR_EXPIRY // Perto de vencer (0 a 7 dias)
            else -> ValidityStatus.VALID // Validade normal
        }
    } catch (e: Exception) {
        Log.e("dash", "Falha ao verificar a data ${e.message}")
        return ValidityStatus.VALID
    }
}

@Composable
fun DashboardScreen(
    openDrawer: () -> Unit,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it.productsDao(), it.validityDao()) } }
    val products by viewModel?.products ?: remember { mutableStateOf(emptyList()) }
    val productsWithValidities by viewModel?.produtosComValidades?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList()) }
    var lastNotificationTime by remember { mutableStateOf(0L) }
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val drawerState = rememberDrawerState(DrawerValue.Closed)


    LaunchedEffect(Unit) {
       while(true){
           currentTime = getCurrentTime()
           delay(1000)
       }

    }


    LaunchedEffect(Unit) {
        try {
            viewModel?.getAll()
            Log.d("dash", "produtos carregados")
        } catch (e: Exception) {
            Log.e("dash", "falha ao carregar os produtos: ${e.message}")
        }

    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                userViewModel = userViewModel,
                onMenuClick = { scope.launch { drawerState.open() } },
                openDrawer = openDrawer,
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Gestão de Produtos e Etiquetas",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Data: $currentDate",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.Blue
                            )
                            Text(
                                text = "Hora: $currentTime",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.Blue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            KpiCard(
                title = "Total do Estoque",
                value = "${products.size} un.",
                color = MaterialTheme.colorScheme.primary,
                icon = Icons.Default.Inventory,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(horizontal = 16.dp)
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Validades Cadastradas",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        TextButton(onClick = { navController.navigate("TelaValidades") }) {
                            Text(
                                "Ver Todas",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (productsWithValidities.isEmpty()) {
                        Text(
                            text = "Nenhuma validade cadastrada",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(productsWithValidities) { productWithValidities ->
                                val validities = productWithValidities.validades
                                validities.forEach { validity ->
                                    val validityStatus = checkValidityStatus(validity.validity)
                                    if (validityStatus != ValidityStatus.VALID) {
                                        AnimatedVisibility(
                                            visible = validityStatus == ValidityStatus.NEAR_EXPIRY || validityStatus == ValidityStatus.EXPIRED,
                                            enter = fadeIn(animationSpec = tween(500)),
                                            exit = fadeOut(animationSpec = tween(500))
                                        ) {
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .border(
                                                        1.dp,
                                                        Color.Gray,
                                                        RoundedCornerShape(12.dp)
                                                    ),
                                                colors = CardDefaults.cardColors(containerColor = Color.White)
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = productWithValidities.product.name,
                                                            style = MaterialTheme.typography.titleMedium.copy(
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                        Icon(
                                                            Icons.Default.DateRange,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(vertical = 4.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column {
                                                            Text(
                                                                text = "Validade: ${validity.validity}",
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                color = when (validityStatus) {
                                                                    ValidityStatus.NEAR_EXPIRY -> Color(0xFFFFD700)
                                                                    ValidityStatus.EXPIRED -> Color.Red
                                                                    else -> MaterialTheme.colorScheme.onSurface
                                                                }
                                                            )
                                                            Text(
                                                                text = "Fabricação: ${validity.fabrication}",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                        Text(
                                                            text = when (validityStatus) {
                                                                ValidityStatus.NEAR_EXPIRY -> "Perto de Vencer"
                                                                ValidityStatus.EXPIRED -> "Vencido"
                                                                else -> ""
                                                            },
                                                            style = MaterialTheme.typography.labelMedium.copy(
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            color = when (validityStatus) {
                                                                ValidityStatus.NEAR_EXPIRY -> Color(0xFFFFD700)
                                                                ValidityStatus.EXPIRED -> Color.Red
                                                                else -> MaterialTheme.colorScheme.onSurface
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getCurrentTime(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
        }
    }
}