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
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.ui.theme.VibeTheme

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
    offsetY: Dp
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
                onEventClick = onEventClick
            )

        else -> ErrorScreen(retryAction, modifier)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                when {
                    offsetY == 148.dp -> Modifier.offset(y = offsetY - 188.dp)
                    else -> Modifier.offset(y = offsetY - 148.dp)
                }
            )

            //.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        // Content goes here...

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

    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier
    )
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
    onEventClick: (String) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            items = events,
            key = { event ->
                event.id
            }
        ) { event ->
            EventCard(
                event = event,
                onClick = { onEventClick(event.id) },
                modifier = Modifier
                    .fillMaxSize(),
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

    val pagerState = rememberPagerState(pageCount = { 4 })

    Card(
        modifier = modifier
            .padding(4.dp, 4.dp, 4.dp, 12.dp)
            .shadow(10.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column {
            Text(
                text = event.partyname,
                //fontFamily = Route159Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 12.dp, 12.dp, 12.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

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
                        .padding(16.dp, 0.dp, 16.dp, 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onClick() // Trigger the card's click action on tap
                                }
                            )
                        }

                ) { page ->
                    AsyncImage(
                        model = images[page],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.defaultimg)
                    )
                }

                // Floating Bubble
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(28.dp, 16.dp, 16.dp, 16.dp)
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

                // Page Indicators
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
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

            Text(
                text = event.location,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 4.dp)
            )

            Text(
                text = event.openslots + " Open Slots",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                color = Color.DarkGray
            )

            Text(
                text = event.formattedDate + " @ " + event.formattedTime,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                color = Color.DarkGray
            )


            Text(
                text = event.description,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )



        }
    }


}



@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    VibeTheme {
        LoadingScreen(
            Modifier
                .fillMaxSize()
                .size(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    VibeTheme {
        ErrorScreen({}, Modifier.fillMaxSize())
    }
}


/*
@Preview(showBackground = true)
@Composable
fun AmphibiansListScreenPreview() {
    VibeTheme {
        val mockData = List(10) {
            Event(
                "Lorem Ipsum - $it",
                "$it",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do" +
                        " eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad" +
                        " minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip" +
                        " ex ea commodo consequat.",
                imgSrc = ""
            )
        }
        //AmphibiansListScreen(mockData, Modifier.fillMaxSize())
    }
}
*/

