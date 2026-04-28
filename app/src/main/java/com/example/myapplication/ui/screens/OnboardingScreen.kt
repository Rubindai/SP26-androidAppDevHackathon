package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun OnboardingScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val student by viewModel.studentState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F2))
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to", // is this the name?
            fontSize = 28.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = Fraunces,
            fontStyle = FontStyle.Italic,
        )

        Text(
            text = "BigRedPath", // is this the name?
            fontSize = 40.sp,
            color = Color(0xFF8B1818),
            fontWeight = FontWeight.Bold,
            fontFamily = Fraunces
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sign in to get started.",
            fontSize = 16.sp,
            color = Color.Gray
        )

        // input here
        OnboardingTextField(
            label = "Full Name",
            value = student.name,
            onValueChange = { viewModel.updateName(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OnboardingTextField(
            label = "NetID",
            value = student.netid,
            onValueChange = { viewModel.updateNetId(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OnboardingTextField(
            label = "Major",
            value = student.major,
            onValueChange = { viewModel.updateMajor(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OnboardingTextField(
            label = "College",
            value = student.college,
            onValueChange = { viewModel.updateCollege(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.finishOnboarding(onClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1818)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Start Planning",
                color = Color.White,
                fontSize = 18.sp)
        }
    }
}

@Composable
fun OnboardingTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF8B1818),
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color(0xFF8B1818)
        )
    )
}