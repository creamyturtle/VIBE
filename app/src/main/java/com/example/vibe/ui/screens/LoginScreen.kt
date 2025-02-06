package com.example.vibe.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.ui.components.OrDivider
import com.example.vibe.ui.components.StyledButton
import com.example.vibe.ui.components.StyledTextField

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel, // ✅ Use AuthViewModel
    onBack: () -> Unit
) {
    val isLoading = authViewModel.isLoading
    val errorMessage = authViewModel.errorMessage

    //DO SOMETHING WITH THIS TO DISPLAY TO ALREADY LOGGED IN USERS>>>
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.Start)
                .background(color = Color.White, shape = CircleShape)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Log in to your VIBE account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(20.dp))

        StyledTextField(
            value = authViewModel.email, // ✅ Use ViewModel state
            onValueChange = { authViewModel.updateEmail(it) }, // ✅ Update ViewModel
            label = "Email"
        )

        Spacer(modifier = Modifier.height(8.dp))

        StyledTextField(
            value = authViewModel.password,
            onValueChange = { authViewModel.updatePassword(it) },
            label = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (authViewModel.errorMessage != null) {
            Text(authViewModel.errorMessage!!, color = Color.Red, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        StyledButton(
            text = "Login",
            isLoading = authViewModel.isLoading,
            onClick = {
                authViewModel.login {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show() // ✅ Show toast
                    navController.navigate("home_screen/all") { // ✅ Navigate
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        OrDivider()

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "New to VIBE?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(24.dp))

        StyledButton(
            text = "Sign up",
            onClick = {
                navController.navigate("signup") {
                    popUpTo("login") { inclusive = true }
                }
            }
        )
    }
}











