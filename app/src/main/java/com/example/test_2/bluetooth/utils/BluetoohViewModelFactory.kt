package com.example.test_2.bluetooth.utils


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test_2.bluetooth.model.BluetoothViewModel

class BluetoothViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BluetoothViewModel(app) as T
    }
}
