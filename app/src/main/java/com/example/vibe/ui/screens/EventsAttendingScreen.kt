package com.example.vibe.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.model.Event
import com.example.vibe.ui.viewmodel.EventsUiState
import com.example.vibe.ui.viewmodel.EventsViewModel
import com.example.vibe.utils.generateQRCode
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventsAttendingScreen(
    eventsUiState: EventsUiState,
    navController: NavController,
    //token: String,
    onBack: () -> Unit
) {


    when (eventsUiState) {
        is EventsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        }

        is EventsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(text = "Failed to load events", color = Color.Red)
            }
        }

        is EventsUiState.Success -> {
            val events = eventsUiState.events

            val coroutineScope = rememberCoroutineScope()

            var selectedQrCode by remember { mutableStateOf<String?>(null) }
            var showQrModal by remember { mutableStateOf(false) }


            //already fetched in navhost

            //LaunchedEffect(Unit) {
            //    viewModel.fetchEvents(token)
            //}



            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Events You're Attending") })
                }
            ) {



                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(top = 68.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {


                    items(events) { event ->
                        EventCard(
                            event = event,
                            onViewQrCode = { qrcode ->
                                selectedQrCode = qrcode
                                showQrModal = true
                            },
                            onCancelReservation = {
                                coroutineScope.launch {
                                    // TODO: Call API to cancel reservation
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (showQrModal) {
                    selectedQrCode?.let { qrcodeData ->
                        QRCodeDialog(qrcodeData) { showQrModal = false }
                    }
                }
            }


        }
    }


}

@Composable
fun EventCard(event: Event, onViewQrCode: (String) -> Unit, onCancelReservation: () -> Unit) {
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
