package com.example.test_2.bluetooth.utils




import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val context: Context) {

    fun hasBluetoothPermissions(): Boolean {
        return getRequiredPermissions().all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(onDenied: () -> Unit, onGranted: () -> Unit) {
        if (context !is Activity) return onDenied()

        val permissions = getRequiredPermissions()
        ActivityCompat.requestPermissions(context, permissions, 1001)

        // Você deve tratar o resultado manualmente no Activity, caso deseje um controle refinado
        onGranted()
    }

    private fun getRequiredPermissions(): Array<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            else -> arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }
    }

}
