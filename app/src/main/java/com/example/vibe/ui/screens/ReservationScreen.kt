package com.example.vibe.ui.screens

import android.os.Build
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationScreen(
    event: Event?,
    onBack: () -> Unit,
    authViewModel: AuthViewModel
) {

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

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
                modifier = Modifier
                    .fillMaxSize(),
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
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Fill the `IconButton` area
                        .padding(0.dp) // Adjust the internal padding here
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(20.dp)
                    )
                }


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
                name = "Peter Daveloose",
                age = 40,
                gender = "Male",
                instagram = "creamyturte"
            )

            AdditionalGuestsSection()


            AgreementSection()

            Spacer(Modifier.height(148.dp))
        }
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

        Text(
            text = eventName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .padding(16.dp, 12.dp, 12.dp, 0.dp)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

            Spacer(Modifier.width(2.dp))

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
fun AdditionalGuestsSection() {
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Guest #${it + 1}") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Guest #${it + 3}") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Bringing Any Items? (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun AgreementSection() {
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
            Text(
                "RSVP Data:",
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
            )
            Text(
                "To request entry to the party, some of your personal data will be shared with the host. This includes your name, age, gender, and Instagram profile. The host will use this info to make a decision about whether to accept or deny your RSVP.",
                fontSize = 14.sp
            )
            Text(
                "By clicking here, you agree to have your information sent to the Host of this event.",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Button(
                onClick = { /* Confirm action */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm & Submit")
            }
        }
    }
}




