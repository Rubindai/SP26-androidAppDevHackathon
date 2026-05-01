package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.ProgressStatus

@Composable
fun StatusTag(status: ProgressStatus, modifier: Modifier = Modifier) {
    val label: String
    val bg: Color
    val fg: Color
    when (status) {
        ProgressStatus.COMPLETE -> {
            label = "Complete"
            bg = Color(0xFFE3F1E8)  // success soft
            fg = Color(0xFF2E7D45)  // success fg
        }

        ProgressStatus.IN_PROGRESS -> {
            label = "In Progress"
            bg = Color(0xFFE0ECF5)  // info soft
            fg = Color(0xFF1F5A88)  // info fg
        }

        ProgressStatus.MISSING -> {
            label = "Missing"
            bg = Color(0xFFF3EFE6)  // paper shadow
            fg = Color(0xFF6C665C)  // ink soft
        }

        ProgressStatus.RECOMMENDED -> {
            label = "Recommended"
            bg = Color(0xFFFAECD0)  // warn soft
            fg = Color(0xFFA76F18)  // warn fg
        }
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(fg),
        )
        Text(
            text = label.uppercase(),
            color = fg,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatusTagPreview() {

    Row(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatusTag(ProgressStatus.COMPLETE)
        StatusTag(ProgressStatus.IN_PROGRESS)
        StatusTag(ProgressStatus.MISSING)
        StatusTag(ProgressStatus.RECOMMENDED)
    }
}
