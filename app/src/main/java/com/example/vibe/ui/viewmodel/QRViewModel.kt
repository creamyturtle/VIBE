package com.example.vibe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibe.data.AppContainer
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QRViewModel(private val appContainer: AppContainer) : ViewModel() {

    private val _qrScanResult = MutableStateFlow<String?>(null)
    val qrScanResult = _qrScanResult.asStateFlow()

    fun processQRCode(qrCode: String, tableName: String) {
        viewModelScope.launch {
            val result = try {
                appContainer.qrCodeApi.processQRCode(qrCode, tableName).message
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
            _qrScanResult.value = result
        }
    }
}
