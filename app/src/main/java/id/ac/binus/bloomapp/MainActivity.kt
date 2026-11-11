package id.ac.binus.bloomapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import id.ac.binus.bloomapp.data.LearningData
import id.ac.binus.bloomapp.ui.HomeScreen
import id.ac.binus.bloomapp.ui.QuizScreen
import id.ac.binus.bloomapp.ui.QuizViewModel
import id.ac.binus.bloomapp.ui.theme.BloomAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val quizViewModel =
            ViewModelProvider(this)[QuizViewModel::class.java]

        setContent {
            BloomAppTheme {
                val lesson = quizViewModel.currentLesson

                BackHandler(
                    enabled = lesson != null,
                    onBack = quizViewModel::goHome
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val screenModifier = Modifier.padding(innerPadding)

                    // ponytail: Two destinations; use Navigation Compose for more routes.
                    if (lesson == null) {
                        HomeScreen(
                            chapters = LearningData.chapters,
                            onLessonClick = quizViewModel::startLesson,
                            modifier = screenModifier
                        )
                    } else {
                        QuizScreen(
                            state = quizViewModel.uiState,
                            question = quizViewModel.currentQuestion,
                            totalQuestions = lesson.questions.size,
                            lessonTitle = lesson.title,
                            onSelectAnswer = quizViewModel::selectAnswer,
                            onCheckAnswer = quizViewModel::checkAnswer,
                            onNext = quizViewModel::nextQuestion,
                            onRestart = quizViewModel::restart,
                            onBackHome = quizViewModel::goHome,
                            modifier = screenModifier
                        )
                    }
                }
            }
        }
    }
}