package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _studentState = MutableStateFlow(Student())
    val studentState: StateFlow<Student> = _studentState.asStateFlow()

    fun updateName(name: String) {
        _studentState.value = _studentState.value.copy(name = name)
    }

    fun updateMajor(major: String) {
        _studentState.value = _studentState.value.copy(major = major)
    }

    fun updateNetId(netid: String) {
        _studentState.value = _studentState.value.copy(netid = netid)
    }

    fun updateCollege(college: String) {
        _studentState.value = _studentState.value.copy(college = college)
    }

    fun finishOnboarding(onComplete: () -> Unit) { // call this when Done button is clicked
        viewModelScope.launch {
            studentRepository.updateStudent(_studentState.value)
            onComplete()
        }
    }
}