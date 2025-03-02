package com.example.vibe.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.network.RSVPItem
import com.example.vibe.ui.viewmodel.ApproveReservationsViewModel


@Composable
fun ApproveReservationsScreen(
    navController: NavController,
    approveReservationsViewModel: ApproveReservationsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val rsvpList = approveReservationsViewModel.rsvpList
    val isLoading = approveReservationsViewModel.isLoading
    val errorMessage = approveReservationsViewModel.errorMessage
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedRSVP by remember { mutableStateOf<RSVPItem?>(null) }

    // ✅ Show Toast when approval is successful
    LaunchedEffect(approveReservationsViewModel.successMessage) {
        approveReservationsViewModel.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            approveReservationsViewModel.clearSuccessMessage() // Clear message after showing
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 104.dp)
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
                    Text(text = errorMessage)
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
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
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
                        Spacer(modifier = Modifier.height(120.dp))
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
            text = { Text("Are you sure you want to approve this RSVP for ${selectedRSVP!!.name}?", fontSize = 16.sp) },
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
            },
            containerColor = MaterialTheme.colorScheme.surface
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
            .padding(vertical = 16.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {

            Spacer(Modifier.height(24.dp))

            Text(text = rsvpItem.partyName, style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))


            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {

                Column() {


                    Text(text = "Guest: ${rsvpItem.name}", fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(text = "Age: ${rsvpItem.age} | Gender: ${rsvpItem.gender}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Additional Guests:")

                    if (rsvpItem.addguest1.isNullOrEmpty()) {
                        Text(text = "None")
                    } else {
                        Text(text = "${rsvpItem.addguest1}")
                        Text(text = "${rsvpItem.addguest2}")
                        Text(text = "${rsvpItem.addguest3}")
                        Text(text = "${rsvpItem.addguest4}")
                    }




                }

                Column() {

                    val baseUrl = "https://www.vibesocial.org/" // ✅ Base URL


                    AsyncImage(
                        model = baseUrl + rsvpItem.usersphoto, // ✅ Load the full image URL
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop // ✅ Ensures proper cropping
                    )

                }


            }

            Spacer(Modifier.height(24.dp))

            SocialMediaButton("Instagram : @${rsvpItem.instagram}", R.drawable.instagram, "https://www.instagram.com/${rsvpItem.instagram}", Modifier.fillMaxWidth())

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column() {
                    Text(text = "Bringing:", fontWeight = FontWeight.Bold)

                    if (rsvpItem.bringing.isNullOrEmpty()) {
                        Text(text = "Nothing")
                    }

                    Text(text = "${rsvpItem.bringing}")


                }



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

