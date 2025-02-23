package com.example.vibe.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.model.Event
import com.example.vibe.ui.viewmodel.EventsUiState
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ManageHostedScreen(
    eventsUiState: EventsUiState,
    navController: NavController,
    retryAction: () -> Unit,
    onCancelReservation: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var selectedQrCode by remember { mutableStateOf<String?>(null) }
    var showQrModal by remember { mutableStateOf(false) }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 104.dp) // Ensure it doesn't overlap with the TopBar
    ) {
        // ✅ Title at the top
        Text(
            text = "Events Attending",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        )

        when (eventsUiState) {
            is EventsUiState.Success -> {


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
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        items(events) { event ->

                            Log.d("UI", "🔹 Rendering event: ${event.partyname}")

                            HostedCard(
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
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        // ✅ Adds a spacer at the bottom for extra padding when scrolling
                        item {
                            Spacer(modifier = Modifier.height(120.dp)) // Adjust height as needed
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


    }

    // ✅ Display QR Code Dialog when showQrModal is true
    if (showQrModal && selectedQrCode != null) {
        QRCodeDialog(qrcodeData = selectedQrCode!!) {
            showQrModal = false
            selectedQrCode = null
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HostedCard(event: Event, onViewQrCode: (String) -> Unit, onCancelReservation: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            Text(text = event.partyname, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "${event.formattedDate} @ ${event.formattedTime}", color = MaterialTheme.colorScheme.secondaryContainer)

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Location:", fontWeight = FontWeight.Bold)
                    Text(text = event.location)
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Open Slots:", fontWeight = FontWeight.Bold)
                    Text(text = event.openslots.toString())
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "RSVP Status:", fontWeight = FontWeight.Bold)
                    Text(
                        text = "Approved",
                        color = Color.Green
                    )
                }
            }




            Spacer(modifier = Modifier.height(32.dp))





            Button(
                onClick = onCancelReservation,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel Reservation", color = Color.White)
            }
        }
    }
}
