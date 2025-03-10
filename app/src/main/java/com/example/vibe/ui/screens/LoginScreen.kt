package com.example.vibe.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.R
import com.example.vibe.ui.components.OrDivider
import com.example.vibe.ui.components.StyledButton
import com.example.vibe.ui.components.StyledTextField
import com.example.vibe.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel, // ✅ Use AuthViewModel
    onBack: () -> Unit
) {

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
                //.background(color = Color.White, shape = CircleShape)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.log_in_to_your_vibe_account),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(20.dp))

        StyledTextField(
            value = authViewModel.email, // ✅ Use ViewModel state
            onValueChange = { authViewModel.updateEmail(it) }, // ✅ Update ViewModel
            label = stringResource(R.string.email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        StyledTextField(
            value = authViewModel.password,
            onValueChange = { authViewModel.updatePassword(it) },
            label = stringResource(R.string.password),
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (authViewModel.errorMessage != null) {
            Text(authViewModel.errorMessage!!, color = Color.Red, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        StyledButton(
            text = stringResource(R.string.login),
            isLoading = authViewModel.isLoading,
            onClick = {
                authViewModel.login {
                    Toast.makeText(context,
                        context.getString(R.string.login_successful), Toast.LENGTH_LONG).show() // ✅ Show toast
                    navController.navigate("home_screen/all") { // ✅ Navigate
                        popUpTo("login") { inclusive = false }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        OrDivider()

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.new_to_vibe),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(24.dp))

        StyledButton(
            text = stringResource(R.string.sign_up),
            onClick = {
                navController.navigate("signup") {
                    popUpTo("login") { inclusive = false }
                }
            }
        )
    }
}











