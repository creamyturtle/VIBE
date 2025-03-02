package com.example.vibe.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp), // Rounded corners
        colors = OutlinedTextFieldDefaults.colors( // ✅ Use Material3's API
            focusedContainerColor = Color.Transparent, // ✅ Removes background
            unfocusedContainerColor = Color.Transparent, // ✅ Keeps UI clean
            focusedBorderColor = Color(0xFFFE1943), // ✅ Red border when focused
            unfocusedBorderColor = Color.LightGray, // ✅ Gray border when not focused
            cursorColor = MaterialTheme.colorScheme.primary, // ✅ Cursor color
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth() // Extends across the screen width
            .padding(horizontal = 16.dp),
        textStyle = TextStyle( // Ensures text starts from the top-left
            textAlign = TextAlign.Start,
            fontSize = 16.sp
        )
    )
}

@Composable
fun StyledTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = RoundedCornerShape(8.dp), // Rounded corners
        colors = OutlinedTextFieldDefaults.colors( // ✅ Use Material3's API
            focusedContainerColor = Color.Transparent, // ✅ Removes background
            unfocusedContainerColor = Color.Transparent, // ✅ Keeps UI clean
            focusedBorderColor = Color(0xFFFE1943), // ✅ Red border when focused
            unfocusedBorderColor = Color.LightGray, // ✅ Gray border when not focused
            cursorColor = MaterialTheme.colorScheme.primary, // ✅ Cursor color
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(120.dp), // Adjust height as needed
        textStyle = TextStyle( // Ensures text starts from the top-left
            textAlign = TextAlign.Start,
            fontSize = 16.sp
        ),
        maxLines = 4 // Ensures at least 3-4 lines appear
    )
}
