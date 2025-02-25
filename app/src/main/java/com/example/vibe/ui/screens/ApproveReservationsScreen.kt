package com.example.vibe.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.network.RSVPItem
import com.example.vibe.ui.viewmodel.ApproveReservationsViewModel



@Composable
fun ApproveReservationsScreen(
    navController: NavController,
    approveReservationsViewModel: ApproveReservationsViewModel,
    onBack: () -> Unit
) {
    val rsvpList = approveReservationsViewModel.rsvpList
    val isLoading = approveReservationsViewModel.isLoading
    val errorMessage = approveReservationsViewModel.errorMessage
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedRSVP by remember { mutableStateOf<RSVPItem?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 104.dp) // ✅ Pushes content down by 104.dp
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
                text = "Pending Reservations",
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
                    Text("No pending RSVPs found.")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(rsvpList) { rsvp ->
                        RSVPCard(
                            rsvpItem = rsvp,
                            onApproveClick = {
                                selectedRSVP = rsvp
                                showConfirmDialog = true
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(120.dp)) // Adjust height as needed
                    }


                }
            }
        }
    }


    // Confirm Approval Dialog
    if (showConfirmDialog && selectedRSVP != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Approve RSVP") },
            text = { Text("Are you sure you want to approve this RSVP for ${selectedRSVP!!.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    approveReservationsViewModel.approveRSVP(selectedRSVP!!)
                }) {
                    Text("Approve", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}





@Composable
fun RSVPCard(
    rsvpItem: RSVPItem,
    onApproveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = rsvpItem.partyName, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Text(text = "Guest: ${rsvpItem.name}")
            Text(text = "Age: ${rsvpItem.age} | Gender: ${rsvpItem.gender}")
            Text(text = "Instagram: ${rsvpItem.instagram}")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Additional Guests:")
            Text(text = "${rsvpItem.addguest1}")
            Text(text = "${rsvpItem.addguest2}")
            Text(text = "${rsvpItem.addguest3}")
            Text(text = "${rsvpItem.addguest4}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Bringing: ${rsvpItem.bringing ?: "None"}", fontWeight = FontWeight.Bold)
                Button(
                    onClick = onApproveClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Approve", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun RSVPCard2(
    rsvpItem: RSVPItem,
    onApproveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // 🔹 Party Name
            Text(
                text = rsvpItem.partyName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Guest Details Row (Name, Age, Gender)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Guest: ${rsvpItem.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Gender",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${rsvpItem.gender} | ${rsvpItem.age} yrs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 🔹 Instagram Handle (if available)
            if (!rsvpItem.instagram.isNullOrEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Instagram",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "@${rsvpItem.instagram}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Bringing Section
            Text(
                text = "Bringing:",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = rsvpItem.bringing ?: "Nothing specified",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Approve Button
            Button(
                onClick = onApproveClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Approve",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Approve", color = Color.White)
            }
        }
    }
}


