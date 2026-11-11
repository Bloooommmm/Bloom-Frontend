package id.ac.binus.bloomapp.data.model

data class Question(
    val id: String,
    val text: String,
    val options: Map<String, String>,
    val correctOptionId: String,
    val explanation: String
)