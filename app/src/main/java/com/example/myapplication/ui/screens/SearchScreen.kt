package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.components.FilterDropdown
import com.example.myapplication.components.SearchCourseCard
import com.example.myapplication.components.SemesterDropdown
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.SearchViewModel


@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) { // import viewmodel in

    val searchBarText by viewModel.searchingText.collectAsState() // what is being typed into it
    val filteredCourses by viewModel.filteredCourses.collectAsState() // filtered courses
    val selectedFilters by viewModel.selectedFilters.collectAsState() // get dropdown filters
    val addedCourses by viewModel.addedCourses.collectAsState() // get added courses


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F2))
            .padding(start = 16.dp, end = 16.dp, top = 20.dp),

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Find courses",
                fontFamily = Fraunces,
                fontWeight = FontWeight(600),
                fontSize = 28.sp,
            )
            Box() {
                SemesterDropdown(viewModel)
            }
        }


        // 2. Search Bar
        OutlinedTextField(
            value = searchBarText,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = { Text("Search courses, depts, professors...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFFE5E0D4),
                focusedBorderColor = Color(0xFF8B1818)
            )
        )

        LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
            item {
                FilterDropdown(
                    label = "Distributions",
                    options = listOf("ALC-AS", "SBA-AS", "HA-AS"),
                    selectedOption = selectedFilters["Distributions"], // current selection
                    onOptionSelected = { viewModel.dropdownFilter("Distributions", it) }
                )
            }
            item {
                FilterDropdown(
                    label = "Subject",
                    options = listOf("INFO", "HIST", "CS"),
                    selectedOption = selectedFilters["Subject"],
                    onOptionSelected = { viewModel.dropdownFilter("Subject", it) }
                )
            }
            item {
                FilterDropdown(
                    label = "Credits",
                    options = listOf("1", "2", "3", "4+"),
                    selectedOption = selectedFilters["Credits"],
                    onOptionSelected = { viewModel.dropdownFilter("Credits", it) }
                )
            }
            item {
                FilterDropdown(
                    label = "Level",
                    options = listOf("1000s", "2000s", "3000s", "4000+"),
                    selectedOption = selectedFilters["Level"],
                    onOptionSelected = { viewModel.dropdownFilter("Level", it) }
                )
            }
            item {
                FilterDropdown(
                    label = "Days",
                    options = listOf("M/W/F", "Tu/Th"),
                    selectedOption = selectedFilters["Days"],
                    onOptionSelected = { viewModel.dropdownFilter("Days", it) }
                )
            }
            item {
                FilterDropdown(
                    label = "Time",
                    options = listOf("8AM - 11:59AM", "12PM - 4:59PM", "5PM - 11:59PM"),
                    selectedOption = selectedFilters["Time"],
                    onOptionSelected = { viewModel.dropdownFilter("Time", it) }
                )
            }
        }

        LazyColumn() {
            items(filteredCourses) { course ->
                val courseId = "${course.department}${course.courseNumber}"

                SearchCourseCard(
                    course = course,
                    isAdded = addedCourses.contains(courseId), // Passes true/false to the card
                    onAddClick = { viewModel.addOrDeleteCourse(course) } // Triggers the toggle
                )
            }
        }

    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}