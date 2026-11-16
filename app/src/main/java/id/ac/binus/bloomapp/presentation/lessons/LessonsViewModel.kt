package id.ac.binus.bloomapp.presentation.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.binus.bloomapp.data.lesson.LessonRepository
import id.ac.binus.bloomapp.data.network.Lesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LessonsUiState(
    val isLoading: Boolean = true,
    val lessons: List<Lesson> = emptyList(),
    val error: String? = null,
)

class LessonsViewModel(
    private val repository: LessonRepository = LessonRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(LessonsUiState())
    val uiState: StateFlow<LessonsUiState> = _uiState.asStateFlow()

    init {
        loadLessons()
    }

    fun loadLessons() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            runCatching { repository.getLessons() }
                .onSuccess { lessons -> _uiState.value = LessonsUiState(isLoading = false, lessons = lessons) }
                .onFailure {
                    _uiState.value = LessonsUiState(
                        isLoading = false,
                        error = "Materi belum bisa dimuat. Coba lagi.",
                    )
                }
        }
    }
}
