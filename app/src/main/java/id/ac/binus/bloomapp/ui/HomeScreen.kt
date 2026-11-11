package id.ac.binus.bloomapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.ac.binus.bloomapp.data.unit

private val unitColors = listOf(
    Color(0xFF2E7D32),
    Color(0xFFC62828),
    Color(0xFF1565C0),
    Color(0xFF6A1B9A)
)

@Composable
fun HomeScreen(
    units: List<unit>,
    onLessonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item(
            key = "home_header",
            contentType = "header"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Bloom",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Small lessons. Meaningful learning.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        units.forEachIndexed { unitIndex, unit ->
            val unitColor =
                unitColors[unitIndex % unitColors.size]

            item(
                key = "unit:${unit.id}",
                contentType = "unit"
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = unitColor,
                    contentColor = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "unit ${unitIndex + 1}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = unit.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${unit.lessons.size} lessons",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            itemsIndexed(
                items = unit.lessons,
                key = { _, lesson ->
                    "lesson:${unit.id}:${lesson.id}"
                },
                contentType = { _, _ -> "lesson" }
            ) { lessonIndex, lesson ->
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val nodeWidth = minOf(160.dp, maxWidth)
                    val availableOffset = (maxWidth - nodeWidth) / 2

                    val direction = when (lessonIndex % 4) {
                        0 -> 0f
                        1 -> 0.85f
                        2 -> 0f
                        else -> -0.85f
                    }

                    Column(
                        modifier = Modifier
                            .width(nodeWidth)
                            .align(Alignment.TopCenter)
                            .offset(x = availableOffset * direction),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(
                                width = 88.dp,
                                height = 96.dp
                            )
                        ) {
                            // Native shapes provide the raised button effect.
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .size(88.dp)
                                    .background(
                                        color = lerp(
                                            unitColor,
                                            Color.Black,
                                            0.25f
                                        ),
                                        shape = CircleShape
                                    )
                            )

                            Button(
                                onClick = {
                                    onLessonClick(lesson.id)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .size(88.dp)
                                    .semantics {
                                        contentDescription =
                                            "Start lesson ${lessonIndex + 1}: ${lesson.title}"
                                    },
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = unitColor,
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp
                                )
                            ) {
                                Text(
                                    text = "${lessonIndex + 1}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Text(
                            text = lesson.title,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        val questionCount = lesson.questions.size

                        Text(
                            text = if (questionCount == 1) {
                                "1 question"
                            } else {
                                "$questionCount questions"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}