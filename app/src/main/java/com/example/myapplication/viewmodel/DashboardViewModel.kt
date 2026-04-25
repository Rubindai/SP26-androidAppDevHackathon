package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.UserProgressState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {
    // hardcoded so far
    private val _uiState = MutableStateFlow(
        UserProgressState(
            name = "Bob",
            major = "UNDECLARED",
            college = "DUFFIELD ENGINEERING",
            gradYear = 2028,
            chosenSemester = "Fall 2026",
            currentCredits = 16,
            totalCredits = 47,
            creditsNeeded = 120
        )
    )
    val uiState: StateFlow<UserProgressState> = _uiState.asStateFlow()

    fun getCreditPercentage(state: UserProgressState): Float {
        return state.totalCredits.toFloat() / state.creditsNeeded.toFloat()
    }
}