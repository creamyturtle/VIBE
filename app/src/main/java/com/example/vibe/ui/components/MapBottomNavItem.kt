package com.example.vibe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun MapBottomNavItem(
    filterName: String,
    icon: ImageVector,
    label: String,
    selectedFilter: String?,
    onClick: () -> Unit
) {
    val isSelected = filterName == selectedFilter
    val color by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.Gray,
        animationSpec = tween(300)
    )

    FancyAnimatedButton(
        onClick = { onClick() } // ✅ Keeps FancyAnimatedButton unchanged
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = color // ✅ Changes color dynamically
            )
            Text(
                text = label,
                fontSize = 10.sp,
                style = TextStyle(fontSize = 10.sp, lineHeight = 10.sp),
                color = color // ✅ Changes color dynamically
            )
        }
    }
}
