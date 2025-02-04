package com.example.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.network.SignupApi
import com.example.vibe.ui.components.GenderDropdown
import com.example.vibe.ui.components.StyledButton
import com.example.vibe.ui.components.StyledTextField
import kotlinx.coroutines.launch


@Composable
fun SignupScreen(
    navController: NavController,
    signupApi: SignupApi, // ✅ Inject Repository Instead of AppContainer
    onSignupSuccess: () -> Unit, // ✅ No Need to Pass UserData, Extract from JWT Instead
    onBack: () -> Unit
) {
    val viewModel = remember { SignupViewModel(signupApi) }

    val snackbarHostState = remember { SnackbarHostState() } // ✅ Manages Snackbar messages
    val coroutineScope = rememberCoroutineScope() // ✅ Needed for showing Snackbar


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // ✅ Attach Snackbar
        topBar = { /* Your top bar UI */ }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))



            IconButton(
                onClick = onBack,
                modifier = Modifier
                    //.padding(16.dp)
                    .align(Alignment.Start)
                    .background(color = Color.White, shape = CircleShape)
                    .size(40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Fill the `IconButton` area
                        .padding(0.dp) // Adjust the internal padding here
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(28.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create your VIBE account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(20.dp))

            StyledTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = "Name"
            )

            Spacer(modifier = Modifier.height(8.dp))

            StyledTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(8.dp))

            StyledTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = "Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your password must be at least 8 characters long and must contain letters, numbers and special characters.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            StyledTextField(
                value = viewModel.age,
                onValueChange = { viewModel.age = it },
                label = "Age"
            )

            Spacer(modifier = Modifier.height(8.dp))

            GenderDropdown(
                selectedOption = viewModel.gender,
                onOptionSelected = { viewModel.gender = it },
                label = "Gender"
            )

            Spacer(modifier = Modifier.height(8.dp))

            StyledTextField(
                value = viewModel.instagram,
                onValueChange = { viewModel.instagram = it },
                label = "Instagram"
            )

            Spacer(modifier = Modifier.height(32.dp))




            if (viewModel.errorMessage != null) {
                Text(viewModel.errorMessage!!, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            StyledButton(
                text = "Sign Up",
                isLoading = viewModel.isLoading,
                onClick = {
                    viewModel.signup {
                        coroutineScope.launch {
                            val snackbarResult = snackbarHostState.showSnackbar(
                                message = "Signup successful! Please check your email for confirmation.",
                                actionLabel = "OK", // ✅ User must tap "OK" to dismiss
                                duration = SnackbarDuration.Indefinite // ✅ Snackbar stays until dismissed
                            )

                            if (snackbarResult == SnackbarResult.ActionPerformed) {
                                // ✅ Only navigate when user taps "OK"
                                onSignupSuccess()
                                navController.navigate("login") {
                                    popUpTo("signup") { inclusive = true }
                                }
                            }
                        }
                    }
                }
            )






            Spacer(modifier = Modifier.height(24.dp))


        }
    }
}

