package id.ac.binus.bloomapp.screens.lessons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.binus.bloomapp.presentation.lessons.LessonsViewModel

@Composable
fun LessonsScreen(viewModel: LessonsViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Lessons", style = MaterialTheme.typography.headlineMedium)
        when {
            state.isLoading -> CircularProgressIndicator()
            state.error != null -> {
                Text(state.error ?: "Unable to load lessons")
                TextButton(onClick = viewModel::loadLessons) { Text("Try again") }
            }
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.lessons, key = { it.id }) { lesson ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                        Text("${lesson.estimatedReadTime} min read", style = MaterialTheme.typography.labelMedium)
                        Text(lesson.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
