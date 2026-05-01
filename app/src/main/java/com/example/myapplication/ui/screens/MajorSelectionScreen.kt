package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.OnboardingUiState
import com.example.myapplication.viewmodel.SupportedMajor
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun MajorSelectionScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onNext: () -> Unit,
) {
    val student by viewModel.studentState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val submitting = uiState is OnboardingUiState.Submitting

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F2))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Choose your major",
            fontFamily = Fraunces,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1A16),
        )
        Text(
            text = "BigRedPath currently supports requirement rules for these majors.",
            fontSize = 15.sp,
            color = Color(0xFF4E473F),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
        )

        SupportedMajor.options.forEach { major ->
            MajorOptionRow(
                major = major,
                selected = student.major == major.code,
                onClick = { viewModel.updateMajor(major.code) },
            )
            Spacer(Modifier.height(10.dp))
        }

        (uiState as? OnboardingUiState.Error)?.let { err ->
            Text(
                text = err.message,
                color = Color(0xFF8B1818),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { viewModel.finishOnboarding(onNext) },
            enabled = !submitting && student.major.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1818)),
            shape = RoundedCornerShape(12.dp),
        ) {
            if (submitting) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.height(20.dp),
                )
            } else {
                Text("Continue", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun MajorOptionRow(
    major: SupportedMajor,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color(0xFFFFF5F3) else Color.White)
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF8B1818) else Color(0xFFC9C4B9),
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF8B1818)),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = major.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1A16),
            )
            Text(
                text = major.description,
                fontSize = 13.sp,
                color = Color(0xFF4E473F),
                modifier = Modifier.padding(top = 3.dp),
            )
        }
        Text(
            text = major.code,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B1818),
        )
    }
}
