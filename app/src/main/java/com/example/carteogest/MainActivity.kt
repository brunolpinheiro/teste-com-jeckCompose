package com.example.carteogest

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.carteogest.datadb.data_db.AppDatabase
import com.example.carteogest.menu.MainApp
import com.example.carteogest.ui.telas.roomBackup.LocalRoomBackup
import com.example.carteogest.ui.theme.ComposeTutorialTheme
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import android.util.Log

class MainActivity : ComponentActivity() {
    private lateinit var roomBackup: RoomBackup

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "expiration_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Notificações de Validade"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomBackup = RoomBackup(this)
        createNotificationChannel() // Criar canal de notificação ao iniciar
        setContent {
            ComposeTutorialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalRoomBackup provides roomBackup) {
                        // Solicitar permissões usando Compose
                        val permissionLauncher =
                            androidx.activity.compose.rememberLauncherForActivityResult(
                                ActivityResultContracts.RequestMultiplePermissions()
                            ) { permissions ->
                                permissions.forEach { (permission, isGranted) ->
                                    if (isGranted) {
                                        Log.d("MainActivity", "Permissão concedida: $permission")
                                    } else {
                                        Log.d("MainActivity", "Permissão negada: $permission")
                                    }
                                }
                            }

                        LaunchedEffect(Unit) {
                            val missingPermissions = mutableListOf<String>()
                            // Permissões de Bluetooth para Android 12+
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    missingPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
                                }
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.BLUETOOTH_SCAN
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    missingPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
                                }
                            }
                            // Permissão de localização para Android 6-11
                            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            }
                            // Permissão de notificação para Android 13+
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    missingPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                            if (missingPermissions.isNotEmpty()) {
                                permissionLauncher.launch(missingPermissions.toTypedArray())
                            }
                        }
                        MainApp()
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações para produtos próximos ao vencimento"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}