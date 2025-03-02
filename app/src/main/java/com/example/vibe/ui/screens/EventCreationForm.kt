package com.example.vibe.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.location.Geocoder
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.ui.components.OrDivider
import com.example.vibe.ui.components.SectionTitle
import com.example.vibe.ui.components.StyledButton2
import com.example.vibe.ui.components.StyledTextField
import com.example.vibe.ui.components.StyledTextField2
import com.example.vibe.ui.viewmodel.EventsViewModel
import com.example.vibe.ui.viewmodel.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar
import java.util.Locale


@Composable
fun EventCreationForm(
    navController: NavController,
    eventsViewModel: EventsViewModel,
    userViewModel: UserViewModel,
    context: Context
) {
    val partyName = remember { mutableStateOf("") }
    //val partyType = remember { mutableStateOf("House Party") }
    val description = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val rules = remember { mutableStateOf("") }
    val totalSlots = remember { mutableStateOf("") }
    val offerings1 = remember { mutableStateOf("") }
    val offerings2 = remember { mutableStateOf("") }
    val offerings3 = remember { mutableStateOf("") }
    val musicType = remember { mutableStateOf("") }
    val locationLong = remember { mutableStateOf("") }
    val hostInstagram = remember { mutableStateOf("") }
    val videoUrl = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }

    val userId by userViewModel.userId.observeAsState()

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

    // Map UI-friendly labels to database values
    val partyTypeMap = mapOf(
        "House Party" to "House",
        "Pool Party" to "Pool",
        "Finca Party" to "Finca",
        "Activity" to "Activity"
    )

    val selectedPartyType = remember { mutableStateOf("House Party") } // Default UI selection



    val selectedImages = remember { mutableStateListOf<Uri>() }
    val selectedVideo = remember { mutableStateOf<Uri?>(null) }

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
                .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
                .size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        //Spacer(Modifier.height(2.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = stringResource(R.string.host_your_event_on_vibe), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                HorizontalDivider()

                Text(
                    text = stringResource(R.string.vibe_is_a_completely_free),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.please_read_the_pages_below),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { navController.navigate("faq") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F51B5),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFBDBDBD),
                            disabledContentColor = Color.White
                        ),
                    ) {
                        Text("FAQ")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { navController.navigate("about_us") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F51B5),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFBDBDBD),
                            disabledContentColor = Color.White
                        ),
                    ) {
                        Text(text = stringResource(R.string.about))
                    }
                }


            }
        }


        Spacer(Modifier.height(16.dp))


        // General Info Section
        SectionTitle(stringResource(R.string.event_information))

        StyledTextField(
            value = partyName.value,
            onValueChange = { partyName.value = it },
            label = stringResource(R.string.event_name)
        )

        CustomDropdownMenu(
            options = partyTypeMap.keys.toList(),
            selectedOption = selectedPartyType,
            label = stringResource(R.string.category)
        )

        StyledTextField2(
            value = description.value,
            onValueChange = { description.value = it },
            label = stringResource(R.string.event_description)
        )

        StyledTextField(
            value = location.value,
            onValueChange = { location.value = it },
            label = stringResource(R.string.general_location_city_or_barrio)
        )

        Spacer(Modifier.height(4.dp))

        DateTimeSelector(date, time)

        Spacer(Modifier.height(0.dp))

        StyledTextField(
            value = rules.value,
            onValueChange = { rules.value = it },
            label = stringResource(R.string.rules_restrictions_optional)
        )

        StyledTextField(
            value = totalSlots.value,
            onValueChange = { totalSlots.value = it },
            label = stringResource(R.string.max_of_guests_allowed)
        )

        Spacer(Modifier.height(24.dp))

        // Attractions Section
        SectionTitle("Attractions")

        Text(
            text = stringResource(R.string.list_three_things),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(horizontal = 24.dp)
        )


        StyledTextField(
            value = offerings1.value,
            onValueChange = { offerings1.value = it },
            label = stringResource(R.string.offering_1)
        )
        StyledTextField(
            value = offerings2.value,
            onValueChange = { offerings2.value = it },
            label = stringResource(R.string.offering_2)
        )
        StyledTextField(
            value = offerings3.value,
            onValueChange = { offerings3.value = it },
            label = stringResource(R.string.offering_3)
        )

        Spacer(Modifier.height(8.dp))

        StyledTextField(
            value = musicType.value,
            onValueChange = { musicType.value = it },
            label = stringResource(R.string.music_type)
        )


        Spacer(Modifier.height(24.dp))


        MapWithSearch(locationLong = locationLong)



        Spacer(Modifier.height(24.dp))

        // Social Media Section
        SectionTitle(stringResource(R.string.social_media))
        StyledTextField(
            value = hostInstagram.value,
            onValueChange = { hostInstagram.value = it },
            label = stringResource(R.string.instagram_username)
        )

        Spacer(Modifier.height(24.dp))



        // Image Picker (Max 4 images)
        SectionTitle(stringResource(R.string.media_uploads))

        Text(
            text = stringResource(R.string.add_up_to_4_images),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        MediaSelectionRow(selectedImages, selectedVideo)



        OrDivider()



        StyledTextField(value = videoUrl.value, onValueChange = { videoUrl.value = it }, label = "YouTube Video URL")

        Spacer(Modifier.height(24.dp))

        Column() {
            // Amenities Section
            SectionTitle("Amenities")

            Spacer(Modifier.height(16.dp))

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
        }

        Spacer(Modifier.height(2.dp))


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

        Spacer(Modifier.height(8.dp))



        // Submit Button
        val isUploading by eventsViewModel.isUploading.collectAsState()
        val progressMessage = remember { mutableStateOf<String?>(null) }


        progressMessage.value?.let {
            Text(
                text = it,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }

        // Submit Button
        StyledButton2(
            text = if (isUploading) "Uploading..." else "Submit Event",
            isLoading = isUploading, // ✅ Disable button during upload
            enabled = !isUploading,  // ✅ Prevent multiple clicks
            onClick = {


                val isValid = validateFields(
                    context = context,
                    partyName = partyName.value,
                    partyType = partyTypeMap[selectedPartyType.value] ?: "House",
                    description = description.value,
                    location = location.value,
                    date = date.value,
                    time = time.value,
                    totalSlots = totalSlots.value,
                    musicType = musicType.value,
                    locationLong = locationLong.value,
                    hostInstagram = hostInstagram.value,
                    isChecked = isChecked.value
                )

                if (!isValid) {
                    return@StyledButton2 // ⛔ Stop submission if validation fails
                }

                progressMessage.value = "Starting submission..."

                val event = Event(
                    partyname = partyName.value,
                    partytype = partyTypeMap[selectedPartyType.value] ?: "House",
                    description = description.value,
                    location = location.value,
                    date = date.value,
                    time = time.value,
                    rules = rules.value,
                    totalslots = totalSlots.value.ifEmpty { "0" },
                    openslots = totalSlots.value.ifEmpty { "0" },
                    offerings1 = offerings1.value,
                    offerings2 = offerings2.value,
                    offerings3 = offerings3.value,
                    musictype = musicType.value,
                    locationlong = locationLong.value,
                    hostgram = hostInstagram.value,
                    videourl = videoUrl.value, // Keep typed URL if no upload
                    isfreeparking = if (amenities.value["Free Parking"] == true) "1" else "0",
                    iswifi = if (amenities.value["WiFi"] == true) "1" else "0",
                    isalcoholprov = if (amenities.value["Alcohol Provided"] == true) "1" else "0",
                    ispetfriendly = if (amenities.value["Pet Friendly"] == true) "1" else "0",
                    issmokingallow = if (amenities.value["Smoking Allowed"] == true) "1" else "0",
                    hostid = userId?.toString() ?: "0"
                )


                eventsViewModel.submitEventWithMedia(
                    context = context,
                    event = event,
                    selectedImages = selectedImages,
                    selectedVideo = selectedVideo.value,
                    onSuccess = {
                        progressMessage.value = "Event submitted successfully!"
                        Toast.makeText(context, "Event submitted successfully!", Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    },
                    onError = { errorMessage ->
                        progressMessage.value = "Error: $errorMessage"
                        Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                    },
                    onProgressUpdate = { message ->
                        progressMessage.value = message
                    }
                )
            }
        )





        Spacer(Modifier.height(16.dp))

        Text(
            text = "To submit an event, you must agree to our Terms & Conditions and Privacy Policy.",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        )



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate("terms_and_conditions") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFBDBDBD),
                    disabledContentColor = Color.White
                ),
            ) {
                Text("Terms & Cond's")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { navController.navigate("privacy_policy") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFBDBDBD),
                    disabledContentColor = Color.White
                ),
            ) {
                Text("Privacy Policy")
            }
        }


        Text(
            text = "Please note that all events are subject to approval by our VIBE moderators.  Your event will be published within 24 hours.  Watch for an email from VIBE with your event link, or check your profile for updates.",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        )



        Spacer(Modifier.height(148.dp))
    }
}




