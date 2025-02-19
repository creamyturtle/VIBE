package com.example.vibe.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.vibe.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun RightSideDrawer(
    isOpen: MutableState<Boolean>,
    navController: NavController,
    isLoggedIn: Boolean
) {
    val density = LocalDensity.current
    val drawerWidth = with(density) { 300.dp.toPx() } // Drawer width in pixels
    val animationState = remember { Animatable(drawerWidth) }
    val backgroundAlpha = remember { Animatable(0f) } // Controls background opacity
    val scope = rememberCoroutineScope()
    var shouldRender by remember { mutableStateOf(false) }

    LaunchedEffect(isOpen.value) {
        if (isOpen.value) {
            shouldRender = true // ✅ Ensure visibility before animation starts
            scope.launch {
                animationState.animateTo(0f, animationSpec = tween(300)) // ✅ Smooth slide in
            }
            backgroundAlpha.animateTo(0.3f, animationSpec = tween(300)) // ✅ Fade in background
        } else {
            scope.launch {
                animationState.animateTo(drawerWidth, animationSpec = tween(300)) // ✅ Smooth slide out
                shouldRender = false // ✅ Hide everything once animation completes
            }
            backgroundAlpha.animateTo(0f, animationSpec = tween(200)) // ✅ Fade out background after menu slides out
        }
    }

    if (!shouldRender) return // ✅ Prevents instant appearance of the drawer

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(10f) // ✅ Ensures menu is above all content
    ) {
        // ✅ Background Overlay (Fades in & out smoothly)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha.value))
                .clickable { isOpen.value = false }
        )

        // Right-Side Drawer
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .offset { IntOffset(animationState.value.roundToInt(), 0) } // Smooth slide
                .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)) // Rounded edges
                .align(Alignment.CenterEnd)
                //.shadow(8.dp, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                .padding(16.dp)
        ) {
            Column {

                Spacer(Modifier.height(60.dp))


                if (isLoggedIn) {
                    UserProfileSection()
                }


                // **🔹 Menu Items**
                Spacer(Modifier.height(8.dp))

                DrawerMenuItem(icon = Icons.Default.Dashboard, text = "Dashboard") {
                    isOpen.value = false
                    navController.navigate("dashboard")
                }
                DrawerMenuItem(icon = Icons.Default.Event, text = "Host an Event") {
                    isOpen.value = false
                    navController.navigate("host_event")
                }

                // Submenu: Information
                var submenuExpanded by remember { mutableStateOf(false) }
                DrawerMenuItem(icon = Icons.Default.Info, text = "Information", hasSubmenu = true, isExpanded = submenuExpanded) {
                    submenuExpanded = !submenuExpanded
                }
                if (submenuExpanded) {
                    DrawerSubMenuItem(text = "About", onClick = { submenuExpanded = false; navController.navigate("about") })
                    DrawerSubMenuItem(text = "FAQ", onClick = { submenuExpanded = false; navController.navigate("faq") })
                    DrawerSubMenuItem(text = "Terms & Conditions", onClick = { submenuExpanded = false; navController.navigate("terms_conditions") })
                    DrawerSubMenuItem(text = "Privacy Policy", onClick = { submenuExpanded = false; navController.navigate("privacy_policy") })
                }

                DrawerMenuItem(icon = Icons.Default.Person, text = "User Profile") {
                    isOpen.value = false
                    navController.navigate("profile")
                }

                Spacer(Modifier.height(8.dp))

                DrawerMenuItem(icon = Icons.Default.Settings, text = "Settings") {
                    isOpen.value = false
                    navController.navigate("settings")
                }
                DrawerMenuItem(icon = Icons.Default.ExitToApp, text = "Logout") {
                    isOpen.value = false
                }
            }
        }
    }
}





@Composable
fun UserProfileSection() {
    //val username = userViewModel.username.collectAsState().value ?: "Guest"
    //val karma = userViewModel.karma.collectAsState().value ?: "0"
    //val userImage = userViewModel.profilePicture.collectAsState().value

    val username =  "Guest"
    val karma =  "0"
    val userImage = R.drawable.avatar



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)) // Light gray background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        // Profile Image
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (userImage != null) {
                Image(
                    painter = painterResource(id = userImage),
                    contentDescription = "User Profile",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Default Profile",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Username
        Text(text = "u/$username", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Karma
        Text(
            text = "$karma Karma",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun DrawerSubMenuItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
    }
}
