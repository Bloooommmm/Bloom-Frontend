package id.ac.binus.bloomapp.data.model

data class Lesson(
    val id: String,
    val title: String,
    val material: String,
    val questions: List<Question>
)