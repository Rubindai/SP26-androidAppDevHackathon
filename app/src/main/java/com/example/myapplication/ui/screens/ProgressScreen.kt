package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.components.ProgressCircle
import com.example.myapplication.components.RequirementCard
import com.example.myapplication.components.StatInline
import com.example.myapplication.data.model.ProgressStatus
import com.example.myapplication.data.remote.RequirementSource
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.ProgressViewModel


@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ProgressContent(state = uiState)
}

@Composable
fun ProgressContent(state: ProgressViewModel.ProgressUiState) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F2))
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "Degree Progress · ${state.major}".uppercase(),
                fontWeight = FontWeight(700),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF8B1818),
                modifier = Modifier
                    .padding(start = 3.dp, bottom = 10.dp)
            )
            Text(
                text = "You're on track for",
                fontWeight = FontWeight(600),
                fontFamily = Fraunces,
                fontSize = 36.sp
            )
            Text(
                text = state.targetTerm,
                fontWeight = FontWeight(500),
                fontStyle = FontStyle.Italic,
                fontFamily = Fraunces,
                fontSize = 36.sp,
                color = Color(0xFF8B1818),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box() {
                    ProgressCircle(progress = state.percent)
                }
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 5.dp),
                    ) {
                        Text(
                            text = "${state.completedCredits} / ${state.totalCreditsTarget} credits",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 18.sp
                        )

                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        StatInline(state.complete, "Done", Color(0xFF2E7D45))
                        StatInline(state.inProgress, "Active", Color(0xFF1F5A88))
                        StatInline(state.left, "Left", Color(0xFFC9C4B9))
                    }

                }

            }

            HorizontalDivider(
                color = Color(0xFFE5E0D4),
                modifier = Modifier
                    .padding(16.dp)
            )
        }


        LazyColumn() {
            items(state.requirements) { req ->
                RequirementCard(req)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewDashBoard() {
    val sampleRequirements = RequirementSource.requirements
    val items = sampleRequirements.flatMap { it.items }
    val complete = items.count { it.status == ProgressStatus.COMPLETE }
    val inProgress = items.count { it.status == ProgressStatus.IN_PROGRESS }
    val percent = (complete + inProgress * 0.5f) / items.size

    ProgressContent(
        state = ProgressViewModel.ProgressUiState(
            major = "Computer Science",
            requirements = sampleRequirements,
            percent = percent,
            complete = complete,
            inProgress = inProgress,
            left = items.size - complete - inProgress,
            targetTerm = "Spring 2026",
            onPaceTerm = "Spring 2028",
            completedCredits = 47,
            totalCreditsTarget = 120,
        )
    )
}
