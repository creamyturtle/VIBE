package com.example.vibe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEventCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {


        Column {

            // Shimmer Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush = ShimmerEffect()) // Shimmer Effect
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Shimmer Title Placeholder
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .height(20.dp)
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = ShimmerEffect()) // Shimmer Effect
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Shimmer Subtitle (Date & Time) Placeholder
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .height(16.dp)
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = ShimmerEffect()) // Shimmer Effect
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Shimmer Location Placeholder
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .height(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = ShimmerEffect()) // Shimmer Effect
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
