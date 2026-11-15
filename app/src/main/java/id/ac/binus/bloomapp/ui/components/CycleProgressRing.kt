
package id.ac.binus.bloomapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.ac.binus.bloomapp.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CycleProgressRing(
    cycleDay: Int,
    cycleLength: Int,
    phase: CyclePhase,
    modifier: Modifier = Modifier
) {
    val safeCycleLength = cycleLength.coerceAtLeast(15)
    val selectedDay = cycleDay.coerceIn(1, safeCycleLength)
    val ovulationDay = (safeCycleLength - 14).coerceIn(6, safeCycleLength - 1)

    val phaseColor = getPhaseColor(phase)
    val phaseDarkColor = getPhaseDarkColor(phase)

    val phaseName = when (phase) {
        CyclePhase.MENSTRUATION -> "Menstruation"
        CyclePhase.FOLLICULAR -> "Follicular Phase"
        CyclePhase.OVULATION -> "Ovulation"
        CyclePhase.LUTEAL -> "Luteal Phase"
    }

    val phaseDescription = when (phase) {
        CyclePhase.MENSTRUATION ->
            "Your period is here."

        CyclePhase.FOLLICULAR ->
            "Your body is preparing for ovulation."

        CyclePhase.OVULATION ->
            "Your body may be releasing an egg."

        CyclePhase.LUTEAL ->
            "Your body is preparing for a new cycle."
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val ringSize = maxWidth.coerceAtMost(640.dp)

        Box(
            modifier = Modifier.size(ringSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(
                    x = size.width / 2f,
                    y = size.height / 2f
                )

                val radius = size.minDimension * 0.39f

                val dotRadiusNormal = 5.dp.toPx()
                val dotRadiusLarge = 6.dp.toPx()

                val selectedRadius = 12.dp.toPx()
                val selectedInnerRadius = 8.dp.toPx()

                val strokeWidth = 1.dp.toPx()
                val selectedStrokeWidth = 1.5.dp.toPx()

                // Inner circular divider
                drawCircle(
                    color = Outline,
                    radius = radius - 12.dp.toPx() * 1.15f,
                    center = center,
                    style = Stroke(width = strokeWidth)
                )

                // Cycle day indicators
                for (day in 1..safeCycleLength) {
                    val angle = -PI / 2.0 +
                            ((day - 1).toDouble() / safeCycleLength) * 2.0 * PI

                    val position = Offset(
                        x = center.x + radius * cos(angle).toFloat(),
                        y = center.y + radius * sin(angle).toFloat()
                    )

                    val dotPhase = getDayPhase(day, ovulationDay)
                    val dotColor = getPhaseColor(dotPhase)

                    val dotRadius = when (dotPhase) {
                        CyclePhase.MENSTRUATION,
                        CyclePhase.OVULATION -> dotRadiusLarge

                        CyclePhase.FOLLICULAR,
                        CyclePhase.LUTEAL -> dotRadiusNormal
                    }

                    if (day == selectedDay) {
                        // Selection background
                        drawCircle(
                            color = Color.White,
                            radius = selectedRadius,
                            center = position
                        )

                        // Selection outline
                        drawCircle(
                            color = dotColor,
                            radius = selectedRadius,
                            center = position,
                            style = Stroke(width = selectedStrokeWidth)
                        )

                        // Selected day indicator
                        drawCircle(
                            color = dotColor,
                            radius = selectedInnerRadius,
                            center = position
                        )
                    } else {
                        drawCircle(
                            color = dotColor,
                            radius = dotRadius,
                            center = position
                        )
                    }
                }
            }

            // Center content
            Column(
                modifier = Modifier
                    .fillMaxSize(0.66f)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Day $selectedDay",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = phaseName,
                    style = MaterialTheme.typography.titleMedium,
                    color = phaseDarkColor,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = phaseDescription,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

private fun getDayPhase(
    day: Int,
    ovulationDay: Int
): CyclePhase {
    return when (day) {
        in 1..5 -> CyclePhase.MENSTRUATION
        in 6 until ovulationDay -> CyclePhase.FOLLICULAR
        ovulationDay -> CyclePhase.OVULATION
        else -> CyclePhase.LUTEAL
    }
}

private fun getPhaseColor(phase: CyclePhase): Color {
    return when (phase) {
        CyclePhase.MENSTRUATION -> PhaseMenstruation
        CyclePhase.FOLLICULAR,
        CyclePhase.OVULATION -> PhaseFollicular
        CyclePhase.LUTEAL -> PhaseLuteal
    }
}

private fun getPhaseDarkColor(phase: CyclePhase): Color {
    return when (phase) {
        CyclePhase.MENSTRUATION -> PhaseMenstruationDark
        CyclePhase.FOLLICULAR,
        CyclePhase.OVULATION -> PhaseFollicularDark
        CyclePhase.LUTEAL -> PhaseLutealDark
    }
}



