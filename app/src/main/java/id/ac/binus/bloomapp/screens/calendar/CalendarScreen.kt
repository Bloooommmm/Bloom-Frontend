package id.ac.binus.bloomapp.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.ac.binus.bloomapp.data.network.PeriodRecord
import id.ac.binus.bloomapp.presentation.tracking.TrackingViewModel
import id.ac.binus.bloomapp.ui.components.CustomButton
import id.ac.binus.bloomapp.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun CalendarScreen(tracking: TrackingViewModel, modifier: Modifier = Modifier) {
    val today = remember { LocalDate.now() }
    val state by tracking.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { tracking.refresh() }
    var month by remember { mutableStateOf(YearMonth.from(today)) }
    var selected by remember { mutableStateOf(today) }
    var confirmDelete by remember { mutableStateOf<PeriodRecord?>(null) }
    val active = state.periods.firstOrNull { it.endDate == null }
    val selectedPeriod = state.periods.firstOrNull { it.includes(selected) }
    val start = month.atDay(1).minusDays((month.atDay(1).dayOfWeek.value - 1).toLong())
    val dates = (0..41).map { start.plusDays(it.toLong()) }
    Column(modifier.fillMaxSize().background(Background).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
        Text("Calendar", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton({ month = month.minusMonths(1) }) { Icon(Icons.Outlined.ChevronLeft, "Previous month", tint = TextPrimary) }
            Text("${month.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${month.year}", Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
            IconButton({ month = month.plusMonths(1) }) { Icon(Icons.Outlined.ChevronRight, "Next month", tint = TextPrimary) }
        }
        Row(Modifier.fillMaxWidth()) { listOf("M", "T", "W", "T", "F", "S", "S").forEach { Text(it, Modifier.weight(1f), textAlign = TextAlign.Center, color = TextSecondary, fontWeight = FontWeight.Bold) } }
        dates.chunked(7).forEach { week -> Row(Modifier.fillMaxWidth()) { week.forEach { date -> CalendarDate(date, month, today, selected, state.periods) { selected = date } } } }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) { Box(Modifier.size(10.dp).clip(CircleShape).background(PhaseMenstruation)); Text("Logged period", color = TextSecondary); Spacer(Modifier.width(8.dp)); Box(Modifier.size(10.dp).clip(CircleShape).background(Secondary)); Text("Today", color = TextSecondary) }
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = PrimaryLight) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(selected.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(if (selectedPeriod != null) "Period logged" else "No period logged", color = TextSecondary)
                val validDate = !selected.isAfter(today) && (active == null || !selected.isBefore(active.startDate))
                CustomButton(text = if (active == null) "Log period start" else "Log period end", onClick = { if (active == null) tracking.start(selected) else tracking.finish(active.id, selected) }, enabled = !state.saving && validDate, modifier = Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    selectedPeriod?.let { period ->
                        TextButton(onClick = { confirmDelete = period }, enabled = !state.saving) { Text("Delete this record", color = MaterialTheme.colorScheme.error) }
                    } ?: Spacer(Modifier.width(1.dp))
                    if (selected != today) TextButton({ selected = today; month = YearMonth.from(today) }) { Text("Back to today", color = Primary) }
                }
            }
        }
        Text("Period history", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
        if (state.periods.isEmpty()) Text("Your logged periods will appear here.", color = TextSecondary)
        state.periods.sortedByDescending { it.startDate }.distinctBy { it.startDate }.forEach { period ->
            val end = period.endDate
            val duration = end?.let { ChronoUnit.DAYS.between(period.startDate, it).toInt() + 1 }
            Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(10.dp).clip(CircleShape).background(PhaseMenstruation))
                Spacer(Modifier.width(12.dp))
                Column { Text(period.startDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)), fontWeight = FontWeight.SemiBold, color = TextPrimary); Text(if (duration != null) "$duration days" else "Ongoing", color = TextSecondary) }
            }
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
    confirmDelete?.let { period ->
        AlertDialog(
            onDismissRequest = { confirmDelete = null },
            title = { Text("Delete period record?") },
            text = { Text("This removes the ${period.startDate} record from your history.") },
            confirmButton = { TextButton(onClick = { tracking.delete(period.id); confirmDelete = null }) { Text("Delete", color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton(onClick = { confirmDelete = null }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun RowScope.CalendarDate(date: LocalDate, month: YearMonth, today: LocalDate, selected: LocalDate, periods: List<PeriodRecord>, onClick: () -> Unit) {
    val logged = periods.any { it.includes(date) }
    val current = YearMonth.from(date) == month
    Box(Modifier.weight(1f).height(52.dp).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(Modifier.size(34.dp).clip(CircleShape).background(if (date == selected) PrimaryLight else Color.Transparent), contentAlignment = Alignment.Center) { Text(date.dayOfMonth.toString(), color = if (!current) Outline else if (date == today) Secondary else TextPrimary, fontWeight = if (date == today || date == selected) FontWeight.Bold else FontWeight.Normal) }
            Box(Modifier.size(6.dp).clip(CircleShape).background(if (logged) PhaseMenstruation else Color.Transparent))
        }
    }
}
