package com.example.vibe.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
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
    //token: String,
    onBack: () -> Unit
) {

    when (eventsUiState) {
        is EventsUiState.SuccessAttending -> {
            val events = eventsUiState.events

            val coroutineScope = rememberCoroutineScope()

            var selectedQrCode by remember { mutableStateOf<String?>(null) }
            var showQrModal by remember { mutableStateOf(false) }

            Log.d("UI", "✅ Received ${events.size} events in UI")

            if (events.isEmpty()) {
                Log.e("UI", "❌ No events to display in LazyColumn")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No events found.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(top = 104.dp),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    items(events) { event ->
                        Log.d("UI", "🔹 Rendering event: ${event.partyname}") // ✅ Log each event
                        EventCard(
                            event = event,
                            onViewQrCode = { qrcode ->
                                selectedQrCode = qrcode
                                showQrModal = true
                            },
                            onCancelReservation = {
                                coroutineScope.launch {
                                    Log.d("UI", "❌ User clicked cancel for: ${event.partyname}")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        is EventsUiState.Error -> {
            Log.e("UI", "❌ UI in error state - failed to load events")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to load events", color = Color.Red)
            }
        }
        else -> {
            Log.d("UI", "⏳ UI in loading state - waiting for data")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }





}

@Composable
fun EventCard(event: EventAttending, onViewQrCode: (String) -> Unit, onCancelReservation: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.partyname, style = MaterialTheme.typography.h6)
            Text(text = "${event.date} @ ${event.time}")
            Text(text = "Location: ${event.location}")
            Text(text = "Open Slots: ${event.openslots}")

            Spacer(modifier = Modifier.height(8.dp))


            //commented out temporarily until I improve Event data class

            /*
            if (event.rsvpapproved == 1 && event.qrcode != null) {
                Button(onClick = { onViewQrCode(event.qrcode) }) {
                    Text("View QR Code")
                }
            }

             */


            //test code
            Button(onClick = { onViewQrCode(event.partyname) }) {
                Text("View QR Code")
            }




            Button(
                onClick = onCancelReservation,
                modifier = Modifier.padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
                Text("Cancel Reservation", color = MaterialTheme.colors.onError)
            }
        }
    }
}

@Composable
fun QRCodeDialog(qrcodeData: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Your QR Code") },
        text = {
            val qrBitmap: Bitmap? = generateQRCode(qrcodeData, 300, 300)
            qrBitmap?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = "QR Code")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Close") }
        }
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
