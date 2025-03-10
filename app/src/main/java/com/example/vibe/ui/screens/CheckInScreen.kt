package com.example.vibe.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.network.RSVPItem
import com.example.vibe.ui.viewmodel.CheckInViewModel
import com.example.vibe.ui.viewmodel.QRViewModel
import com.example.vibe.utils.CameraPermissionRequest
import com.example.vibe.utils.QRScanner


@Composable
fun CheckInScreen(
    checkInViewModel: CheckInViewModel,
    qrViewModel: QRViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val rsvpList by checkInViewModel.rsvpList.collectAsState()

    val isLoading = checkInViewModel.isLoading
    val errorMessage = checkInViewModel.errorMessage
    val successMessage = checkInViewModel.successMessage
    val isScanning by qrViewModel.isScanning.collectAsState() // ✅ Use ViewModel's scanning state
    val scannedQRCode by qrViewModel.qrScanResult.collectAsState()
    var isProcessing by remember { mutableStateOf(false) }

    // ✅ Handle scanned QR codes
    LaunchedEffect(scannedQRCode) {
        scannedQRCode?.let { qrCode ->
            if (qrCode.isNotBlank()) {
                qrViewModel.stopScanning() // ✅ Close scanner
                checkInViewModel.markUserCheckedIn(qrCode) {
                    qrViewModel.stopScanning()
                }
                qrViewModel.updateScannedQRCode("") // ✅ Reset QR Code state
            }
        }
    }

    // ✅ Fetch RSVPs when screen loads
    LaunchedEffect(Unit) {
        checkInViewModel.fetchApprovedRSVPs()
    }

    // ✅ Show success toast
    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            checkInViewModel.clearSuccessMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 104.dp)
    ) {
        Row {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(24.dp, 8.dp, 16.dp, 8.dp)
                    .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
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
                text = stringResource(R.string.check_in_guests),
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
            !errorMessage.isNullOrEmpty() && rsvpList.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage)
                }
            }
            rsvpList.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_guests_to_check_in))
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    val sortedList = rsvpList.sortedBy { it.enteredparty } // ✅ Show enteredparty = 0 first

                    items(sortedList) { rsvp ->
                        CheckInCard(
                            rsvpItem = rsvp,
                            onScanClick = { qrViewModel.startScanning() }, // ✅ Use ViewModel to start scanning
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

    // ✅ Request Camera Permission Before Scanning
    if (isScanning) {
        CameraPermissionRequest(
            onPermissionGranted = {
                qrViewModel.startScanning() // ✅ Keep scanning enabled after permission
            }
        )
    }

    // ✅ Show QR Scanner when scanning is enabled
    if (isScanning) {
        QRScannerScreen(
            onQRCodeScanned = { scannedQRCode ->
                if (!isProcessing && scannedQRCode.isNotBlank()) {
                    isProcessing = true
                    qrViewModel.stopScanning() // ✅ Close scanner immediately

                    qrViewModel.updateScannedQRCode(scannedQRCode)

                    checkInViewModel.markUserCheckedIn(scannedQRCode) {
                        qrViewModel.stopScanning() // ✅ Ensure scanner is closed
                        isProcessing = false // ✅ Reset flag after processing
                    }
                }
            },
            onBack = { qrViewModel.stopScanning() } // ✅ Close scanner when back button is pressed
        )
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
            .padding(vertical = 8.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            Row {


                Column(Modifier.width(184.dp)) {

                    Text(text = rsvpItem.name, style = MaterialTheme.typography.titleMedium, fontSize = 24.sp)
                    Spacer(Modifier.height(16.dp))

                    Text(text = stringResource(R.string.age_gender2, rsvpItem.age, rsvpItem.gender), fontSize = 16.sp)
                    Spacer(Modifier.height(16.dp))



                    Text(text = stringResource(R.string.add_guests2, rsvpItem.guestCount), fontSize = 16.sp)

                    Spacer(Modifier.height(8.dp))

                    Text(text = "${rsvpItem.bringing}", fontSize = 16.sp)

                    Spacer(Modifier.height(24.dp))

                    Text(text = rsvpItem.partyName, fontSize = 16.sp, fontWeight = FontWeight.W400)



                }

                //Spacer(Modifier.weight(1f))


                Column(
                    horizontalAlignment = Alignment.End
                ) {


                    val baseUrl = "https://www.vibesocial.org/" // ✅ Base URL

                    Row(horizontalArrangement = Arrangement.End) {
                        AsyncImage(
                            model = baseUrl + rsvpItem.usersphoto, // ✅ Load the full image URL
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop // ✅ Ensures proper cropping
                        )
                        Spacer(Modifier.width(16.dp))
                    }


                    Spacer(Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (isCheckedIn) {
                            Text(stringResource(R.string.checked_in), color = Color.Green, fontSize = 18.sp)
                        } else {
                            Button(
                                onClick = onScanClick,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(stringResource(R.string.scan_qr_code), color = Color.White)
                            }
                        }
                    }


                }






            }




        }
    }
}

@Composable
fun QRScannerScreen(
    onQRCodeScanned: (String) -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Ensure full black background for contrast
    ) {
        QRScanner(onQRCodeScanned)



        // Scanner Overlay with Cutout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Dim background
        ) {
            // Transparent cutout area for QR code
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cornerRadius = 20.dp.toPx()
                val cutoutSize = size.width * 0.7f // 70% of screen width
                val cutoutOffsetX = (size.width - cutoutSize) / 2
                val cutoutOffsetY = (size.height - cutoutSize) / 2

                drawRect(
                    color = Color.Transparent,
                    topLeft = Offset(cutoutOffsetX + 4, cutoutOffsetY + 6),
                    size = Size(cutoutSize - 8, cutoutSize - 12),
                    blendMode = BlendMode.Clear // Makes the area transparent
                )

                // Draw a border around the scanning area
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(cutoutOffsetX, cutoutOffsetY),
                    size = Size(cutoutSize, cutoutSize),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(width = 8.dp.toPx()) // Thick border
                )
            }
        }

        // Back button & title (moved down)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 172.dp), // Adjust for TopAppBar height
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.scan_your_guest_s_qr_code),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Back Button (Bottom Left)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 80.dp)
                .align(Alignment.BottomStart)
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}



