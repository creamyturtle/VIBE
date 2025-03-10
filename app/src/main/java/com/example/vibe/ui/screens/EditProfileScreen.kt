package com.example.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.ui.components.GenderDropdown
import com.example.vibe.ui.viewmodel.UserViewModel



@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val user by userViewModel.userData.observeAsState()

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var instagram by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var facebook by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var profilePhotoUrl by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            age = it.age.toString()
            gender = if (it.gender == "Male") "Male" else "Female"
            instagram = it.instagram
            bio = it.bio.toString()
            facebook = it.facebook ?: ""
            whatsapp = it.whatsapp ?: ""
            profilePhotoUrl = it.photourl ?: ""
        }
    }

    val fullImageUrl = user?.let {
        val baseUrl = "https://www.vibesocial.org/"
        val defaultImageUrl = "https://www.vibesocial.org/images/team5.jpg"

        when {
            it.photourl.isNullOrEmpty() -> defaultImageUrl
            it.photourl.startsWith("http") -> it.photourl
            else -> baseUrl + it.photourl
        }
    } ?: "https://www.vibesocial.org/images/team5.jpg" // Fallback if `user` is null



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(24.dp, 108.dp, 16.dp, 8.dp)
                .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
                .size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        // Profile Image
        AsyncImage(
            model = fullImageUrl,
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { /* Handle Image Upload */ },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Upload & Remove Photo Buttons
        Row {
            Button(onClick = { /* Handle Upload */ }) {
                Text(text = "Upload Photo")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    profilePhotoUrl = "images/team5.jpg" // Reset photo
                    userViewModel.removeProfilePhoto()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Remove Photo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })

        Spacer(modifier = Modifier.height(8.dp))

        // Age Field
        OutlinedTextField(
            value = age,
            onValueChange = { age = it.filter { char -> char.isDigit() } },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Gender Dropdown
        GenderDropdown(
            selectedOption = gender,
            onOptionSelected = { gender = it },
            label = stringResource(R.string.gender) // ✅ Pass the label
        )


        Spacer(modifier = Modifier.height(8.dp))

        // Social Fields
        OutlinedTextField(value = instagram, onValueChange = { instagram = it }, label = { Text("Instagram") })
        OutlinedTextField(value = facebook, onValueChange = { facebook = it }, label = { Text("Facebook (Optional)") })
        OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = it }, label = { Text("WhatsApp (Optional)") })
        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Short Bio (Optional)") })

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                isLoading = true
                userViewModel.updateUserProfile(
                    name = name,
                    age = age.toIntOrNull() ?: 0,
                    gender = gender,
                    instagram = instagram,
                    bio = bio,
                    facebook = facebook,
                    whatsapp = whatsapp
                ) { success, error ->
                    isLoading = false
                    if (success) {
                        navController.popBackStack() // Go back to profile
                    } else {
                        errorMessage = error
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text("Save Changes")
            }
        }

        // Error Message
        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(220.dp))
    }
}
