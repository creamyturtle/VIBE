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

package com.example.vibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.vibe.data.AuthRepository
import com.example.vibe.data.DefaultAppContainer
import com.example.vibe.ui.components.BottomBar
import com.example.vibe.ui.components.TopBar
import com.example.vibe.ui.screens.EventsViewModel
import com.example.vibe.utils.SessionManager
import kotlin.math.max
import kotlin.math.min


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VibeApp() {

    val listState = rememberLazyListState()

        // CODE FOR ANIMATED APP BAR

        var appBarOffset by remember { mutableStateOf(0f) }
        var previousOffset by remember { mutableStateOf(0) }

        LaunchedEffect(listState.firstVisibleItemScrollOffset, listState.firstVisibleItemIndex) {
            val currentOffset = listState.firstVisibleItemScrollOffset
            val scrollDelta = currentOffset - previousOffset

            // Update the appBarOffset based on scroll direction
            appBarOffset = when {
                scrollDelta > 0 -> min(148f, appBarOffset + scrollDelta) // Scrolling down
                scrollDelta < 0 -> max(0f, appBarOffset + scrollDelta)  // Scrolling up
                else -> appBarOffset
            }

            // Update the previous offset for the next comparison
            previousOffset = currentOffset
        }

        val animatedOffset by animateDpAsState(
            targetValue = appBarOffset.dp,
            animationSpec = tween(durationMillis = 600) // Smooth animation
        )


    val navController = rememberNavController()

    val context = LocalContext.current

    val appContainer = remember { DefaultAppContainer(context) } // ✅ Initialize AppContainer
    val authRepository = remember { AuthRepository(appContainer.authApi, SessionManager(context)) }

    val eventsViewModel: EventsViewModel = viewModel(factory = EventsViewModel.Factory)


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController, animatedOffset, eventsViewModel) }

    ) { innerPadding ->


        VibeNavHost(
            navController = navController,
            listState = listState,
            innerPadding = innerPadding,
            eventsViewModel = eventsViewModel,
            authRepository = authRepository,
            signupApi = appContainer.signupApi,
            animatedOffset = animatedOffset
        )


    }
}