@Composable
fun CustomDropdownMenu(
    options: List<String>,
    selectedOption: MutableState<String>,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    val dropdownWidth = remember { mutableIntStateOf(0) } // Store width dynamically

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = selectedOption.value,
            onValueChange = { },
            readOnly = true,
            label = { Text(text = label) },
            colors = OutlinedTextFieldDefaults.colors( // ✅ Use Material3's API
                focusedContainerColor = Color.Transparent, // ✅ Removes background
                unfocusedContainerColor = Color.Transparent, // ✅ Keeps UI clean
                focusedBorderColor = Color(0xFFFE1943), // ✅ Red border when focused
                unfocusedBorderColor = Color.LightGray, // ✅ Gray border when not focused
                cursorColor = MaterialTheme.colorScheme.primary, // ✅ Cursor color
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    dropdownWidth.intValue = coordinates.size.width
                },
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { dropdownWidth.intValue.toDp() }) // Match text field width
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(0.dp) // Ensures it stays within the boundaries
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption.value = option
                        expanded = false
                    },
                    text = {
                        Text(
                            text = option,
                            fontSize = 16.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}




@Composable
fun DateTimeSelector(date: MutableState<String>, time: MutableState<String>) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Event Date & Time",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker(context, date) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color(0xFFFE1943))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date",
                        tint = Color(0xFFFE1943),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (date.value.isNotEmpty()) date.value else "Select Date",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showTimePicker(context, time) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color(0xFFFE1943))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Select Time",
                        tint = Color(0xFFFE1943),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (time.value.isNotEmpty()) time.value else "Select Time",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


