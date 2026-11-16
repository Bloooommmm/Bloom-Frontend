package id.ac.binus.bloomapp.presentation.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import id.ac.binus.bloomapp.data.network.BloomApi
import id.ac.binus.bloomapp.data.network.PeriodRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

data class TrackingState(val periods: List<PeriodRecord> = emptyList(), val loading: Boolean = false, val saving: Boolean = false, val error: String? = null)

class TrackingViewModel(private val api: BloomApi = BloomApi()) : ViewModel() {
    private val _state = MutableStateFlow(TrackingState())
    val state = _state.asStateFlow()

    private suspend fun token(): String = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token ?: error("Silakan login kembali")

    fun refresh() = viewModelScope.launch {
        _state.value = _state.value.copy(loading = true, error = null)
        try { _state.value = _state.value.copy(periods = api.getPeriods(token()), loading = false) }
        catch (_: Exception) { _state.value = _state.value.copy(loading = false, error = "Data haid gagal dimuat. Coba lagi.") }
    }

    fun start(date: LocalDate) = viewModelScope.launch {
        _state.value = _state.value.copy(saving = true, error = null)
        try { api.recordPeriod(token(), date.toString()); _state.value = _state.value.copy(saving = false); refresh() }
        catch (_: Exception) { _state.value = _state.value.copy(saving = false, error = "Catatan gagal disimpan. Coba lagi.") }
    }

    fun finish(id: String, date: LocalDate) = viewModelScope.launch {
        _state.value = _state.value.copy(saving = true, error = null)
        try { api.finishPeriod(token(), id, date.toString()); _state.value = _state.value.copy(saving = false); refresh() }
        catch (_: Exception) { _state.value = _state.value.copy(saving = false, error = "Catatan gagal disimpan. Coba lagi.") }
    }
}
