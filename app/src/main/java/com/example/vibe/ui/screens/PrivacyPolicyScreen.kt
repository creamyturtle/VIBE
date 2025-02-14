package com.example.vibe.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vibe.ui.components.ContentCard


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        Spacer(Modifier.height(78.dp))

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(8.dp, 40.dp, 0.dp, 0.dp)
                .background(color = Color.White, shape = CircleShape)
                .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                .size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }

        ContentCard(
            title = "Welcome to VIBE Social",
            content = "VIBE is a unique new concept that connects partygoers and hosts in your city who want to get together and have a great time. We host only private events that are free to enter as a guest. No businesses or commercial promotions allowed.\n" +
                    "\n" +
                    "This platform was designed to create unique experiences and allow people to meet each other in a more intimate setting, without having to be concerned about paying cover charges or going to a place where it's difficult to meet people."

        )

        ContentCard(
            title = "Our Mission",
            content = "Our Mission Is Simple: Make Finding Parties Easier.\n\n" +
                    "By connecting willing hosts with eager partygoers, we provide a new way to enjoy nightlife in Medellin. Enjoy a more intimate experience where you can truly get to know other people.\n" +
                    "\n" +
                    "No more waiting in lines, paying cover, and trying to meet new people in a distant corporate setting. With VIBE you can go to people's private homes or events and meet them on a more personal level."

        )

        ContentCard(
            title = "A Unique New Experience",
            content = "The VIBE platform is open to anybody age 18+. We make registration simple so you can quickly find a party and have a great night out.\n\n" + "The onus is on party hosts and their potential guests to learn about each other and see if they want to party together. It's almost like Tinder in a way, but in a more open group-based environment. \n\nOur hope is to help people make new friends and connect them in a new fun and interesting way."

        )

        ContentCard(
            title = "Secure Platform",
            content = "Utilizing Instagram, we allow hosts and guests to communicate to understand each other better before deciding to accept a reservation. Party with confidence."

        )

        ContentCard(
            title = "Completely Free",
            content = "There are no charges on the VIBE platform, neither for hosts nor guests to participate. We don't charge any subscription fees and everything is totally free to enjoy."

        )

        ContentCard(
            title = "More Information",
            content = "For more information, please check out our Frequently Asked Questions page.\n\n" + "Also, check out our Terms of Service and Privacy Policy pages."

        )






        Spacer(Modifier.height(148.dp))

    }



}

