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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.vibe.data.AuthRepository
import com.example.vibe.data.DefaultAppContainer
import com.example.vibe.data.LanguageViewModelFactory
import com.example.vibe.data.UserViewModelFactory
import com.example.vibe.ui.components.BottomBar
import com.example.vibe.ui.components.RightSideDrawer
import com.example.vibe.ui.components.TopBar
import com.example.vibe.ui.viewmodel.AuthViewModel
import com.example.vibe.ui.viewmodel.EventsViewModel
import com.example.vibe.ui.viewmodel.LanguageViewModel
import com.example.vibe.ui.viewmodel.RSVPViewModel
import com.example.vibe.ui.viewmodel.UserViewModel
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VibeApp() {

    //val listState = rememberLazyListState()

    val navController = rememberNavController()

    val context = LocalContext.current

    val appContainer = remember { DefaultAppContainer(context) } // ✅ Initialize AppContainer

    val authRepository = remember { AuthRepository(appContainer.authApi, appContainer.sessionManager) } // ✅ Inject properly
    val authViewModel = remember { AuthViewModel(appContainer.sessionManager, authRepository) } // ✅ Pass AuthRepository

    val rsvpViewModel = remember { RSVPViewModel(appContainer.rsvpApi, appContainer.sessionManager) }

    //val userViewModel = remember { UserViewModel(appContainer.userApi, appContainer.sessionManager, context) }

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(appContainer.userApi, appContainer.sessionManager, context)
    )


    val eventsViewModel: EventsViewModel = viewModel(factory = EventsViewModel.Factory)

    val listState = eventsViewModel.listState



    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val isDrawerOpen = remember { mutableStateOf(false) }

    val systemLanguage = Locale.getDefault().language // Detect system language

    val languageViewModel: LanguageViewModel = viewModel(factory = LanguageViewModelFactory(context, appContainer.sessionManager))

    val savedLanguage = appContainer.sessionManager.getLanguage()

    if (savedLanguage == "ES") {
        languageViewModel.setLanguage(context, "ES")
    } else if (savedLanguage == "EN") {
        languageViewModel.setLanguage(context, "EN")
    } else if (systemLanguage == "es") {
        languageViewModel.setLanguage(context, "ES") // Default to Spanish if system is Spanish
    } else {
        languageViewModel.setLanguage(context, "EN") // Default to English otherwise
    }





    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopBar(navController, isDrawerOpen, listState) },
            bottomBar = { BottomBar(navController, isLoggedIn, eventsViewModel, authViewModel, languageViewModel) }

        ) { innerPadding ->


            VibeNavHost(
                navController = navController,
                listState = listState,
                innerPadding = innerPadding,
                eventsViewModel = eventsViewModel,
                signupApi = appContainer.signupApi,
                authViewModel = authViewModel,
                rsvpViewModel = rsvpViewModel,
                userViewModel = userViewModel,
                context = context,
                languageViewModel = languageViewModel
            )


        }
        RightSideDrawer(isDrawerOpen, navController, isLoggedIn, authViewModel, context, languageViewModel)
    }



}







