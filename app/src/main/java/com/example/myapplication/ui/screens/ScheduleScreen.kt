package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.myapplication.data.model.Block
import com.example.myapplication.data.model.BlockCategory
import com.example.myapplication.data.model.CalendarEvent
import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.Day
import com.example.myapplication.data.model.EventType
import com.example.myapplication.data.model.code
import com.example.myapplication.ui.components.WeekCalendar
import com.example.myapplication.ui.theme.Fraunces
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.ScheduleViewModel
import com.example.myapplication.viewmodel.ScheduleViewModel.ScheduleUiState
import com.example.myapplication.viewmodel.scheduleCalendarEvents

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
    onCourseClick: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    var editingBlock by remember { mutableStateOf<Block?>(null) }

    ScheduleContent(
        state = state,
        onAddBlock = { editingBlock = NewBlock },
        onEventClick = { event ->
            event.courseIdOrNull()?.let(onCourseClick)
            event.blockIdOrNull()
                ?.let { blockId -> state.blocks.firstOrNull { it.id == blockId } }
                ?.let { editingBlock = it }
        },
        onCourseRowClick = onCourseClick,
        onCourseRemove = viewModel::removeCourse,
    )

    editingBlock?.let { block ->
        BlockEditorSheet(
            initial = block.takeUnless { it == NewBlock },
            onDismiss = { editingBlock = null },
            onSave = {
                viewModel.upsertBlock(it)
                editingBlock = null
            },
            onDelete = {
                viewModel.deleteBlock(it)
                editingBlock = null
            },
        )
    }
}

@Composable
fun ScheduleContent(
    state: ScheduleUiState,
    onAddBlock: () -> Unit,
    onEventClick: (CalendarEvent) -> Unit,
    onCourseRowClick: (String) -> Unit,
    onCourseRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFBF8F2),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddBlock,
                containerColor = Color(0xFF8B1818),
                contentColor = Color.White,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Add to calendar", fontWeight = FontWeight.SemiBold) },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 120.dp),
        ) {
            item { Header(state) }
            item { CalendarSection(state, onEventClick) }
            item { CoursesHeading() }

            if (state.addedCourses.isEmpty()) {
                item { EmptyCourseList() }
            } else {
                items(state.addedCourses, key = { it.courseId }) { course ->
                    CourseRow(
                        course = course,
                        onClick = { onCourseRowClick(course.courseId) },
                        onRemove = { onCourseRemove(course.courseId) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(state: ScheduleUiState) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            text = "${state.term} · WEEKLY VIEW",
            color = Color(0xFF8B1818),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp,
        )
        Text(
            text = "Schedule builder",
            fontFamily = Fraunces,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 32.sp,
            color = Color(0xFF1C1A16),
            modifier = Modifier.padding(top = 6.dp),
        )
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SumStat(
                value = state.courseCount,
                label = "Courses",
                color = Color(0xFF8B1818),
                modifier = Modifier.weight(1f),
            )
            SumStat(
                value = state.totalCredits,
                label = "Credits",
                color = if (state.isOverCreditLoad) Color(0xFFA76F18) else Color(0xFF8B1818),
                modifier = Modifier.weight(1f),
            )
            SumStat(
                value = state.conflictPairCount,
                label = "Conflicts",
                color = if (state.hasConflict) Color(0xFFB42A2A) else Color(0xFF2E7D45),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SumStat(
    value: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E0D4), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(
            text = value.toString(),
            fontFamily = Fraunces,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            lineHeight = 22.sp,
        )
        Text(
            text = label.uppercase(),
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = Color(0xFF6C665C),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun CalendarSection(
    state: ScheduleUiState,
    onEventClick: (CalendarEvent) -> Unit,
) {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        WeekCalendar(
            events = state.events,
            conflictIds = state.conflictIds,
            onEventClick = onEventClick,
        )
    }
}

@Composable
private fun CoursesHeading() {
    Text(
        text = "Your courses",
        fontFamily = Fraunces,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1C1A16),
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 6.dp),
    )
}

@Composable
private fun EmptyCourseList() {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFC9C4B9), RoundedCornerShape(12.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Nothing added yet",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1C1A16),
        )
        Text(
            text = "Add courses from Search or block off time here.",
            fontSize = 12.sp,
            color = Color(0xFF6C665C),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun CourseRow(
    course: Courses,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E0D4), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(course.rowColor())),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = course.code,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF1C1A16),
            )
            Text(
                text = course.name,
                fontSize = 12.sp,
                color = Color(0xFF6C665C),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = "${course.credits} cr",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6C665C),
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove ${course.code}",
                tint = Color(0xFF6C665C),
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

private fun Courses.rowColor(): Long =
    if (open) 0xFFB31B1B else 0xFF775653

private fun CalendarEvent.courseIdOrNull(): String? =
    if (type == EventType.COURSE) id.split(":").getOrNull(1) else null

private fun CalendarEvent.blockIdOrNull(): String? =
    if (type == EventType.BLOCK) id.removePrefix("block:") else null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlockEditorSheet(
    initial: Block?,
    onDismiss: () -> Unit,
    onSave: (Block) -> Unit,
    onDelete: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isEditing = initial != null
    var label by remember { mutableStateOf(initial?.label ?: "") }
    var startHour by remember { mutableFloatStateOf(initial?.startHour ?: 9f) }
    var endHour by remember { mutableFloatStateOf(initial?.endHour ?: 10f) }
    var day by remember { mutableStateOf(initial?.day ?: Day.MON) }
    var category by remember { mutableStateOf(initial?.category ?: BlockCategory.OTHER) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = if (isEditing) "Edit block" else "Add a block",
                style = MaterialTheme.typography.headlineSmall,
            )
            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Label") },
                placeholder = { Text("Work study, club, study group") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            DayChips(selected = day, onSelect = { day = it })
            HourSlider(
                title = "Starts at",
                value = startHour,
                onValueChange = {
                    startHour = it
                    if (endHour <= it) endHour = (it + 0.5f).coerceAtMost(20f)
                },
                range = 8f..19.5f,
            )
            HourSlider(
                title = "Ends at",
                value = endHour,
                onValueChange = { endHour = it.coerceAtLeast(startHour + 0.5f) },
                range = (startHour + 0.5f)..20f,
            )
            CategoryChips(selected = category, onSelect = { category = it })
            ActionButtons(
                isEditing = isEditing,
                canSave = label.isNotBlank() && endHour > startHour,
                onSave = {
                    onSave(
                        Block(
                            id = initial?.id ?: "block-${System.currentTimeMillis()}",
                            day = day,
                            startHour = startHour,
                            endHour = endHour,
                            label = label.trim(),
                            category = category,
                        )
                    )
                },
                onDelete = initial?.let { { onDelete(it.id) } },
                onCancel = onDismiss,
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DayChips(
    selected: Day,
    onSelect: (Day) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Day", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Day.Week.forEach { day ->
                FilterChip(
                    selected = day == selected,
                    onClick = { onSelect(day) },
                    label = { Text(day.short) },
                )
            }
        }
    }
}

@Composable
private fun CategoryChips(
    selected: BlockCategory,
    onSelect: (BlockCategory) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Category", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            BlockCategory.entries.forEach { category ->
                FilterChip(
                    selected = category == selected,
                    onClick = { onSelect(category) },
                    label = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) },
                )
            }
        }
    }
}

