package id.ac.binus.bloomapp.screens.lessons

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
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

internal data class ArticleSection(val heading: String, val body: String)

@Composable
fun LessonsScreen(viewModel: LessonsViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf<Lesson?>(null) }
    if (selected != null) {
        BackHandler { selected = null }
        LessonDetail(selected!!, onBack = { selected = null })
        return
    }
    Column(Modifier.fillMaxSize().background(Surface).statusBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Learn", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Text("Simple answers for every phase of your cycle", color = TextSecondary)
        when {
            state.isLoading -> CircularProgressIndicator(color = Primary)
            state.error != null -> {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = viewModel::loadLessons) { Text("Try again") }
            }
            else -> state.lessons.forEach { lesson -> LessonCard(lesson) { selected = lesson } }
        }
    }
}

@Composable
private fun LessonCard(lesson: Lesson, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Box(Modifier.fillMaxWidth().heightIn(min = 150.dp).background(Color(0xFFDC4862), shape)) {
        Surface(Modifier.fillMaxWidth().offset(y = (-4).dp).clickable(onClick = onClick), shape = shape, color = Color.White, border = BorderStroke(1.dp, Primary)) {
            Column(Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.MenuBook, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("${lesson.estimatedReadTime} min read", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = "Read article", tint = Primary)
                }
                Text(lesson.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(top = 12.dp))
                Text(articlePreview(lesson.content), maxLines = 3, color = TextSecondary, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
internal fun LessonDetail(lesson: Lesson, onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sections = remember(lesson.content) { parseSections(lesson.content) }
    val audio = remember(lesson.id) {
        val resource = when {
            lesson.title.contains("First Period", true) -> R.raw.article_first_period
            lesson.title.contains("Fatigue", true) -> R.raw.article_fatigue
            lesson.title.contains("Crave", true) -> R.raw.article_cravings
            lesson.title.contains("Cramps", true) -> R.raw.article_cramps
            lesson.title.contains("Mood", true) -> R.raw.article_mood
            else -> null
        }
        ArticleAudio(context, sections.joinToString("\n") { "${it.heading}. ${it.body}" }, resource)
    }
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    DisposableEffect(audio, lifecycle) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_STOP) audio.pause()
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer); audio.close() }
    }
    Column(Modifier.fillMaxSize().background(Surface).statusBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.size(52.dp)) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back to Learn", tint = Primary, modifier = Modifier.size(30.dp))
            }
            Text("Learn", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        Text(lesson.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(top = 18.dp))
        Row(Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("${lesson.estimatedReadTime} min read", color = TextSecondary)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { if (audio.playing) audio.pause() else audio.play() }, enabled = audio.ready, modifier = Modifier.size(52.dp)) {
                Icon(if (audio.playing) Icons.Outlined.Pause else Icons.Outlined.PlayArrow, contentDescription = if (audio.playing) "Pause audio" else if (audio.paused) "Resume audio" else "Listen to article", tint = Primary, modifier = Modifier.size(30.dp))
            }
            if (audio.playing || audio.paused) IconButton(onClick = audio::stop, modifier = Modifier.size(52.dp)) { Icon(Icons.Outlined.Stop, contentDescription = "Stop audio", tint = TextSecondary, modifier = Modifier.size(26.dp)) }
        }
        audio.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
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

internal fun parseSections(content: String): List<ArticleSection> {
    val matches = content.split(Regex("(?m)^##\\s+")).drop(1).mapNotNull { block ->
        val lines = block.trim().split(Regex("\\n+"), limit = 2)
        lines.firstOrNull()?.trim()?.takeIf { it.isNotEmpty() }?.let { ArticleSection(it, lines.getOrElse(1) { "" }.trim()) }
    }
    return if (matches.isNotEmpty()) matches else listOf(ArticleSection("A gentle guide", content.trim()))
}

internal fun lessonIllustrations(lesson: Lesson): List<Int> = when {
    lesson.title.contains("Mood", true) -> listOf(R.drawable.article_mood_1, R.drawable.article_mood_2, R.drawable.article_mood_3, R.drawable.article_mood_4)
    lesson.title.contains("Cramps", true) -> listOf(R.drawable.article_cramps_1, R.drawable.article_cramps_2, R.drawable.article_cramps_3, R.drawable.article_cramps_4)
    lesson.title.contains("Crave", true) -> listOf(R.drawable.article_cravings_1, R.drawable.article_cravings_2, R.drawable.article_cravings_3, R.drawable.article_cravings_4)
    lesson.title.contains("Fatigue", true) -> listOf(R.drawable.article_fatigue_1, R.drawable.article_fatigue_2, R.drawable.article_fatigue_3, R.drawable.article_fatigue_4)
    lesson.title.contains("First Period", true) -> listOf(R.drawable.article_first_period_1, R.drawable.article_first_period_2, R.drawable.article_first_period_3, R.drawable.article_first_period_4)
    else -> emptyList()
}
