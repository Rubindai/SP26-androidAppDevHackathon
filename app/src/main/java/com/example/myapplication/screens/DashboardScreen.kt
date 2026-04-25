package com.example.myapplication.screens

import android.R.attr.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.components.ProgressCircle
import com.example.myapplication.model.UserProgressState
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.viewmodel.DashboardViewModel


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val creditProgress = viewModel.getCreditPercentage(uiState)
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
                text = "${uiState.major} | ${uiState.college} ${uiState.gradYear} ",
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
                text = uiState.chosenSemester,
                fontWeight = FontWeight(500),
                fontStyle = FontStyle.Italic,
                fontFamily = Fraunces,
                fontSize = 36.sp,
                color = Color(0xFF8B1818),
            )

            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
            ) {
                Box() {
                    ProgressCircle(progress = creditProgress)
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
                            text = "${uiState.totalCredits} / ${uiState.creditsNeeded}",
                            fontWeight = FontWeight(600),
                        )
                        Text(
                            text = " credits",
                            fontWeight = FontWeight(500),
                        )
                    }

                    Text(
                        text = "Currently taking ${uiState.currentCredits} credits... other info here",
                    )


                }

            }



        }
    }
}
