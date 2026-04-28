package com.example.myapplication.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.remote.creation.dsl.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.ProgressStatus
import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.model.RequirementItem
import com.example.myapplication.ui.theme.Fraunces
import kotlin.compareTo
import kotlin.text.toFloat


@Composable
fun RequirementCard(requirement: Requirement){
    var open by remember { mutableStateOf(false) }
    val complete = requirement.items.count { it.status == ProgressStatus.COMPLETE }
    val total = requirement.items.size
    Column(Modifier
        .clip(RoundedCornerShape(14.dp))
        .background(color = Color.White)
        .fillMaxWidth()) {
    Row (  Modifier
        .clickable { open = !open }
        .fillMaxWidth()
        .padding(16.dp)
        .border(1.dp, Color(0xFFE5E0D4), RoundedCornerShape(14.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                requirement.title,
                fontFamily = Fraunces,
                fontSize = 17.sp

            )
            Text("$complete of $total complete",
                fontFamily = Fraunces,
                fontSize = 12.sp,
                color = Color(0xFF6C665C)
            )

            }
        Box(modifier = Modifier.width(72.dp)) {
            LinearProgressBar(progress = complete.toFloat() / total.toFloat())
        }
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = if (open) "Collapse" else "Expand",
            tint = Color(0xFF6C665C),
            modifier = Modifier
                .size(20.dp)
                .rotate(if (open) 180f else 0f),
        )

        }
    AnimatedVisibility(visible = open) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(color = Color(0xFFE5E0D4))
            requirement.items.forEachIndexed { idx, item ->
                if (idx > 0) HorizontalDivider(color = Color(0xFFE5E0D4))
                RequirementItemRow(item)
            }
        }
    }
    }
    }





private val sampleRequirement = Requirement(
    title = "Core & Foundation",
    items = listOf(
        RequirementItem("First-Year Writing I", ProgressStatus.COMPLETE, "ENGL 1170"),
        RequirementItem(
            "First-Year Writing II", ProgressStatus.MISSING,
            course = "random course"
        ),
//        RequirementItem("Quantitative Reasoning", ProgressStatus.COMPLETE, "MATH 1110"),
//        RequirementItem("Physical & Biological Sci", ProgressStatus.IN_PROGRESS, "BIOG 1140"),
//        RequirementItem(
//            "Intro to Humanities", ProgressStatus.RECOMMENDED,
//            course = "random course"
//        ),
    ),
)

@Preview
@Composable
fun PreviewRequireCard(){
    RequirementCard(requirement = sampleRequirement)
}