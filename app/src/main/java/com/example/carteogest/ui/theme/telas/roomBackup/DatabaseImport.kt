package com.example.carteogest.ui.telas.roomBackup

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Process
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHostState  // Adicione para Snackbar (opcional)
import androidx.compose.material3.SnackbarHost
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
import com.example.carteogest.MainActivity
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.ui.theme.ComposeTutorialTheme
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun DataBaseImport(
    openDrawer: () -> Unit

) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope ) }
    var loading by remember { mutableStateOf(false) }
    val backup = LocalRoomBackup.current
    var successImport by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }  // Opcional para feedback de erro

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
                //.backupIsEncrypted(true)
                //.customEncryptPassword("SUA_SENHA_SECRETA")
                .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                .apply {
                    onCompleteListener { success, message, exitCode ->
                        Log.d("Restore", "Sucesso: $success, Mensagem: $message, Código: $exitCode")
                        loading = false
                        if (success) {
                            Log.d("Restore", "Sucesso")
                            successImport = true  // Agora só aqui, após sucesso confirmado
                        } else {
                            Log.e("Restore", "Falha na importação: $message")
                            successImport = false
                            // Opcional: Mostre feedback ao usuário
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
            snackbarHost = { SnackbarHost(snackbarHostState) }  // Opcional para Snackbar
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Importe o seu arquivo de banco de dados",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 16.dp)
                            .semantics { contentDescription = "Importando o arquivo" },
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    if (successImport) {
                        Text(text = "Voltando à tela inicial em alguns segundos")
                    } else {
                        Button(onClick = { importDb() }) {
                            Text("Importar o banco de dados")
                        }
                    }
                }
            }
        }
    }
}