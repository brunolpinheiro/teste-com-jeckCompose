package com.example.test_2.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.core.content.FileProvider
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.test_2.data_db.AppDatabase
import com.example.test_2.data_db.products.ProductViewModel
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.io.File

@Composable
fun DataBaseExport() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val viewModel = remember { database?.let { ProductViewModel(it) } } // Não usado, mas mantido

    fun exportDb() {
        val backup = RoomBackup(context)
        backup
            .database(database!!)
            .enableLogDebug(true)
       //     .backupIsEncrypted(true)
        //    .customEncryptPassword("SUA_SENHA_SECRETA")
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_EXTERNAL)
            .maxFileCount(5)
            .apply {
                onCompleteListener { success, message, exitCode ->
                    Log.d("Backup", "Sucesso: $success, Mensagem: $message, Código: $exitCode")
                    if (success) {
                        // Calcular o diretório de backup
                        val backupDir = context.getExternalFilesDir("backup")
                        // Pegar a lista de arquivos e ordenar pelo mais recente
                        val files = backupDir?.listFiles()?.filter { it.isFile }?.sortedByDescending { it.lastModified() }
                        val latestBackupFile = files?.firstOrNull()
                        if (latestBackupFile != null) {
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                latestBackupFile
                            )
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/octet-stream"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Compartilhar backup"))
                        } else {
                            Log.d("Backup", "Nenhum arquivo de backup encontrado")
                        }
                    }
                }
            }
            .backup()
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
                Button(onClick = { exportDb() }) {
                    Text("Exportar")
                }
            }
        }
    }
}