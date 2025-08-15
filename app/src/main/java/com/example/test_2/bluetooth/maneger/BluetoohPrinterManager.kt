package com.example.test_2.bluetooth.maneger



import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import java.io.IOException
import java.io.OutputStream
import java.util.*
import android.Manifest
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.annotation.SuppressLint

@SuppressLint("MissingPermission")
class BluetoothPrinterManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val discoveredDevices = mutableSetOf<BluetoothDevice>()
    private var isReceiverRegistered = false

    var onDeviceFound: ((BluetoothDevice) -> Unit)? = null
    var onDiscoveryFinished: (() -> Unit)? = null

    private val sppUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && discoveredDevices.add(device)) {
                        onDeviceFound?.invoke(device)
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    onDiscoveryFinished?.invoke()
                    unregisterReceiver()
                }
            }
        }
    }

    fun startDiscovery() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
            == PackageManager.PERMISSION_GRANTED) {

       try{
           // Aqui é seguro usar métodos protegidos
           bluetoothAdapter?.startDiscovery()
       }
       catch(e:Exception){
           Log.e("BluetoohPrinterManager", "falha ao tentar descobrir os dispositivos")
       }
        }
        if (!isReceiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
            context.registerReceiver(receiver, filter)
            isReceiverRegistered = true
        }

        discoveredDevices.clear()
        bluetoothAdapter?.startDiscovery()
    }

    fun cancelDiscovery() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
            == PackageManager.PERMISSION_GRANTED) {
            if (bluetoothAdapter?.isDiscovering == true) {
                bluetoothAdapter.cancelDiscovery()
            }
            unregisterReceiver()
        }}

    private fun unregisterReceiver() {
        if (isReceiverRegistered) {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                Log.w("BluetoothPrinterManager", "Receiver not registered.")
            }
            isReceiverRegistered = false
        }
    }

    fun getPairedDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices ?: emptySet()
    }
    fun getBluetoothAdapter(): BluetoothAdapter? {
        return bluetoothAdapter
    }
    fun connectToDevice(device: BluetoothDevice): BluetoothSocket? {
        return try {
            val uuid = device.uuids?.firstOrNull()?.uuid ?: sppUUID
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothAdapter?.cancelDiscovery()
            socket.connect()
            socket
        } catch (e: IOException) {
            Log.e("BluetoothPrinterManager", "Erro ao conectar: ${e.message}", e)
            null
        }
    }

    fun sendPrintData(socket: BluetoothSocket, data: ByteArray): Boolean {
        return try {
            val outputStream: OutputStream = socket.outputStream
            outputStream.write(data)
            outputStream.flush()
            true
        } catch (e: IOException) {
            Log.e("BluetoothPrinterManager", "Erro ao enviar dados: ${e.message}", e)
            false
        }
    }

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    fun enableBluetooth(context: Context) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        enableBtIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(enableBtIntent)
    }
}