package com.example.vibe.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibe.model.Event

@Composable
fun ReservationScreen(event: Event?) {
    Log.d("ReservationScreen", "Received event: $event")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // ✅ Added padding for better visibility
        verticalArrangement = Arrangement.Center, // ✅ Center content in the screen
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (event == null) {
            CircularProgressIndicator()
        } else {
            Text(
                text = event.partyname,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

