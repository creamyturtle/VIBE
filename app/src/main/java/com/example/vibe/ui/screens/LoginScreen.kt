package com.example.vibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.data.AuthRepository
import com.example.vibe.model.Event


@Composable
fun LoginScreen(
    navController: NavController,
    authRepository: AuthRepository, // ✅ Inject Repository Instead of AppContainer
    onLoginSuccess: () -> Unit, // ✅ No Need to Pass UserData, Extract from JWT Instead
    onBack: () -> Unit
) {
    val viewModel = remember { LoginViewModel(authRepository) } // ✅ Use AuthRepository

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
            text = "Log in to your VIBE account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )



        Spacer(modifier = Modifier.height(20.dp))

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
            isPassword  = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.errorMessage != null) {
            Text(viewModel.errorMessage!!, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        StyledButton(
            text = "Login",
            isLoading = viewModel.isLoading,
            onClick = {
                viewModel.login {
                    onLoginSuccess()
                    navController.navigate("home_screen/all") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        )


        Spacer(modifier = Modifier.height(24.dp))

        OrDivider()

        Spacer(modifier = Modifier.height(24.dp))

        StyledButton(
            text = "Sign up",
            onClick = {
                navController.navigate("signup") {
                    popUpTo("login") { inclusive = true }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        /*

        OrDivider()

        Spacer(modifier = Modifier.height(24.dp))

        StyledButton(
            text = "Logout",
            onClick = {
                viewModel.logout {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true } // Clears back stack
                    }
                }
            }
        )

         */


    }
}


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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White, // White background
            focusedBorderColor = Color.Gray, // Gray border when focused
            unfocusedBorderColor = Color.LightGray, // Lighter border when not focused
            cursorColor = Color.Black // Cursor color
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth() // Extends across the screen width
            .padding(horizontal = 16.dp) // Adds side padding
    )
}


@Composable
fun StyledButton(
    text: String,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFE1943),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFBDBDBD),
            disabledContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }

}


