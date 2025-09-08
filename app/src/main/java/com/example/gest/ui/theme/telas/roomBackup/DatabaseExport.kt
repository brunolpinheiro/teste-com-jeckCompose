    package com.example.gest.ui.telas.roomBackup

    import android.content.Intent
    import android.util.Log
    import androidx.compose.animation.AnimatedVisibility
    import androidx.compose.animation.core.tween
    import com.example.gest.datadb.data_db.login.UserViewModel
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
    import androidx.compose.material3.DrawerValue
    import androidx.compose.material3.rememberDrawerState
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.semantics.contentDescription
    import androidx.compose.ui.semantics.semantics
    import androidx.navigation.NavController
    import com.example.gest.MainActivity
    import com.example.gest.datadb.data_db.AppDatabase
    import com.example.gest.menu.TopBarWithLogo
    import com.example.gest.ui.theme.ComposeTutorialTheme
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.launch


    @Composable
    fun DataBaseExport(
        openDrawer: () -> Unit,
        userViewModel: UserViewModel,
        navController: NavController

        ) {
        val context = LocalContext.current
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val database = remember { AppDatabase.getDatabase(context, scope) }
        var loading by remember { mutableStateOf(false) }
        var backup = LocalRoomBackup.current

        LaunchedEffect(loading) {
            if (loading) {
                delay(2000L) // Atraso de 2 segundos
                loading = false
            }
        }

        fun exportDb(): Boolean {
            loading = true
            try {
                backup
                    .database(database!!)
                    .enableLogDebug(true)
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
            } catch (e: Exception) {
                Log.e("databaseExport", "falha ao fazer backup ${e}")
                return false
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
                }
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
                            // Opcional: Adicionar ícone aqui
                            // Image(
                            //     painter = painterResource(id = R.drawable.icone),
                            //     contentDescription = "Ícone de exportação de banco de dados",
                            //     modifier = Modifier.size(100.dp)
                            // )
                            Text(
                                text = "Exporte o seu arquivo de banco de dados",
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(all = 16.dp)
                            )
                            Text(
                                text = "Salve seus dados em um arquivo seguro",
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
                                        .semantics { contentDescription = "mandando o arquivo" },
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (!loading) {
                                Button(
                                    onClick = { exportDb() },
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(top = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Exportar",
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