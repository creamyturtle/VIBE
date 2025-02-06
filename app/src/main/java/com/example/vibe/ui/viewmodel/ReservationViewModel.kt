package com.example.vibe.ui.viewmodel

/*
class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {
    var reservationState: ReservationState by mutableStateOf(ReservationState.Idle)
        private set

    fun makeReservation(eventId: String, userDetails: String) {
        viewModelScope.launch {
            reservationState = ReservationState.Loading
            try {
                reservationRepository.makeReservation(eventId, userDetails)
                reservationState = ReservationState.Success
            } catch (e: Exception) {
                reservationState = ReservationState.Error("Failed to make reservation.")
            }
        }
    }
}
*/