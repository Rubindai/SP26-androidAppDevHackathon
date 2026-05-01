package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.RequirementCheckItem
import com.example.myapplication.data.model.Student
import com.example.myapplication.ui.theme.Fraunces

@Composable
fun RequirementsChecklistScreen(
    student: Student,
    onToggleCourse: (String) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F2))
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Before you start planning...",
            fontFamily = Fraunces,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Which of the following AP classes did you get a 5 in?\n" +
                    "This will help us determine what requirements you have already fulfilled.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        RequirementCheckItem(
            label = "AP Computer Science A",
            isChecked = student.completed.contains("CS1110"),
            onClicked = { onToggleCourse("CS1110") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RequirementCheckItem(
            label = "AP Calculus BC",
            isChecked = student.completed.contains("MATH1910"),
            onClicked = { onToggleCourse("MATH1910") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RequirementCheckItem(
            label = "AP Chemistry",
            isChecked = student.completed.contains("CHEM2090"),
            onClicked = { onToggleCourse("CHEM2090") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RequirementCheckItem(
            label = "AP Physics C: Mechanics",
            isChecked = student.completed.contains("PHYS1110"),
            onClicked = { onToggleCourse("PHYS1110") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RequirementCheckItem(
            label = "AP Physics C: Electricity and Magnetism",
            isChecked = student.completed.contains("PHYS2213"),
            onClicked = { onToggleCourse("PHYS2213") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RequirementCheckItem(
            label = "AP English Language / AP English Literature and Composition",
            isChecked = student.completed.contains("FWS"),
            onClicked = { onToggleCourse("FWS") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Final Button to move to Dashboard
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1818))
        ) {
            Text("Start Planning", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}