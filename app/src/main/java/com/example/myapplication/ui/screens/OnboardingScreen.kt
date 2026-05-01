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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.myapplication.ui.theme.AppBorder
import com.example.myapplication.ui.theme.AppCream
import com.example.myapplication.ui.theme.AppMuted
import com.example.myapplication.ui.theme.AppRed
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.OnboardingUiState
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun OnboardingScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onNext: () -> Unit
) {
    val student by viewModel.studentState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val submitting = uiState is OnboardingUiState.Submitting

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppCream)
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
            color = AppRed,
            fontWeight = FontWeight.Bold,
            fontFamily = Fraunces
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sign in to get started.",
            fontSize = 16.sp,
            color = AppMuted
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
            label = "Year Graduating",
            value = student.year,
            onValueChange = { viewModel.updateYear(it) }
        )

        (uiState as? OnboardingUiState.Error)?.let { err ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = err.message,
                color = AppRed,
                fontSize = 13.sp,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.continueFromBasicInfo(onNext) },
            enabled = !submitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (submitting) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.height(20.dp),
                )
            } else {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontSize = 18.sp)
            }
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
            focusedBorderColor = AppRed,
            unfocusedBorderColor = AppBorder,
            focusedLabelColor = AppRed
        )
    )
}
