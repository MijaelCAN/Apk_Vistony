package com.vistony.app.clean.presentation.viewmodels

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.vistony.app.clean.core.utils.ObservableObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel() {
    private val _scanData = MutableStateFlow<String?>(null)
    val scanData: StateFlow<String?> = _scanData.asStateFlow()

    init {
        observeScanData()
    }

    private fun observeScanData() {
        ObservableObject.instance.addObserver { _, data ->
            if (data is Intent) {
                val scannedData = data.getStringExtra("SCAN_DATA")
                Log.e("REOS", "ScanViewModel - Dato recibido: $scannedData")
                _scanData.value = scannedData
            }
        }
    }

    fun clearScanData() {
        _scanData.value = null
    }
}