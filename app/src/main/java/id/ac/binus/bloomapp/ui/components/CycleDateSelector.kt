
package id.ac.binus.bloomapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.binus.bloomapp.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

enum class CyclePhase {
    MENSTRUATION,
    FOLLICULAR,
    OVULATION,
    LUTEAL
}

data class CycleDateItem(
    val date: LocalDate,
    val phase: CyclePhase?
)

@Composable
fun CycleDateSelector(
    dates: List<CycleDateItem>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()

    val selectedIndex = dates.indexOfFirst {
        it.date == selectedDate
    }

    val listState = rememberLazyListState()

    // Keep selected date near the center
    LaunchedEffect(selectedDate, selectedIndex) {
        if (selectedIndex >= 0) {
            listState.animateScrollToItem(
                (selectedIndex - 3).coerceAtLeast(0)
            )
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = dates,
            key = { it.date.toString() }
        ) { item ->

            val isSelected = item.date == selectedDate
            val isToday = item.date == today

            val phaseColor = when (item.phase) {
                CyclePhase.MENSTRUATION -> PhaseMenstruation
                CyclePhase.FOLLICULAR -> PhaseFollicular
                CyclePhase.OVULATION -> PhaseOvulation
                CyclePhase.LUTEAL -> PhaseLuteal
                null -> null
            }

            Column(
                modifier = Modifier
                    .width(if (isToday) 54.dp else 44.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isToday -> phaseColor ?: PhaseNeutral
                            isSelected -> SurfaceVariant
                            else -> Color.Transparent
                        }
                    )
                    .clickable {
                        onDateSelected(item.date)
                    }
                    .padding(
                        horizontal = 6.dp,
                        vertical = 10.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {

                // Day of week
                Text(
                    text = item.date.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.ENGLISH
                    ),
                    fontSize = 10.sp,
                    fontWeight = if (isToday) {
                        FontWeight.Bold
                    } else {
                        FontWeight.SemiBold
                    },
                    color = if (isToday) Color.White else TextSecondary
                )

                // Date number
                Box(
                    modifier = Modifier
                        .size(if (isToday) 40.dp else 26.dp)
                        .then(
                            if (isToday) {
                                Modifier
                                    .clip(CircleShape)
                                    .background(Color.White)
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.date.dayOfMonth.toString(),
                        fontSize = if (isToday) 16.sp else 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when {
                            isToday -> TextPrimary
                            else -> TextPrimary
                        }
                    )
                }

                // Cycle phase indicator
                if (!isToday && phaseColor != null) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(phaseColor)
                    )
                } else if (!isToday) {
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}
