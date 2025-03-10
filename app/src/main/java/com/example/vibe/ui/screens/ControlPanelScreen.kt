package com.example.vibe.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.DisplaySettings
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.data.MoreUserData
import com.example.vibe.ui.viewmodel.UserViewModel


@Composable
fun ControlPanelScreen(
    userViewModel: UserViewModel,
    navController: NavController
) {

    val user by userViewModel.userData.observeAsState()

    LaunchedEffect(Unit) {
        if (user == null) {
            userViewModel.fetchUserData()
        }
    }



    Box( // ✅ Use Box to allow absolute positioning
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // ✅ Scroll only this part
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(128.dp))

            // User Profile Section
            user?.let {
                ProfileSection(navController, it)
            } ?: Text(
                text = "Loading...",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )



            Spacer(modifier = Modifier.height(36.dp))

            // Dashboard Overview
            DashboardSection()

            Spacer(modifier = Modifier.height(36.dp))



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
                .padding(0.dp, 0.dp, 32.dp, 88.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
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
fun ProfileSection(
    navController: NavController,
    user: MoreUserData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier.size(82.dp),
            color = Color.Gray
        ) {


            val baseUrl = "https://www.vibesocial.org/" // ✅ Base URL
            val defaultImageUrl = "https://www.vibesocial.org/images/team5.jpg" // ✅ Fallback image

            val fullImageUrl = when {
                user.photourl.isNullOrEmpty() -> defaultImageUrl // ✅ If null or empty, use default
                user.photourl.startsWith("http") -> user.photourl // ✅ If already a full URL, use as is
                else -> baseUrl + user.photourl // ✅ Append base URL if relative path
            }


            AsyncImage(
                model = fullImageUrl, // ✅ Load the full image URL
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                    //.border(2.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop // ✅ Ensures proper cropping
            )

        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = user.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .clickable { navController.navigate("user_profile") })
            Text(
                text = stringResource(R.string.view_profile),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .clickable { navController.navigate("user_profile") }
            )

        }
    }
}


@Composable
fun DashboardSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DashboardCard(stringResource(R.string.events_hosted), "5")
        DashboardCard(stringResource(R.string.reservations_pending), "2")
        DashboardCard(stringResource(R.string.guests_to_check_in), "3")
    }
}


@Composable
fun DashboardCard(title: String, count: String) {
    Surface(
        modifier = Modifier
            .width(108.dp)
            .height(108.dp)
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
        modifier = Modifier.fillMaxWidth()
    ) {
        ListButton(icon = Icons.Outlined.Event, text = stringResource(R.string.view_events_attending)) { navController.navigate("events_attending")}
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        ListButton(icon = Icons.Outlined.DisplaySettings, text = stringResource(R.string.manage_hosted_events)) { navController.navigate("manage_hosted")}
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        ListButton(icon = Icons.Outlined.ThumbUp, text = stringResource(R.string.approve_reservations)) { navController.navigate("approve_reservations")}
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        ListButton(icon = Icons.Outlined.Checklist, text = stringResource(R.string.check_in_guests)) { navController.navigate("check_in")}
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        ListButton(icon = Icons.Outlined.PersonOutline, text = stringResource(R.string.edit_profile)) { navController.navigate("edit_profile")}
    }
}





@Composable
fun ListButton(icon: ImageVector, text: String, onClick: () -> Unit) {

    Surface(
        shape = RoundedCornerShape(12.dp), // Set rounded corners
        color = Color.Transparent, // Keeps the default background transparent
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp)) // Ensures ripple effect follows the rounded corners
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(60.dp)

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
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )


        }
    }

}



