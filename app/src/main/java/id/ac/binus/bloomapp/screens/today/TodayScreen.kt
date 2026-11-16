
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
import id.ac.binus.bloomapp.presentation.tracking.TrackingViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

@Composable
fun TodayScreen(tracking: TrackingViewModel) {

    val today = LocalDate.now()

    var selectedDate by remember {
        mutableStateOf(today)
    }
    val trackingState by tracking.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { tracking.refresh() }
    val activePeriod = trackingState.periods.firstOrNull { it.endDate == null }
    val selectedPeriod = trackingState.periods.firstOrNull { it.includes(selectedDate) }

    // Temporary preview data
    // Will be replaced by actual cycle calculations later.
    val dateItems = remember(today, trackingState.periods) {
        (-14..16).map { offset ->

            // Preview: today is Day 8 of a 28-day cycle.
            val cycleDay = ((7 + offset) % 28 + 28) % 28 + 1

            val phase = when {
                trackingState.periods.any { it.includes(today.plusDays(offset.toLong())) } -> CyclePhase.MENSTRUATION
                cycleDay in 1..5 -> CyclePhase.MENSTRUATION
                cycleDay in 6..13 -> CyclePhase.FOLLICULAR
                cycleDay == 14 -> CyclePhase.OVULATION
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                CycleProgressRing(
                    cycleDay = selectedCycleDay,
                    cycleLength = 28,
                    phase = selectedPhase,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }

            Text(
                text = if (selectedPeriod != null) "Period tercatat ${selectedPeriod.startDate}" else "Belum ada catatan period untuk tanggal ini",
                color = TextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = { if (activePeriod == null) tracking.start(selectedDate) else tracking.finish(activePeriod.id, selectedDate) },
                enabled = !trackingState.saving,
                modifier = Modifier.fillMaxWidth(),
            ) { Text(if (activePeriod == null) "Catat mulai haid" else "Catat selesai haid") }
            trackingState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
