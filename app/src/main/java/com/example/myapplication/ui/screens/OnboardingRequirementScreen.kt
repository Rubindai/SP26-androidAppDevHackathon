package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.code
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.CompletedCoursesUiState
import com.example.myapplication.viewmodel.OnboardingRequirementViewModel

@Composable
fun CompletedCoursesScreen(
    viewModel: OnboardingRequirementViewModel = hiltViewModel(),
    onDone: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val selectedIds = viewModel.completedCourseIds

    CompletedCoursesContent(
        state = state,
        selectedIds = selectedIds,
        onQueryChange = viewModel::onSearchQueryChanged,
        onToggleCourse = viewModel::toggleCourse,
        onRefresh = viewModel::refreshCourses,
        onDone = { viewModel.finishOnboarding(onDone) },
    )
}

@Composable
fun CompletedCoursesContent(
    state: CompletedCoursesUiState,
    selectedIds: List<String>,
    onQueryChange: (String) -> Unit,
    onToggleCourse: (String) -> Unit,
    onRefresh: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F2))
            .padding(20.dp),
    ) {
        Text(
            text = "Courses you have finished",
            fontFamily = Fraunces,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1A16),
        )
        Text(
            text = "Search and select completed Cornell courses so your dashboard starts with accurate progress.",
            fontSize = 15.sp,
            color = Color(0xFF4E473F),
            modifier = Modifier.padding(top = 8.dp, bottom = 18.dp),
        )

        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search by course code, title, or subject") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFFC9C4B9),
                focusedBorderColor = Color(0xFF8B1818),
            ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${selectedIds.size} selected",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1A16),
            )
            Text(
                text = "Showing ${state.courses.size}",
                color = Color(0xFF4E473F),
                fontSize = 13.sp,
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = Color(0xFF8B1818),
                        modifier = Modifier.align(Alignment.TopCenter).padding(top = 24.dp),
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Couldn't load courses", fontFamily = Fraunces, fontSize = 18.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(state.error, color = Color(0xFF4E473F), fontSize = 13.sp)
                        Spacer(Modifier.height(12.dp))
                        OutlinedButton(onClick = onRefresh) { Text("Retry") }
                    }
                }
                state.courses.isEmpty() -> {
                    Text(
                        text = "No matching courses",
                        color = Color(0xFF4E473F),
                        modifier = Modifier.align(Alignment.TopCenter).padding(top = 24.dp),
                    )
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.courses, key = { it.courseId }) { course ->
                            CompletedCourseRow(
                                course = course,
                                selected = selectedIds.contains(course.courseId),
                                onClick = { onToggleCourse(course.courseId) },
                            )
                        }
                    }
                }
            }
        }

        state.saveError?.let {
            Text(
                text = it,
                color = Color(0xFF8B1818),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 10.dp),
            )
        }

        Button(
            onClick = onDone,
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1818)),
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Text("Start Planning", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CompletedCourseRow(
    course: Courses,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Color(0xFFFFF5F3) else Color.White)
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF8B1818) else Color(0xFFC9C4B9),
                shape = RoundedCornerShape(10.dp),
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (selected) Color(0xFF8B1818) else Color(0xFF4E473F),
            modifier = Modifier.size(24.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = course.code,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B1818),
            )
            Text(
                text = course.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1A16),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 3.dp),
            )
        }
        Text(
            text = "${course.credits} cr",
            fontSize = 12.sp,
            color = Color(0xFF4E473F),
            fontWeight = FontWeight.SemiBold,
        )
    }
}
