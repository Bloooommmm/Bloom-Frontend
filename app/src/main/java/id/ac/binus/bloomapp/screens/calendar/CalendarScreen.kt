
package id.ac.binus.bloomapp.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.binus.bloomapp.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private enum class CalendarPhase {
    MENSTRUATION,
    FOLLICULAR,
    OVULATION,
    LUTEAL
}

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }

    var currentMonth by remember {
        mutableStateOf(YearMonth.from(today))
    }

    var selectedDate by remember {
        mutableStateOf(today)
    }

    val monthFormatter = DateTimeFormatter.ofPattern(
        "MMMM yyyy",
        Locale.ENGLISH
    )

    val dateFormatter = DateTimeFormatter.ofPattern(
        "MMMM d, yyyy",
        Locale.ENGLISH
    )

    val firstDay = currentMonth.atDay(1)

    // Calendar starts on Monday
    val calendarStart = firstDay.minusDays(
        (firstDay.dayOfWeek.value - 1).toLong()
    )

    val calendarDates = (0 until 42).map { offset ->
        calendarStart.plusDays(offset.toLong())
    }

    val selectedPhase = getCalendarPhase(selectedDate, today)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // Header
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Calendar",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        // Calendar section
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Month navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(
                        onClick = {
                            currentMonth = currentMonth.minusMonths(1)
                        }
                    ) {
                        Text(
                            text = "‹",
                            fontSize = 30.sp,
                            color = TextSecondary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentMonth.month.getDisplayName(
                            java.time.format.TextStyle.FULL,
                            Locale.ENGLISH
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = " ${currentMonth.year}",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = {
                            currentMonth = currentMonth.plusMonths(1)
                        }
                    ) {
                        Text(
                            text = "›",
                            fontSize = 30.sp,
                            color = TextSecondary
                        )
                    }
                }
            }


            // Weekday labels
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val weekdays = listOf(
                    "M", "T", "W", "T",
                    "F", "S", "S"
                )

                weekdays.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Calendar dates
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                calendarDates.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        week.forEach { date ->
                            CalendarDateItem(
                                date = date,
                                today = today,
                                selectedDate = selectedDate,
                                currentMonth = currentMonth,
                                phase = getCalendarPhase(date, today),
                                onClick = {
                                    selectedDate = date
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }


        // Phase legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PhaseLegend(
                color = PhaseMenstruation,
                label = "Period"
            )

            Spacer(modifier = Modifier.width(20.dp))

            PhaseLegend(
                color = PhaseFollicular,
                label = "Follicular"
            )

            Spacer(modifier = Modifier.width(20.dp))

            PhaseLegend(
                color = PhaseLuteal,
                label = "Luteal"
            )
        }

        // Selected date information
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(SurfaceVariant)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Selected Date",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )

            Text(
                text = selectedDate.format(dateFormatter),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = when (selectedPhase) {
                    CalendarPhase.MENSTRUATION ->
                        "Menstruation"

                    CalendarPhase.FOLLICULAR ->
                        "Follicular Phase"

                    CalendarPhase.OVULATION ->
                        "Ovulation"

                    CalendarPhase.LUTEAL ->
                        "Luteal Phase"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = getCalendarPhaseColor(selectedPhase),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}




@Composable
private fun CalendarDateItem(
    date: LocalDate,
    today: LocalDate,
    selectedDate: LocalDate,
    currentMonth: YearMonth,
    phase: CalendarPhase,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = date == selectedDate
    val isToday = date == today
    val isCurrentMonth = YearMonth.from(date) == currentMonth

    val phaseColor = getCalendarPhaseColor(phase)

    Column(
        modifier = modifier
            .height(68.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

// Date number and Today indicator
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            // Selected date background
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant)
                )
            }

            // Date number
            Text(
                text = date.dayOfMonth.toString(),
                color = when {
                    !isCurrentMonth -> Outline
                    isToday -> Color(0xFF4E95EE)
                    else -> TextPrimary
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = when {
                    isToday -> FontWeight.Bold
                    isSelected -> FontWeight.SemiBold
                    else -> FontWeight.Medium
                }
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        // Full-width phase indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 3.dp)
                .height(3.dp)
                .background(
                    color = if (
                        isCurrentMonth &&
                        phase != CalendarPhase.LUTEAL
                    ) {
                        phaseColor
                    } else {
                        Color.Transparent
                    }
                )
        )
    }
}

@Composable
private fun PhaseLegend(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 10.sp
            ),
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun getCalendarPhase(
    date: LocalDate,
    today: LocalDate
): CalendarPhase {
    val daysFromToday = java.time.temporal.ChronoUnit.DAYS
        .between(today, date)
        .toInt()

    val cycleDay = Math.floorMod(
        7 + daysFromToday,
        28
    ) + 1

    return when (cycleDay) {
        in 1..5 -> CalendarPhase.MENSTRUATION
        in 6..13 -> CalendarPhase.FOLLICULAR
        14 -> CalendarPhase.OVULATION
        else -> CalendarPhase.LUTEAL
    }
}

private fun getCalendarPhaseColor(
    phase: CalendarPhase
): Color {
    return when (phase) {
        CalendarPhase.MENSTRUATION -> PhaseMenstruation

        CalendarPhase.FOLLICULAR,
        CalendarPhase.OVULATION -> PhaseFollicular

        CalendarPhase.LUTEAL -> PhaseLuteal
    }
}
