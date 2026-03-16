package com.vistony.app.ViewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Service.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkStatusViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    @ApplicationContext private val context: Context
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

    /**
     * Verifica manualmente el estado de la conexión a internet
     * y actualiza el estado si hay conexión disponible
     */
    fun checkConnection() {
        viewModelScope.launch {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val capabilities = network?.let { connectivityManager.getNetworkCapabilities(it) }
            
            val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            
            if (isConnected) {
                _status.value = ConnectivityObserver.Status.Available
            } else {
                _status.value = ConnectivityObserver.Status.Unavailable
            }
        }
    }
}
