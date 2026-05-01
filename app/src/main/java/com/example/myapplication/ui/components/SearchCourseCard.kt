package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Fraunces

data class courseInformation(
    val name: String,
    val department: String,
    val courseNumber: String,
    val credits: Int,
    val description: String,
    val prerequisites: List<String>,
    val corequisites: List<String>,
    val distributions: List<String>,
    val year: Int,
    val semester: String,
    val instructor: String,
    val days: String,
    val time: String,
    val location: String,
    val open: Boolean
)

@Composable
fun AddCourseButton(
    isAdded: Boolean,
    onClick: () -> Unit
) {

    var color = Color(0xFFD1E7DD)
    if (isAdded) {
        color = Color(0xFFD1E7DD) // green
    } else {
        color = Color(0xFF8B1818) // red
    }

    var icon = Icons.Default.Check
    if (isAdded) {
        icon = Icons.Default.Check
    } else {
        icon = Icons.Default.Add
    }

    var text = "Add Course"
    if (isAdded) {
        text = "Remove Course"
    } else {
        text = "Add Course"
    }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(start = 5.dp, end = 5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SearchCourseCard(
    course: courseInformation,
    isAdded: Boolean,
    onAddClick: () -> Unit
) {
    var openOrClosed = ""
    var colorTag = Color(0xFF2E7D45)
    var colorTagBackground = Color(0xFFE3F1E8)
    if (course.open) {
        openOrClosed = "OPEN"
        colorTag = Color(0xFF2E7D45)
        colorTagBackground = Color(0xFFE3F1E8)
    } else {
        openOrClosed = "CLOSED"
        colorTag = Color(0xFF8B1818)
        colorTagBackground = Color(0xFFFDF0ED)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 50.dp, height = 50.dp) // Specific rectangular shape
                    .background(
                        color = Color(0xFFFDF0ED),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // the red box on the left
                Text(
                    text = course.department,
                    fontFamily = Fraunces,
                    color = Color(0xFF8B1818),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700)
                )
            }

            // info

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f) // how much remaining space to use
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${course.department} ${course.courseNumber} | ${course.credits} cr",
                        fontFamily = FontFamily.Monospace,
                        color = Color.Gray,
                        fontWeight = FontWeight(600),
                        fontSize = 12.sp
                    )

                    Box(
                        modifier = Modifier
                            .size(width = 70.dp, height = 20.dp) // Specific rectangular shape
                            .padding(start = 10.dp)
                            .background(
                                color = colorTagBackground,
                                shape = RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = openOrClosed,
                            fontFamily = FontFamily.Monospace,
                            color = colorTag,
                            fontWeight = FontWeight(600),
                            fontSize = 12.sp
                        )
                    }
                }

                Text(
                    text = course.name,
                    fontFamily = Fraunces,
                    fontWeight = FontWeight(500),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                )

                // date and time
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Class Time",
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 5.dp),
                        tint = Color(0xFF6C665C),
                    )

                    Text(
                        text = "${course.days} ${course.time}",
                        color = Color.DarkGray
                    )
                }
                // prof
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Class Time",
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 5.dp),
                        tint = Color(0xFF6C665C),
                    )

                    Text(
                        text = course.instructor,
                        color = Color.DarkGray
                    )
                }
            }
            // add button
            AddCourseButton(
                isAdded = isAdded,
                onClick = onAddClick
            )
        }

    }
}