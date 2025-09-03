package com.example.carteogest.bluetooth.model

import android.app.Application
import android.bluetooth.BluetoothAdapter
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
import android.bluetooth.BluetoothClass
import android.content.Intent
import android.content.IntentFilter
import com.example.carteogest.bluetooth.utils.PermissionHelper
import com.example.carteogest.bluetooth.manager.BluetoothPrinterManager
import android.content.BroadcastReceiver
import java.util.UUID


//@SuppressLint("MissingPermission")

/*
class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val manager = BluetoothPrinterManager(application)
    private val _deviceList = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val deviceList: StateFlow<List<BluetoothDevice>> = _deviceList
    val MY_SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var selectedSocket: BluetoothSocket? = null

    var onToastMessage: ((String) -> Unit)? = null
    val printing = MutableStateFlow(false)
    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val connectedDevice: StateFlow<BluetoothDevice?> = _connectedDevice
    fun isConnected(): Boolean {
        val device = _connectedDevice.value ?: return false
        return device.bluetoothClass?.majorDeviceClass == BluetoothClass.Device.Major.IMAGING
    }
    suspend fun connectToDevice(device: BluetoothDevice): Boolean {
        return try {
            // Tenta abrir socket SPP
            val socket = device.createRfcommSocketToServiceRecord(MY_SPP_UUID)
            socket.connect() // Bloqueante, confirma conexão
            _connectedDevice.value = device
            true
        } catch (e: Exception) {
            _connectedDevice.value = null
            false
        }
    }

    fun setConnectedDevice(device: BluetoothDevice) {
        _connectedDevice.value = device
    }

    fun requestPermissions(onDenied: () -> Unit, onGranted: () -> Unit) {
        val context = getApplication<Application>()
        val permissionHelper = PermissionHelper(context)
        if (permissionHelper.hasBluetoothPermissions()) {
            onGranted()
        } else {
            permissionHelper.requestPermissions(onDenied = onDenied, onGranted = onGranted)
        }
    }

    fun registerDisconnectReceiver(context: Context) {
        val filter = IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device == _connectedDevice.value) {
                    _connectedDevice.value = null
                }
            }
        }, filter)
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

    /*fun printLabel(text1: String, text2: String, text3: String, text4: String,text5: String) {
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
    }*/
    fun printLabel(
        text1: String, text2: String, text3: String, text4: String, text5: Int
    ) {
        viewModelScope.launch {
            try {
                printing.value = true
                val outputStream: OutputStream? = selectedSocket?.outputStream

                val comandos = StringBuilder()
                comandos.appendLine("! 0 200 200 250 $text5")
                comandos.appendLine("PAGE-WIDTH 350")
                comandos.appendLine("SETFF 0")

                var y = 30

                // 1️⃣ Primeira linha: primeira palavra maior + linha horizontal
                //val parts = text1.split(" ", limit = 2)
                //val firstWord = parts[0]
                //val rest = if (parts.size > 1) parts[1] else ""

                comandos.appendLine("""TEXT 4 0 10 $y $text1""") // primeira palavra grande
                y += 40
                comandos.appendLine("""LINE 10 $y 350 $y 2""")        // linha horizontal
                y += 10
                /*if (rest.isNotEmpty()) {    
                    comandos.appendLine("""TEXT 2 0 10 $y $rest""") // resto da primeira linha
                    y += 30
                }*/

                // 2️⃣ Demais linhas: fonte menor, sem linha horizontal
                val otherLines = listOf(
                    "Fab.:     $text3",
                    "Validade:     $text4",
                    "Responsavel:    $text2"
                )

                for (line in otherLines) {
                    comandos.appendLine("""TEXT 2 0 10 $y $line""")
                    y += 30
                }

                comandos.appendLine("PRINT")

                outputStream?.write(comandos.toString().toByteArray(Charsets.UTF_8))
                outputStream?.flush()
                onToastMessage?.invoke("Etiqueta enviada.")

            } catch (e: Exception) {
                onToastMessage?.invoke("Erro ao imprimir: ${e.message}")
            } finally {
                printing.value = false
            }
        }
    }

}
*/


