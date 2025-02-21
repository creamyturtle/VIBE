package com.example.vibe.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.data.MoreUserData
import com.example.vibe.ui.viewmodel.UserViewModel
import com.example.vibe.utils.SessionManager

@RequiresApi(Build.VERSION_CODES.O)
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


@RequiresApi(Build.VERSION_CODES.O)
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
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
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

        // ✅ User Details (Styled with Cards)
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                InfoRow(Icons.Default.CalendarToday, "Joined", user.formattedCreatedAt)
                InfoRow(Icons.Default.Cake, "Age", "${user.age}")
                InfoRow(Icons.Default.Person, "Gender", user.gender)
                InfoRow(Icons.Default.Info, "Bio", user.bio ?: "No bio available")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ Social Media Links (with Icons)
        Text(
            text = "Connect with ${user.name}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp) // ✅ Adds 16.dp padding on both left & right
        )

        Spacer(modifier = Modifier.height(0.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            if (!user.facebook.isNullOrEmpty()) {
                SocialMediaButton("Facebook", R.drawable.facebook, "https://www.facebook.com/${user.facebook}", Modifier.weight(1f))
            }
            if (!user.whatsapp.isNullOrEmpty()) {
                SocialMediaButton("WhatsApp", R.drawable.whatsapp, "https://wa.me/${user.whatsapp}", Modifier.weight(1f))
            }
            if (!user.instagram.isNullOrEmpty()) {
                SocialMediaButton("Instagram", R.drawable.instagram, "https://www.instagram.com/${user.instagram}", Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(150.dp))


    }
}


@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ✅ Ensures label stays at the TOP
        Row(
            modifier = Modifier.width(108.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$label:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Top) // Ensures it's aligned with first line
            )
        }

        // ✅ Bio text starts at the same height as the label
        Text(
            text = value,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f) // Expands so text flows properly
        )
    }
}




@Composable
fun SocialMediaButton(platform: String, icon: Int, url: String, modifier: Modifier) {
    val context = LocalContext.current

    Button(
        onClick = { openUrl(url, context) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Image(painter = painterResource(icon), contentDescription = "$platform Icon", modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(platform, fontSize = 14.sp)
    }
}




fun openUrl(url: String, context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure it works in Compose
        ContextCompat.startActivity(context, intent, null)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
    }
}



