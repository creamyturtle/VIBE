package com.example.vibe.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.ui.viewmodel.ContactViewModel


@Composable
fun ContactScreen(
    navController: NavController,
    contactViewModel: ContactViewModel,
    onBack: () -> Unit
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(132.dp))

        Text(
            text = "Contact VIBE Support",
            textAlign = TextAlign.Center,
            fontSize = 28.sp
        )


        Spacer(Modifier.height(24.dp))

        // Contact Info Section
        ContactInfoSection()

        Spacer(Modifier.height(8.dp))

        // Contact Form Section
        ContactFormSection(contactViewModel)
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
                content = "Use the Gmail App",
                link = "mailto:vibemedellin2025@vibesocial.org",
                modifier = Modifier.weight(1f)
            )
            ContactInfoItem(
                icon = Icons.Default.LocationOn,
                title = "Home Base",
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
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(Color.LightGray.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable {
                link?.let {
                    handleLinkClick(context, it) // Open email/WhatsApp accordingly
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = title, tint = Color(0xFFFE1943), modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold)
        Text(text = content, textAlign = TextAlign.Center)
    }
}




@Composable
fun ContactFormSection(
    contactViewModel: ContactViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Send us a message", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }

        val response by contactViewModel.response.collectAsState()

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Your Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth().height(140.dp), maxLines = 4)



        Button(onClick = { /* Submit form */ }, modifier = Modifier.align(Alignment.End)) {
            Text("Send Message")
        }

        response?.let {
            Text(
                text = it.message,
                color = if (it.success) Color.Green else Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


fun sendEmail(context: Context, email: String, subject: String, message: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Ensures only email apps handle this
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)
    }
    context.startActivity(Intent.createChooser(intent, "Send Email"))
}

fun handleLinkClick(context: Context, link: String) {
    when {
        link.startsWith("mailto:") -> {
            sendEmail(context, link.removePrefix("mailto:"), "Contact Inquiry", "Hello, I have a question...")
        }
        link.startsWith("https://wa.me/") -> {
            openWhatsApp(context, link)
        }
        else -> {
            openBrowser(context, link) // Open other links in browser
        }
    }
}

fun openWhatsApp(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    intent.setPackage("com.whatsapp") // Ensures it opens WhatsApp directly
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp is not installed!", Toast.LENGTH_SHORT).show()
    }
}

fun openBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No web browser found!", Toast.LENGTH_SHORT).show()
    }
}
