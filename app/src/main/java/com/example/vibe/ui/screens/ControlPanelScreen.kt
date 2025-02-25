package com.example.vibe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.DisplaySettings
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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

            Spacer(modifier = Modifier.height(48.dp))



            // Main Action Buttons
            ActionButtonsGrid(navController)




            Spacer(Modifier.height(400.dp)) // Keep some space at the bottom
        }

        // ✅ Floating Action Button: Now positioned at bottom-right of screen
        FloatingActionButton(
            onClick = { navController.navigate("host_event") },
            containerColor = MaterialTheme.colorScheme.surface, // ✅ Forces surface color
            elevation = FloatingActionButtonDefaults.elevation(0.dp), // ❗ Ensures no tonal elevation effect
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 32.dp, 72.dp)
                .border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
            ) {

                Text(
                    text = stringResource(R.string.host_event),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = "Add Event",
                    tint = MaterialTheme.colorScheme.onBackground
                )

            }

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ListButton(icon = Icons.Outlined.Event, text = "View Events Attending") { navController.navigate("events_attending")}
        Divider(
            // = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
        ListButton(icon = Icons.Outlined.DisplaySettings, text = "Manage Hosted Events") { navController.navigate("manage_hosted")}
        Divider(
            //modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
        ListButton(icon = Icons.Outlined.ThumbUp, text = "Approve Reservations") { navController.navigate("events_attending")}
        Divider(
            //modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
        ListButton(icon = Icons.Outlined.Checklist, text = "Check-in Guests") { navController.navigate("events_attending")}
        Divider(
            //modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
        ListButton(icon = Icons.Outlined.PersonOutline, text = "Edit Profile") { navController.navigate("events_attending")}
    }
}





@Composable
fun ListButton(icon: ImageVector, text: String, onClick: () -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            //.padding(8.dp)
            .clickable(onClick = onClick)
    )
    {

        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(28.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondaryContainer,
            style = TextStyle(fontSize = 20.sp, lineHeight = 20.sp)
        )

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
            contentDescription = "Arrow",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )



    }

}



