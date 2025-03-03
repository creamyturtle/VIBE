package com.example.vibe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vibe.R


@Composable
fun MapBottomAppBar(
    navController: NavController,
    selectedLanguage: String,
    onFilterSelected: (String) -> Unit
) {

    val density = LocalDensity.current.density
    val onePixel = 1f / density

    var selectedFilter by remember { mutableStateOf<String?>("all") }

    //val selectedLanguage by languageViewModel.language.collectAsState()


    key(selectedLanguage) {

        Column {
            // Separator bar (1px light grey)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(onePixel.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant) // Light grey separator
            )

            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .height(104.dp),
                contentPadding = PaddingValues(0.dp)
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top
                ) {

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                        MapBottomNavItem(
                            "all",
                            Icons.Default.Public,
                            stringResource(R.string.all_events),
                            selectedFilter,
                            navController
                        ) {
                            selectedFilter = "all"
                            onFilterSelected("All Events")
                        }
                    }

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                        MapBottomNavItem(
                            "House",
                            Icons.Default.Home,
                            stringResource(R.string.house_parties),
                            selectedFilter,
                            navController
                        ) {
                            selectedFilter = "House"
                            onFilterSelected("House")
                        }
                    }

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                        MapBottomNavItem(
                            "Finca",
                            Icons.Default.Agriculture,
                            stringResource(R.string.finca_parties),
                            selectedFilter,
                            navController
                        ) {
                            selectedFilter = "Finca"
                            onFilterSelected("Finca")
                        }
                    }

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                        MapBottomNavItem(
                            "Pool",
                            Icons.Default.Pool,
                            stringResource(R.string.pool_parties),
                            selectedFilter,
                            navController
                        ) {
                            selectedFilter = "Pool"
                            onFilterSelected("Pool")
                        }
                    }

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                        MapBottomNavItem(
                            "Activity",
                            Icons.AutoMirrored.Filled.DirectionsBike,
                            stringResource(R.string.activities),
                            selectedFilter,
                            navController
                        ) {
                            selectedFilter = "Activity"
                            onFilterSelected("Activity")
                        }
                    }


                }

            }


        }
    }



}
