package com.example.vibe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.R


@Composable
fun ControlPanelScreen(
    navController: NavController,
    onBack: () -> Unit
) {
    Box( // ✅ Use Box to allow absolute positioning
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // ✅ Scroll only this part
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(88.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // User Profile Section
            ProfileSection()

            Spacer(modifier = Modifier.height(32.dp))

            // Dashboard Overview
            DashboardSection()

            Spacer(modifier = Modifier.height(32.dp))

            // Main Action Buttons
            ActionButtonsGrid(navController)

            Spacer(Modifier.height(280.dp)) // Keep some space at the bottom
        }

        // ✅ Floating Action Button: Now positioned at bottom-right of screen
        FloatingActionButton(
            onClick = { /* Handle event creation */ },
            containerColor = Color(0xFF6200EE),
            modifier = Modifier
                .align(Alignment.BottomEnd) // ✅ Ensures it stays at the bottom-right of screen
                .padding(0.dp, 0.dp, 32.dp, 72.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                contentDescription = "Add Event",
                tint = Color.White
            )
        }
    }
}


@Composable
fun ProfileSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier.size(64.dp),
            color = Color.Gray
        ) {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Profile Photo"
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("John Doe", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Text("View Profile", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
        }
    }
}


@Composable
fun DashboardSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DashboardCard("Events Attending", "5")
        DashboardCard("Events Hosting", "2")
        DashboardCard("Pending Approvals", "3")
    }
}


@Composable
fun DashboardCard(title: String, count: String) {
    Surface(
        modifier = Modifier
            .width(108.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(8.dp))
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}


@Composable
fun ActionButtonsGrid(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ActionButton("View Events Attending", Color(0xFFBB86FC)) { navController.navigate("events_attending")}
        ActionButton("Manage Hosted Events", Color(0xFF03DAC5)) { navController.navigate("events_attending")}
        ActionButton("Approve Reservations", Color(0xFFFFC107)) { navController.navigate("events_attending")}
        ActionButton("Check-in Guests", Color(0xFF6200EE)) { navController.navigate("events_attending")}
        ActionButton("Edit Profile", Color(0xFFE91E63)) { navController.navigate("events_attending")}
    }
}


@Composable
fun ActionButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp), // ✅ Adjust this value for squarer corners
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp) // Slightly taller for better touchability
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewControlPanel() {
    ControlPanelScreen(onBack = {  })
}

 */
