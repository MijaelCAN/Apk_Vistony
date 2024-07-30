package com.vistony.app.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vistony.app.Entidad.Evaluacion
import com.vistony.app.Entidad.EvaluacionResponse
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EvalViewModel @Inject constructor() : ViewModel() {

    private val evalService = RetrofitInstance.evalService

    var _evalState by mutableStateOf(EvalResponseState())
        private set

    fun EnviarEvaluacion(data: Evaluacion) {
        Log.e("PASO1", "Entro a Funcion")
        viewModelScope.launch {
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
                        _evalState = EvalResponseState(state = true, evalResponde = body)
                    } else {
                        _evalState = EvalResponseState(state = false)
                        Log.e("RESPONS", body?.data.toString())
                    }
                } else {
                    Log.e("PASO5", "Entro al Else - Respuesta, No exitoso")
                }
            } catch (e: Exception) {
                Log.e("PASO3", "Entro al CATH")
                Log.e("sdfsd", "Error de comunicacion $e")
            }
        }
    }
}

data class EvalResponseState(
    val state: Boolean = false,
    val evalResponde: EvaluacionResponse? = null,
)
/*
{
    "U_Fecha": "20240726",
    "U_Turno": "Noche",
    "U_OT": "240004145",
    "U_Peso_Check": "Y",
    "U_Peso_Comment": "OK",
    "U_Etiq_Check": "N",
    "U_Etiq_Comment": "",
    "U_Lot_Check": "Y",
    "U_Lot_Comment": "",
    "U_Limp_Check": "N",
    "U_Limp_Comment": "",
    "U_Sell_Check": "N",
    "U_Sell_Comment": "Y",
    "U_Enc_Check": "",
    "U_Enc_Comment": "Y",
    "U_Rotulo_Check": "",
    "U_Rotulo_Comment": "N",
    "U_Palet_Check": "",
    "U_Palet_Comment": "Y",
    "U_Conformidad": "Y",
    "U_Conformidad_Comment": "Conforme"
}*/