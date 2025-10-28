package com.vistony.app.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.ConsultaOTItem
import com.vistony.app.Entidad.Temperatura
import com.vistony.app.Entidad.TemperaturaRequest
import com.vistony.app.Repository.TemperaturaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TemperaturaViewModel @Inject constructor(
    private val temperaturaRepository: TemperaturaRepository
) : ViewModel() {

    // Almacenamiento local con listas
    private val _temperaturas = MutableStateFlow<List<Temperatura>>(emptyList())
    val temperaturas: StateFlow<List<Temperatura>> = _temperaturas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()
    
    private val _isConsultingOT = MutableStateFlow(false)
    val isConsultingOT: StateFlow<Boolean> = _isConsultingOT.asStateFlow()
    
    // Para evitar consultas repetidas
    private var lastConsultedOT = ""
    private var consultJob: kotlinx.coroutines.Job? = null

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // Fechas para filtrado
    private val _fechaIni = MutableStateFlow<LocalDate?>(LocalDate.now())
    val fechaIni: StateFlow<LocalDate?> = _fechaIni.asStateFlow()

    private val _fechaFin = MutableStateFlow<LocalDate?>(LocalDate.now())
    val fechaFin: StateFlow<LocalDate?> = _fechaFin.asStateFlow()

    // Estado del formulario
    data class TemperaturaFormState(
        var fecha: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        var ot: String = "",
        var descripcion: List<ConsultaOTItem> = emptyList(), // Lista de productos disponibles
        var productoSeleccionado: ConsultaOTItem? = null, // Producto seleccionado del dropdown
        var hora: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
        var temperatura: String = "",
        //var auxiliar: String = "",
        var observaciones: String = "",
        var isFormValid: Boolean = false
    )

    private val _formState = MutableStateFlow(TemperaturaFormState())
    val formState: StateFlow<TemperaturaFormState> = _formState.asStateFlow()

    // No hay datos de ejemplo - se cargan desde el API

    fun updateFecha(fecha: String) {
        _formState.value = _formState.value.copy(fecha = fecha)
        validateForm()
    }

    fun updateOT(ot: String) {
        _formState.value = _formState.value.copy(ot = ot)
        validateForm()
        
        // Cancelar consulta anterior si existe
        consultJob?.cancel()
        
        // Consultar OT automáticamente con debounce para evitar múltiples consultas
        if (ot.isNotEmpty() && ot.length >= 6) {
            consultJob = viewModelScope.launch {
                // Esperar 800ms antes de consultar (debounce)
                kotlinx.coroutines.delay(800)
                
                // Solo consultar si la OT es diferente a la última consultada
                if (ot != lastConsultedOT) {
                    consultarOT(ot)
                    lastConsultedOT = ot
                }
            }
            } else {
                // Si la OT es menor a 6 caracteres, limpiar todo
                if (ot.length < 6) {
                    _formState.value = _formState.value.copy(
                        descripcion = emptyList(),
                        productoSeleccionado = null,
                        ot = ot,
                        //auxiliar = ""
                    )
                }
            }
    }
    
    fun consultarOT(ot: String) {
        viewModelScope.launch {
            Log.d("TemperaturaViewModel", "=== CONSULTANDO OT ===")
            Log.d("TemperaturaViewModel", "OT a consultar: $ot")
            
            _isConsultingOT.value = true
            
            try {
                val result = temperaturaRepository.consultarOT(ot)
                
                result.fold(
                    onSuccess = { response ->
                        Log.d("TemperaturaViewModel", "SUCCESS - Response.success: ${response.success}")
                        Log.d("TemperaturaViewModel", "SUCCESS - Cantidad de items: ${response.data.size}")
                        
                        if (response.success && response.data.isNotEmpty()) {
                            val productos = response.data
                            Log.d("TemperaturaViewModel", "Productos encontrados: ${productos.size}")
                            Log.d("TemperaturaViewModel", "Productos: ${productos.map { "${it.ot} - ${it.producto}" }}")
                            // Si solo hay un producto, seleccionarlo automáticamente
                            if (productos.size == 1) {
                                val productoUnico = productos.first()
                                _formState.value = _formState.value.copy(
                                    descripcion = productos,
                                    productoSeleccionado = productoUnico,
                                    ot = productoUnico.ot,
                                    //auxiliar = "" // Se llenará con el nombre del usuario
                                )
                                Log.d("TemperaturaViewModel", "Producto único seleccionado automáticamente: ${productoUnico.producto}")
                            } else {
                                // Si hay múltiples productos, mostrar la lista para seleccionar
                                _formState.value = _formState.value.copy(
                                    descripcion = productos,
                                    productoSeleccionado = null
                                )
                                Log.d("TemperaturaViewModel", "Hay ${productos.size} productos disponibles")
                            }
                        } else {
                            // Si no hay datos, limpiar
                            if (response.data.isEmpty()) {
                                Log.w("TemperaturaViewModel", "No se encontraron productos para OT: $ot")
                                _formState.value = _formState.value.copy(
                                    descripcion = emptyList(),
                                    productoSeleccionado = null
                                )
                            }else{}                        }
                    },
                    onFailure = { exception ->
                        Log.e("TemperaturaViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        // No mostrar error si la OT no se encuentra, solo limpiar
                        _formState.value = _formState.value.copy(
                            descripcion = emptyList(),
                            productoSeleccionado = null
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e("TemperaturaViewModel", "EXCEPCIÓN INESPERADA", e)
                _formState.value = _formState.value.copy(descripcion = emptyList())
            }
            
            _isConsultingOT.value = false
            Log.d("TemperaturaViewModel", "=== FIN CONSULTAR OT ===")
        }
    }

    fun seleccionarProducto(producto: ConsultaOTItem) {
        _formState.value = _formState.value.copy(
            productoSeleccionado = producto,
            ot = producto.ot
            // No tocar auxiliar - se llenará con el nombre del usuario separado
        )
        validateForm()
        Log.d("TemperaturaViewModel", "Producto seleccionado: OT=${producto.ot}, Producto=${producto.producto}")
    }
    
    fun updateAuxiliar(auxiliar: String) {
        //_formState.value = _formState.value.copy(auxiliar = auxiliar)
        validateForm()
    }

    fun updateHora(hora: String) {
        _formState.value = _formState.value.copy(hora = hora)
        validateForm()
    }

    fun updateTemperatura(temperatura: String) {
        _formState.value = _formState.value.copy(temperatura = temperatura)
        validateForm()
    }

    fun updateObservaciones(observaciones: String) {
        _formState.value = _formState.value.copy(observaciones = observaciones)
        validateForm()
    }

    private fun validateForm() {
        val current = _formState.value
        val isValid = current.fecha.isNotEmpty() &&
                current.ot.isNotEmpty() &&
                current.productoSeleccionado != null &&
                current.hora.isNotEmpty() &&
                current.temperatura.isNotEmpty()
                //current.auxiliar.isNotEmpty()
        
        _formState.value = current.copy(isFormValid = isValid)
    }

    fun obtenerTemperaturas(userId: String, fechaInicio: LocalDate, fechaFin: LocalDate, auxiliar: String? = "Admin") {
        viewModelScope.launch {
            Log.d("TemperaturaViewModel", "=== OBTENIENDO TEMPERATURAS ===")
            Log.d("TemperaturaViewModel", "UserId: $userId, FechaInicio: $fechaInicio, FechaFin: $fechaFin")
            
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val fechaInicioStr = fechaInicio.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                val fechaFinStr = fechaFin.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                
                Log.d("TemperaturaViewModel", "Formato de fechas - Inicio: $fechaInicioStr, Fin: $fechaFinStr")
                
                val result = temperaturaRepository.obtenerTemperaturas(userId, fechaInicioStr, fechaFinStr, auxiliar)
                
                Log.d("TemperaturaViewModel", "Resultado recibido del repository")
                
                result.fold(
                    onSuccess = { response ->
                        Log.d("TemperaturaViewModel", "SUCCESS - Response.success: ${response.success}")
                        Log.d("TemperaturaViewModel", "SUCCESS - Cantidad de temperaturas: ${response.data.size}")
                        
                        if (response.success) {
                            _temperaturas.value = response.data
                            _fechaIni.value = fechaInicio
                            _fechaFin.value = fechaFin
                            Log.d("TemperaturaViewModel", "Temperaturas actualizadas en el estado")
                        } else {
                            Log.e("TemperaturaViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        Log.e("TemperaturaViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al obtener temperaturas: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                Log.e("TemperaturaViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isLoading.value = false
            Log.d("TemperaturaViewModel", "=== FIN OBTENER TEMPERATURAS ===")
        }
    }

    fun registrarTemperatura(userDni: String) {
        viewModelScope.launch {
            Log.d("TemperaturaViewModel", "=== INICIANDO REGISTRO DE TEMPERATURA ===")
            
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                val formData = _formState.value
                Log.d("TemperaturaViewModel", "FormData - OT: ${formData.ot}, DNI: $userDni")
                //Log.d("TemperaturaViewModel", "FormData - Temperatura: ${formData.temperatura}, Auxiliar: ${formData.auxiliar}")
                Log.d("TemperaturaViewModel", "FormData - Producto Seleccionado: ${formData.productoSeleccionado?.producto}")
                
                // Combinar fecha y hora en el formato esperado: "yyyy-MM-dd HH:mm"
                val fechaRegistro = "${formData.fecha} ${formData.hora}"
                Log.d("TemperaturaViewModel", "Fecha de registro: $fechaRegistro")
                
                // Convertir temperatura a Double manteniendo los decimales exactos
                val temperaturaDouble = formData.temperatura.toDoubleOrNull() ?: 0.0
                
                // Obtener la descripción del producto seleccionado
                val descripcionProducto = formData.productoSeleccionado?.producto ?: ""
                
                val request = TemperaturaRequest(
                    ot = formData.ot,
                    descripcion = descripcionProducto,
                    dni = userDni,
                    //userName = formData.auxiliar,
                    temperatura = temperaturaDouble, // Enviar como Double para mantener decimales
                    controlTemperatura = formData.temperatura, // String para el campo requerido
                    fechaRegistro = fechaRegistro,
                    observacion = formData.observaciones
                )
                
                Log.d("TemperaturaViewModel", "Temperatura enviada - Original: ${formData.temperatura}, Convertida: $temperaturaDouble")
                
                Log.d("TemperaturaViewModel", "Creando request con: OT=${request.ot}, Temperatura=${request.temperatura}, controlTemperatura=${request.controlTemperatura}")
                
                val result = temperaturaRepository.registrarTemperatura(request)
                
                Log.d("TemperaturaViewModel", "Resultado recibido del repository")
                
                result.fold(
                    onSuccess = { response ->
                        Log.d("TemperaturaViewModel", "SUCCESS - Response.success: ${response.success}")
                        Log.d("TemperaturaViewModel", "SUCCESS - Response.message: ${response.message}")
                        Log.d("TemperaturaViewModel", "SUCCESS - Response.statusCode: ${response.statusCode}")
                        
                        if (response.success) {
                            _successMessage.value = response.message ?: "Temperatura registrada exitosamente"
                            Log.d("TemperaturaViewModel", "Limpiando formulario...")
                            resetForm()
                            
                            // Refrescar la lista de temperaturas después de registrar
                            val fechaInicio = _fechaIni.value ?: LocalDate.now()
                            val fechaFin = _fechaFin.value ?: LocalDate.now()
                            Log.d("TemperaturaViewModel", "Refrescando lista de temperaturas...")
                            obtenerTemperaturas(userDni, fechaInicio, fechaFin)
                        } else {
                            Log.e("TemperaturaViewModel", "ERROR - Response.success es false")
                            _errorMessage.value = response.message ?: "Error al registrar temperatura"
                        }
                    },
                    onFailure = { exception ->
                        Log.e("TemperaturaViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = exception.message ?: "Error desconocido al registrar temperatura"
                    }
                )
                
            } catch (e: Exception) {
                Log.e("TemperaturaViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isCreating.value = false
            Log.d("TemperaturaViewModel", "=== FIN DEL REGISTRO DE TEMPERATURA ===")
        }
    }

    fun eliminarTemperatura(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                // Simular delay de red
                kotlinx.coroutines.delay(500)
                
                val listaActual = _temperaturas.value.toMutableList()
                listaActual.removeAll { it.id == id }
                _temperaturas.value = listaActual
                
                _successMessage.value = "Temperatura eliminada exitosamente"
                
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar temperatura: ${e.message}"
            }
            
            _isLoading.value = false
        }
    }

    fun obtenerTemperaturaPorId(id: String): Temperatura? {
        return _temperaturas.value.find { it.id == id }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun resetForm() {
        _formState.value = TemperaturaFormState()
        _isConsultingOT.value = false
        lastConsultedOT = ""
        consultJob?.cancel()
    }

    // Función para refrescar datos (útil para pull-to-refresh)
    fun refrescarTemperaturas(userId: String) {
        obtenerTemperaturas(
            userId,
            _fechaIni.value ?: LocalDate.now(),
            _fechaFin.value ?: LocalDate.now()
        )
    }
}
