package com.example.vibe.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vibe.model.Event
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
    onBack: () -> Unit
) {

    val pagerState = rememberPagerState(initialPage = 0) // Control month navigation
    val today = remember { LocalDate.now() }



    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(78.dp))

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(8.dp, 40.dp, 0.dp, 0.dp)
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

        Spacer(Modifier.height(16.dp))


        // Month Title
        Text(
            text = YearMonth.now().plusMonths(pagerState.currentPage.toLong())
                .month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + YearMonth.now().year,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(count = 12, state = pagerState) { page ->
            val currentMonth = YearMonth.now().plusMonths(page.toLong())
            MonthCalendar(currentMonth, today, events)
        }

        Spacer(Modifier.height(32.dp))

        Text(text = "Swipe right or left to change month")


    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCalendar(month: YearMonth, today: LocalDate, events: List<Event>) {
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfMonth = month.atDay(1).dayOfWeek.value

    Column(
        modifier = Modifier
            .fillMaxWidth() // ✅ Ensures the entire width is used
            .border(1.dp, Color.DarkGray) // ✅ Adds a border around the whole calendar
    ) {
        // ✅ Weekday Labels (Ensures they match grid width)
        Row(
            modifier = Modifier.fillMaxWidth(), // ✅ Make sure this row takes full width
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f) // ✅ Ensures each label takes equal width
                        .padding(4.dp),
                    textAlign = TextAlign.Center // ✅ Center the text
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Calendar Grid (Now properly spaced)
        Column {
            var currentDay = 1
            for (week in 0..5) {
                Row(Modifier.fillMaxWidth()) {
                    for (day in 1..7) {
                        if ((week == 0 && day < firstDayOfMonth) || currentDay > daysInMonth) {
                            Box(
                                modifier = Modifier
                                    .weight(1f) // ✅ Ensures spacing matches weekday headers
                                    .aspectRatio(1f) // ✅ Keeps the boxes square
                                    .border(1.dp, Color.LightGray) // ✅ Adds grid lines
                            )
                        } else {
                            val date = month.atDay(currentDay)
                            val hasEvent = events.any { it.date == date.toString() }
                            CalendarDay(date, hasEvent, date == today, Modifier.weight(1f))
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
fun CalendarDay(date: LocalDate, hasEvent: Boolean, isToday: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f) // ✅ Ensures cells stay square
            .border(1.dp, Color.LightGray) // ✅ Adds grid lines
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

