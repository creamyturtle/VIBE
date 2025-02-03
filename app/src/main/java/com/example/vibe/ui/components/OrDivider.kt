package com.example.vibe.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun OrDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )

        Text(
            text = "or",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Divider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}