// Date Picker Function

fun showDatePicker(context: Context, date: MutableState<String>) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val formattedDate = String.format(Locale.US, "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
        date.value = formattedDate
    }, year, month, day).show()
}

// Time Picker Function
fun showTimePicker(context: Context, time: MutableState<String>) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        val formattedTime = String.format(Locale.US, "%02d:%02d", selectedHour, selectedMinute) // ✅ Fix applied
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
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapPosition.value, 12f)
    }

    val predictions = remember { mutableStateListOf<AutocompletePrediction>() }
    var showDropdown by remember { mutableStateOf(false) }
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    // Focus management
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_maps_api_key))
        }
    }

    val isDarkTheme = isSystemInDarkTheme() // ✅ Detect dark mode
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = if (isDarkTheme) {
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_night)
                } else {
                    null // Default Google Maps style
                }
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SectionTitle("Precise Location")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select the exact location of your event by clicking on the map or entering the complete address below.\n\nExact location will only be shown to approved guests.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                properties = mapProperties,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    coroutineScope.launch {
                        mapPosition.value = latLng
                        val address = getAddressFromLatLng(context, latLng)
                        locationLong.value = address
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(latLng, cameraPositionState.position.zoom)
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

        Spacer(Modifier.height(12.dp))

        // Address Input Field
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = locationLong.value,
                onValueChange = { newValue ->
                    locationLong.value = newValue
                    showDropdown = true

                    debounceJob?.cancel() // Cancel previous debounce
                    debounceJob = coroutineScope.launch {
                        delay(300) // Delay for 300ms
                        predictions.clear()
                        if (newValue.isNotEmpty()) {
                            predictions.addAll(getPlacePredictions(context, newValue))
                        }
                    }
                },
                label = { Text("Complete Address") },
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    showDropdown = false // Close dropdown on done
                }),
                colors = OutlinedTextFieldDefaults.colors( // ✅ Use Material3's API
                    focusedContainerColor = Color.Transparent, // ✅ Removes background
                    unfocusedContainerColor = Color.Transparent, // ✅ Keeps UI clean
                    focusedBorderColor = Color(0xFFFE1943), // ✅ Red border when focused
                    unfocusedBorderColor = Color.LightGray, // ✅ Gray border when not focused
                    cursorColor = MaterialTheme.colorScheme.primary, // ✅ Cursor color
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )

            // Custom Dropdown
            if (showDropdown && predictions.isNotEmpty()) {
                Dropdown(predictions = predictions, onSelect = { prediction ->
                    locationLong.value = prediction.getFullText(null).toString()
                    showDropdown = false
                    coroutineScope.launch {
                        val latLng = getLatLngFromPlaceId(context, prediction.placeId)
                        if (latLng != null) {
                            mapPosition.value = latLng
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(latLng, cameraPositionState.position.zoom)
                            )
                        }
                    }
                })
            }
        }




    }
}

@Composable
fun Dropdown(
    predictions: List<AutocompletePrediction>,
    onSelect: (AutocompletePrediction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 56.dp, start = 16.dp, end = 16.dp) // Add padding to move it down
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            predictions.forEach { prediction ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Column {
                            Text(
                                text = prediction.getPrimaryText(null).toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = prediction.getSecondaryText(null).toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    },
                    onClick = { onSelect(prediction) }
                )
            }
        }
    }
}