@Suppress("DEPRECATION")
class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val manager = BluetoothPrinterManager(application)
    private var selectedSocket: BluetoothSocket? = null

    private val _deviceList = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val deviceList: StateFlow<List<BluetoothDevice>> = _deviceList

    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val connectedDevice: StateFlow<BluetoothDevice?> = _connectedDevice

    val printing = MutableStateFlow(false)
    var onToastMessage: ((String) -> Unit)? = null

    val MY_SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    /** Verifica se há conexão ativa com uma impressora */
    fun isConnected(): Boolean {
        val device = _connectedDevice.value ?: return false
        val context = getApplication<Application>()

        // Verifica permissão de forma compatível com todas as versões
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

        return try {
            hasPermission &&
                    device.bluetoothClass?.majorDeviceClass == BluetoothClass.Device.Major.IMAGING &&
                    selectedSocket?.isConnected == true
        } catch (e: SecurityException) {
            onToastMessage?.invoke("Permissão Bluetooth necessária")
            false
        }
    }

    /** Conecta a um dispositivo Bluetooth e retorna true se conectado */
    suspend fun connectToDevice(device: BluetoothDevice): Boolean {
        return try {
            val socket = device.createRfcommSocketToServiceRecord(MY_SPP_UUID)
            socket.connect()
            selectedSocket = socket
            _connectedDevice.value = device
            true
        } catch (e: Exception) {
            Log.e("Bluetooth", "Erro ao conectar: ${e.message}")
            _connectedDevice.value = null
            selectedSocket = null
            false
        }
    }

    /** Conecta usando endereço */
    fun connectToDevice(address: String) {
        try {
            val device = manager.getBluetoothAdapter()?.getRemoteDevice(address)
            if (device != null) {
                viewModelScope.launch {
                    val success = connectToDevice(device)
                    onToastMessage?.invoke(
                        if (success) "Conectado a ${device.name}" else "Falha ao conectar"
                    )
                }
            } else {
                onToastMessage?.invoke("Dispositivo não encontrado")
            }
        } catch (e: SecurityException) {
            onToastMessage?.invoke("Permissão negada para conectar: ${e.message}")
        }
    }

    /** Solicita permissões Bluetooth */
    fun requestPermissions(onDenied: () -> Unit, onGranted: () -> Unit) {
        val context = getApplication<Application>()
        val permissionHelper = PermissionHelper(context)
        if (permissionHelper.hasBluetoothPermissions()) {
            onGranted()
        } else {
            permissionHelper.requestPermissions(onDenied, onGranted)
        }
    }

    /** Registra BroadcastReceiver para desconexão */
    fun registerDisconnectReceiver(context: Context) {
        val filter = IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device == _connectedDevice.value) {
                    _connectedDevice.value = null
                    selectedSocket = null
                    onToastMessage?.invoke("Impressora desconectada")
                }
            }
        }, filter)
    }

    /** Habilita Bluetooth se necessário */
    fun enableBluetooth() {
        val context = getApplication<Application>()
        if (!manager.isBluetoothEnabled()) manager.enableBluetooth(context)
    }

    /** Inicia descoberta de dispositivos */
    fun startDiscovery() {
        val foundDevices = mutableListOf<BluetoothDevice>()
        manager.onDeviceFound = { device ->
            if (!foundDevices.contains(device)) {
                foundDevices.add(device)
                _deviceList.value = foundDevices.toList()
            }
        }
        manager.onDiscoveryFinished = { onToastMessage?.invoke("Busca finalizada") }
        manager.startDiscovery()
    }

    /** Verifica se há permissões Bluetooth */
    fun hasBluetoothPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    /** Imprime etiqueta se estiver conectado */
    fun printLabel(
        text1: String,
        text2: String,
        text3: String,
        text4: String,
        text5: Int
    ) {
        if (!isConnected()) {
            onToastMessage?.invoke("Impressora não conectada")
            return
        }

        viewModelScope.launch {
            try {
                printing.value = true
                val outputStream: OutputStream? = selectedSocket?.outputStream
                val builder = StringBuilder()

                builder.appendLine("! 0 200 200 250 $text5")
                builder.appendLine("PAGE-WIDTH 350")
                builder.appendLine("SETFF 0")

                var y = 30
                builder.appendLine("""TEXT 4 0 10 $y $text1""")
                y += 40
                builder.appendLine("""LINE 10 $y 350 $y 2""")
                y += 10

                listOf(
                    "Fab.:     $text3",
                    "Validade: $text4",
                    "Responsavel: $text2"
                ).forEach { line ->
                    builder.appendLine("""TEXT 2 0 10 $y $line""")
                    y += 30
                }

                builder.appendLine("PRINT")
                outputStream?.write(builder.toString().toByteArray(Charsets.UTF_8))
                outputStream?.flush()

                onToastMessage?.invoke("Etiqueta enviada")
            } catch (e: Exception) {
                onToastMessage?.invoke("Erro ao imprimir: ${e.message}")
            } finally {
                printing.value = false
            }
        }
    }
}
