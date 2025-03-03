package com.example.vibe.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.data.EventAttending
import com.example.vibe.ui.viewmodel.EventsUiState
import com.example.vibe.utils.generateQRCode
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventsAttendingScreen(
    eventsUiState: EventsUiState,
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

        Row {

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
                text = "Events Attending",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

        }


        when (eventsUiState) {
            is EventsUiState.SuccessAttending -> {


                val events = eventsUiState.events
                val coroutineScope = rememberCoroutineScope()



                if (events.isEmpty()) {

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No events found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
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

                                        onCancelReservation(event.tablename)


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

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Failed to load events", color = MaterialTheme.colorScheme.error)

                    Spacer(Modifier.height(32.dp))

                    ErrorScreen(retryAction, modifier)
                }
            }

            else -> {

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
fun EventCard(
    event: EventAttending,
    onViewQrCode: (String) -> Unit,
    onCancelReservation: () -> Unit
) {
    var showCancelConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = event.partyname, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "${event.formattedDate} @ ${event.formattedTime}", color = MaterialTheme.colorScheme.secondaryContainer)

            Spacer(Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = event.fullImgSrc,
                    contentDescription = event.partyname,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.defaultimg)
                )

                Spacer(Modifier.height(16.dp))

                Text(text = "Address:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.height(4.dp))


                OpenInMaps(event.locationlong)


                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Bringing:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text(text = event.bringing.takeIf { it.isNotBlank() } ?: "Nothing")
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Add. Guests:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text(text = "${event.guestCount}")
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "People Attending:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text(text = "${event.totalslots - event.openslots}")
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Open Slots:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text(text = event.openslots.toString())
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "RSVP Status:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        text = if (event.rsvpapproved == 1) "Approved" else "Pending",
                        color = if (event.rsvpapproved == 1) Color.Green else MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (event.rsvpapproved == 1 && event.qrcode.isNotEmpty()) {
                Button(
                    onClick = { onViewQrCode(event.qrcode) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View QR Code", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = { showCancelConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel Reservation", color = Color.White)
            }
        }
    }

    // Confirmation Dialog
    if (showCancelConfirmation) {
        AlertDialog(
            onDismissRequest = { showCancelConfirmation = false },
            confirmButton = {
                TextButton(onClick = {
                    onCancelReservation()
                    showCancelConfirmation = false
                }) {
                    Text("Yes, Cancel", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelConfirmation = false }) {
                    Text("No", color = MaterialTheme.colorScheme.primary)
                }
            },
            title = {
                Text("Confirm Cancellation")
            },
            text = {
                Text("Are you sure you want to cancel your reservation?")
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
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
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun OpenInMaps(location: String) {
    val context = LocalContext.current
    val openMaps: () -> Unit = {
        val uri = Uri.encode(location)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$uri"))
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }

    Row(
        modifier = Modifier
            .clickable { openMaps() }, // Clickable on the entire row
            //.padding(8.dp),
        verticalAlignment = Alignment.Top // Aligns text with the top of the image
    ) {
        Text(
            text = location,
            modifier = Modifier
                .width(220.dp), // Space between text and image
            color = MaterialTheme.colorScheme.secondaryContainer
        )

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.google_maps_tile),
            contentDescription = "Google Maps",
            modifier = Modifier
                .size(100.dp) // Adjust size as needed
        )
    }
}
