package com.vistony.app.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.OperarioResponse
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperarioViewModel @Inject constructor():ViewModel() {

    private val operService = RetrofitInstance.operService

    var _operarioState by mutableStateOf(OperarioResponseState())
        private set
    init {
        viewModelScope.launch {
            try {
                val response = operService.getOperarios()
                if (response.statusCode == 200) {
                    _operarioState = OperarioResponseState(state = true, operarioResponse = response)
                } else {
                    _operarioState = OperarioResponseState(state = false)
                }
            } catch (e: Exception) {
                _operarioState = OperarioResponseState(state = false)
            }
        }
    }

}


data class OperarioResponseState(
    val state: Boolean = false,
    val operarioResponse: OperarioResponse? = null
)