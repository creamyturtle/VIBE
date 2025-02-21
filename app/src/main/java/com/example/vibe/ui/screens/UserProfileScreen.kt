package com.example.vibe.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.vibe.data.MoreUserData
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.UserViewModel
import com.example.vibe.utils.SessionManager

@Composable
fun UserProfileScreen(
    userViewModel: UserViewModel,
    sessionManager: SessionManager,
    navController: NavController,
    onLogout: () -> Unit // Callback for handling logout
) {

    val user by userViewModel.userData.observeAsState() // Observe user data
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userViewModel.fetchUserData() // Fetch user data on screen launch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            user == null -> {
                CircularProgressIndicator() // Loading indicator
            }
            else -> {
                UserProfileContent(user!!, sessionManager, context, navController, onLogout)
            }
        }
    }
}


@Composable
fun UserProfileContent(
    user: MoreUserData,
    sessionManager: SessionManager,
    context: Context,
    navController: NavController,
    onLogout: () -> Unit
) {
    val baseUrl = "https://www.vibesocial.org/" // ✅ Base URL
    val defaultImageUrl = "https://www.vibesocial.org/images/team5.jpg" // ✅ Fallback image

    val fullImageUrl = when {
        user.photourl.isNullOrEmpty() -> defaultImageUrl // ✅ If null or empty, use default
        user.photourl.startsWith("http") -> user.photourl // ✅ If already a full URL, use as is
        else -> baseUrl + user.photourl // ✅ Append base URL if relative path
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        //horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Spacer(Modifier.height(60.dp))

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(8.dp, 40.dp, 0.dp, 0.dp)
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

        Spacer(Modifier.height(4.dp))


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = fullImageUrl, // ✅ Load the full image URL
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop // ✅ Ensures proper cropping
            )

            Spacer(Modifier.height(16.dp))

            Text(text = user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(text = user.email, fontSize = 16.sp, color = Color.Gray)
        }



        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Age: ${user.age}")
        Text(text = "Gender: ${user.gender}")
        Text(text = "Bio: ${user.bio ?: "No bio available"}")

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (!user.facebook.isNullOrEmpty()) {
                SocialMediaButton("Facebook", user.facebook, context)
            }
            if (!user.whatsapp.isNullOrEmpty()) {
                SocialMediaButton("WhatsApp", user.whatsapp, context)
            }
            if (user.instagram.isNotEmpty()) {
                SocialMediaButton("Instagram", user.instagram, context)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                sessionManager.clearToken() // Clear token
                onLogout() // Navigate to login
            },
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text("Logout", color = Color.White)
        }
    }
}


@Composable
fun SocialMediaButton(platform: String, url: String, context: Context) {
    Button(
        onClick = { openUrl(url, context) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = platform)
    }
}


fun openUrl(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    ContextCompat.startActivity(context, intent, null)
}


