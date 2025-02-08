package com.example.vibe.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vibe.model.Event
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.RSVPViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationScreen(
    event: Event?,
    onBack: () -> Unit,
    authViewModel: AuthViewModel,
    rsvpViewModel: RSVPViewModel
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val rsvpState by rsvpViewModel.rsvpStatus.observeAsState()

    // Store user input (guest names and additional items)
    var guest1 by remember { mutableStateOf("") }
    var guest2 by remember { mutableStateOf("") }
    var guest3 by remember { mutableStateOf("") }
    var guest4 by remember { mutableStateOf("") }
    var bringingItems by remember { mutableStateOf("") }

    if (isLoggedIn) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (event == null) {
                // Show loading indicator while waiting for event data
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(Modifier.height(120.dp))
                    CircularProgressIndicator()
                }
            } else {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(8.dp, 40.dp, 0.dp, 0.dp)
                        .background(color = Color.White, shape = CircleShape)
                        .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                EventSummaryCard(
                    eventName = event.partyname,
                    location = event.location,
                    openSlots = event.openslots,
                    date = event.formattedDate,
                    time = event.formattedTime,
                    imageUrl = event.fullImgSrc ?: ""
                )

                GuestDetailsCard(
                    name = "Peter Daveloose", // This should come from logged-in user data
                    age = 40,
                    gender = "Male",
                    instagram = "creamyturte"
                )

                // Collect user input for additional guests
                AdditionalGuestsSection(
                    guest1 = guest1,
                    onGuest1Change = { guest1 = it },
                    guest2 = guest2,
                    onGuest2Change = { guest2 = it },
                    guest3 = guest3,
                    onGuest3Change = { guest3 = it },
                    guest4 = guest4,
                    onGuest4Change = { guest4 = it },
                    bringingItems = bringingItems,
                    onBringingChange = { bringingItems = it }
                )

                AgreementSection(
                    onSubmit = {
                        event.id?.toIntOrNull()?.let { partyId ->
                            rsvpViewModel.submitRSVP(
                                partyId = partyId,
                                guest1 = guest1.ifBlank { null },
                                guest2 = guest2.ifBlank { null },
                                guest3 = guest3.ifBlank { null },
                                guest4 = guest4.ifBlank { null },
                                bringing = bringingItems.ifBlank { null }
                            )
                        } ?: Log.e("ReservationScreen", "Invalid party ID: ${event.id}")
                    }
                )

                Spacer(Modifier.height(16.dp))

                // ✅ Display RSVP Response Messages
                rsvpState?.let { result ->
                    when {
                        result.isSuccess -> {
                            val response = result.getOrNull()
                            if (response?.status == "already_rsvp") {
                                Text(
                                    text = "You have already RSVP'd for this event.",
                                    color = Color.Red,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                Text(
                                    text = "RSVP Successful!",
                                    color = Color.Green,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        result.isFailure -> {
                            Text(
                                text = "RSVP Failed: ${result.exceptionOrNull()?.message}",
                                color = Color.Red,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(148.dp))
            }
        }
    } else {
        Text("Please log in to view this page")
    }
}



@Composable
fun EventSummaryCard(
    eventName: String,
    location: String,
    openSlots: String,
    date: String,
    time: String,
    imageUrl: String // Add Image URL Parameter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // Add a Column with padding to apply padding to all inner content
        Column(
            modifier = Modifier.padding(16.dp) // Add padding inside the Card
        ) {
            Text(
                text = eventName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp)) // Extra space after the title

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Event Image Box
                Box(
                    modifier = Modifier
                        .size(80.dp) // Adjust size as needed
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .align(Alignment.Top)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Event Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Event Info Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "📍 $location",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "🟢 Open Slots: $openSlots",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50), // Green
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "📅 $date",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "⏰ $time",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}





@Composable
fun GuestDetailsCard(
    name: String,
    age: Int,
    gender: String,
    instagram: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Guest Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider()
            Text("Name: $name", fontSize = 16.sp)
            Text("Age: $age", fontSize = 16.sp)
            Text("Gender: $gender", fontSize = 16.sp)
            Text("Instagram: @$instagram", fontSize = 16.sp, color = Color.Blue)
        }
    }
}

@Composable
fun AdditionalGuestsSection(
    guest1: String,
    onGuest1Change: (String) -> Unit,
    guest2: String,
    onGuest2Change: (String) -> Unit,
    guest3: String,
    onGuest3Change: (String) -> Unit,
    guest4: String,
    onGuest4Change: (String) -> Unit,
    bringingItems: String,
    onBringingChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Additional Guests (add up to 4)", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = guest1,
                onValueChange = onGuest1Change,
                shape = RoundedCornerShape(8.dp),
                label = { Text("Guest #1") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = guest2,
                onValueChange = onGuest2Change,
                shape = RoundedCornerShape(8.dp),
                label = { Text("Guest #2") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = guest3,
                onValueChange = onGuest3Change,
                shape = RoundedCornerShape(8.dp),
                label = { Text("Guest #3") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = guest4,
                onValueChange = onGuest4Change,
                shape = RoundedCornerShape(8.dp),
                label = { Text("Guest #4") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bringingItems,
                onValueChange = onBringingChange,
                shape = RoundedCornerShape(8.dp),
                label = { Text("Bringing Any Items? (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun AgreementSection(onSubmit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Agreement", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Divider()
            Text("RSVP Data:", fontSize = 14.sp, textDecoration = TextDecoration.Underline)
            Text(
                "To request entry to the party, some of your personal data will be shared with the host...",
                fontSize = 14.sp
            )

            Spacer(Modifier.height(8.dp))

            Text("By clicking here, you agree to have your information sent to the Host.", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(8.dp))

            Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                Text("Confirm & Submit")
            }
        }
    }
}