@Composable
private fun HourSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Text(
                text = formatHourLabel(value),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF8B1818),
            )
        }
        Slider(
            value = value,
            onValueChange = { onValueChange(snapToHalfHour(it)) },
            valueRange = range,
            steps = ((range.endInclusive - range.start) * 2).toInt() - 1,
        )
    }
}

@Composable
private fun ActionButtons(
    isEditing: Boolean,
    canSave: Boolean,
    onSave: () -> Unit,
    onDelete: (() -> Unit)?,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (isEditing && onDelete != null) {
            OutlinedButton(
                onClick = onDelete,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB42A2A)),
            ) {
                Text("Delete")
            }
        }
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onCancel) { Text("Cancel") }
        Button(onClick = onSave, enabled = canSave) {
            Text(if (isEditing) "Save" else "Add")
        }
    }
}

private fun formatHourLabel(hour: Float): String {
    val whole = hour.toInt()
    val minutes = ((hour - whole) * 60).toInt()
    val display = ((whole + 11) % 12) + 1
    val period = if (whole < 12) "AM" else "PM"
    return if (minutes == 0) "$display:00 $period" else "%d:%02d %s".format(display, minutes, period)
}

private fun snapToHalfHour(value: Float): Float =
    (value * 2f).toInt() / 2f

private fun buildSampleScheduleState(): ScheduleUiState {
    val courses = emptyList<Courses>()
    val blocks = listOf(
        Block("seed-0", Day.MON, 16f, 18f, "Club: Cornell Outing", BlockCategory.CLUB),
        Block("seed-1", Day.WED, 16f, 18f, "Club: Cornell Outing", BlockCategory.CLUB),
        Block("seed-2", Day.TUE, 15f, 17f, "Work study", BlockCategory.WORK),
    )

    return ScheduleUiState(
        events = scheduleCalendarEvents(courses, blocks),
        totalCredits = courses.sumOf { it.credits },
        courseCount = courses.size,
        addedCourses = courses,
        blocks = blocks,
    )
}

private val NewBlock = Block(
    id = "new",
    day = Day.MON,
    startHour = 9f,
    endHour = 10f,
    label = "",
)

@Preview(name = "Schedule", showBackground = true, widthDp = 396, heightDp = 1200)
@Composable
private fun SchedulePreview() {
    MyApplicationTheme {
        ScheduleContent(
            state = buildSampleScheduleState(),
            onAddBlock = {},
            onEventClick = {},
            onCourseRowClick = {},
            onCourseRemove = {},
        )
    }
}
