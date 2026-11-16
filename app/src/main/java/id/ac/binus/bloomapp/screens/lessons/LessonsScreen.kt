package id.ac.binus.bloomapp.screens.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.DisposableEffect
import android.speech.tts.TextToSpeech
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

@Composable
fun LessonsScreen(viewModel: LessonsViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf<Lesson?>(null) }
    if (selected != null) { LessonDetail(selected!!){ selected = null }; return }
    Column(Modifier.fillMaxSize().background(Surface).verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Learn", style=MaterialTheme.typography.headlineMedium.copy(fontWeight=FontWeight.Bold), color=TextPrimary)
        Text("Simple answers for every phase of your cycle", color=TextSecondary, modifier=Modifier.padding(top=4.dp))
        Spacer(Modifier.height(18.dp))
        Spacer(Modifier.height(20.dp))
        when { state.isLoading -> CircularProgressIndicator(color=Primary); state.error != null -> Text(state.error!!, color=TextSecondary); else -> Column(verticalArrangement=Arrangement.spacedBy(12.dp)){ state.lessons.forEach { lesson -> LessonCard(lesson){selected=lesson} } } }
    }
}
@Composable private fun LessonCard(lesson: Lesson,onClick:()->Unit){ Card(Modifier.fillMaxWidth().clickable(onClick=onClick).shadow(6.dp, RoundedCornerShape(20.dp), clip=false),RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(Color.White),elevation=CardDefaults.cardElevation(0.dp),border=BorderStroke(1.dp,Outline)){ Column(Modifier.padding(16.dp)){ Row(verticalAlignment=Alignment.CenterVertically){Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp));Text("  ${lesson.estimatedReadTime} min read",color=TextSecondary,style=MaterialTheme.typography.labelMedium);Spacer(Modifier.weight(1f));Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "Read article", tint = Primary)}; Text(lesson.title,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold,color=TextPrimary,modifier=Modifier.padding(top=10.dp));Text(lesson.content,maxLines=3,color=TextSecondary,modifier=Modifier.padding(top=6.dp)) } } }
@Composable private fun LessonDetail(lesson:Lesson,onBack:()->Unit){
    val paragraphs = remember(lesson.content) { lesson.content.split(Regex("\\n+|(?<=[.!?])\\s+(?=[A-Z])")).filter { it.isNotBlank() } }
    var audioState by remember { mutableIntStateOf(0) } // 0 idle, 1 playing, 2 paused
    val context = androidx.compose.ui.platform.LocalContext.current
    val tts = remember { TextToSpeech(context) { } }
    DisposableEffect(Unit) { onDispose { tts.stop(); tts.shutdown() } }
    Column(Modifier.fillMaxSize().background(Surface).verticalScroll(rememberScrollState()).padding(horizontal=20.dp, vertical=16.dp)) {
        Text("←  Learn", color=Primary, modifier=Modifier.clickable(onClick=onBack))
        Column(verticalArrangement=Arrangement.spacedBy(16.dp), modifier=Modifier.padding(bottom=24.dp)) {
                Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(20.dp))) { Image(painterResource(lessonIllustration(lesson)), "Bloom mascot illustration", Modifier.fillMaxWidth().height(190.dp).padding(vertical=12.dp), contentScale=ContentScale.Fit) }
                Text(lesson.title, style=MaterialTheme.typography.headlineSmall, fontWeight=FontWeight.Bold, color=TextPrimary)
                Row(Modifier.fillMaxWidth().padding(top=6.dp), verticalAlignment=Alignment.CenterVertically) {
                    Text("${lesson.estimatedReadTime} min read", color=TextSecondary)
                    Spacer(Modifier.weight(1f))
                    if (audioState == 0) TextButton(onClick = { tts.language = Locale.ENGLISH; tts.speak(lesson.content, TextToSpeech.QUEUE_FLUSH, null, "bloom-${lesson.id}"); audioState = 1 }) { Text("Play") }
                    else if (audioState == 1) TextButton(onClick = { tts.stop(); audioState = 2 }) { Text("Pause") }
                    else TextButton(onClick = { tts.speak(lesson.content, TextToSpeech.QUEUE_FLUSH, null, "bloom-resume-${lesson.id}"); audioState = 1 }) { Text("Resume") }
                    TextButton(onClick = { tts.stop(); audioState = 0 }) { Text("Stop") }
                }
            paragraphs.forEachIndexed { index, paragraph ->
                if (index > 0 && index % 2 == 0) {
                    val illustrations = lessonIllustrations(lesson)
                    Image(painterResource(illustrations[(index / 2 - 1).mod(illustrations.size)]), "Bloom mascot article illustration", Modifier.fillMaxWidth().height(150.dp).background(Color.White), contentScale=ContentScale.Fit)
                }
                Text(paragraph, style=MaterialTheme.typography.bodyLarge, color=TextPrimary)
            }
        }
    }
}

private fun lessonIllustration(lesson: Lesson): Int = when {
    lesson.title.contains("Mood", ignoreCase = true) -> R.drawable.article_mood_1
    lesson.title.contains("Cramps", ignoreCase = true) -> R.drawable.article_cramps_1
    lesson.title.contains("Crave", ignoreCase = true) -> R.drawable.article_cravings_1
    lesson.title.contains("Fatigue", ignoreCase = true) -> R.drawable.article_fatigue_1
    lesson.title.contains("First Period", ignoreCase = true) -> R.drawable.article_first_period_1
    else -> R.drawable.learn_mascot_hero
}

private fun lessonIllustrations(lesson: Lesson): List<Int> = when {
    lesson.title.contains("Mood", ignoreCase = true) -> listOf(R.drawable.article_mood_1, R.drawable.article_mood_2, R.drawable.article_mood_3, R.drawable.article_mood_4)
    lesson.title.contains("Cramps", ignoreCase = true) -> listOf(R.drawable.article_cramps_1, R.drawable.article_cramps_2, R.drawable.article_cramps_3, R.drawable.article_cramps_4)
    lesson.title.contains("Crave", ignoreCase = true) -> listOf(R.drawable.article_cravings_1, R.drawable.article_cravings_2, R.drawable.article_cravings_3, R.drawable.article_cravings_4)
    lesson.title.contains("Fatigue", ignoreCase = true) -> listOf(R.drawable.article_fatigue_1, R.drawable.article_fatigue_2, R.drawable.article_fatigue_3, R.drawable.article_fatigue_4)
    lesson.title.contains("First Period", ignoreCase = true) -> listOf(R.drawable.article_first_period_1, R.drawable.article_first_period_2, R.drawable.article_first_period_3, R.drawable.article_first_period_4)
    else -> listOf(R.drawable.learn_mascot_hero, R.drawable.learn_phases, R.drawable.learn_selfcare)
}




