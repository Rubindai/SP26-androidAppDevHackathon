package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.ProgressStatus
import com.example.myapplication.data.model.RequirementItem

@Composable
fun RequirementItemRow(requirementItem: RequirementItem) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatusDot(requirementItem.status)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = requirementItem.label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1C1A16),
            )

            Text(
                text = requirementItem.course,
                fontSize = 12.sp,
                color = Color(0xFF6C665C),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 1.dp),
            )
        }

        StatusTag(requirementItem.status)


    }

}


@Preview
@Composable
fun PreviewRequirementItemRow() {
    RequirementItemRow(
        RequirementItem(
            "First-Year Writing I",
            ProgressStatus.COMPLETE,
            "ENGL 1170"
        ),
    )
}