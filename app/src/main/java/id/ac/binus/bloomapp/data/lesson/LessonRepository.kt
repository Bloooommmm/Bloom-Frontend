package id.ac.binus.bloomapp.data.lesson

import id.ac.binus.bloomapp.data.network.BloomApi
import id.ac.binus.bloomapp.data.network.Lesson

class LessonRepository(
    private val api: BloomApi = BloomApi(),
) {
    suspend fun getLessons(): List<Lesson> = api.getLessons()
}
