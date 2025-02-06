package com.example.vibe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.size

@Composable
fun BottomNavItem(
    navController: NavController,
    baseRoute: String,
    currentRoute: String?,
    icon: ImageVector,
    label: String
) {
    val isSelected = currentRoute?.startsWith(baseRoute) == true
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.Gray,
        animationSpec = tween(300)
    )

    FancyAnimatedButtonNew(
        onClick = {
            val destinationRoute = when (baseRoute) {
                "home_screen" -> "home_screen/all" // ✅ Ensure Explore button navigates properly
                else -> baseRoute // ✅ Other buttons work normally
            }

            navController.navigate(destinationRoute) {
                popUpTo("home_screen/all") { inclusive = false }
            }
        },
        isSelected = isSelected
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = iconColor,
                style = TextStyle(fontSize = 10.sp, lineHeight = 10.sp)
            )
        }
    }
}




