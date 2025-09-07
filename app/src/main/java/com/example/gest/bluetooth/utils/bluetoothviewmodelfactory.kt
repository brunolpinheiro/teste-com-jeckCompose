package com.example.gest.bluetooth.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gest.bluetooth.model.BluetoothViewModel

class BluetoothViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BluetoothViewModel(app) as T
    }
}
