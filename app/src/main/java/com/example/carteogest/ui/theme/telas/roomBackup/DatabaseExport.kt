package com.example.carteogest.ui.telas.roomBackup

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.carteogest.MainActivity
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.ui.theme.ComposeTutorialTheme
import kotlinx.coroutines.delay



@Composable
fun DataBaseExport(
    openDrawer: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    var loading  by remember { mutableStateOf(false) }
    var backup = LocalRoomBackup.current


    LaunchedEffect(loading) {

        if (loading) {
            delay(2000L) // Atraso de 2 segundos
            loading = false

        }
    }

    fun exportDb(): Boolean {
        loading = true
        try{
            backup
                .database(database!!)
                .enableLogDebug(true)
                //     .backupIsEncrypted(true)
                //    .customEncryptPassword("SUA_SENHA_SECRETA")
                .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                .maxFileCount(5)
                .apply {
                    onCompleteListener { success, message, exitCode ->
                        Log.d("Backup", "Sucesso: $success, Mensagem: $message, Código: $exitCode")
                        onCompleteListener { success, message, exitCode ->
                            loading = false // Pare o loading aqui
                            if (success) {
                                // Reiniciar o app: Crie Intent para MainActivity com flags para limpar stack
                                val restartIntent = Intent(context, MainActivity::class.java).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                }
                                context.startActivity(restartIntent)
                            }

                        }
        }
                }.backup()
            return true
            }
        catch (e: Exception){
            Log.e("databaseExport", "falha ao fazer backup ${e}")
            return  false
        }

    }

    ComposeTutorialTheme {
        Scaffold(
            containerColor = Color.White
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
                    text = "Exporte o seu arquivo de banco de dados",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
               if(loading){
                   CircularProgressIndicator(
                       modifier = Modifier
                           .size(48.dp)
                           .padding(bottom = 16.dp)
                           .semantics { contentDescription = "mandando o arquivo" },
                       color = MaterialTheme.colorScheme.primary
                   )
               }
                else{
                   Button(onClick = { exportDb() }) {
                       Text("Exportar")
                   }
                }
            }
        }
    }}
