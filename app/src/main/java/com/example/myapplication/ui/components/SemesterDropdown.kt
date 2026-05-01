package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.viewmodel.SearchViewModel

@Composable
fun SemesterDropdown(viewModel: SearchViewModel) {
    val selectedSemester by viewModel.selectedSemester.collectAsState()
    val semesters by viewModel.semesters.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(text = selectedSemester, color = Color(0xFF8B1818))
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF8B1818)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            semesters.forEach { term ->
                DropdownMenuItem(
                    text = { Text(term) },
                    onClick = {
                        viewModel.onSemesterChanged(term)
                        expanded = false
                    }
                )
            }
        }
    }
}

