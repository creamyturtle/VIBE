/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vibe.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.ui.components.SearchBar
import com.example.vibe.ui.components.ShimmerEventCard
import com.example.vibe.ui.components.shimmerEffect
import com.example.vibe.ui.viewmodel.EventsUiState
import com.example.vibe.ui.viewmodel.EventsViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    listState: LazyListState,
    eventsUiState: EventsUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onEventClick: (String) -> Unit,
    navController: NavController,
    eventsViewModel: EventsViewModel
) {



    when (eventsUiState) {

        is EventsUiState.Loading -> LoadingScreen(modifier.fillMaxSize())

        is EventsUiState.Success ->

            EventsListScreen(
                listState = listState,
                events = eventsUiState.events,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    ),
                contentPadding = contentPadding,
                onEventClick = onEventClick,
                eventsViewModel = eventsViewModel
            )

        else -> ErrorScreen(retryAction, modifier)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 104.dp)
    ) {

        FloatingActionButton(
            onClick = { navController.navigate("map_screen/all") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .height(40.dp),
            containerColor = Color.DarkGray,
            shape = RoundedCornerShape(24.dp)
        ) {

            Row(modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp)
            ) {


                Text(
                    text = "Map",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(Modifier.size(4.dp))

                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Open Map",
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp) // Adjust size if needed
                        .align(Alignment.CenterVertically)
                )


            }


        }
    }


}

/**
 * The home screen displaying the loading message.
 */


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(164.dp))

        repeat(5) {
            ShimmerEventCard()
        }
    }
}


/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun EventsListScreen(
    listState: LazyListState,
    events: List<Event>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onEventClick: (String) -> Unit,
    eventsViewModel: EventsViewModel
) {

    var searchQuery by remember { mutableStateOf(eventsViewModel.lastSearchQuery) }


    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {


        item {
            // Search Box
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { query -> eventsViewModel.getByLocation(query) }
            )
        }
        items(
            items = events,
            key = { event ->
                event.id ?: "default_${event.hashCode()}" // ✅ Provide a fallback key
            }
        ) { event ->
            EventCard(
                event = event,
                onClick = { onEventClick(event.id ?: "") }, // ✅ Ensure it's not null
                modifier = Modifier.fillMaxSize(),
            )
        }


        item {
            Spacer(modifier = Modifier.height(64.dp)) // Adjust height as needed
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCard(event: Event, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val images = listOfNotNull(
        event.fullImgSrc,
        event.fullImgSrc2,
        event.fullImgSrc3,
        event.fullImgSrc4
    )

    val pagerState = rememberPagerState(pageCount = { images.size }) // Ensure proper count
    var isImageLoading by remember { mutableStateOf(true) } // Track loading state

    Card(
        modifier = modifier
            .padding(6.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                // Shimmer placeholder (Only visible when loading)
                if (isImageLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = shimmerEffect()) // ✅ FIXED
                    )
                }

                // Horizontal Pager for event images
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { onClick() })
                        }
                ) { page ->
                    AsyncImage(
                        model = images[page],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            //.clip(RoundedCornerShape(12.dp))
                            .then(if (isImageLoading) Modifier.background(brush = shimmerEffect()) else Modifier), // ✅ FIXED
                        contentScale = ContentScale.Crop,
                        onSuccess = { isImageLoading = false }, // Hide shimmer when loaded
                        onError = { isImageLoading = false },
                        error = painterResource(id = R.drawable.defaultimg)
                    )
                }


                // Floating Bubble
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(20.dp, 20.dp, 16.dp, 16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp) // Padding inside the bubble
                ) {
                    Text(
                        text = event.partyMod, // The bubble text
                        color = Color.Black, // Text color
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }




                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 14.dp, end = 12.dp)
                        .size(36.dp) // ✅ Adjust container size
                        .clip(CircleShape) // ✅ Ensures it's clipped properly
                        .clickable { /* Handle click */ },
                    contentAlignment = Alignment.Center // ✅ Centers the icon
                ) {
                    // ✅ Semi-transparent bookmark icon
                    Icon(
                        imageVector = Icons.Filled.Bookmark, // ✅ Filled heart icon for the background
                        contentDescription = null, // No need for duplicate content description
                        modifier = Modifier.size(28.dp), // ✅ Adjust heart size
                        tint = Color.Black.copy(alpha = 0.3f) // ✅ 50% black transparency inside
                    )

                    // ✅ White outline heart (Front layer)
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder, // ✅ Use the same Filled heart
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .size(28.dp),
                        tint = Color.White // ✅ White outline
                    )
                }




                // Page Indicators
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            // Event Details
            Text(
                text = event.partyname,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp
            )

            Text(
                text = "${event.formattedDate} @ ${event.formattedTime}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                color = Color.Gray
            )

            Text(
                text = "${event.openslots} Open Slots",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                color = Color.Gray
            )

            Text(
                text = event.location,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp, 0.dp, 16.dp, 4.dp)
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}





