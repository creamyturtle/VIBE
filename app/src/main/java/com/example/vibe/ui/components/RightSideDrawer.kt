package com.example.vibe.ui.components

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.vibe.R
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.LanguageViewModel
import com.example.vibe.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt


@Composable
fun RightSideDrawer(
    isOpen: MutableState<Boolean>,
    navController: NavController,
    isLoggedIn: Boolean,
    authViewModel: AuthViewModel,
    context: Context,
    languageViewModel: LanguageViewModel,
    settingsViewModel: SettingsViewModel,
    isDarkMode: Boolean,
    selectedLanguage: String
) {
    val density = LocalDensity.current
    val drawerWidth = with(density) { 280.dp.toPx() } // Drawer width in pixels
    val animationState = remember { Animatable(drawerWidth) }
    val backgroundAlpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var shouldRender by remember { mutableStateOf(false) }



    //val selectedLanguage by languageViewModel.language



    LaunchedEffect(isOpen.value) {
        if (isOpen.value) {
            shouldRender = true
            scope.launch { animationState.animateTo(0f, animationSpec = tween(300)) }
            backgroundAlpha.animateTo(0.3f, animationSpec = tween(300))
        } else {
            scope.launch { animationState.animateTo(drawerWidth, animationSpec = tween(300)) }
            backgroundAlpha.animateTo(0f, animationSpec = tween(200))
            shouldRender = false
        }
    }

    if (!shouldRender) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(10f)
    ) {
        // ✅ Background Overlay (Closes menu when clicked)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha.value))
                .clickable { isOpen.value = false }
        )

        // ✅ Right-Side Drawer (Now supports swipe-to-close)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .offset { IntOffset(animationState.value.roundToInt(), 0) }
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
                .align(Alignment.CenterEnd)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (animationState.value > drawerWidth * 0.3f) { // ✅ Close if dragged enough
                                isOpen.value = false
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume() // ✅ Prevents other gestures from interfering
                        if (dragAmount > 10) { // ✅ Detects right swipe (positive direction)
                            isOpen.value = false
                        }
                    }
                }
                .padding(16.dp)
        ) {
            Column {

                Spacer(Modifier.height(40.dp))

                LanguageToggle(
                    selectedLanguage = selectedLanguage, // ✅ This should be working correctly now
                    onLanguageChange = { lang ->
                        languageViewModel.setLanguage(
                            context,
                            lang
                        ) // ✅ Ensure it updates the app language
                    }
                )




                Spacer(Modifier.height(16.dp))

                if (isLoggedIn) {
                    UserProfileSection()

                    // **🔹 Menu Items**
                    Spacer(Modifier.height(16.dp))

                    DrawerMenuItem(
                        icon = Icons.Outlined.Dashboard,
                        text = stringResource(R.string.dashboard)
                    ) {
                        isOpen.value = false
                        navController.navigate("control_panel")
                    }

                    DrawerMenuItem(
                        icon = Icons.Outlined.Event,
                        text = stringResource(R.string.host_event)
                    ) {
                        isOpen.value = false
                        navController.navigate("host_event")
                    }

                    DrawerMenuItem(
                        icon = Icons.Outlined.Bookmarks,
                        stringResource(R.string.saved_events)
                    ) {
                        isOpen.value = false
                        navController.navigate("saved_events")
                    }

                    DrawerMenuItem(
                        icon = Icons.Outlined.AccountCircle,
                        text = stringResource(R.string.user_profile)
                    ) {
                        isOpen.value = false
                        navController.navigate("user_profile")
                    }

                    // Submenu: Information
                    var submenuExpanded by remember { mutableStateOf(false) }
                    DrawerMenuItem(
                        icon = Icons.Outlined.Info,
                        text = stringResource(R.string.information),
                        hasSubmenu = true,
                        isExpanded = submenuExpanded
                    ) {
                        submenuExpanded = !submenuExpanded
                    }
                    if (submenuExpanded) {
                        DrawerSubMenuItem(
                            text = stringResource(R.string.about),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("about_us")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.faq),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("faq")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.terms_conditions),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("terms_and_conditions")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.privacy_policy),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("privacy_policy")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.contact_support),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("contact")
                            })
                    }

                    DrawerMenuItem(
                        icon = Icons.Outlined.CalendarMonth,
                        text = stringResource(R.string.calendar)
                    ) {
                        isOpen.value = false
                        navController.navigate("calendar")
                    }

                    DrawerMenuItem(icon = Icons.Outlined.Map, text = stringResource(R.string.map)) {
                        isOpen.value = false
                        navController.navigate("map_screen/all")
                    }


                    DrawerMenuItem(
                        icon = Icons.AutoMirrored.Outlined.ExitToApp,
                        text = stringResource(R.string.logout)
                    ) {
                        isOpen.value = false
                        authViewModel.logout(context) // ✅ Pass context for Toast
                        navController.navigate("login") {
                            popUpTo("home_screen/all") {
                                inclusive = false
                            }
                        }
                    }


                } else {

                    Spacer(Modifier.height(16.dp))

                    DrawerMenuItem(
                        icon = Icons.Outlined.AccountCircle,
                        text = stringResource(R.string.create_account)
                    ) {
                        isOpen.value = false
                        navController.navigate("signup")
                    }

                    DrawerMenuItem(
                        icon = Icons.AutoMirrored.Outlined.Login,
                        text = stringResource(R.string.login)
                    ) {
                        isOpen.value = false
                        navController.navigate("login")
                    }

                    Spacer(Modifier.height(32.dp))

                    // Submenu: Information
                    var submenuExpanded by remember { mutableStateOf(false) }
                    DrawerMenuItem(
                        icon = Icons.Outlined.Info,
                        text = stringResource(R.string.information),
                        hasSubmenu = true,
                        isExpanded = submenuExpanded
                    ) {
                        submenuExpanded = !submenuExpanded
                    }
                    if (submenuExpanded) {
                        DrawerSubMenuItem(
                            text = stringResource(R.string.about),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("about_us")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.faq),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("faq")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.terms_conditions),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("terms_and_conditions")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.privacy_policy),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("privacy_policy")
                            })
                        DrawerSubMenuItem(
                            text = stringResource(R.string.contact_support),
                            onClick = {
                                submenuExpanded = false; isOpen.value =
                                false; navController.navigate("contact")
                            })
                    }

                    DrawerMenuItem(
                        icon = Icons.Outlined.CalendarMonth,
                        text = stringResource(R.string.calendar)
                    ) {
                        isOpen.value = false
                        navController.navigate("calendar")
                    }

                    DrawerMenuItem(icon = Icons.Outlined.Map, text = stringResource(R.string.map)) {
                        isOpen.value = false
                        navController.navigate("map_screen/all")
                    }


                }


                Spacer(Modifier.height(32.dp))

                key(selectedLanguage) {

                    DarkModeToggle(
                        isDarkMode = isDarkMode,
                        onThemeChange = { newTheme ->
                            settingsViewModel.toggleDarkMode(newTheme)
                        }
                    )
                }



            }
        }
    }
}




