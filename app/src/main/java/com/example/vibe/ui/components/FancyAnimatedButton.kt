package com.example.vibe.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun FancyAnimatedButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    // Scale Animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f, // Shrink slightly when pressed
        animationSpec = tween(durationMillis = 150) // Fast animation
    )

    // Ripple Effect
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true // Start press animations
                        try {
                            awaitRelease() // Wait for user to release
                        } finally {
                            isPressed = false // Reset press state
                        }
                        onClick() // Trigger onClick action
                    }
                )
            }
            .indication(interactionSource, LocalIndication.current), // Default ripple effect
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
