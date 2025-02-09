package com.example.vibe.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vibe.ui.components.StyledButton
import com.example.vibe.ui.components.StyledTextField
import com.example.vibe.ui.components.StyledTextField2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar
import java.util.Locale


@Composable
fun EventCreationForm(
    navController: NavController
) {
    val partyName = remember { mutableStateOf("") }
    val partyType = remember { mutableStateOf("House Party") }
    val description = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val rules = remember { mutableStateOf("") }
    val totalSlots = remember { mutableStateOf("") }
    val offerings = remember { mutableStateOf(List(3) { "" }) }
    val musicType = remember { mutableStateOf("") }
    val locationLong = remember { mutableStateOf("") }
    val hostInstagram = remember { mutableStateOf("") }
    val videoUrl = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }

    val amenities = remember {
        mutableStateOf(
            mapOf(
                "Free Parking" to false,
                "WiFi" to false,
                "Alcohol Provided" to false,
                "Pet Friendly" to false,
                "Smoking Allowed" to false
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Spacer(Modifier.height(24.dp))

        IconButton(
            onClick = { navController.popBackStack() },
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

        // Host Event Section
        SectionTitle("Host an Event")


        Text(
            text = "Want to publish your event on VIBE?  Fill out the form below to get started!",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
        )

        Text(
            text = "VIBE is a completely free platform and charging entry fees is not allowed.  Any commercial events or businesses will be denied.",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
        )

        Text(
            text = "Please read our About Us page for more information.",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
        )

        Spacer(Modifier.height(16.dp))


        // General Info Section
        SectionTitle("Event Information")

        StyledTextField(value = partyName.value, onValueChange = { partyName.value = it }, label = "Event Name")
        CustomDropdownMenu(
            options = listOf("House Party", "Finca Party", "Pool Party", "Activity"),
            selectedOption = partyType,
            label = "Category"
        )
        StyledTextField2(
            value = description.value,
            onValueChange = { description.value = it },
            label = "Event Description"
        )

        StyledTextField(value = location.value, onValueChange = { location.value = it }, label = "General Location (City or Barrio)")

        Spacer(Modifier.height(4.dp))

        DateTimeSelector(date, time)

        Spacer(Modifier.height(0.dp))

        StyledTextField(value = rules.value, onValueChange = { rules.value = it }, label = "Rules & Restrictions (Optional)")
        StyledTextField(value = totalSlots.value, onValueChange = { totalSlots.value = it }, label = "Max # of Guests Allowed")

        Spacer(Modifier.height(24.dp))

        // Attractions Section
        SectionTitle("Attractions")
        repeat(3) { index ->
            StyledTextField(value = offerings.value[index], onValueChange = { offerings.value = offerings.value.toMutableList().apply { this[index] = it } }, label = "Offering ${index + 1}")
        }
        StyledTextField(value = musicType.value, onValueChange = { musicType.value = it }, label = "Music Type")


        Spacer(Modifier.height(24.dp))
/*
        // Precise Location Section
        SectionTitle("Precise Location")
        StyledTextField(value = locationLong.value, onValueChange = { locationLong.value = it }, label = "Complete Address")
*/

        MapWithSearch(locationLong = locationLong)



        Spacer(Modifier.height(24.dp))

        // Social Media Section
        SectionTitle("Social Media")
        StyledTextField(value = hostInstagram.value, onValueChange = { hostInstagram.value = it }, label = "Instagram Username")

        Spacer(Modifier.height(24.dp))

        // Media Upload Section
        SectionTitle("Media Uploads")
        UploadButton(label = "Upload Gallery Images (Max 4)")
        UploadButton(label = "Upload Video or Enter YouTube URL")
        StyledTextField(value = videoUrl.value, onValueChange = { videoUrl.value = it }, label = "Video URL")

        Spacer(Modifier.height(24.dp))

        // Amenities Section
        SectionTitle("Amenities")
        amenities.value.forEach { (label, state) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state,
                    onCheckedChange = { checked ->
                        amenities.value = amenities.value.toMutableMap().apply { this[label] = checked }
                    }
                )
                Text(text = label, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(Modifier.height(8.dp))

        // Terms Agreement
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(checked = isChecked.value, onCheckedChange = { isChecked.value = it })
            Text(
                text = "I agree to the Terms & Conditions",
                Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(24.dp))

        // Submit Button
        StyledButton(
            text = "Submit Event",
            onClick = { /* Handle form submission */ }
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "To submit an event, you must agree to our Terms & Conditions and Privacy Policy.",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            fontStyle = FontStyle.Italic
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Please note that all events are subject to approval by our VIBE moderators.  Your event will be published within 24 hours.  Watch for an email from VIBE with your event link, or check your profile for updates.",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            fontStyle = FontStyle.Italic
        )



        Spacer(Modifier.height(148.dp))
    }
}


@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 24.dp)
    )
}

