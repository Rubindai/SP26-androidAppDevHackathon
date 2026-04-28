package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.ProgressStatus
import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class ProgressUiState(
    val major: String,
    val requirements: List<Requirement>,
    val percent: Float,
    val complete: Int,
    val inProgress: Int,
    val left: Int,
    val targetTerm: String,
    val onPaceTerm: String,
    val completedCredits: Int,
    val totalCreditsTarget: Int,
)

@OptIn(FlowPreview::class)
@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    //    private val _uiState = MutableStateFlow(ProgressUiState(
//
//    ))
    private val _uiState = MutableStateFlow(
        ProgressUiState(
            major = studentRepository.student().major,
            requirements = studentRepository.requirements(),
            percent = 0f,
            complete = 0,
            inProgress = 0,
            left = 0,
            targetTerm = studentRepository.student().targetTerm,
            onPaceTerm = "Spring 2028",
            completedCredits = studentRepository.completedCredits(),
            totalCreditsTarget = studentRepository.targetTotalCredits(),
        ),
    )
    val uiState = _uiState.asStateFlow()

    data class ProgressUiState(
        val major: String,
        val requirements: List<Requirement>,
        val percent: Float,
        val complete: Int,
        val inProgress: Int,
        val left: Int,
        val targetTerm: String,
        val onPaceTerm: String,
        val completedCredits: Int,
        val totalCreditsTarget: Int,
    ) {


        /** Progress shown as 0–100 for the ring's center label. */
        val percentLabel: Int
            get() = (percent * 100).toInt()
    }

    init {
        // Compute the progress counts from the requirement list and patch them in.
        val items = _uiState.value.requirements.flatMap { it.items }
        val complete = items.count { it.status == ProgressStatus.COMPLETE }
        val inProgress = items.count { it.status == ProgressStatus.IN_PROGRESS }
        val percent = if (items.isEmpty()) 0f else (complete + inProgress * 0.5f) / items.size

        _uiState.value = _uiState.value.copy(
            percent = percent,
            complete = complete,
            inProgress = inProgress,
            left = items.size - complete - inProgress,
        )
    }

}


