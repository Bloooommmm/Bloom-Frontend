package id.ac.binus.bloomapp.screens.lessons

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.runtime.*
import java.util.Locale

internal class ArticleAudio(context: Context, text: String, @RawRes audioRes: Int? = null) {
    var ready by mutableStateOf(false); private set
    var playing by mutableStateOf(false); private set
    var paused by mutableStateOf(false); private set
    var error by mutableStateOf<String?>(null); private set
    private val handler = Handler(Looper.getMainLooper())
    private val chunks = text.split(Regex("(?<=[.!?])\\s+"))
        .filter { it.isNotBlank() }.flatMap { it.chunked(3000) }
    private var index = 0
    private var offset = 0
    private var baseOffset = 0
    private var request = 0
    private var closed = false
    private val mediaPlayer = audioRes?.let { MediaPlayer.create(context.applicationContext, it) }
    private val tts = if (mediaPlayer == null) TextToSpeech(context.applicationContext) { status -> handler.post { initialized(status) } } else null

    init {
        if (mediaPlayer != null) {
            ready = true
            mediaPlayer.setOnCompletionListener { handler.post { playing = false; paused = false } }
        }
    }

    private fun initialized(status: Int) {
        if (closed) return
        if (tts == null || status != TextToSpeech.SUCCESS || tts.setLanguage(Locale.ENGLISH) < 0) {
            error = "English audio is unavailable on this device."
            return
        }
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {}
            override fun onRangeStart(utteranceId: String, start: Int, end: Int, frame: Int) {
                handler.post { if (current(utteranceId)) offset = baseOffset + start }
            }
            override fun onDone(utteranceId: String) {
                handler.post {
                    if (current(utteranceId)) {
                        index++; offset = 0
                        if (index < chunks.size) speak() else stop()
                    }
                }
            }
            override fun onError(utteranceId: String) {
                handler.post { if (current(utteranceId)) failed() }
            }
        })
        ready = chunks.isNotEmpty()
    }

    private fun current(id: String) = !closed && playing && id == request.toString()

    private fun speak() {
        baseOffset = offset
        request++
        if (tts?.speak(chunks[index].substring(offset), TextToSpeech.QUEUE_FLUSH, null, request.toString()) == TextToSpeech.ERROR) failed()
    }

    fun play() {
        if (!ready || closed) return
        error = null; playing = true; paused = false
        if (mediaPlayer != null) mediaPlayer.start() else speak()
    }

    fun pause() {
        if (!playing) return
        playing = false; paused = true; request++
        if (mediaPlayer != null) mediaPlayer.pause() else tts?.stop()
    }

    fun stop() {
        playing = false; paused = false; request++; index = 0; offset = 0
        if (mediaPlayer != null) { mediaPlayer.pause(); mediaPlayer.seekTo(0) } else tts?.stop()
    }

    private fun failed() {
        stop()
        error = "Audio could not play. Check your device's speech settings."
    }

    fun close() {
        closed = true
        stop()
        mediaPlayer?.release()
        tts?.shutdown()
        handler.removeCallbacksAndMessages(null)
    }
}
