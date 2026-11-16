package id.ac.binus.bloomapp.screens.lessons

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.binus.bloomapp.R
import id.ac.binus.bloomapp.data.network.Lesson
import id.ac.binus.bloomapp.presentation.lessons.LessonsViewModel
import id.ac.binus.bloomapp.ui.theme.*
import java.util.Locale

private data class ArticleSection(val heading: String, val body: String)

@Composable
fun LessonsScreen(viewModel: LessonsViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf<Lesson?>(null) }
    if (selected != null) {
        BackHandler { selected = null }
        LessonDetail(selected!!, onBack = { selected = null })
        return
    }
    Column(Modifier.fillMaxSize().background(Surface).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Learn", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Text("Simple answers for every phase of your cycle", color = TextSecondary)
        when {
            state.isLoading -> CircularProgressIndicator(color = Primary)
            state.error != null -> Text(state.error!!, color = MaterialTheme.colorScheme.error)
            else -> state.lessons.forEach { lesson -> LessonCard(lesson) { selected = lesson } }
        }
    }
}

@Composable
private fun LessonCard(lesson: Lesson, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Box(Modifier.fillMaxWidth().heightIn(min = 150.dp).background(Color(0xFFDC4862), shape)) {
        Surface(Modifier.fillMaxWidth().offset(y = (-4).dp).clickable(onClick = onClick), shape = shape, color = Color.White) {
            Column(Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.MenuBook, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("${lesson.estimatedReadTime} min read", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Outlined.ArrowForward, contentDescription = "Read article", tint = Primary)
                }
                Text(lesson.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(top = 12.dp))
                Text(articlePreview(lesson.content), maxLines = 3, color = TextSecondary, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun LessonDetail(lesson: Lesson, onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sections = remember(lesson.content) { parseSections(lesson.content) }
    val chunks = remember(sections) { sections.map { "${it.heading}. ${it.body}" } }
    var audioState by remember { mutableIntStateOf(0) }
    var currentChunk by remember { mutableIntStateOf(0) }
    val tts = remember { TextToSpeech(context) { } }
    DisposableEffect(tts) {
        tts.language = Locale.ENGLISH
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {}
            override fun onDone(utteranceId: String) {
                val next = utteranceId.removePrefix("bloom-").toIntOrNull()?.plus(1) ?: chunks.size
                if (next < chunks.size) { currentChunk = next; tts.speak(chunks[next], TextToSpeech.QUEUE_FLUSH, null, "bloom-$next") } else audioState = 0
            }
            override fun onError(utteranceId: String) { audioState = 0 }
        })
        onDispose { tts.stop(); tts.shutdown() }
    }
    Column(Modifier.fillMaxSize().background(Surface).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text("←  Learn", color = Primary, modifier = Modifier.clickable(onClick = onBack))
        Text(lesson.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(top = 18.dp))
        Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("${lesson.estimatedReadTime} min read", color = TextSecondary)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = {
                if (audioState == 1) { tts.stop(); audioState = 2 } else { if (audioState == 0) currentChunk = 0; tts.speak(chunks.getOrElse(currentChunk) { chunks.firstOrNull().orEmpty() }, TextToSpeech.QUEUE_FLUSH, null, "bloom-$currentChunk"); audioState = 1 }
            }) { Text(if (audioState == 1) "Pause" else if (audioState == 2) "Resume" else "Listen") }
            if (audioState != 0) TextButton(onClick = { tts.stop(); currentChunk = 0; audioState = 0 }) { Text("Stop") }
        }
        Spacer(Modifier.height(12.dp))
        sections.forEachIndexed { index, section ->
            Text(section.heading, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(top = if (index == 0) 0.dp else 24.dp))
            Text(section.body, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, modifier = Modifier.padding(top = 8.dp))
            lessonIllustrations(lesson).getOrNull(index)?.let { resource -> Image(painterResource(resource), "Illustration for ${section.heading}", Modifier.fillMaxWidth().height(170.dp).padding(top = 12.dp), contentScale = ContentScale.Fit) }
        }
        Spacer(Modifier.height(28.dp))
    }
}

private fun articlePreview(content: String): String = content.substringAfter("\n\n", content).replace(Regex("##\\s*"), "").take(180)

private fun parseSections(content: String): List<ArticleSection> {
    val matches = content.split(Regex("(?m)^##\\s+")).drop(1).mapNotNull { block ->
        val lines = block.trim().split(Regex("\\n+"), limit = 2)
        lines.firstOrNull()?.trim()?.takeIf { it.isNotEmpty() }?.let { ArticleSection(it, lines.getOrElse(1) { "" }.trim()) }
    }
    return if (matches.isNotEmpty()) matches else listOf(ArticleSection("A gentle guide", content.trim()))
}

private fun lessonIllustrations(lesson: Lesson): List<Int> = when {
    lesson.title.contains("Mood", true) -> listOf(R.drawable.article_mood_1, R.drawable.article_mood_2, R.drawable.article_mood_3, R.drawable.article_mood_4)
    lesson.title.contains("Cramps", true) -> listOf(R.drawable.article_cramps_1, R.drawable.article_cramps_2, R.drawable.article_cramps_3, R.drawable.article_cramps_4)
    lesson.title.contains("Crave", true) -> listOf(R.drawable.article_cravings_1, R.drawable.article_cravings_2, R.drawable.article_cravings_3, R.drawable.article_cravings_4)
    lesson.title.contains("Fatigue", true) -> listOf(R.drawable.article_fatigue_1, R.drawable.article_fatigue_2, R.drawable.article_fatigue_3, R.drawable.article_fatigue_4)
    lesson.title.contains("First Period", true) -> listOf(R.drawable.article_first_period_1, R.drawable.article_first_period_2, R.drawable.article_first_period_3, R.drawable.article_first_period_4)
    else -> emptyList()
}
