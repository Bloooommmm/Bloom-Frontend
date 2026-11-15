
package id.ac.binus.bloomapp.screens.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.ac.binus.bloomapp.ui.components.CycleDateItem
import id.ac.binus.bloomapp.ui.components.CycleDateSelector
import id.ac.binus.bloomapp.ui.components.CyclePhase
import id.ac.binus.bloomapp.ui.components.CycleProgressRing
import id.ac.binus.bloomapp.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TodayScreen() {

    val today = LocalDate.now()

    var selectedDate by remember {
        mutableStateOf(today)
    }

    // Temporary preview data
    // Will be replaced by actual cycle calculations later.
    val dateItems = remember(today) {
        (-14..16).map { offset ->

            // Preview: today is Day 8 of a 28-day cycle.
            val cycleDay = ((7 + offset) % 28 + 28) % 28 + 1

            val phase = when (cycleDay) {
                in 1..5 -> CyclePhase.MENSTRUATION
                in 6..13 -> CyclePhase.FOLLICULAR
                14 -> CyclePhase.OVULATION
                else -> CyclePhase.LUTEAL
            }

            CycleDateItem(
                date = today.plusDays(offset.toLong()),
                phase = phase
            )
        }
    }

    val formattedDate = today.month.getDisplayName(
        TextStyle.FULL,
        Locale.ENGLISH
    ) + " " + today.dayOfMonth


    val selectedCycleDay = (
            (7 + java.time.temporal.ChronoUnit.DAYS
                .between(today, selectedDate).toInt()) % 28 + 28
            ) % 28 + 1

    val selectedPhase = dateItems
        .firstOrNull { it.date == selectedDate }
        ?.phase ?: CyclePhase.FOLLICULAR

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 20.dp),

        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Header
            Text(
                text = "Today, $formattedDate",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Cycle Date Selector
            CycleDateSelector(
                dates = dateItems,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            //Cycle Progress
            CycleProgressRing(
                cycleDay = selectedCycleDay,
                cycleLength = 28,
                phase = selectedPhase,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
