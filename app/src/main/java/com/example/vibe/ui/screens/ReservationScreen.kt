package com.example.vibe.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.model.Event
import com.example.vibe.ui.components.OrDivider
import com.example.vibe.ui.components.StyledButton
import com.example.vibe.ui.components.StyledTextField
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.RSVPViewModel
import com.example.vibe.ui.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationScreen(
    event: Event?,
    onBack: () -> Unit,
    authViewModel: AuthViewModel,
    rsvpViewModel: RSVPViewModel,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val rsvpState by rsvpViewModel.rsvpStatus.observeAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchUserData() // ✅ Fetch user details on screen load
    }

    val userData by userViewModel.userData.observeAsState()


    // Store user input (guest names and additional items)
    var guest1 by remember { mutableStateOf("") }
    var guest2 by remember { mutableStateOf("") }
    var guest3 by remember { mutableStateOf("") }
    var guest4 by remember { mutableStateOf("") }
    var bringingItems by remember { mutableStateOf("") }

    val context = LocalContext.current // ✅ Get the app context for Toast messages

    if (isLoggedIn) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (event == null) {
                // Show loading indicator while waiting for event data
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(Modifier.height(120.dp))
                    CircularProgressIndicator()
                }
            } else {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(8.dp, 40.dp, 0.dp, 0.dp)
                        .background(color = Color.White, shape = CircleShape)
                        .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                EventSummaryCard(
                    eventName = event.partyname,
                    location = event.location,
                    openSlots = event.openslots,
                    date = event.formattedDate,
                    time = event.formattedTime,
                    imageUrl = event.fullImgSrc
                )

                // This should come from logged-in user data

                userData?.let { user ->
                    GuestDetailsCard(
                        name = user.name,
                        age = user.age,
                        gender = user.gender,
                        instagram = user.instagram
                    )
                } ?: run {
                    Text(
                        text = "Loading user details...",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Collect user input for additional guests
                AdditionalGuestsSection(
                    guest1 = guest1,
                    onGuest1Change = { guest1 = it },
                    guest2 = guest2,
                    onGuest2Change = { guest2 = it },
                    guest3 = guest3,
                    onGuest3Change = { guest3 = it },
                    guest4 = guest4,
                    onGuest4Change = { guest4 = it },
                    bringingItems = bringingItems,
                    onBringingChange = { bringingItems = it }
                )



                AgreementSection(
                    onSubmit = {
                        event.id?.toIntOrNull()?.let { partyId ->
                            rsvpViewModel.submitRSVP(
                                partyId = partyId,
                                guest1 = guest1.ifBlank { null },
                                guest2 = guest2.ifBlank { null },
                                guest3 = guest3.ifBlank { null },
                                guest4 = guest4.ifBlank { null },
                                bringing = bringingItems.ifBlank { null }
                            )

                            // ✅ Show Toast Message AFTER Submission
                            rsvpState?.let { result ->
                                when {
                                    result.isSuccess -> {
                                        val response = result.getOrNull()
                                        val message = if (response?.status == "already_rsvp") {
                                            "You have already RSVP'd for this event."
                                        } else {
                                            "RSVP Sent to Event Host!  Please watch your email or profile page for updates"
                                        }
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                    result.isFailure -> {
                                        Toast.makeText(context, "RSVP Failed: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        } ?: Log.e("ReservationScreen", "Invalid party ID: ${event.id}")
                    }
                )


                Spacer(Modifier.height(148.dp))
            }
        }
    } else {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp), // Push content slightly down from the very top
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally, // Centers everything horizontally
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.Start) // Aligns to the left of the screen
                        .padding(16.dp) // Adds spacing from the edges
                        //.background(color = Color.White, shape = CircleShape)
                        //.border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                        .size(40.dp) // Slightly larger size for easier clicking
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                //Spacer(Modifier.height(12.dp)) // Adds spacing between back button and card

                PleaseLogin(navController = navController, authViewModel = authViewModel)
            }
        }



    }
}



@Composable
fun PleaseLogin(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    // Add a Column with padding to apply padding to all inner content
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally// Add padding inside the Card
    ) {


        Text(
            text = "Returning User",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(32.dp))

        StyledButton(
            text = "Login",
            isLoading = authViewModel.isLoading,
            onClick = {
                navController.navigate("login")
            }
        )


        Spacer(Modifier.height(48.dp))

        OrDivider()

        Spacer(Modifier.height(32.dp))

        Text(
            text = "New User",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(32.dp))

        StyledButton(
            text = "Sign Up",
            isLoading = authViewModel.isLoading,
            onClick = {
                navController.navigate("signup")
            }
        )



    }



}



