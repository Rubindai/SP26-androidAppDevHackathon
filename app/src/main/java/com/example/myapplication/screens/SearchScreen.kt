package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.components.FilterDropdown
import com.example.myapplication.components.SearchCourseCard
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.SearchViewModel


@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) { // import viewmodel in

    val searchBarText by viewModel.searchingText.collectAsState() // what is being typed into it
    val filteredCourses by viewModel.filteredCourses.collectAsState() // filtered courses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F2))
            .padding(start = 16.dp, end = 16.dp, top = 20.dp),
    ) {
        Text(
            text = "Find courses",
            fontFamily = Fraunces,
            fontWeight = FontWeight(600),
            fontSize = 28.sp,
        )

        Row(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 10.dp)
        ) {
//            Text(
//                text = "NEED",
//                fontFamily = FontFamily.SansSerif,
//                fontWeight = FontWeight(600),
//                color = Color(0xFF6C665C),
//            )
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
                FilterDropdown("Distributions", listOf("Any", "ALC-AS"))
            }
            item {
                FilterDropdown("Subject", listOf("Any", "there", "are", "too", "many",
                    "subjects", "there", "are", "too", "many",
                    "subjects"))
            }
            item {
                FilterDropdown("Credits", listOf("Any", "1", "2", "3", "4+"))
            }
            item {
                FilterDropdown("Level", listOf("Any", "1000s", "2000s", "3000+"))
            }
            item {
                FilterDropdown("Days", listOf("Any", "M/W/F", "Tu/Th"))
            }
            item {
                // need to change list
                FilterDropdown("When", listOf("Any", "Mornings", "Afternoons", "Evenings"))
            }
        }

        LazyColumn() {
            items(filteredCourses) { course ->
                SearchCourseCard(course = course)
            }
        }

    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}