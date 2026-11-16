package id.ac.binus.bloomapp.data.network

import id.ac.binus.bloomapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

data class Lesson(
    val id: String,
    val title: String,
    val content: String,
    val estimatedReadTime: Int,
)

class BloomApi(
    private val baseUrl: String = BuildConfig.BLOOM_API_BASE_URL,
) {
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
