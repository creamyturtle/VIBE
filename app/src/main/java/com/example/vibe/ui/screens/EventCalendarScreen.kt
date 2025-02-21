package com.example.vibe.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.vibe.model.Event
import com.example.vibe.ui.components.EventDetailsCard
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCalendarScreen(
    events: List<Event>,
    navController: NavController,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val today = remember { LocalDate.now() }

    // ✅ Track the selected date & selected event
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val selectedEvent = remember { mutableStateOf<Event?>(null) }

    // ✅ Track events happening on selectedDate
    val eventsOnSelectedDate = events.filter { it.date == selectedDate.value?.toString() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState())
            .padding(horizontal = 16.dp), // ✅ Add padding for better spacing
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(78.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 40.dp, bottom = 8.dp), // ✅ Left-aligned with padding
            horizontalArrangement = Arrangement.Start // ✅ Align to the left
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ✅ Month Title
        Text(
            text = YearMonth.now().plusMonths(pagerState.currentPage.toLong())
                .month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + YearMonth.now().year,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Swipeable Calendar
        HorizontalPager(count = 12, state = pagerState) { page ->
            val currentMonth = YearMonth.now().plusMonths(page.toLong())
            MonthCalendar(
                month = currentMonth,
                today = today,
                events = events,
                onDayClick = { date -> selectedDate.value = date } // ✅ Capture clicked date
            )
        }

        Spacer(Modifier.height(16.dp))

        // ✅ Event Popup when clicking a day with events
        if (selectedDate.value != null && eventsOnSelectedDate.isNotEmpty()) {
            EventSelectionPopup(
                events = eventsOnSelectedDate,
                onEventSelected = { selectedEvent.value = it },
                onDismiss = { selectedDate.value = null }
            )
        }

        Text(text = "Swipe right or left to change month")

        Spacer(Modifier.height(32.dp))



        // ✅ Show event details below the calendar
        selectedEvent.value?.let { event ->


            var isPressed by remember { mutableStateOf(false) }

            // ✅ Animate scale effect
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f, // Shrink when pressed
                animationSpec = tween(durationMillis = 100) // Smooth quick animation
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )

                    // ✅ Apply scale effect to the Box too!
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .shadow(10.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                tryAwaitRelease() // ✅ Wait for user to release
                                isPressed = false
                                navController.navigate("event_details/${event.id}") // ✅ Navigate AFTER release
                            }
                        )
                    }
            ) {
                EventDetailsCard(
                    event,
                    onClose = { selectedEvent.value = null }
                )
            }


        }

        Spacer(Modifier.height(120.dp))

    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCalendar(
    month: YearMonth,
    today: LocalDate,
    events: List<Event>,
    onDayClick: (LocalDate) -> Unit // ✅ Pass click event
) {
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfMonth = month.atDay(1).dayOfWeek.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.DarkGray)
    ) {
        // ✅ Weekday Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Calendar Grid
        Column {
            var currentDay = 1
            for (week in 0..5) {
                Row(Modifier.fillMaxWidth()) {
                    for (day in 1..7) {
                        if ((week == 0 && day < firstDayOfMonth) || currentDay > daysInMonth) {
                            Box(modifier = Modifier.weight(1f).aspectRatio(1f).border(1.dp, Color.LightGray))
                        } else {
                            val date = month.atDay(currentDay)
                            val hasEvent = events.any { it.date == date.toString() }
                            CalendarDay(date, hasEvent, date == today, Modifier.weight(1f), onClick = onDayClick)
                            currentDay++
                        }
                    }
                }
            }
        }
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDay(
    date: LocalDate,
    hasEvent: Boolean,
    isToday: Boolean,
    modifier: Modifier = Modifier,
    onClick: (LocalDate) -> Unit // ✅ Click event
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(1.dp, Color.LightGray)
            .clickable { onClick(date) } // ✅ Make the day clickable
            .background(
                when {
                    isToday -> Color.Blue
                    hasEvent -> Color.Red
                    else -> Color.Transparent
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = if (isToday) Color.White else Color.Black,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}


@Composable
fun EventSelectionPopup(
    events: List<Event>,
    onEventSelected: (Event) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Events on ${events.first().date}",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Surface(
                color = MaterialTheme.colorScheme.surface, // ✅ Ensures the background is white
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    events.forEach { event ->
                        Button(
                            onClick = {
                                onEventSelected(event)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = getColorByPartyType(event.partytype)
                            ),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(event.partyname, color = Color.White)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}



fun getColorByPartyType(partyType: String?): Color {
    return when (partyType) {
        "House" -> Color.Magenta
        "Pool" -> Color.Blue
        "Finca" -> Color.Green
        else -> Color.Red
    }
}


