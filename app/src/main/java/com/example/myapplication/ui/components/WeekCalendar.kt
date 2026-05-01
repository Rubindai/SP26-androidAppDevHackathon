package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.CalendarEvent
import com.example.myapplication.data.model.Day
import com.example.myapplication.data.model.EventType
import com.example.myapplication.ui.theme.MyApplicationTheme

private val calendarCornerRadius = 14.dp
private val calendarOutline = Color(0xFFE5E0D4)
private val calendarSurface = Color.White
private val dayHeaderBackground = Color(0xFFF3EFE6)
private val scheduleText = Color(0xFF6C665C)
private val timeAxisWidth = 36.dp
private val dayHeaderHeight = 32.dp
private val eventCornerRadius = 6.dp
private val conflictBorderColor = Color(0xFFB42A2A)

@Composable
fun WeekCalendar(
    events: List<CalendarEvent>,
    conflictIds: Set<String>,
    onEventClick: (CalendarEvent) -> Unit,
    modifier: Modifier = Modifier,
    days: List<Day> = Day.Week,
    firstHour: Int = 8,
    lastHour: Int = 20,
    hourHeight: Dp = 44.dp,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(calendarCornerRadius))
            .background(calendarSurface)
            .border(1.dp, calendarOutline, RoundedCornerShape(calendarCornerRadius)),
    ) {
        DayHeaderRow(days = days)
        CalendarGrid(
            events = events,
            conflictIds = conflictIds,
            days = days,
            firstHour = firstHour,
            lastHour = lastHour,
            hourHeight = hourHeight,
            onEventClick = onEventClick,
        )
    }
}

