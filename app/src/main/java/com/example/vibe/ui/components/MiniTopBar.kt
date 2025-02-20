package com.example.vibe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MiniTopBar(
    navController: NavController,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {

    //var selectedFilter2 by rememberSaveable { mutableStateOf<String?>("all") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ) {

        EventBottomNavItem(
            "all",
            Icons.Default.Public,
            "All Events",
            selectedFilter,
            navController
        ) {
            //selectedFilter2 = "all"
            onFilterSelected("All Events")
        }

        EventBottomNavItem(
            "House",
            Icons.Default.Home,
            "House Parties",
            selectedFilter,
            navController
        ) {
            //selectedFilter2 = "House"
            onFilterSelected("House")
        }


        EventBottomNavItem(
            "Finca",
            Icons.Default.Agriculture,
            "Finca Parties",
            selectedFilter,
            navController
        ) {
            //selectedFilter2 = "Finca"
            onFilterSelected("Finca")
        }

        EventBottomNavItem(
            "Pool",
            Icons.Default.Pool,
            "Pool Parties",
            selectedFilter,
            navController
        ) {
            //selectedFilter2 = "Pool"
            onFilterSelected("Pool")
        }

        EventBottomNavItem(
            "Activity",
            Icons.AutoMirrored.Filled.DirectionsBike,
            "Activities",
            selectedFilter,
            navController
        ) {
            //selectedFilter2 = "Activity"
            onFilterSelected("Activity")
        }


    }
}