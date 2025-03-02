package com.example.vibe.ui.screens

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Liquor
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.SmokeFree
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.ui.components.HostInfoCard
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailsScreen(
    contentPadding: PaddingValues,
    event: Event?,
    onBack: () -> Unit,
    context: Context
) {

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

    if (event == null) {
        // Show loading indicator while waiting for event data
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {


        var coordinates by remember { mutableStateOf<LatLng?>(null) }
        val scope = rememberCoroutineScope()

        // Fetch coordinates for the address
        LaunchedEffect(event.location) {
            scope.launch {
                coordinates = geocodeAddress(context, event.location)
            }
        }

        val cameraPositionState = rememberCameraPositionState()

        LaunchedEffect(coordinates) {
            coordinates?.let {
                cameraPositionState.position = CameraPosition.Builder()
                    .target(it)
                    .zoom(13f)
                    .build()
            }
        }




        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {

            val images = listOfNotNull(
                event.fullImgSrc,
                event.fullImgSrc2,
                event.fullImgSrc3,
                event.fullImgSrc4
            )

            val pagerState = rememberPagerState(pageCount = { 4 })




            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                // Horizontal Pager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()


                ) { page ->
                    AsyncImage(
                        model = images[page],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        //.clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.defaultimg)
                    )
                }

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                        .size(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize() // Fill the `IconButton` area
                            .padding(0.dp) // Adjust the internal padding here
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(20.dp)
                        )
                    }


                }



                // Page Indicators
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Add space between dots
                ) {
                    repeat(images.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else Color.Gray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.partyname,
                //fontFamily = Route159Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 16.dp, 16.dp, 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )

            //Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth() // Makes the Box take the full width of the parent
                    .padding(24.dp, 16.dp, 60.dp, 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant, // Light grey background
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp) // Padding inside the Box to provide space around the content
            ) {
                Column {
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 12.dp) // Space below the text
                    )

                    Text(
                        text = "${event.openslots} Open Slots",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.padding(bottom = 4.dp) // Space below the text
                    )

                    Text(
                        text = "${event.formattedDate} @ ${event.formattedTime}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = event.partyMod,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 0.dp, 16.dp, 0.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.description,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 0.dp, 24.dp, 0.dp)
            )


            Spacer(modifier = Modifier.height(48.dp))


            Text(
                text = "Offerings",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp, 16.dp, 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 0.dp, 60.dp, 0.dp)
                    .background(Color.Transparent)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star Icon",
                            tint = Color(0xFFFE1943),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = event.properCaseOffer1,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star Icon",
                            tint = Color(0xFFFE1943),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = event.properCaseOffer2,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star Icon",
                            tint = Color(0xFFFE1943),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = event.properCaseOffer3,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(48.dp))


            Text(
                text = "Amenities / Rules",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp, 16.dp, 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(24.dp, 8.dp, 24.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Policy,
                    contentDescription = "Policy Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = event.properCaseRules,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(24.dp, 8.dp, 24.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalParking,
                    contentDescription = "Parking Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (event.isfreeparking == "0") "No Free Parking" else "Free Parking",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(24.dp, 8.dp, 24.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Wifi Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (event.iswifi == "0") "No Wifi Available" else "Free Wifi",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(24.dp, 8.dp, 24.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Liquor,
                    contentDescription = "Liquor Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (event.isalcoholprov == "0") "Bring Your Own Alcohol" else "Alcohol Provided",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(24.dp, 8.dp, 24.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Pets Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (event.ispetfriendly == "0") "No Pets Allowed" else "Pets Allowed",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(24.dp, 8.dp, 24.dp, 8.dp)
            ) {


                Icon(
                    imageVector = if (event.issmokingallow == "0") Icons.Default.SmokeFree else Icons.Default.SmokingRooms,
                    contentDescription = "Smoking Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (event.issmokingallow == "0") "Smoking Not Allowed" else "Smoking Allowed",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }





            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {


                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music Icon",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Music Type: ${event.properCaseMusic}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )





            if (event.videourl.isNotBlank()) {

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "Event Video",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp, 16.dp, 24.dp)
                )

                CustomVideoPlayer(videoUrl = event.videourl)
            } else {

                /*
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 60.dp, 0.dp)
                    .background(Color.Transparent)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .size(width = 160.dp, height = 90.dp)
            ) {
                Text(text = "Host did not include a video")
            }
            */


            }



            Spacer(modifier = Modifier.height(48.dp))


            Text(
                text = "General Location",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp, 16.dp, 0.dp)
            )


            // Map View
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 16.dp, 24.dp, 16.dp)
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (coordinates != null) {
                    GoogleMap(properties = mapProperties, cameraPositionState = cameraPositionState) {
                        coordinates?.let { position ->
                            Circle(
                                center = position,
                                radius = 1000.0, // Radius in meters (adjust as needed)
                                strokeColor = Color(0xFF1E88E5),
                                fillColor = Color(0x331E88E5),
                                strokeWidth = 4f // Outline width
                            )
                        }
                    }
                } else {
                    Text(text = "Loading map...")
                }
            }

            Text(
                text = "(Exact Location will be revealed upon reservation acceptance)",
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp, 32.dp, 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))


            Text(
                text = "Hosted By",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
            )

            HostInfoCard(
                profileImageUrl = "https://www.vibesocial.org/uploads/profile_photos/profile_674fac45498a53.34172521.jpg",
                hostName = "John Doe",
                age = 28,
                gender = "Male",
                instagram = "johndoe",
                facebook = "https://facebook.com/johndoe",
                whatsapp = "1234567890"
            )


            //spacer to protect bottom bar from blocking at scroll bottom

            Spacer(modifier = Modifier.height(148.dp))


        }
    }

}


@Composable
fun CustomVideoPlayer(videoUrl: String?) {
    //val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp, 0.dp, 60.dp, 0.dp)
            .fillMaxWidth()
            .aspectRatio(16 / 9f) // Adjust the aspect ratio as needed
            .clip(RoundedCornerShape(16.dp)) // Clip the content to rounded corners
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(16.dp)) // Add the border
    ) {
        // VideoView
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    setVideoURI(Uri.parse(videoUrl))
                }
            },
            modifier = Modifier.matchParentSize(),
            update = { videoView ->
                if (isPlaying) {
                    videoView.start()
                } else {
                    videoView.pause()
                }
            }
        )

        // Play Button Overlay
        if (!isPlaying) {
            IconButton(
                onClick = { isPlaying = true },
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}


suspend fun geocodeAddress(context: Context, address: String): LatLng? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            @Suppress("DEPRECATION") // ✅ Suppress the warning until Google provides a replacement
            val results = geocoder.getFromLocationName(address, 1)

            if (!results.isNullOrEmpty()) {
                LatLng(results[0].latitude, results[0].longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Geocoder", "Error geocoding address: ${e.message}")
            null
        }
    }
}


