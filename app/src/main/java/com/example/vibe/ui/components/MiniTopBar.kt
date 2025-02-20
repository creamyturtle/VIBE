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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MiniTopBar(
    navController: NavController,
    onFilterSelected: (String) -> Unit
) {

    val density = LocalDensity.current.density
    val onePixel = 1f / density

    var selectedFilter by remember { mutableStateOf<String?>("all") }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ) {

        MapBottomNavItem("all", Icons.Default.Public, "All Events", selectedFilter, navController) {
            selectedFilter = "all"
            onFilterSelected("All Events")
        }

        MapBottomNavItem(
            "House",
            Icons.Default.Home,
            "House Parties",
            selectedFilter,
            navController
        ) {
            selectedFilter = "House"
            onFilterSelected("House")
        }


        MapBottomNavItem(
            "Finca",
            Icons.Default.Agriculture,
            "Finca Parties",
            selectedFilter,
            navController
        ) {
            selectedFilter = "Finca"
            onFilterSelected("Finca")
        }

        MapBottomNavItem(
            "Pool",
            Icons.Default.Pool,
            "Pool Parties",
            selectedFilter,
            navController
        ) {
            selectedFilter = "Pool"
            onFilterSelected("Pool")
        }

        MapBottomNavItem(
            "Activity",
            Icons.AutoMirrored.Filled.DirectionsBike,
            "Activities",
            selectedFilter,
            navController
        ) {
            selectedFilter = "Activity"
            onFilterSelected("Activity")
        }


    }
}