@Composable
fun CustomDropdownMenu(
    options: List<String>,
    selectedOption: MutableState<String>,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selectedOption.value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", modifier = Modifier.clickable { expanded = true })
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOption.value = option
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun UploadButton(label: String) {
    Button(
        onClick = { /* File picker logic */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFE1943),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFBDBDBD),
            disabledContentColor = Color.White
        ),
    ) {
        Text(label, color = Color.White)
    }
}


@Composable
fun DateTimeSelector(date: MutableState<String>, time: MutableState<String>) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { showDatePicker(context, date) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFE1943),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFBDBDBD),
                disabledContentColor = Color.White
            ),
        ) {
            Text(if (date.value.isNotEmpty()) date.value else "Select Date")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { showTimePicker(context, time) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFE1943),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFBDBDBD),
                disabledContentColor = Color.White
            ),
        ) {
            Text(if (time.value.isNotEmpty()) time.value else "Select Time")
        }
    }
}

// Date Picker Function
@OptIn(ExperimentalMaterial3Api::class)
fun showDatePicker(context: Context, date: MutableState<String>) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
        date.value = formattedDate
    }, year, month, day).show()
}

// Time Picker Function
fun showTimePicker(context: Context, time: MutableState<String>) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        time.value = formattedTime
    }, hour, minute, true).show()
}


@Composable
fun MapWithSearch(locationLong: MutableState<String>) {
    val context = LocalContext.current
    val medellin = LatLng(6.2442, -75.5812) // Default location: Medellín, Colombia

    // States
    val mapPosition = remember { mutableStateOf(medellin) }
    val coroutineScope = rememberCoroutineScope()

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapPosition.value, 12f)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        SectionTitle("Precise Location")

        // Address Input Field
        OutlinedTextField(
            value = locationLong.value, // ✅ Syncs with the state
            onValueChange = { newValue ->
                locationLong.value = newValue
                coroutineScope.launch {
                    searchLocation(context, newValue, mapPosition, locationLong, cameraPositionState)
                }
            },
            label = { Text("Complete Address") },
            shape = RoundedCornerShape(8.dp), // Rounded corners
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White, // White background
                focusedBorderColor = Color(0xFFFE1943), // Red border when focused
                unfocusedBorderColor = Color.LightGray, // Lighter border when not focused
                cursorColor = Color.Black // Cursor color
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                coroutineScope.launch {
                    searchLocation(context, locationLong.value, mapPosition, locationLong, cameraPositionState)
                }
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Google Map

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    mapPosition.value = latLng
                    coroutineScope.launch {
                        val address = getAddressFromLatLng(context, latLng)
                        locationLong.value = address // ✅ Updates text field when clicking map
                        val currentZoom = cameraPositionState.position.zoom // ✅ Get current zoom level
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(latLng, currentZoom) // ✅ Maintain zoom level
                        )
                    }
                }

            ) {
                Marker(
                    state = MarkerState(position = mapPosition.value),
                    title = locationLong.value
                )
            }
        }
    }
}

// Function to Search for Address and Update Map + Text Field + Focus Map
suspend fun searchLocation(
    context: Context,
    address: String,
    mapPosition: MutableState<LatLng>,
    locationLong: MutableState<String>,
    cameraPositionState: CameraPositionState
) {
    getLatLngFromAddress(context, address)?.let { newLocation ->
        mapPosition.value = newLocation
        locationLong.value = address // ✅ Ensures text field keeps the correct address
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(newLocation, 15f)) // ✅ Move focus
    }
}



// Function to Convert Address to LatLng
suspend fun getLatLngFromAddress(
    context: Context,
    address: String
): LatLng? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val results = geocoder.getFromLocationName(address, 1)
            results?.firstOrNull()?.let {
                LatLng(it.latitude, it.longitude)
            }
        } catch (e: IOException) {
            Log.e("Geocoder", "Failed to get location", e)
            null
        }
    }
}


// Function to Convert LatLng to Address
suspend fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val results = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            results?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"
        } catch (e: IOException) {
            Log.e("Geocoder", "Failed to get address", e)
            "Unknown Location"
        }
    }
}

