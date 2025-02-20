package com.example.vibe.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vibe.R

@Composable
fun HostInfoCard(
    profileImageUrl: String,
    hostName: String,
    age: Int,
    gender: String,
    instagram: String?,
    facebook: String?,
    whatsapp: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 16.dp, 24.dp, 0.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {


            Column(
                modifier = Modifier
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // ✅ Profile Picture
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Host Profile Picture",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ Host Name
                Text(
                    text = hostName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // ✅ Age & Gender
                Text(
                    text = "$age • $gender",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )


            }


            Spacer(Modifier.width(8.dp))


            // ✅ Social Media Links
            Column(
                modifier = Modifier
                    .padding(16.dp, 48.dp, 16.dp, 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                instagram?.let {
                    SocialLinkRowCustom(iconResId = R.drawable.instagram, label = "Instagram", url = "https://instagram.com/$it")
                }
                facebook?.let {
                    SocialLinkRowCustom(iconResId = R.drawable.facebook, label = "Facebook", url = it)
                }
                whatsapp?.let {
                    SocialLinkRowCustom(iconResId = R.drawable.whatsapp, label = "WhatsApp", url = "https://wa.me/$it")
                }



            }



        }



    }
}


@Composable
fun SocialLinkRowCustom(iconResId: Int, label: String, url: String) {

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)) // ✅ Create intent
                context.startActivity(intent) // ✅ Open the browser
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconResId),
            contentDescription = null,
            //tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF007AFF),
            fontWeight = FontWeight.Medium
        )
    }
}