@Composable
private fun CalendarGrid(
    events: List<CalendarEvent>,
    conflictIds: Set<String>,
    days: List<Day>,
    firstHour: Int,
    lastHour: Int,
    hourHeight: Dp,
    onEventClick: (CalendarEvent) -> Unit,
) {
    val visibleHours = lastHour - firstHour
    val gridHeight = hourHeight * visibleHours

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(gridHeight),
    ) {
        TimeAxis(
            firstHour = firstHour,
            lastHour = lastHour,
            hourHeight = hourHeight,
            gridHeight = gridHeight,
        )

        days.forEachIndexed { index, day ->
            DayColumn(
                events = events.filter { it.day == day },
                conflictIds = conflictIds,
                firstHour = firstHour,
                visibleHours = visibleHours,
                hourHeight = hourHeight,
                gridHeight = gridHeight,
                isLast = index == days.lastIndex,
                onEventClick = onEventClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun DayHeaderRow(days: List<Day>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(dayHeaderBackground)
            .height(dayHeaderHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(timeAxisWidth))
        days.forEach { day ->
            Text(
                text = day.short.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = scheduleText,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TimeAxis(
    firstHour: Int,
    lastHour: Int,
    hourHeight: Dp,
    gridHeight: Dp,
) {
    Column(
        modifier = Modifier
            .width(timeAxisWidth)
            .height(gridHeight)
            .timeAxisDivider(),
    ) {
        for (hour in firstHour until lastHour) {
            Box(
                modifier = Modifier
                    .height(hourHeight)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = formatHourShort(hour),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = scheduleText,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = if (hour == firstHour) 0.dp else (-5).dp)
                        .padding(end = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun DayColumn(
    events: List<CalendarEvent>,
    conflictIds: Set<String>,
    firstHour: Int,
    visibleHours: Int,
    hourHeight: Dp,
    gridHeight: Dp,
    isLast: Boolean,
    onEventClick: (CalendarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(gridHeight)
            .calendarGridLines(
                visibleHours = visibleHours,
                hourHeight = hourHeight,
                drawRightBorder = !isLast,
            ),
    ) {
        events.forEach { event ->
            EventBlock(
                event = event,
                isConflict = event.id in conflictIds,
                offsetY = eventTopOffset(event, firstHour, hourHeight),
                heightDp = eventHeight(event, hourHeight),
                onClick = { onEventClick(event) },
            )
        }
    }
}

@Composable
private fun EventBlock(
    event: CalendarEvent,
    isConflict: Boolean,
    offsetY: Dp,
    heightDp: Dp,
    onClick: () -> Unit,
) {
    val isCourse = event.type == EventType.COURSE
    val eventShape = RoundedCornerShape(eventCornerRadius)
    val background = if (isCourse) Color(event.color) else dayHeaderBackground
    val textColor = if (isCourse) Color.White else scheduleText

    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .fillMaxWidth()
            .height(heightDp)
            .padding(horizontal = 2.dp, vertical = 1.dp)
            .clip(eventShape)
            .background(background)
            .blockedTimeStripes(enabled = !isCourse)
            .conflictBorder(enabled = isConflict, shape = eventShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 5.dp, vertical = 4.dp),
    ) {
        Column {
            Text(
                text = event.label,
                color = textColor,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                lineHeight = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (heightDp > 32.dp) {
                event.sublabel?.let { sublabel ->
                    Text(
                        text = sublabel,
                        color = textColor.copy(alpha = 0.85f),
                        fontSize = 9.sp,
                        lineHeight = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        if (isConflict) {
            Icon(
                imageVector = Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = conflictBorderColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 1.dp, end = 1.dp)
                    .size(11.dp),
            )
        }
    }
}

private fun Modifier.timeAxisDivider(): Modifier =
    drawBehind {
        drawLine(
            color = calendarOutline,
            start = Offset(size.width, 0f),
            end = Offset(size.width, size.height),
            strokeWidth = 1f,
        )
    }

private fun Modifier.calendarGridLines(
    visibleHours: Int,
    hourHeight: Dp,
    drawRightBorder: Boolean,
): Modifier =
    drawBehind {
        val dash = PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f)
        val hourPx = hourHeight.toPx()

        for (hour in 0..visibleHours) {
            val y = hour * hourPx
            drawLine(
                color = calendarOutline,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f,
                pathEffect = dash,
            )
        }

        if (drawRightBorder) {
            drawLine(
                color = calendarOutline,
                start = Offset(size.width, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = 1f,
            )
        }
    }

private fun Modifier.blockedTimeStripes(enabled: Boolean): Modifier =
    if (!enabled) {
        this
    } else {
        drawBehind {
            val spacing = 8.dp.toPx()
            val stroke = 1.dp.toPx()
            val color = Color.Black.copy(alpha = 0.06f)
            val width = size.width
            val height = size.height
            var distance = -height

            while (distance <= width + height) {
                drawLine(
                    color = color,
                    start = Offset(distance, 0f),
                    end = Offset(distance + height, height),
                    strokeWidth = stroke,
                )
                distance += spacing
            }
        }
    }

private fun Modifier.conflictBorder(
    enabled: Boolean,
    shape: Shape,
): Modifier =
    if (enabled) border(2.dp, conflictBorderColor, shape) else this

private fun eventTopOffset(
    event: CalendarEvent,
    firstHour: Int,
    hourHeight: Dp,
): Dp = hourHeight * (event.startHour - firstHour)

private fun eventHeight(event: CalendarEvent, hourHeight: Dp): Dp =
    hourHeight * (event.endHour - event.startHour)

private fun formatHourShort(hour24: Int): String {
    val hour12 = ((hour24 + 11) % 12) + 1
    val period = if (hour24 < 12) "a" else "p"
    return "$hour12$period"
}

@Preview(name = "Week Calendar", showBackground = true, widthDp = 396, heightDp = 620)
@Composable
private fun WeekCalendarPreview() {
    MyApplicationTheme {
        WeekCalendar(
            events = previewEvents,
            conflictIds = setOf("course:INFO2300:M"),
            onEventClick = {},
        )
    }
}

private val previewEvents = listOf(
    CalendarEvent(
        id = "course:INFO2300:M",
        day = Day.MON,
        startHour = 10.166f,
        endHour = 11f,
        label = "INFO 2300",
        sublabel = "10:10 AM - 11:00 AM · Gates Hall G01",
        color = 0xFFB31B1B,
        type = EventType.COURSE,
    ),
    CalendarEvent(
        id = "course:HIST1530:R",
        day = Day.THU,
        startHour = 11.667f,
        endHour = 12.917f,
        label = "HIST 1530",
        sublabel = "11:40 AM - 12:55 PM · McGraw 165",
        color = 0xFFB31B1B,
        type = EventType.COURSE,
    ),
    CalendarEvent(
        id = "block:work",
        day = Day.TUE,
        startHour = 15f,
        endHour = 17f,
        label = "Work study",
        color = 0xFF775653,
        type = EventType.BLOCK,
    ),
)
