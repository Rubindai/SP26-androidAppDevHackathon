package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class OnboardingRequirementViewModel @Inject constructor() : ViewModel() {
    private val _completedCourseIds = mutableStateListOf<String>()
    val completedCourseIds: List<String> = _completedCourseIds

    fun toggleCourse(courseId: String) {
        if (_completedCourseIds.contains(courseId)) {
            _completedCourseIds.remove(courseId)
        } else {
            _completedCourseIds.add(courseId)
        }
    }

    fun finishOnboarding() {
        // This will eventually call the repository you set up in your NetworkModule
        Log.d("Onboarding", "Ready to POST: $_completedCourseIds")
    }
}