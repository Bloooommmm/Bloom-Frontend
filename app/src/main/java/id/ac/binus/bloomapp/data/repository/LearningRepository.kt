package id.ac.binus.bloomapp.data.repository

import id.ac.binus.bloomapp.data.local.LocalLearningData
import id.ac.binus.bloomapp.data.model.Unit

class LearningRepository {
    suspend fun getUnits(): List<Unit> = LocalLearningData.units

//    suspend fun getLesson(lessonId: String): Lesson? =
//        getUnits()
//            .flatMap { it.lessons }
//            .firstOrNull { it.id == lessonId }
    
}