@Composable
fun UserProfileSection() {
    //val username = userViewModel.username.collectAsState().value ?: "Guest"
    //val karma = userViewModel.karma.collectAsState().value ?: "0"
    //val userImage = userViewModel.profilePicture.collectAsState().value

    val username =  "John Doe"
    val pHosted =  "0"
    val userImage = R.drawable.avatar



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp)) // Light gray background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        // Profile Image
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (userImage != null) {
                Image(
                    painter = painterResource(id = userImage),
                    contentDescription = "User Profile",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Default Profile",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Username
        Text(text = username, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Parties Hosted
        Text(
            text = "$pHosted Parties Hosted",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}


@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    text: String,
    hasSubmenu: Boolean = false,
    isExpanded: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // ✅ Rounded corners
            .clickable(onClick = onClick) // ✅ Material3 handles ripple automatically
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color(0xFFFE1943),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )

        if (hasSubmenu) {
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Expand Submenu",
                Modifier.size(20.dp)
            )
        }
    }
}




@Composable
fun DrawerSubMenuItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // ✅ Rounded corners
            .clickable(onClick = onClick) // ✅ Material3 handles ripple automatically
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}



@Composable
fun LanguageToggle(selectedLanguage: String, onLanguageChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp, 8.dp, 28.dp, 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isEnglish = selectedLanguage.uppercase(Locale.ROOT) == "EN" // ✅ Always compare uppercase

        // English Option
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isEnglish) Color(0xFFFE1943) else Color.Transparent)
                .clickable { onLanguageChange("EN") } // ✅ Pass uppercase
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "EN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isEnglish) Color.White else MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.usa),
                    contentDescription = "English",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Spanish Option
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(if (!isEnglish) Color(0xFFFE1943) else Color.Transparent)
                .clickable { onLanguageChange("ES") } // ✅ Pass uppercase
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ES",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!isEnglish) Color.White else MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.co),
                    contentDescription = "Spanish",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}






@Composable
fun DarkModeToggle(
    isDarkMode: Boolean?,
    onThemeChange: (Boolean?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp, 8.dp, 28.dp, 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val darkModeEnabled = isDarkMode == true

        // 🌞 Light Mode Option
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(if (!darkModeEnabled) Color(0xFFFE1943) else Color.Transparent)
                .clickable { onThemeChange(false) }
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.light),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!darkModeEnabled) Color.White else MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Light Mode",
                    modifier = Modifier.size(24.dp),
                    tint = if (!darkModeEnabled) Color.White else MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // 🌙 Dark Mode Option
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(if (darkModeEnabled) Color(0xFFFE1943) else Color.Transparent)
                .clickable { onThemeChange(true) }
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.dark),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (darkModeEnabled) Color.White else MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "Dark Mode",
                    modifier = Modifier.size(24.dp),
                    tint = if (darkModeEnabled) Color.White else MaterialTheme.colorScheme.onBackground
                )
            }
        }


    }
}




