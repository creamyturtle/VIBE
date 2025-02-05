package com.example.vibe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    color = Color.Black
                )

                // ✅ Age & Gender
                Text(
                    text = "$age • $gender",
                    fontSize = 16.sp,
                    color = Color.DarkGray
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
                    SocialLinkRow(icon = Icons.Filled.Facebook, label = "Facebook", url = it)
                }
                whatsapp?.let {
                    SocialLinkRow(icon = Icons.Filled.Phone, label = "WhatsApp", url = "https://wa.me/$it")
                }



            }



        }



    }
}

// ✅ Composable for a single social media row
@Composable
fun SocialLinkRow(icon: ImageVector, label: String, url: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Open link */ }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            //tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF007AFF), // Airbnb-style blue color for links
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SocialLinkRowCustom(iconResId: Int, label: String, url: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Open link */ }
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
