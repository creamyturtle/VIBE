package com.example.vibe.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.network.RSVPApiService
import com.example.vibe.network.RSVPItem
import com.example.vibe.ui.viewmodel.ApproveReservationsViewModel
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pending RSVPs") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { paddingValues ->
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
                        .padding(paddingValues)
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
            Text(text = rsvpItem.partyName, style = MaterialTheme.typography.titleLarge)
            Text(text = "Guest: ${rsvpItem.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Age: ${rsvpItem.age} | Gender: ${rsvpItem.gender}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Instagram: ${rsvpItem.instagram}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

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

