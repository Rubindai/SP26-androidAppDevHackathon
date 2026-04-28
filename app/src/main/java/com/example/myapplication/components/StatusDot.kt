package com.example.myapplication.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.ProgressStatus


/**
 * 22dp circle that lives at the start of a requirement row. Filled with the
 * status color (with a small content icon), or transparent with a dashed
 * outlined border for the MISSING state — matches the web prototype's `StatusDot`.
 */
@Composable
fun StatusDot(status: ProgressStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        ProgressStatus.COMPLETE -> Color(0xFF2E7D45)
        ProgressStatus.IN_PROGRESS -> Color(0xFF1F5A88)
        ProgressStatus.RECOMMENDED -> Color(0xFFA76F18)
        ProgressStatus.MISSING -> Color(0xFF6C665C)
    }
    val isMissing = status == ProgressStatus.MISSING

    Box(
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .let {
                if (isMissing) it.border(2.dp, color, CircleShape)
                else it.background(color)
            },
        contentAlignment = Alignment.Center,
    ) {
        when (status) {
            ProgressStatus.COMPLETE -> Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp),
            )

            ProgressStatus.IN_PROGRESS -> Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.White),
            )

            ProgressStatus.RECOMMENDED -> Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp),
            )

            ProgressStatus.MISSING -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatusDotPreview() {

    Row(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatusDot(ProgressStatus.COMPLETE)
        StatusDot(ProgressStatus.IN_PROGRESS)
        StatusDot(ProgressStatus.RECOMMENDED)
        StatusDot(ProgressStatus.MISSING)
    }

}
