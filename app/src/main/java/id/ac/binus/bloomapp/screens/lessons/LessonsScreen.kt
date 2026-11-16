package id.ac.binus.bloomapp.screens.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Column(Modifier.fillMaxSize().background(Surface).padding(20.dp)) {
        Text("Learn", style=MaterialTheme.typography.headlineMedium.copy(fontWeight=FontWeight.Bold), color=TextPrimary)
        Text("Simple answers for every phase of your cycle", color=TextSecondary, modifier=Modifier.padding(top=4.dp))
        Spacer(Modifier.height(18.dp))
        Card(shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=PrimaryLight), elevation=CardDefaults.cardElevation(0.dp), border=BorderStroke(1.dp,Outline)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment=Alignment.CenterVertically) {
                Image(painterResource(R.drawable.bg_onboarding_illustration), "Bloom mascot", Modifier.size(92.dp).clip(RoundedCornerShape(18.dp)), contentScale=ContentScale.Crop)
                Column(Modifier.padding(start=14.dp)){ Text("You’ve got this", style=MaterialTheme.typography.titleMedium, fontWeight=FontWeight.Bold, color=TextPrimary); Text("Learn what your body is telling you.", color=TextSecondary, style=MaterialTheme.typography.bodySmall) }
            }
        }
        Spacer(Modifier.height(20.dp))
        when { state.isLoading -> CircularProgressIndicator(color=Primary); state.error != null -> Text(state.error!!, color=TextSecondary); else -> LazyColumn(verticalArrangement=Arrangement.spacedBy(12.dp)){ items(state.lessons,key={it.id}){ lesson -> LessonCard(lesson){selected=lesson} } } }
    }
}
@Composable private fun LessonCard(lesson: Lesson,onClick:()->Unit){ Card(Modifier.fillMaxWidth().clickable(onClick=onClick),RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(Color.White),elevation=CardDefaults.cardElevation(0.dp),border=BorderStroke(1.dp,Outline)){ Column(Modifier.padding(16.dp)){ Row(verticalAlignment=Alignment.CenterVertically){Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp));Text("  ${lesson.estimatedReadTime} min read",color=TextSecondary,style=MaterialTheme.typography.labelMedium);Spacer(Modifier.weight(1f));Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "Read article", tint = Primary)}; Text(lesson.title,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold,color=TextPrimary,modifier=Modifier.padding(top=10.dp));Text(lesson.content,maxLines=3,color=TextSecondary,modifier=Modifier.padding(top=6.dp)) } } }
@Composable private fun LessonDetail(lesson:Lesson,onBack:()->Unit){ Column(Modifier.fillMaxSize().background(Surface).padding(20.dp)){Text("←  Learn",color=Primary,modifier=Modifier.clickable(onClick=onBack));Image(painterResource(R.drawable.login_illustration),"Bloom mascot illustration",Modifier.fillMaxWidth().height(190.dp).padding(vertical=12.dp),contentScale=ContentScale.Fit);Text(lesson.title,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold,color=TextPrimary);Text("${lesson.estimatedReadTime} min read",color=TextSecondary,modifier=Modifier.padding(top=6.dp));Text(lesson.content,style=MaterialTheme.typography.bodyLarge,color=TextPrimary,modifier=Modifier.padding(top=18.dp))} }


