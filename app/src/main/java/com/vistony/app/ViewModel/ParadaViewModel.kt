package com.vistony.app.ViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.vistony.app.Entidad.Area
import com.vistony.app.Entidad.AreaResponse
import com.vistony.app.Entidad.ListaRequest
import com.vistony.app.Entidad.MaquinaResponse
import com.vistony.app.Entidad.MotivoResponse
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.ParadaRequest
import com.vistony.app.Entidad.ParadaResponse
import com.vistony.app.Entidad.PostParada
import com.vistony.app.Extras.formatoServidor
import com.vistony.app.Repository.ParadaRepository
import com.vistony.app.Service.NotificationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ParadaViewModel @Inject constructor(
    private val notificationService: NotificationService
) : ViewModel() {
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


    private val _fechaIni = mutableStateOf(LocalDateTime.now().minusDays(1))
    val fechaIni: State<LocalDateTime?> = _fechaIni

    private val _fechaFin = mutableStateOf(LocalDateTime.now())
    val fechaFin: State<LocalDateTime?> = _fechaFin


    fun actualizarEstadoParada(nuevoEstado: EstadoParada) {
        _estadoParada.value = nuevoEstado
    }
    fun setFechaIni(fecha: LocalDateTime?) { _fechaIni.value = fecha?.toLocalDate()?.atStartOfDay() }
    fun setFechaFin(fecha: LocalDateTime?) { _fechaFin.value = fecha?.toLocalDate()?.atStartOfDay() }

    init {
        // Las APIs se cargan explícitamente desde la pantalla via cargarDatosIniciales()
        // para evitar llamadas en background antes de que el usuario esté logueado
    }

    fun cargarDatosIniciales() {
        viewModelScope.launch {
            try {
                val responseArea = paradaRepository.getAreas()
                if (responseArea.isSuccessful) {
                    val body = responseArea.body()
                    if (body?.statusCode == 200) {
                        _areas.value = areaResponseState(true, body, "OK")
                    } else {
                        _areas.value = areaResponseState(false, AreaResponse(500, data = emptyList()), "Error al obtener las áreas")
                    }
                } else {
                    _areas.value = areaResponseState(false, AreaResponse(500, data = emptyList()), "Error al obtener las áreas")
                }
            } catch (e: Exception) {
                Log.e("ParadaViewModel", "Error al obtener áreas: $e")
                _areas.value = areaResponseState(false, AreaResponse(500, data = emptyList()), "Sin conexión")
            }

            try {
                val responseMaqui = paradaRepository.getMaquinas()
                if (responseMaqui.isSuccessful) {
                    val body = responseMaqui.body()
                    if (body?.statusCode == 200) {
                        _maquinas.value = maquinaResponseState(true, body, "OK")
                    }
                } else {
                    _maquinas.value = maquinaResponseState(false, MaquinaResponse(500, emptyList()), "Error al obtener las maquinas")
                }
            } catch (e: Exception) {
                Log.e("ParadaViewModel", "Error al obtener maquinas: $e")
                _maquinas.value = maquinaResponseState(false, MaquinaResponse(500, emptyList()), "Sin conexión")
            }
        }
    }
    fun obtenerParadas(request: ListaRequest) {
        Log.e("MDCR", request.toString())
        viewModelScope.launch {
            try {
                val response = paradaRepository.obtenerParadas(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 200) {
                        _listParadas.value = ParadaResponse(200, data =body.data)
                        Log.e("MDCR", "LISTA PARADAS: ${body.data}")
                    }else{
                        _listParadas.value = ParadaResponse(500, data = emptyList())
                    }
                }
            }catch (e: Exception) {
                Log.e("Error GETpA", e.toString())
            }
        }
    }


    // Funcion para registrar una parada
    fun registrarParada(parada: ParadaRequest) {
        viewModelScope.launch {
            _estadoParada.value = EstadoParada.Cargando
            Log.d("Envío-EstadoCargando", parada.toString())
            try {
                val response = paradaRepository.registrarParada(parada)
                Log.d("Envío-Response", parada.toString())
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("Envío-Body", body.toString());
                    if (body?.statusCode == 200){
                        Log.d("IF-Body", body.toString());
                        _paradas.value =
                            ParadaResponseState(state = true, paradaResponse = body, message = body.message)
                        _estadoParada.value = EstadoParada.Exitoso
                        Log.d("Envío-AfterBody", body.toString())
                        
                        // Enviar notificación a usuarios de mantenimiento
                        val docEntry = body.data.firstOrNull()?.DocEntry
                        notificationService.sendParadaNotification(
                            maquina = parada.U_Maquina,
                            area = parada.U_Area,
                            motivo = parada.U_MotivoParaMaq,
                            usuario = parada.U_Usuario,
                            docEntry = docEntry
                        ).onFailure { error ->
                            Log.e("ParadaViewModel", "Error al enviar notificación", error)
                        }
                    }else{
                        Log.d("ELSE", body.toString());
                    }
                } else {
                    Log.d("ELSE", response.toString());
                    _paradas.value = ParadaResponseState(
                        state = false,
                        message = "Error al registrar la parada"
                    )
                    _estadoParada.value = EstadoParada.Error("Error al registrar la parada")
                }
                /*delay(4000)
                _paradas.value =
                    ParadaResponseState(state = true, paradaResponse = PostParada(200, "Registro Exitoso"), message = "OK")
                _estadoParada.value = EstadoParada.Exitoso*/
                Log.d("Envío", parada.toString())
            } catch (e: Exception) {
                Log.d("Error Cath", e.toString())
                _paradas.value =
                    ParadaResponseState(state = false, message = "Error de Comunicacion")
            }
        }
    }

    // Funcion para detener una parada
    fun detenerParada(idParada: Int) {
        viewModelScope.launch {
            _estadoParada.value = EstadoParada.Cargando
            try {
                val response = paradaRepository.detenerParada(idParada)
                if (response.isSuccessful) {
                    val body = response.body()
                    if(body?.statusCode == 200){
                        _paradas.value =
                            ParadaResponseState(
                                state = true,
                                paradaResponse = PostParada(
                                    body.statusCode,
                                    body.success,
                                    body.message,
                                    emptyList()
                                ),
                                message = body.data
                            )
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
                    ParadaResponseState(state = false, message = e.toString())
                _estadoParada.value = EstadoParada.Error(e.toString())
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