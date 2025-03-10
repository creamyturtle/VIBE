package com.example.vibe.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vibe.R
import com.example.vibe.ui.viewmodel.AuthViewModel


@Composable
fun VibeBottomAppBarNew(
    navController: NavController,
    isLoggedIn: Boolean,
    authViewModel: AuthViewModel
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val density = LocalDensity.current.density
    val onePixel = 1f / density

    val context = LocalContext.current

    //val selectedLanguage by languageViewModel.language.collectAsState()


    Column {
        // Separator bar (1px light grey)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(onePixel.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant) // Light grey separator
        )

        BottomAppBar(
            modifier = Modifier
                //.offset(y = offsetY)
                .height(104.dp),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = Color.Gray,
            contentPadding = PaddingValues(0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                    BottomNavItem(
                        navController,
                        "home_screen",
                        currentRoute,
                        Icons.Default.Search,
                        stringResource(R.string.explore)
                    )
                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                    if (isLoggedIn) {
                        BottomNavItem(
                            navController,
                            "host_event",
                            currentRoute,
                            Icons.Default.Groups,
                            stringResource(R.string.host)
                        )
                    } else {
                        BottomNavItem(
                            navController,
                            "login",
                            currentRoute,
                            Icons.Default.Groups,
                            stringResource(R.string.host)
                        )
                    }

                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                    BottomNavItem(
                        navController,
                        "calendar",
                        currentRoute,
                        Icons.Default.Event,
                        stringResource(R.string.calendar)
                    )
                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                    if (isLoggedIn) {
                        BottomNavItem(
                            navController,
                            "user_profile",
                            currentRoute,
                            Icons.Default.AccountCircle,
                            stringResource(R.string.profile)
                        )
                    } else {
                        BottomNavItem(
                            navController,
                            "login",
                            currentRoute,
                            Icons.Default.AccountCircle,
                            stringResource(R.string.profile)
                        )
                    }

                }

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                    if (isLoggedIn) {
                        BottomNavItem(
                            navController,
                            baseRoute = "logout",
                            currentRoute = currentRoute,
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
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
                            stringResource(R.string.log_in)
                        )
                    }
                }


            }
        }
    }


}




