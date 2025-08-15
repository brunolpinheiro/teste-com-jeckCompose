package com.example.test_2.bluetooth.model


import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.OutputStream
import android.content.pm.PackageManager
import android.content.Context
import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import android.util.Log
import android.Manifest
import com.example.test_2.bluetooth.utils.PermissionHelper
import com.example.test_2.bluetooth.maneger.BluetoothPrinterManager




@SuppressLint("MissingPermission")


class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val manager = BluetoothPrinterManager(application)
    private val _deviceList = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val deviceList: StateFlow<List<BluetoothDevice>> = _deviceList

    private var selectedSocket: BluetoothSocket? = null

    var onToastMessage: ((String) -> Unit)? = null
    val printing = MutableStateFlow(false)

    fun requestPermissions(onDenied: () -> Unit, onGranted: () -> Unit) {
        val context = getApplication<Application>()
        val permissionHelper = PermissionHelper(context)
        if (permissionHelper.hasBluetoothPermissions()) {
            onGranted()
        } else {
            permissionHelper.requestPermissions(onDenied = onDenied, onGranted = onGranted)
        }
    }
    fun hasBluetoothPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    fun enableBluetooth() {
        val context = getApplication<Application>()
        if (!manager.isBluetoothEnabled()) {
            manager.enableBluetooth(context)
        }
    }

    fun startDiscovery() {
        val foundDevices = mutableListOf<BluetoothDevice>()
        manager.onDeviceFound = { device ->
            if (!foundDevices.contains(device)) {
                foundDevices.add(device)
                _deviceList.value = foundDevices.toList()
            }
        }
        manager.onDiscoveryFinished = {
            onToastMessage?.invoke("Busca finalizada.")
        }
        manager.startDiscovery()
    }

    /*fun connectToDevice(info: String) {
        val address = info.split("\n").getOrNull(1) ?: return
        val device = manager.getPairedDevices().find { it.address == address } ?: return
        viewModelScope.launch {
            selectedSocket = manager.connectToDevice(device)
            onToastMessage?.invoke("Conectado a ${device.name}")
        }
    }*/
    fun connectToDevice(address: String) {
        try {
            val adapter = manager.getBluetoothAdapter()
            val device = adapter?.getRemoteDevice(address)
            if (device != null) {
                viewModelScope.launch {
                    selectedSocket = manager.connectToDevice(device)
                    onToastMessage?.invoke("Conectado a ${device.name}")
                }
            } else {
                onToastMessage?.invoke("Dispositivo não encontrado.")
            }
        } catch (e: SecurityException) {
            Log.e("Bluetooth", "Permissão negada ao conectar: ${e.message}")
            onToastMessage?.invoke("Permissão negada para conectar.")
        }
    }

    fun printLabel(text1: String, text2: String, text3: String, text4: String,text5: String) {
        viewModelScope.launch {
            try {
                printing.value = true
                val outputStream: OutputStream? = selectedSocket?.outputStream
                val textoMultilinha = listOf(
                    "Produto: $text1.",
                    "Responsavel: $text2",
                    "Fab.: $text3",
                    "Validade: $text4"
                )
                /*val comandosTexto = textoMultilinha.mapIndexed { i, linha ->
                    val y = 10 + (i * 30) // cada linha 30px abaixo da anterior
                    """TEXT 15,$y,"5",0,1,1,"$linha""""
                }.joinToString("\n")*/
                val comandosTexto = textoMultilinha.mapIndexed { i, linha ->
                    val y = 20 + (i * 40) // Mais espaçamento e posição inicial mais baixa
                    """TEXT 4 0 0 $y $linha"""
                }.joinToString("\n")

                val label = """
                     ^RESET
                """.trimIndent()

                outputStream?.write(label.toByteArray(Charsets.UTF_8))
                outputStream?.flush()
                onToastMessage?.invoke("Etiqueta enviada.")
            } catch (e: Exception) {
                onToastMessage?.invoke("Erro ao imprimir.")
            } finally {
                printing.value = false
            }
        }
    }
}
