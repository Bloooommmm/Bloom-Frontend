package id.ac.binus.bloomapp.data.network

import id.ac.binus.bloomapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate

data class Lesson(
    val id: String,
    val title: String,
    val content: String,
    val estimatedReadTime: Int,
)

data class PeriodRecord(val id: String, val startDate: LocalDate, val endDate: LocalDate?) {
    fun includes(date: LocalDate): Boolean = !date.isBefore(startDate) && !date.isAfter(endDate ?: LocalDate.now())
}

class BloomApi(
    private val baseUrl: String = BuildConfig.BLOOM_API_BASE_URL,
) {
    suspend fun getPeriods(idToken: String): List<PeriodRecord> {
        val items = JSONObject(authenticatedGet("/api/tracking/periods", idToken)).getJSONArray("data")
        return List(items.length()) { index ->
            val item = items.getJSONObject(index)
            PeriodRecord(item.getString("id"), LocalDate.parse(item.getString("startDate").take(10)), item.optString("endDate").takeIf { it.length >= 10 }?.let { LocalDate.parse(it.take(10)) })
        }
    }

    suspend fun getMoods(idToken: String): String = authenticatedGet("/api/tracking/moods", idToken)

    suspend fun recordPeriod(idToken: String, startDate: String, endDate: String? = null): String = authenticatedJson("/api/tracking/periods", idToken, "POST", "{\"startDate\":\"$startDate\"${endDate?.let { ",\"endDate\":\"$it\"" } ?: ""}}")

    suspend fun finishPeriod(idToken: String, id: String, endDate: String): String = authenticatedJson("/api/tracking/periods/$id", idToken, "PATCH", "{\"endDate\":\"$endDate\"}")

    suspend fun recordMood(idToken: String, mood: String, loggedAt: String, note: String? = null): String = authenticatedJson("/api/tracking/moods", idToken, "PUT", "{\"mood\":\"${mood.replace("\\", "\\\\").replace("\"", "\\\"")}\",\"loggedAt\":\"$loggedAt\"${note?.let { ",\"note\":\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}" } ?: ""}}")

    private suspend fun authenticatedGet(path: String, idToken: String): String = authenticatedJson(path, idToken, "GET")

    private suspend fun authenticatedJson(path: String, idToken: String, method: String, body: String? = null): String = withContext(Dispatchers.IO) {
        val connection = (URL("${baseUrl.trimEnd('/')}$path").openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("Authorization", "Bearer $idToken")
            setRequestProperty("Accept", "application/json")
            if (body != null) { doOutput = true; setRequestProperty("Content-Type", "application/json"); outputStream.use { it.write(body.toByteArray()) } }
        }
        try { if (connection.responseCode !in 200..299) throw BloomApiException("Backend returned HTTP ${connection.responseCode}"); connection.inputStream.bufferedReader().use { it.readText() } } finally { connection.disconnect() }
    }

    suspend fun syncCurrentUser(idToken: String): Unit = withContext(Dispatchers.IO) {
        val connection = (URL("${baseUrl.trimEnd('/')}/api/users/me").openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("Authorization", "Bearer $idToken")
            setRequestProperty("Accept", "application/json")
        }
        try {
            if (connection.responseCode !in 200..299) {
                throw BloomApiException("Profile sync failed: HTTP ${connection.responseCode}")
            }
            connection.inputStream.close()
        } finally {
            connection.disconnect()
        }
    }

    suspend fun getLessons(): List<Lesson> = withContext(Dispatchers.IO) {
        val connection = (URL("${baseUrl.trimEnd('/')}/api/lessons").openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("Accept", "application/json")
        }

        try {
            if (connection.responseCode !in 200..299) {
                throw BloomApiException("Backend returned HTTP ${connection.responseCode}")
            }

            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val items = JSONArray(org.json.JSONObject(body).getJSONArray("data").toString())
            buildList(items.length()) {
                repeat(items.length()) { index ->
                    val item = items.getJSONObject(index)
                    add(
                        Lesson(
                            id = item.getString("id"),
                            title = item.getString("title"),
                            content = item.getString("content"),
                            estimatedReadTime = item.optInt("estimatedReadTime", 1),
                        ),
                    )
                }
            }
        } finally {
            connection.disconnect()
        }
    }
}

class BloomApiException(message: String) : Exception(message)
