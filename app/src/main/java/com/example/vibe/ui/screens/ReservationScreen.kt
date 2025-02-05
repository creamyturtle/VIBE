package com.example.vibe.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibe.model.Event



@Composable
fun ReservationScreen(event: Event?) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(160.dp))

        GuestDetailsCard(
            name = "Peter Daveloose",
            age = 40,
            gender = "Male",
            instagram = "creamyturte"
        )

        AdditionalGuestsSection()

        EventSummaryCard(
            eventName = "Escape From Routine",
            location = "Medellin",
            openSlots = 20,
            date = "Friday, February 28th",
            time = "8:00 AM"
        )

        AgreementSection()

        Spacer(Modifier.height(148.dp))
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
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
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
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
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
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Agreement", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Divider()
            Text(
                "RSVP Data: To request entry to the party, some of your personal data will be shared with the host. This includes your name, age, gender, and Instagram profile. The host will use this info to make a decision about whether to accept or deny your RSVP.",
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


@Composable
fun EventSummaryCard(
    eventName: String,
    location: String,
    openSlots: Int,
    date: String,
    time: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(eventName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Location: $location", fontSize = 16.sp)
            Text("Open Slots: $openSlots", fontSize = 16.sp, color = Color.Green)
            Text("Date: $date", fontSize = 16.sp)
            Text("Time: $time", fontSize = 16.sp)
        }
    }
}

