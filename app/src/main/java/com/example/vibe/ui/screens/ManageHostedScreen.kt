package com.example.vibe.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vibe.R
import com.example.vibe.model.Event
import com.example.vibe.model.getPartyTypeText
import com.example.vibe.ui.viewmodel.EventsUiState
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ManageHostedScreen(
    eventsUiState: EventsUiState,
    navController: NavController,
    retryAction: () -> Unit,
    onCancelEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 104.dp) // Ensure it doesn't overlap with the TopBar
    ) {


        Row {

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(24.dp, 8.dp, 16.dp, 8.dp)
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

            Text(
                text = stringResource(R.string.events_you_re_hosting),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

        }



        when (eventsUiState) {
            is EventsUiState.Success -> {


                val events = eventsUiState.events
                val coroutineScope = rememberCoroutineScope()


                if (events.isEmpty()) {
                    Log.e("UI", "❌ No events to display in LazyColumn")
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No events found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        items(events) { event ->

                            Log.d("UI", "🔹 Rendering event: ${event.partyname}")

                            HostedCard(
                                navController = navController,
                                event = event,
                                onCancelReservation = {
                                    coroutineScope.launch {



                                        Log.d("UI", "❌ User clicked cancel for: ${event.partyname}")
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        // ✅ Adds a spacer at the bottom for extra padding when scrolling
                        item {
                            Spacer(modifier = Modifier.height(120.dp)) // Adjust height as needed
                        }

                    }


                }
            }

            is EventsUiState.Error -> {
                ErrorScreen(retryAction, modifier)
            }

            else -> {
                Log.d("UI", "⏳ UI in loading state - waiting for data")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }


    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HostedCard(
    navController: NavController,
    event: Event,
    onCancelReservation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            AsyncImage(
                model = event.fullImgSrc,
                contentDescription = event.partyname,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.defaultimg)
            )

            Spacer(Modifier.height(16.dp))

            Text(text = event.partyname, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "${event.formattedDate} @ ${event.formattedTime}", color = MaterialTheme.colorScheme.secondaryContainer)

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.location), fontWeight = FontWeight.Bold)
                    Text(text = event.location)
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.event_type), fontWeight = FontWeight.Bold)
                    Text(
                        text = getPartyTypeText(event.partytype),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.people_attending), fontWeight = FontWeight.Bold)
                    Text(text = "${event.totalslots.toInt() - event.openslots.toInt()}")
                }



                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.open_slots3), fontWeight = FontWeight.Bold)
                    Text(text = event.openslots)
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.total_slots), fontWeight = FontWeight.Bold)
                    Text(text = event.totalslots)
                }


            }




            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = { navController.navigate("approve_reservations")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.approve_reservations), color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))


            Button(
                onClick = { navController.navigate("check_in")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.check_in_guests), color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            /*
            Button(
                onClick = onCancelReservation,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel Event", color = Color.White)
            }

             */
        }
    }
}
