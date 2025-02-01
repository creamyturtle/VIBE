package com.example.vibe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.data.AuthRepository


@Composable
fun LoginScreen(
    navController: NavController,
    authRepository: AuthRepository, // ✅ Inject Repository Instead of AppContainer
    onLoginSuccess: () -> Unit // ✅ No Need to Pass UserData, Extract from JWT Instead
) {
    val viewModel = remember { LoginViewModel(authRepository) } // ✅ Use AuthRepository

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.errorMessage != null) {
            Text(viewModel.errorMessage!!, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                viewModel.login {
                    onLoginSuccess() // ✅ Navigate on success
                    navController.navigate("home_screen/all") {
                        popUpTo("login") { inclusive = true } // ✅ Remove login from backstack
                    }
                }
            },
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Logout Button
        Button(
            onClick = {
                viewModel.logout {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true } // Clears back stack
                    }
                }
            }
        ) {
            Text("Logout")
        }


    }
}


