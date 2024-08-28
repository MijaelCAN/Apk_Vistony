package com.vistony.app.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.Area
import com.vistony.app.Entidad.AreaResponse
import com.vistony.app.Entidad.MaquinaResponse
import com.vistony.app.Entidad.MotivoResponse
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.ParadaRequest
import com.vistony.app.Entidad.ParadaResponse
import com.vistony.app.Entidad.PostParada
import com.vistony.app.Repository.ParadaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ParadaViewModel @Inject constructor() : ViewModel() {
    private val paradaRepository = ParadaRepository()

    private val _paradas = MutableStateFlow(ParadaResponseState())
    val paradas: StateFlow<ParadaResponseState> = _paradas.asStateFlow()
    private val _listParadas = MutableStateFlow(ParadaResponse())
    val listParadas: StateFlow<ParadaResponse> = _listParadas.asStateFlow()

    private val _areas = MutableStateFlow(areaResponseState())
    val areas: StateFlow<areaResponseState> = _areas.asStateFlow()

    private val _maquinas = MutableStateFlow(maquinaResponseState())
    val maquinas: StateFlow<maquinaResponseState> = _maquinas.asStateFlow()

    private val _motivos = MutableStateFlow(motivoResponseState())
    val motivos: StateFlow<motivoResponseState> = _motivos.asStateFlow()

    private val _estadoParada = MutableStateFlow<EstadoParada>(EstadoParada.Idle)
    val estadoParada: StateFlow<EstadoParada> = _estadoParada.asStateFlow()

    fun actualizarEstadoParada(nuevoEstado: EstadoParada) {
        _estadoParada.value = nuevoEstado
    }

    init {
        viewModelScope.launch {

            // Obtener las áreas desde el repositorio
            val responseArea = paradaRepository.getAreas()
            if (responseArea.isSuccessful) {
                val body = responseArea.body()
                if(body?.statusCode==200){
                    _areas.value = areaResponseState(true, body, "OK")
                }else{
                    _areas.value = areaResponseState(false, AreaResponse(500, emptyList()), "Error al obtener las áreas")
                }
            } else {
                _areas.value = areaResponseState(false, AreaResponse(500, emptyList()), "Error al obtener las áreas")
            }

            // Obtener las paradas desde el repositorio
            /*val response = paradaRepository.obtenerParadas()
            if (response.statusCode == 200) {
                _paradas.value =
                    ParadaResponseState(state = true, paradaResponse = response, message = "OK")
            } else {
                val data = listOf(
                    Parada(1, "Maquina 1", "10/10/2024", "10/10/2024", "10:12", ""),
                    Parada(2, "Maquina 2", "12/08/2024", "12/08/2024", "12:09", ""),
                    Parada(3, "Maquina 3", "03/10/2023", "03/10/2023", "22:34", ""),
                    Parada(4, "Maquina 4", "22/05/2023", "22/05/2023", "09:47", ""),
                )
                _paradas.value = ParadaResponseState(
                    state = false,
                    paradaResponse = ParadaResponse(200, data),
                    message = "Error al obtener las paradas"
                )
            }*/

            // Obtener las maquinas desde el repositorio
            try {
                val responseMaqui = paradaRepository.getMaquinas()
                if (responseMaqui.isSuccessful) {
                    val body = responseMaqui.body()
                    if(body?.statusCode == 200){
                        _maquinas.value = maquinaResponseState(true, body, "OK")
                    }
                }else{
                    _maquinas.value = maquinaResponseState(false, MaquinaResponse(500, emptyList()), "Error al obtener las maquinas")
                }
            } catch (e: Exception) {
                Log.e("Error", e.toString())
            }
        }
    }

    // Funcion para registrar una parada
    fun registrarParada(parada: ParadaRequest) {
        viewModelScope.launch {
            _estadoParada.value = EstadoParada.Cargando
            try {
                /*val response = paradaRepository.registrarParada(parada)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 201){
                        _paradas.value =
                            ParadaResponseState(state = true, paradaResponse = body, message = "OK")
                        _estadoParada.value = EstadoParada.Exitoso
                        Log.d("Parada", body.toString())
                    }else{
                        Log.d("ELSE", body.toString());
                    }
                } else {
                    _paradas.value = ParadaResponseState(
                        state = false,
                        message = "Error al registrar la parada"
                    )
                    _estadoParada.value = EstadoParada.Error("Error al registrar la parada")
                }*/
                delay(4000)
                _paradas.value =
                    ParadaResponseState(state = true, paradaResponse = PostParada(200, "Registro Exitoso"), message = "OK")
                _estadoParada.value = EstadoParada.Exitoso
                Log.d("Envío", parada.toString())
            } catch (e: Exception) {
                _paradas.value =
                    ParadaResponseState(state = false, message = "Error de Comunicacion")
            }
        }
    }

    // Funcion para detener una parada
    fun detenerParada(parada: Parada) {
        viewModelScope.launch {
            _estadoParada.value = EstadoParada.Cargando
            try {
                val response = paradaRepository.detenerParada(parada)
                if (response.isSuccessful) {
                    val body = response.body()
                    if(body?.statusCode == 200){
                        _paradas.value =
                            ParadaResponseState(state = true, paradaResponse = body, message = "OK")
                        _estadoParada.value = EstadoParada.Exitoso
                    }
                } else {
                    _paradas.value = ParadaResponseState(
                        state = false,
                        message = "Error al detener la parada"
                    )
                    _estadoParada.value = EstadoParada.Error("Error al detener la parada")
                }
            } catch (e: Exception) {
                _paradas.value =
                    ParadaResponseState(state = false, message = "Error de Comunicacion")
                _estadoParada.value = EstadoParada.Error("Error de Comunicacion")
            }
        }
    }


    // Funcion para obtener las maquinas segun el area
    /*fun obtenerMaquinas(area: String) {
        viewModelScope.launch {
            try {
                val response = paradaRepository.getMaquinas(area)
                if (response.isNotEmpty()) {
                    _maquinas.value = maquinaResponseState(true, maquinaResponse = MaquinaResponse(200, response), "OK")
                }else{
                    _maquinas.value = maquinaResponseState(false, maquinaResponse = MaquinaResponse(500, emptyList()), "Error al obtener las maquinas")
                }
            } catch (e: Exception) {
            }
        }
    }*/

    // funcion para obtener los motivos segun la maquina
    fun obtenerMotivos(areaId:Int) {
        viewModelScope.launch {
            try {
                val response = paradaRepository.getMotivoParada(areaId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if(body?.statusCode == 200){
                        _motivos.value = motivoResponseState(true, body, "OK")
                    }
                }else{
                    _motivos.value = motivoResponseState(false, motivoResponse = MotivoResponse(500, emptyList()), "Lista vacia")
                }
            }catch (e: Exception){
                _motivos.value = motivoResponseState(false, motivoResponse = MotivoResponse(500, emptyList()), "Error al obtener los motivos")
            }
        }
    }
}

// Clase para la respuesta de las paradas
data class ParadaResponseState(
    val state: Boolean = false,
    val paradaResponse: PostParada = PostParada(),
    val message: String? = null
)

// Clase para el estado de las paradas
sealed class EstadoParada {
    object Idle : EstadoParada()
    object Cargando : EstadoParada()
    object Exitoso : EstadoParada()
    data class Error(val mensaje: String) : EstadoParada()
}

// Clase para la respuesta de las áreas
data class areaResponseState(
    val state: Boolean = false,
    val areaResponse: AreaResponse = AreaResponse(),
    val message: String? = null
)

// Clase para la respuesta de las maquinas
data class maquinaResponseState(
    val state: Boolean = false,
    val maquinaResponse: MaquinaResponse = MaquinaResponse(),
    val message: String? = null
)

// Clase para la respuesta de los motivos
data class motivoResponseState(
    val state: Boolean = false,
    val motivoResponse: MotivoResponse = MotivoResponse(),
    val message: String? = null
)