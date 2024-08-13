package com.vistony.app.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.OperarioResponse
import com.vistony.app.Entidad.lineaResponse
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperarioViewModel @Inject constructor():ViewModel() {

    private val operService = RetrofitInstance.operService
    private val lineaService = RetrofitInstance.lineaService

    var _operarioState by mutableStateOf(OperarioResponseState())
        private set
    var _lineaState by mutableStateOf(LineaResponseState())
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

        viewModelScope.launch {
            try {
                val response = lineaService.getLinea()
                if(response.isSuccessful){
                    val body = response.body()
                    if(body?.statusCode==200){
                        _lineaState = LineaResponseState(state = true, lineaResponse = body)

                    }else{
                        _lineaState = LineaResponseState(state = false)
                    }

                }else{
                    _lineaState = LineaResponseState(state = false)
                }
            }catch (e: Exception){
                Log.e("Error","Error de comunicacion: $e")
            }
        }

    }

    /*fun getLinea(){
        viewModelScope.launch {
            try {
                val response = lineaService.getLinea()
                if(response.isSuccessful){
                    val body = response.body()
                    if(body?.statusCode==200){
                        _lineaState = LineaResponseState(state = true, lineaResponse = body)

                    }else{
                        _lineaState = LineaResponseState(state = false)
                    }

                }else{
                    _lineaState = LineaResponseState(state = false)
                }
            }catch (e: Exception){
                Log.e("Error","Error de comunicacion: $e")
            }
        }
    }*/

}


data class OperarioResponseState(
    val state: Boolean = false,
    val operarioResponse: OperarioResponse? = null
)

data class LineaResponseState(
    val state: Boolean = false,
    val lineaResponse: lineaResponse = lineaResponse()
)