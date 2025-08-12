package com.example.carteogest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.carteogest.ui.theme.ComposeTutorialTheme
import android.content.pm.PackageManager
import android.Manifest
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.carteogest.menu.MainApp





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
        checkAndRequestBluetoothPermissions()

    }
    private fun checkAndRequestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 ou superior
            val missingPermissions = mutableListOf<String>()

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            }

            if (missingPermissions.isNotEmpty()) {
                requestPermissions(missingPermissions.toTypedArray(), 100)
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6 a 11
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }
        }
    }
}

