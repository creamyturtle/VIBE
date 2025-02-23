package com.example.vibe.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.data.EventAttending
import com.example.vibe.ui.viewmodel.EventsUiState
import com.example.vibe.utils.generateQRCode
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventsAttendingScreen(
    eventsUiState: EventsUiState,
    navController: NavController,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var selectedQrCode by remember { mutableStateOf<String?>(null) }
    var showQrModal by remember { mutableStateOf(false) }

    when (eventsUiState) {
        is EventsUiState.SuccessAttending -> {
            val events = eventsUiState.events
            val coroutineScope = rememberCoroutineScope()

            Log.d("UI", "✅ Received ${events.size} events in UI")

            if (events.isEmpty()) {
                Log.e("UI", "❌ No events to display in LazyColumn")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No events found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(top = 104.dp),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    items(events) { event ->
                        Log.d("UI", "🔹 Rendering event: ${event.partyname}")
                        EventCard(
                            event = event,
                            onViewQrCode = { qrcode ->
                                Log.d("UI", "🔍 Showing QR Code for: $qrcode")
                                selectedQrCode = qrcode
                                showQrModal = true
                            },
                            onCancelReservation = {
                                coroutineScope.launch {
                                    Log.d("UI", "❌ User clicked cancel for: ${event.partyname}")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
        is EventsUiState.Error -> {
            Log.e("UI", "❌ UI in error state - failed to load events")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to load events", color = MaterialTheme.colorScheme.error)
            }
        }
        else -> {
            Log.d("UI", "⏳ UI in loading state - waiting for data")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }

    // ✅ Display QR Code Dialog when showQrModal is true
    if (showQrModal && selectedQrCode != null) {
        QRCodeDialog(qrcodeData = selectedQrCode!!) {
            showQrModal = false
            selectedQrCode = null
        }
    }
}

@Composable
fun EventCard(event: EventAttending, onViewQrCode: (String) -> Unit, onCancelReservation: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.partyname, style = MaterialTheme.typography.titleMedium)
            Text(text = "${event.date} @ ${event.time}")
            Text(text = "Location: ${event.location}")
            Text(text = "Open Slots: ${event.openslots}")
            Text(
                text = buildAnnotatedString {
                    append("RSVP Status: ") // This part remains black
                    withStyle(SpanStyle(color = if (event.rsvpapproved == 1) Color.Green else Color.Red)) {
                        append(if (event.rsvpapproved == 1) "Approved" else "Pending") // This part changes color
                    }
                }
            )



            Spacer(modifier = Modifier.height(8.dp))

            if (event.rsvpapproved == 1 && event.qrcode.isNotEmpty()) {
                Button(
                    onClick = { onViewQrCode(event.qrcode) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("View QR Code", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Button(
                onClick = onCancelReservation,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cancel Reservation", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}

@Composable
fun QRCodeDialog(qrcodeData: String?, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Your QR Code", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!qrcodeData.isNullOrEmpty()) {
                    val qrBitmap: Bitmap? = generateQRCode(qrcodeData, 400, 400)
                    qrBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier
                                .size(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(8.dp)
                        )
                    }
                } else {
                    Text(
                        text = "No QR Code available",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Close", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
            }
        },
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
    )
}




/*
@Preview(showBackground = true)
@Composable
fun PreviewEventCard() {
    EventCard(
        event = Event(
            id = 1,
            partyname = "Pool Party",
            date = "2025-05-15",
            time = "8:00 PM",
            location = "123 Beach Ave",
            openslots = 5,
            //rsvpapproved = 1,
            //qrcode = "SampleQRCodeData",
            //tablename = "party_table_1"
        ),
        onViewQrCode = {},
        onCancelReservation = {}
    )
}

 */
