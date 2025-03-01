package com.example.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.text.style.TextAlign



@Composable
fun ContactScreen(
    navController: NavController,
    onBack: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(Modifier.height(170.dp))


        // Contact Info Section
        ContactInfoSection()

        // Contact Form Section
        ContactFormSection()
    }

}



@Composable
fun ContactInfoSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContactInfoItem(
                icon = Icons.Default.Phone,
                title = "WhatsApp",
                content = "+57 324 589 7861",
                link = "https://wa.me/573245897861",
                modifier = Modifier.weight(1f) // Move weight here
            )
            ContactInfoItem(
                icon = Icons.Default.Email,
                title = "Email Us",
                content = "support@vibe.com",
                link = "mailto:support@vibe.com",
                modifier = Modifier.weight(1f)
            )
            ContactInfoItem(
                icon = Icons.Default.LocationOn,
                title = "Visit Us",
                content = "Medellín, Colombia",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ContactInfoItem(
    icon: ImageVector,
    title: String,
    content: String,
    link: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier // Modifier should come from parent Row
            .background(Color.LightGray.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable { link?.let { /* Open link */ } },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = title, tint = Color.Blue, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold)
        Text(text = content, textAlign = TextAlign.Center)
    }
}



@Composable
fun ContactFormSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Send us a message", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Your Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)

        Button(onClick = { /* Submit form */ }, modifier = Modifier.align(Alignment.End)) {
            Text("Send Message")
        }
    }
}