@Composable
fun EventSummaryCard(
    eventName: String,
    location: String,
    openSlots: String,
    date: String,
    time: String,
    imageUrl: String // Add Image URL Parameter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // Add a Column with padding to apply padding to all inner content
        Column(
            modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 24.dp) // Add padding inside the Card
        ) {
            Text(
                text = eventName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp)) // Extra space after the title

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Event Image Box
                Box(
                    modifier = Modifier
                        .size(120.dp) // Adjust size as needed
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .align(Alignment.Top)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Event Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.width(4.dp)) // Space between image and event info

                // Event Info Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Location Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = location,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Open Slots Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            buildAnnotatedString {
                                append("Open Slots:  ")
                                withStyle(SpanStyle(color = Color(0xFFFE1943), fontWeight = FontWeight.Bold)) { // Green for number
                                    append(openSlots)
                                }
                            },
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Date Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = date,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Time Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = time,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

            }
        }
    }
}





@Composable
fun GuestDetailsCard(
    name: String,
    age: Int,
    gender: String,
    instagram: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Guest Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider()

            Row() {

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)){

                    Text(
                        text = "Name:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Age:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Gender:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Instagram:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }

                Spacer(Modifier.weight(1f))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)){

                    Text(name, fontSize = 16.sp)
                    Text("$age", fontSize = 16.sp)
                    Text(gender, fontSize = 16.sp)
                    Text("@$instagram", fontSize = 16.sp, color = Color.Blue)


                }

                Spacer(Modifier.weight(1f))


            }


        }
    }
}

@Composable
fun AdditionalGuestsSection(
    guest1: String,
    onGuest1Change: (String) -> Unit,
    guest2: String,
    onGuest2Change: (String) -> Unit,
    guest3: String,
    onGuest3Change: (String) -> Unit,
    guest4: String,
    onGuest4Change: (String) -> Unit,
    bringingItems: String,
    onBringingChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Additional Guests (add up to 4)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            StyledTextField(
                value = guest1,
                onValueChange = onGuest1Change,
                label = "Guest #1"
            )

            StyledTextField(
                value = guest2,
                onValueChange = onGuest2Change,
                label = "Guest #2"
            )

            StyledTextField(
                value = guest3,
                onValueChange = onGuest3Change,
                label = "Guest #3"
            )

            StyledTextField(
                value = guest4,
                onValueChange = onGuest4Change,
                label = "Guest #4"
            )

            StyledTextField(
                value = bringingItems,
                onValueChange = onBringingChange,
                label = "Bringing Any Items? (Optional)"
            )
        }
    }
}



@Composable
fun AgreementSection(onSubmit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Agreement", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider()

            Text(
                "To request entry to the event, some of your personal data will be shared with the host. This includes your name, age, gender, and Instagram profile. The host will use this info to make a decision about whether to accept or deny your RSVP.",
                fontSize = 14.sp
            )

            Spacer(Modifier.height(8.dp))

            Text("By clicking here, you agree to have your information sent to the Host of this event.", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = true,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFE1943),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFBDBDBD),
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirm & Submit",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
/*
            StyledButton(
                text = "Confirm & Submit",
                onClick = onSubmit,
                isLoading = rsvpViewModel.isLoading
            )
*/

        }
    }
}





