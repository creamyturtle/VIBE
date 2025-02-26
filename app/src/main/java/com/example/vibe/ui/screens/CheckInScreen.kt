package com.example.vibe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.ui.viewmodel.QRViewModel
import com.example.vibe.utils.CameraPermissionRequest
import com.example.vibe.utils.QRScanner


@Composable
fun CheckInScreen(
    navController: NavController,
    qrViewModel: QRViewModel,
    onBack: () -> Unit
) {

    val qrScanResult by qrViewModel.qrScanResult.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CameraPermissionRequest {


            QRScanner { scannedQRCode ->
                val tableName = "your_table_name_here" // Replace with actual table logic
                qrViewModel.processQRCode(scannedQRCode, tableName)
            }

            qrScanResult?.let { result ->
                Text(text = result, fontSize = 18.sp)
            }



        }
    }

}