// Updated `getPlacePredictions` with Debouncing
suspend fun getPlacePredictions(context: Context, query: String): List<AutocompletePrediction> {
    return withContext(Dispatchers.IO) {
        try {
            val placesClient = Places.createClient(context)
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setCountries("CO") // Restrict to Colombia
                .setLocationBias(
                    RectangularBounds.newInstance(
                        LatLng(4.0, -75.0),  // SouthWest corner of Colombia
                        LatLng(12.0, -67.0)  // NorthEast corner of Colombia
                    )
                )
                .setTypesFilter(listOf("locality")) // ✅ Use raw string, not Place.Type
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()

            Log.d("PlacesAPI", "Predictions: ${response.autocompletePredictions.map { it.getFullText(null) }}")
            response.autocompletePredictions
        } catch (e: Exception) {
            Log.e("PlacesAPI", "Error fetching predictions: ${e.message}")
            emptyList()
        }
    }
}



@Suppress("DEPRECATION") // TODO: Replace `LAT_LNG` when Google provides a new API

suspend fun getLatLngFromPlaceId(context: Context, placeId: String): LatLng? {
    return withContext(Dispatchers.IO) {
        try {
            val placesClient = Places.createClient(context)
            val request = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG)).build() // ✅ Correct type

            val response = placesClient.fetchPlace(request).await()
            response.place.latLng // ✅ No more type mismatch
        } catch (e: Exception) {
            Log.e("PlacesAPI", "Error fetching place details: ${e.message}")
            null
        }
    }
}



// Function to Convert LatLng to Address
suspend fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            @Suppress("DEPRECATION") // ✅ Temporary fix for Geocoder API
            val results = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            results?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"
        } catch (e: IOException) {
            Log.e("Geocoder", "Failed to get address", e)
            "Unknown Location"
        }
    }
}


fun validateFields(
    context: Context,
    partyName: String,
    partyType: String,
    description: String,
    location: String,
    date: String,
    time: String,
    totalSlots: String,
    musicType: String,
    locationLong: String,
    hostInstagram: String,
    isChecked: Boolean // ✅ Add this parameter
): Boolean {
    if (partyName.isBlank()) {
        Toast.makeText(context, "Event Name is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (partyType.isBlank()) {
        Toast.makeText(context, "Category is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (description.isBlank()) {
        Toast.makeText(context, "Event Description is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (location.isBlank()) {
        Toast.makeText(context, "General Location is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (date.isBlank()) {
        Toast.makeText(context, "Date is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (time.isBlank()) {
        Toast.makeText(context, "Time is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (totalSlots.isBlank()) {
        Toast.makeText(context, "Total Guests is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (musicType.isBlank()) {
        Toast.makeText(context, "Music Type is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (locationLong.isBlank()) {
        Toast.makeText(context, "Complete Address is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (hostInstagram.isBlank()) {
        Toast.makeText(context, "Instagram Username is required", Toast.LENGTH_SHORT).show()
        return false
    }
    if (!isChecked) { // ✅ Ensure checkbox is checked before submission
        Toast.makeText(context, "You must agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
        return false
    }

    return true // ✅ All fields are filled, and the checkbox is checked
}

@Composable
fun MediaSelectionRow(selectedImages: MutableList<Uri>, selectedVideo: MutableState<Uri?>) {
    val context = LocalContext.current

    val pickMultipleImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val availableSlots = 4 - selectedImages.size
            selectedImages.addAll(uris.take(availableSlots)) // Limit to 4 images
        }
    }

    val pickVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { selectedVideo.value = it }
    }

    Column {
        //Text(text = "Upload Media", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        //Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Image Picker Button
            OutlinedCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { pickMultipleImagesLauncher.launch("image/*") },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFFE1943))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Select Images",
                        tint = Color(0xFFFE1943),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedImages.isEmpty()) "Select Images" else "Images: ${selectedImages.size}/4",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Video Picker Button
            OutlinedCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        pickVideoLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFFE1943))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = "Select Video",
                        tint = Color(0xFFFE1943),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedVideo.value == null) "Select Video" else "Video: 1/1",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Show Selected Images
        if (selectedImages.isNotEmpty()) {
            LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                items(selectedImages) { uri ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }

        // Show Selected Video with Thumbnail
        selectedVideo.value?.let { uri ->
            val videoThumbnail = remember { getVideoThumbnail(context, uri) }

            Box(
                modifier = Modifier
                    .size(120.dp, 80.dp)
                    .padding(top = 8.dp)
            ) {
                if (videoThumbnail != null) {
                    Image(
                        bitmap = videoThumbnail.asImageBitmap(),
                        contentDescription = "Video Thumbnail",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    // Show default placeholder if no thumbnail
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = "Video Placeholder",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(4.dp))
}


fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri) // ✅ Set video source using Uri
        val bitmap = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC) // ✅ Extract frame
        retriever.release() // ✅ Prevent memory leaks
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}




