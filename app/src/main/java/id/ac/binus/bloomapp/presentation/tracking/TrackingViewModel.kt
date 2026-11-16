package id.ac.binus.bloomapp.presentation.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import id.ac.binus.bloomapp.data.network.BloomApi
import id.ac.binus.bloomapp.data.network.PeriodRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

data class TrackingState(val periods: List<PeriodRecord> = emptyList(), val loading: Boolean = false, val saving: Boolean = false, val error: String? = null)

class TrackingViewModel(private val api: BloomApi = BloomApi()) : ViewModel() {
    private val _state = MutableStateFlow(TrackingState())
    val state = _state.asStateFlow()
    private var profileSynced = false

    private suspend fun token(): String = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token ?: error("Please sign in again")

    private suspend fun <T> retryRequest(block: suspend () -> T): T {
        var last: Throwable? = null
        repeat(2) { attempt ->
            try { return block() } catch (error: Throwable) {
                last = error
                if (attempt == 0) delay(400)
            }
        }
        throw last ?: IllegalStateException("Request failed")
    }

    fun refresh() = viewModelScope.launch {
        _state.value = _state.value.copy(loading = true, error = null)
        try {
            val authToken = token()
            if (!profileSynced) {
                retryRequest { api.syncCurrentUser(authToken) }
                profileSynced = true
            }
            _state.value = _state.value.copy(periods = retryRequest { api.getPeriods(authToken) }, loading = false)
        }
        catch (_: Exception) { _state.value = _state.value.copy(loading = false, error = "Cycle data could not load. Try again.") }
    }

    fun start(date: LocalDate) = viewModelScope.launch {
        _state.value = _state.value.copy(saving = true, error = null)
        try { retryRequest { api.recordPeriod(token(), date.toString()) }; _state.value = _state.value.copy(saving = false); refresh() }
        catch (_: Exception) { _state.value = _state.value.copy(saving = false, error = "Period record could not be saved. Try again.") }
    }

    fun finish(id: String, date: LocalDate) = viewModelScope.launch {
        _state.value = _state.value.copy(saving = true, error = null)
        try { retryRequest { api.finishPeriod(token(), id, date.toString()) }; _state.value = _state.value.copy(saving = false); refresh() }
        catch (_: Exception) { _state.value = _state.value.copy(saving = false, error = "Period record could not be saved. Try again.") }
    }

    fun delete(id: String) = viewModelScope.launch {
        _state.value = _state.value.copy(saving = true, error = null)
        try { retryRequest { api.deletePeriod(token(), id) }; _state.value = _state.value.copy(saving = false); refresh() }
        catch (_: Exception) { _state.value = _state.value.copy(saving = false, error = "Period record could not be deleted. Try again.") }
    }
}
