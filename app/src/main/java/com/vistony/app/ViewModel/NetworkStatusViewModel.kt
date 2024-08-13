package com.vistony.app.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Service.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkStatusViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status: StateFlow<ConnectivityObserver.Status> = _status

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _status.value = status
            }
        }
    }
}
