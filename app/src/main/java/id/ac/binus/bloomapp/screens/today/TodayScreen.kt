
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
import id.ac.binus.bloomapp.ui.components.CustomButton

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

    val latestPeriodStart = trackingState.periods.maxByOrNull { it.startDate }?.startDate
    val dateItems = remember(today, trackingState.periods, latestPeriodStart) {
        (-14..16).map { offset ->
            val date = today.plusDays(offset.toLong())
            val cycleDay = latestPeriodStart?.let { Math.floorMod(java.time.temporal.ChronoUnit.DAYS.between(it, date).toInt(), 28) + 1 } ?: 1

            val phase = when {
                trackingState.periods.any { it.includes(date) } -> CyclePhase.MENSTRUATION
                latestPeriodStart == null -> CyclePhase.FOLLICULAR
                cycleDay in 1..5 -> CyclePhase.MENSTRUATION
                cycleDay in 6..13 -> CyclePhase.FOLLICULAR
                cycleDay == 14 -> CyclePhase.OVULATION
                else -> CyclePhase.LUTEAL
            }

            CycleDateItem(
                date = date,
                phase = phase
            )
        }
    }

    val formattedDate = today.month.getDisplayName(
        TextStyle.FULL,
        Locale.ENGLISH
    ) + " " + today.dayOfMonth


    val selectedCycleDay = latestPeriodStart?.let {
        Math.floorMod(java.time.temporal.ChronoUnit.DAYS.between(it, selectedDate).toInt(), 28) + 1
    } ?: 1

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
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            Text(
                text = if (selectedPeriod != null) "Period tercatat ${selectedPeriod.startDate}" else "Belum ada catatan period untuk tanggal ini",
                color = TextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            CustomButton(
                text = if (activePeriod == null) "Catat mulai haid" else "Catat selesai haid",
                onClick = { if (activePeriod == null) tracking.start(selectedDate) else tracking.finish(activePeriod.id, selectedDate) },
                enabled = !trackingState.saving,
                modifier = Modifier.fillMaxWidth(),
            )
            trackingState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}






