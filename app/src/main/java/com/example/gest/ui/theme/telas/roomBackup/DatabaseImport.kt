package com.example.gest.ui.telas.roomBackup

import android.content.Intent
import android.os.Process
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gest.MainActivity
import com.example.gest.datadb.data_db.AppDatabase
import com.example.gest.datadb.data_db.login.UserViewModel
import com.example.gest.menu.TopBarWithLogo
import com.example.gest.ui.theme.ComposeTutorialTheme
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DataBaseImport(
    openDrawer: () -> Unit,
    userViewModel: UserViewModel,
    navController: NavController)
{
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    var loading by remember { mutableStateOf(false) }
    val backup = LocalRoomBackup.current
    var successImport by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    suspend fun restartApp(launchIntent: Intent) {
        delay(2000L)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
        Process.killProcess(Process.myPid())
    }

    fun importDb() {
        loading = true
        try {
            backup
                .database(database!!)
                .enableLogDebug(true)
                .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                .apply {
                    onCompleteListener { success, message, exitCode ->
                        Log.d("Restore", "Sucesso: $success, Mensagem: $message, Código: $exitCode")
                        loading = false
                        if (success) {
                            Log.d("Restore", "Sucesso")
                            successImport = true
                        } else {
                            Log.e("Restore", "Falha na importação: $message")
                            successImport = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Falha na importação: $message")
                            }
                        }
                    }
                }
                .restore()
        } catch (e: Exception) {
            Log.e("Restore", "Falha na configuração do import: $e")
            loading = false
            successImport = false
            scope.launch {
                snackbarHostState.showSnackbar("Erro na configuração: ${e.message}")
            }
        }
    }

    LaunchedEffect(successImport) {
        if (successImport) {
            restartApp(Intent(context, MainActivity::class.java))
        }
    }

    ComposeTutorialTheme {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopBarWithLogo(
                    userViewModel = userViewModel,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    openDrawer = openDrawer,
                    navController = navController
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Importe o seu arquivo de banco de dados",
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(all = 16.dp)
                        )
                        Text(
                            text = "Carregue seus dados de um arquivo salvo",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        AnimatedVisibility(
                            visible = loading,
                            enter = fadeIn(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(300))
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(bottom = 16.dp)
                                    .semantics { contentDescription = "Importando o arquivo" },
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (!loading) {
                            if (successImport) {
                                Text(
                                    text = "Voltando à tela inicial em alguns segundos",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            } else {
                                Button(
                                    onClick = { importDb() },
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(top = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Importar",
                                        fontSize = 16.sp
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