package com.example.vibe.ui.components


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.R
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.LanguageViewModel


@Composable
fun VibeBottomAppBarNew(
    navController: NavController,
    isLoggedIn: Boolean,
    authViewModel: AuthViewModel,
    languageViewModel: LanguageViewModel
) {
    var currentRoute by remember { mutableStateOf<String?>(null) }

    // ✅ Ensure NavController updates currentRoute correctly
    LaunchedEffect(navController.currentBackStackEntry) {
        currentRoute = navController.currentBackStackEntry?.destination?.route
        Log.d("BottomBar", "Updated Current Route: $currentRoute") // ✅ Debugging
    }

    val density = LocalDensity.current.density
    val onePixel = 1f / density

    val context = LocalContext.current

    key(languageViewModel.language.value) {

        Column {
            // Separator bar (1px light grey)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(onePixel.dp)
                    .background(Color.LightGray) // Light grey separator
            )

            BottomAppBar(
                modifier = Modifier
                    //.offset(y = offsetY)
                    .height(104.dp),
                containerColor = Color.White,
                contentColor = Color.Gray,
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top
                ) {
                    BottomNavItem(
                        navController,
                        "home_screen",
                        currentRoute,
                        Icons.Default.Search,
                        stringResource(R.string.explore)
                    )
                    BottomNavItem(
                        navController,
                        "host_event",
                        currentRoute,
                        Icons.Default.Public,
                        stringResource(R.string.host)
                    )
                    BottomNavItem(
                        navController,
                        "calendar",
                        currentRoute,
                        Icons.Default.Event,
                        stringResource(R.string.calendar)
                    )
                    BottomNavItem(
                        navController,
                        "user_profile",
                        currentRoute,
                        Icons.Default.AccountCircle,
                        stringResource(R.string.profile)
                    )
                    if (isLoggedIn) {
                        BottomNavItem(
                            navController,
                            baseRoute = "logout",
                            currentRoute = currentRoute,
                            icon = Icons.Default.ExitToApp,
                            label = stringResource(R.string.logout),
                            onClick = {
                                // ✅ Get context for Toast
                                authViewModel.logout(context) // ✅ Pass context for Toast
                                navController.navigate("login") {
                                    popUpTo("home_screen/all") {
                                        inclusive = false
                                    }
                                }
                            }
                        )
                    } else {
                        BottomNavItem(
                            navController,
                            "login",
                            currentRoute,
                            Icons.Default.Person,
                            "Log In"
                        )
                    }
                }
            }
        }
    }
}




