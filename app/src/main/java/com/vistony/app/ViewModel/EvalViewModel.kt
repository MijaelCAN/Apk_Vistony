package com.vistony.app.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.Evaluacion
import com.vistony.app.Entidad.EvaluacionResponse
import com.vistony.app.Entidad.InspecionRequest
import com.vistony.app.Entidad.InspecionResponse
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class EvalViewModel @Inject constructor() : ViewModel() {

    private val evalService = RetrofitInstance.evalService

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _evalState = MutableStateFlow(EvalResponseState())
    var evalState: StateFlow<EvalResponseState> = _evalState.asStateFlow()

    private val _listInspeccionState = MutableStateFlow(ListInspeccionState())
    var listInspeccionState: StateFlow<ListInspeccionState> = _listInspeccionState.asStateFlow()

    fun EnviarEvaluacion(data: Evaluacion) {
        Log.e("PASO1", "Entro a Funcion")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.e("PASO 2", "Entro al Try")
                val response = evalService.postEvaluacion(data)
                /*val gson = Gson()
                val jsonData = gson.toJson(data)
                Log.e("JSON_ENVIADO", jsonData)*/
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 201) {
                        Log.e("PASO4", "Entro al IF")
                        _evalState.value = EvalResponseState(state = true, evalResponde = body)
                    } else {
                        _evalState.value = EvalResponseState(state = false)
                        Log.e("RESPONS", body?.data.toString())
                    }
                } else {
                    Log.e("PASO5", "Entro al Else - Respuesta, No exitoso")
                }
            } catch (e: Exception) {
                Log.e("PASO3", "Entro al CATH")
                Log.e("sdfsd", "Error de comunicacion $e")
                _evalState.value = EvalResponseState(state = false)
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun getListInspeccion(newfechaIni: String, newfechaFin: String) {
        Log.e("eeeeeeeee", "Entro a Funcion")
        viewModelScope.launch {
            try {
                /*val fec_ini = SimpleDateFormat("yyyyMMdd").format(selectedDateIni)
                val fec_fin = SimpleDateFormat("yyyyMMdd").format(selectedDateFin)*/

                val response = evalService.getListInspeccion(InspecionRequest(newfechaIni, newfechaFin))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 200) {
                        Log.e("ok", "Exist")
                        _listInspeccionState.value = ListInspeccionState(state = true, listInspeccion = body, message = "OK")
                        Log.e("RESPONS", body.toString())
                    } else {
                        _listInspeccionState.value = ListInspeccionState(state = false, message = "Lista Vacia")
                        Log.e("RESPONS","Lista vacia")
                    }
                }else{
                    _listInspeccionState.value = ListInspeccionState(state = false, message = "Error de comunicacion")
                    Log.e("RESPONS", response.toString())
                }
            }catch (e: Exception){
                _listInspeccionState.value = ListInspeccionState(state = false, message = "Error de comunicacion")
                Log.e("sdfsd", "Error de comunicacion $e")
            }
        }
    }
}

data class EvalResponseState(
    val state: Boolean = false,
    val evalResponde: EvaluacionResponse = EvaluacionResponse(),
)
data class ListInspeccionState(
    val state: Boolean = false,
    val listInspeccion: InspecionResponse = InspecionResponse(),
    val message: String = ""
)
