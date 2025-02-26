package com.example.vibe.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.network.RSVPItem
import com.example.vibe.ui.viewmodel.CheckInViewModel
import com.example.vibe.ui.viewmodel.QRViewModel
import com.example.vibe.utils.CameraPermissionRequest
import com.example.vibe.utils.QRScanner
import androidx.compose.foundation.lazy.items


@Composable
fun CheckInScreen(
    navController: NavController,
    checkInViewModel: CheckInViewModel,
    qrViewModel: QRViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val rsvpList by remember { derivedStateOf { checkInViewModel.rsvpList } }

    val isLoading = checkInViewModel.isLoading
    val errorMessage = checkInViewModel.errorMessage
    val successMessage = checkInViewModel.successMessage
    var scanningQR by remember { mutableStateOf(false) }



    val scannedQRCode by qrViewModel.qrScanResult.collectAsState()

    // ✅ Automatically process scanned QR Code
    LaunchedEffect(scannedQRCode) {
        scannedQRCode?.let { qrCode ->
            checkInViewModel.markUserCheckedIn(qrCode)
            qrViewModel.updateScannedQRCode("") // Reset after processing
        }
    }

    // ✅ Show Toast when check-in is successful
    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            checkInViewModel.clearSuccessMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 104.dp)
    ) {
        Row() {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(24.dp, 8.dp, 16.dp, 8.dp)
                    .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Check-In Guests",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            !errorMessage.isNullOrEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage!!)
                }
            }
            rsvpList.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No guests to check in.")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    items(rsvpList) { rsvp ->
                        CheckInCard(
                            rsvpItem = rsvp,
                            onScanClick = { scanningQR = true },
                            isCheckedIn = rsvp.enteredparty == 1
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(120.dp))
                    }
                }
            }
        }
    }

    // ✅ Show QR Scanner when scanning is enabled
    if (scanningQR) {
        QRScannerScreen { scannedQRCode2 ->
            qrViewModel.updateScannedQRCode(scannedQRCode2)
            scanningQR = false
        }
    }


}


@Composable
fun CheckInCard(
    rsvpItem: RSVPItem,
    onScanClick: () -> Unit,
    isCheckedIn: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(24.dp))

            Text(text = rsvpItem.partyName, style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))

            Text(text = "Guest: ${rsvpItem.name}", fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text(text = "Age: ${rsvpItem.age} | Gender: ${rsvpItem.gender}", fontSize = 16.sp)

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isCheckedIn) {
                    Text("Checked-In ✅", color = Color.Green, fontSize = 18.sp)
                } else {
                    Button(
                        onClick = onScanClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Scan QR Code", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun QRScannerScreen(onQRCodeScanned: (String) -> Unit) {
    CameraPermissionRequest {
        QRScanner { scannedQRCode ->
            onQRCodeScanned(scannedQRCode)
        }
    }
}

