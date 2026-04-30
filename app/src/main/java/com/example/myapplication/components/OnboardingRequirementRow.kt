package com.example.myapplication.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RequirementCheckItem(
    label: String,
    isChecked: Boolean,
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(10.dp)
            )
            .background(if (isChecked) Color(0xFFFDF7F7) else Color.White)
            .clickable { onClicked() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isChecked) Color(0xFF8B1818) else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
            color = if (isChecked) Color.Black else Color.DarkGray
        )
    }
}

@Preview
@Composable
private fun RequirementCheckItemPreview() {
    RequirementCheckItem(
        "wow",
        true,
        onClicked = {}
    )
}