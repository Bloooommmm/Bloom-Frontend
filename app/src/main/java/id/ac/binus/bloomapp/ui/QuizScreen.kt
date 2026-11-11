package id.ac.binus.bloomapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import id.ac.binus.bloomapp.data.QuizData

@Composable
fun QuizScreen(
    state: QuizUiState,
    question: Question?,
    totalQuestions: Int,
    lessonTitle: String,
    onSelectAnswer: (String) -> Unit,
    onCheckAnswer: () -> Unit,
    onNext: () -> Unit,
    onRestart: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    key(state.questionIndex) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = onBackHome) {
                Text("Back to lessons")
            }
            Text("Bloom", style = MaterialTheme.typography.headlineLarge)
            Text(
                lessonTitle,
                style = MaterialTheme.typography.titleLarge
            )

            if (question == null) {
                Text(
                    "Lesson complete!",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("Your score: ${state.score} / $totalQuestions")
                Text("You can repeat this lesson to keep learning.")

                Button(
                    onClick = onRestart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Try again")
                }
            } else {
                Text("Question ${state.questionIndex + 1} of $totalQuestions")
                Text(
                    question.text,
                    style = MaterialTheme.typography.titleMedium
                )

                Column(Modifier.selectableGroup()) {
                    question.options.forEach { (optionId, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp)
                                .selectable(
                                    selected = state.selectedOptionId == optionId,
                                    enabled = !state.isChecked,
                                    role = Role.RadioButton,
                                    onClick = { onSelectAnswer(optionId) }
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.selectedOptionId == optionId,
                                onClick = null,
                                enabled = !state.isChecked
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(label)
                        }
                    }
                }

                if (state.isChecked) {
                    val isCorrect =
                        state.selectedOptionId == question.correctOptionId

                    Column(
                        modifier = Modifier.semantics {
                            liveRegion = LiveRegionMode.Polite
                        },
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isCorrect) "Correct!" else "Not quite.",
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (!isCorrect) {
                            Text(
                                "Correct answer: " +
                                        question.options.getValue(question.correctOptionId)
                            )
                        }

                        Text(question.explanation)
                    }

                    Button(
                        onClick = onNext,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (state.questionIndex == totalQuestions - 1)
                                "See results"
                            else
                                "Next question"
                        )
                    }
                } else {
                    Button(
                        onClick = onCheckAnswer,
                        enabled = state.selectedOptionId != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Check answer")
                    }
                }
            }

            TextButton(
                onClick = { uriHandler.openUri(QuizData.sourceUrl) }
            ) {
                Text("Source: Planned Parenthood")
            }
        }
    }
}