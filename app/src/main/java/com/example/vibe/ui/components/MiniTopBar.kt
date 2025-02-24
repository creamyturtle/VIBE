package com.example.vibe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.R
import com.example.vibe.ui.viewmodel.LanguageViewModel

@Composable
fun MiniTopBar(
    navController: NavController,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit

) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top
        ) {

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                EventBottomNavItem(
                    "all",
                    Icons.Default.Public,
                    stringResource(R.string.all_events),
                    selectedFilter,
                    navController
                ) {
                    //selectedFilter
                    onFilterSelected("all")
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                EventBottomNavItem(
                    "House",
                    Icons.Default.Home,
                    stringResource(R.string.house_parties),
                    selectedFilter,
                    navController
                ) {
                    //selectedFilter = "House"
                    onFilterSelected("House")
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                EventBottomNavItem(
                    "Finca",
                    Icons.Default.Agriculture,
                    stringResource(R.string.finca_parties),
                    selectedFilter,
                    navController
                ) {
                    //selectedFilter = "Finca"
                    onFilterSelected("Finca")
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                EventBottomNavItem(
                    "Pool",
                    Icons.Default.Pool,
                    stringResource(R.string.pool_parties),
                    selectedFilter,
                    navController
                ) {
                    //selectedFilter = "Pool"
                    onFilterSelected("Pool")
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                EventBottomNavItem(
                    "Activity",
                    Icons.AutoMirrored.Filled.DirectionsBike,
                    stringResource(R.string.activities),
                    selectedFilter,
                    navController
                ) {
                    //selectedFilter = "Activity"
                    onFilterSelected("Activity")
                }
            }


        }

}