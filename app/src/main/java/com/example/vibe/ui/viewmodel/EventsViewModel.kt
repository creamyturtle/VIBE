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

package com.example.vibe.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vibe.VibeApplication
import com.example.vibe.data.EventsRepository
import com.example.vibe.model.Event
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface EventsUiState {
    data class Success(val events: List<Event>) : EventsUiState
    object Error : EventsUiState
    object Loading : EventsUiState
}

/**
 * ViewModel containing the app data and method to retrieve the data
 */
class EventsViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    var eventsUiState: EventsUiState by mutableStateOf(EventsUiState.Loading)
        private set

    var selectedEvent: Event? by mutableStateOf(null)
        private set

    init {
        if (eventsUiState !is EventsUiState.Success) {
            getEvents() // ✅ Ensure events load on ViewModel creation
        }
    }

    fun getEvents() {
        viewModelScope.launch {
            eventsUiState = EventsUiState.Loading
            eventsUiState = try {
                EventsUiState.Success(eventsRepository.getEvents())
            } catch (e: IOException) {
                EventsUiState.Error
            } catch (e: HttpException) {
                EventsUiState.Error
            }
        }
    }


    fun getEventsByType(type: String) {
        viewModelScope.launch {
            eventsUiState = EventsUiState.Loading
            eventsUiState = try {
                EventsUiState.Success(eventsRepository.getEventsByType(type))
            } catch (e: Exception) {
                EventsUiState.Error
            }
        }
    }

    fun selectEvent(eventId: String) {
        val events = (eventsUiState as? EventsUiState.Success)?.events
        selectedEvent = events?.find { it.id == eventId }
    }

    /**
     * Factory for [EventsViewModel] that takes [EventsRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as VibeApplication)
                val eventsRepository = application.container.eventsRepository
                EventsViewModel(eventsRepository = eventsRepository)
            }
        }
    }
}
