package id.ac.binus.bloomapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import id.ac.binus.bloomapp.data.LearningData

data class QuizUiState(
    val lessonId: String? = null,
    val questionIndex: Int = 0,
    val selectedOptionId: String? = null,
    val isChecked: Boolean = false,
    val score: Int = 0
)

class QuizViewModel : ViewModel() {
    var uiState by mutableStateOf(QuizUiState())
        private set

    val currentLesson
        get() = LearningData.lessons.firstOrNull {
            it.id == uiState.lessonId
        }

    val currentQuestion
        get() = currentLesson
            ?.questions
            ?.getOrNull(uiState.questionIndex)

    fun startLesson(lessonId: String) {
        val lesson = LearningData.lessons.firstOrNull {
            it.id == lessonId
        } ?: return

        if (lesson.questions.isEmpty()) return

        uiState = QuizUiState(lessonId = lesson.id)
    }

    fun selectAnswer(optionId: String) {
        val question = currentQuestion ?: return

        if (uiState.isChecked || optionId !in question.options) return

        uiState = uiState.copy(selectedOptionId = optionId)
    }

    fun checkAnswer() {
        val question = currentQuestion ?: return
        val selectedId = uiState.selectedOptionId ?: return

        if (uiState.isChecked) return

        val points =
            if (selectedId == question.correctOptionId) 1 else 0

        uiState = uiState.copy(
            isChecked = true,
            score = uiState.score + points
        )
    }

    fun nextQuestion() {
        if (!uiState.isChecked || currentQuestion == null) return

        uiState = uiState.copy(
            questionIndex = uiState.questionIndex + 1,
            selectedOptionId = null,
            isChecked = false
        )
    }

    fun restart() {
        val lessonId = uiState.lessonId ?: return
        startLesson(lessonId)
    }

    fun goHome() {
        uiState = QuizUiState()
    }
}