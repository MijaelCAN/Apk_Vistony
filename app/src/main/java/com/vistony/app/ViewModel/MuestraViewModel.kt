package com.vistony.app.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.*
import com.vistony.app.Repository.MuestraRepository
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
class MuestraViewModel @Inject constructor(
    private val muestraRepository: MuestraRepository
) : ViewModel() {

    // Estados principales
    private val _muestras = MutableStateFlow<List<MuestraCabecera>>(emptyList())
    val muestras: StateFlow<List<MuestraCabecera>> = _muestras.asStateFlow()
    
    // Cache para muestras completas cuando se necesiten
    private val _muestrasCompletas = MutableStateFlow<List<MuestraCompleta>>(emptyList())
    val muestrasCompletas: StateFlow<List<MuestraCompleta>> = _muestrasCompletas.asStateFlow()
    
    // Productos para el dropdown
    private val _productos = MutableStateFlow<List<ProductoItem>>(emptyList())
    val productos: StateFlow<List<ProductoItem>> = _productos.asStateFlow()
    
    private val _isLoadingProductos = MutableStateFlow(false)
    val isLoadingProductos: StateFlow<Boolean> = _isLoadingProductos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()
    
    // Estados de edición
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()
    
    private val _currentMuestraId = MutableStateFlow<String?>(null)
    val currentMuestraId: StateFlow<String?> = _currentMuestraId.asStateFlow()

    // Estado del formulario de cabecera
    data class CabeceraFormState(
        var fechaRegistro: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        var codigo: String = "",
        var lote: String = "",
        var codigoMaquina: String = "",
        var embalaje: String = "",
        var maquina: String = "",
        var turno: String = "",
        var ot: String = "",
        var producto: String = "",
        var auxiliar: String = "",
        var encargadoProduccion: String = "",
        var isFormValid: Boolean = false
    )

    // Estado del formulario de material
    data class MaterialFormState(
        var material: String = "",
        var marca: String = "",
        var codigo: String = "",
        var lote: String = "",
        var isFormValid: Boolean = false
    )

    // Estado del formulario de inspección dimensional
    data class InspeccionDimensionalFormState(
        var horaInspeccion: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
        var temperaturaChiller: String = "",
        var temperaturaCiclo: String = "",
        var numeroCavidad: String = "1",
        var peso: String = "",
        var diametroRoscaMedida1: String = "",
        var diametroRoscaMedida2: String = "",
        var alturaBocaMedida1: String = "",
        var alturaBocaMedida2: String = "",
        var alturaBocaMedida3: String = "",
        var alturaBocaMedida4: String = "",
        var diametroPrecintoMedida1: String = "",
        var diametroPrecintoMedida2: String = "",
        var diametroPrecintoMedida3: String = "",
        var alturaTotalMedida1: String = "",
        var alturaTotalMedida2: String = "",
        var diametroInternoMedida1: String = "",
        var diametroInternoMedida2: String = "",
        var observacion: String = "",
        var isFormValid: Boolean = false
    )

    // Estado del formulario de check list
    data class CheckListFormState(
        var horaCheckList: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
        var testeado: String = "",
        var estabilidad: String = "",
        var tonalidad: String = "",
        var visorUniforme: String = "",
        var correctaCostura: String = "",
        var libreOvulamiento: String = "",
        var libreContaminacion: String = "",
        var observacion: String = "",
        var isFormValid: Boolean = false
    )

    // Estado del formulario de evaluación
    data class EvaluacionFormState(
        var equipo: String = "",
        var estado: String = "",
        var paletasAprobadas: String = "",
        var paletasObservadas: String = "",
        var paletasRechazadas: String = "",
        var bolsasAprobadas: String = "",
        var bolsasObservadas: String = "",
        var bolsasRechazadas: String = "",
        var criteriosEvaluacion: String = "",
        var isFormValid: Boolean = false
    )

    // Estado del formulario de Registro de Llegada
    data class RegistroLlegadaFormState(
        var numeroOrdenFabricacion: String = "",
        var numeroMuestra: String = "",
        var codigoProducto: String = "",
        var descripcionProducto: String = "",
        var maquina: String = "",
        var isFormValid: Boolean = false
    )

    // Estados de los formularios
    private val _cabeceraFormState = MutableStateFlow(CabeceraFormState())
    val cabeceraFormState: StateFlow<CabeceraFormState> = _cabeceraFormState.asStateFlow()

    private val _materialFormState = MutableStateFlow(MaterialFormState())
    val materialFormState: StateFlow<MaterialFormState> = _materialFormState.asStateFlow()

    private val _inspeccionFormState = MutableStateFlow(InspeccionDimensionalFormState())
    val inspeccionFormState: StateFlow<InspeccionDimensionalFormState> = _inspeccionFormState.asStateFlow()

    private val _checkListFormState = MutableStateFlow(CheckListFormState())
    val checkListFormState: StateFlow<CheckListFormState> = _checkListFormState.asStateFlow()

    private val _evaluacionFormState = MutableStateFlow(EvaluacionFormState())
    val evaluacionFormState: StateFlow<EvaluacionFormState> = _evaluacionFormState.asStateFlow()

    private val _registroLlegadaFormState = MutableStateFlow(RegistroLlegadaFormState())
    val registroLlegadaFormState: StateFlow<RegistroLlegadaFormState> = _registroLlegadaFormState.asStateFlow()

    // Lista de registros de llegada
    private val _registrosLlegada = MutableStateFlow<List<RegistroLlegada>>(emptyList())
    val registrosLlegada: StateFlow<List<RegistroLlegada>> = _registrosLlegada.asStateFlow()

    // Lista de muestras disponibles desde el API
    private val _muestrasDisponibles = MutableStateFlow<List<String>>(emptyList())
    val muestrasDisponibles: StateFlow<List<String>> = _muestrasDisponibles.asStateFlow()
    
    // Estado para mostrar alert cuando se encuentra la orden
    private val _productoEncontrado = MutableStateFlow<String?>(null)
    val productoEncontrado: StateFlow<String?> = _productoEncontrado.asStateFlow()

    // Listas temporales para cada sección
    private val _materialesTemporales = MutableStateFlow<List<MaterialEmpleado>>(emptyList())
    val materialesTemporales: StateFlow<List<MaterialEmpleado>> = _materialesTemporales.asStateFlow()

    private val _inspeccionesTemporales = MutableStateFlow<List<InspeccionDimensional>>(emptyList())
    val inspeccionesTemporales: StateFlow<List<InspeccionDimensional>> = _inspeccionesTemporales.asStateFlow()

    private val _checkListsTemporales = MutableStateFlow<List<CheckListInspeccion>>(emptyList())
    val checkListsTemporales: StateFlow<List<CheckListInspeccion>> = _checkListsTemporales.asStateFlow()
    
    // Especificaciones de soplado
    private val _especificacionSoplado = MutableStateFlow<EspecificacionSopladoData?>(null)
    val especificacionSoplado: StateFlow<EspecificacionSopladoData?> = _especificacionSoplado.asStateFlow()
    
    private val _codigoProductoActual = MutableStateFlow<String?>(null)
    
    // Función para obtener especificaciones de soplado
    fun obtenerEspecificacionSoplado(codProducto: String) {
        viewModelScope.launch {
            // Solo obtener si el código de producto cambió
            if (_codigoProductoActual.value != codProducto) {
                _codigoProductoActual.value = codProducto
                
                try {
                    val result = muestraRepository.obtenerEspecificacionSoplado(codProducto)
                    result.fold(
                        onSuccess = { response ->
                            if (response.success) {
                                _especificacionSoplado.value = response.data
                            } else {
                                android.util.Log.e("MuestraViewModel", "Error obteniendo especificación: ${response.message}")
                                _especificacionSoplado.value = null
                            }
                        },
                        onFailure = { exception ->
                            android.util.Log.e("MuestraViewModel", "Error obteniendo especificación", exception)
                            _especificacionSoplado.value = null
                        }
                    )
                } catch (e: Exception) {
                    android.util.Log.e("MuestraViewModel", "Excepción obteniendo especificación", e)
                    _especificacionSoplado.value = null
                }
            }
        }
    }
    
    // Función para validar si un valor está dentro del rango
    fun validarRango(valor: String, min: String, max: String): Boolean {
        if (valor.isBlank()) return true // Si está vacío, no validar
        return try {
            val valorNum = valor.toDouble()
            val minNum = min.toDoubleOrNull() ?: return true
            val maxNum = max.toDoubleOrNull() ?: return true
            valorNum >= minNum && valorNum <= maxNum
        } catch (e: Exception) {
            false
        }
    }

    // Estado del paso actual del wizard
    private val _pasoActual = MutableStateFlow(1)
    val pasoActual: StateFlow<Int> = _pasoActual.asStateFlow()

    // Función para obtener muestras desde el API
    fun obtenerMuestras(dni: String, role: String? = "soplado") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val fechaInicio = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                val fechaFin = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                
                val result = muestraRepository.obtenerMuestras(dni, fechaInicio, fechaFin, role)
                
                result.fold(
                    onSuccess = { response ->
                        if (response.success) {
                            _muestras.value = response.data
                        } else {
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        _errorMessage.value = "Error al obtener muestras: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isLoading.value = false
        }
    }

    // Función para obtener muestras con fechas personalizadas
    fun obtenerMuestrasConFechas(dni: String, fechaInicio: String, fechaFin: String, role: String? = "soplado") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val result = muestraRepository.obtenerMuestras(dni, fechaInicio, fechaFin, role)
                
                result.fold(
                    onSuccess = { response ->
                        if (response.success) {
                            _muestras.value = response.data
                        } else {
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        _errorMessage.value = "Error al obtener muestras: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isLoading.value = false
        }
    }

    // Funciones para actualizar formularios
    fun updateCabecera(field: String, value: String) {
        val current = _cabeceraFormState.value
        val newState = when (field) {
            "fechaRegistro" -> current.copy(fechaRegistro = value)
            "codigo" -> current.copy(codigo = value)
            "lote" -> current.copy(lote = value)
            "embalaje" -> current.copy(embalaje = value)
            "maquina" -> current.copy(maquina = value)
            "codigoMaquina" -> current.copy(codigoMaquina = value)
            "turno" -> current.copy(turno = value)
            "ot" -> current.copy(ot = value)
            "producto" -> current.copy(producto = value)
            "auxiliar" -> current.copy(auxiliar = value)
            "encargadoProduccion" -> current.copy(encargadoProduccion = value)
            else -> current
        }
        _cabeceraFormState.value = newState
        validateCabeceraForm()
        
        // Guardar automáticamente si está en modo edición
        guardarCambiosAutomaticamente()
    }
    
    // Función para seleccionar un producto del dropdown
    fun seleccionarProducto(productoItem: ProductoItem) {
        val current = _cabeceraFormState.value
        _cabeceraFormState.value = current.copy(
            ot = productoItem.ot,
            producto = productoItem.producto
        )
        validateCabeceraForm()
        guardarCambiosAutomaticamente()
    }

    fun updateMaterial(field: String, value: String) {
        val current = _materialFormState.value
        val newState = when (field) {
            "material" -> current.copy(material = value)
            "marca" -> current.copy(marca = value)
            "codigo" -> current.copy(codigo = value)
            "lote" -> current.copy(lote = value)
            else -> current
        }
        _materialFormState.value = newState
        validateMaterialForm()
    }

    fun updateInspeccion(field: String, value: String) {
        val current = _inspeccionFormState.value
        val newState = when (field) {
            "horaInspeccion" -> current.copy(horaInspeccion = value)
            "temperaturaChiller" -> current.copy(temperaturaChiller = value)
            "temperaturaCiclo" -> current.copy(temperaturaCiclo = value)
            "numeroCavidad" -> current.copy(numeroCavidad = value)
            "peso" -> current.copy(peso = value)
            "diametroRoscaMedida1" -> current.copy(diametroRoscaMedida1 = value)
            "diametroRoscaMedida2" -> current.copy(diametroRoscaMedida2 = value)
            "alturaBocaMedida1" -> current.copy(alturaBocaMedida1 = value)
            "alturaBocaMedida2" -> current.copy(alturaBocaMedida2 = value)
            "alturaBocaMedida3" -> current.copy(alturaBocaMedida3 = value)
            "alturaBocaMedida4" -> current.copy(alturaBocaMedida4 = value)
            "diametroPrecintoMedida1" -> current.copy(diametroPrecintoMedida1 = value)
            "diametroPrecintoMedida2" -> current.copy(diametroPrecintoMedida2 = value)
            "diametroPrecintoMedida3" -> current.copy(diametroPrecintoMedida3 = value)
            "alturaTotalMedida1" -> current.copy(alturaTotalMedida1 = value)
            "alturaTotalMedida2" -> current.copy(alturaTotalMedida2 = value)
            "diametroInternoMedida1" -> current.copy(diametroInternoMedida1 = value)
            "diametroInternoMedida2" -> current.copy(diametroInternoMedida2 = value)
            "observacion" -> current.copy(observacion = value)
            else -> current
        }
        _inspeccionFormState.value = newState
        validateInspeccionForm()
    }

    fun updateCheckList(field: String, value: String) {
        val current = _checkListFormState.value
        val newState = when (field) {
            "horaCheckList" -> current.copy(horaCheckList = value)
            "testeado" -> current.copy(testeado = value)
            "estabilidad" -> current.copy(estabilidad = value)
            "tonalidad" -> current.copy(tonalidad = value)
            "visorUniforme" -> current.copy(visorUniforme = value)
            "correctaCostura" -> current.copy(correctaCostura = value)
            "libreOvulamiento" -> current.copy(libreOvulamiento = value)
            "libreContaminacion" -> current.copy(libreContaminacion = value)
            "observacion" -> current.copy(observacion = value)
            else -> current
        }
        _checkListFormState.value = newState
        validateCheckListForm()
    }

    fun updateEvaluacion(field: String, value: String) {
        val current = _evaluacionFormState.value
        val newState = when (field) {
            "equipo" -> current.copy(equipo = value)
            "paletasAprobadas" -> current.copy(paletasAprobadas = value)
            "paletasObservadas" -> current.copy(paletasObservadas = value)
            "paletasRechazadas" -> current.copy(paletasRechazadas = value)
            "bolsasAprobadas" -> current.copy(bolsasAprobadas = value)
            "bolsasObservadas" -> current.copy(bolsasObservadas = value)
            "bolsasRechazadas" -> current.copy(bolsasRechazadas = value)
            "criteriosEvaluacion" -> current.copy(criteriosEvaluacion = value)
            else -> current
        }
        _evaluacionFormState.value = newState
        validateEvaluacionForm()
    }

    fun updateRegistroLlegada(field: String, value: String) {
        val current = _registroLlegadaFormState.value
        val newState = when (field) {
            "numeroOrdenFabricacion" -> {
                // Si se limpia el número de orden, limpiar también las muestras disponibles
                if (value.isEmpty()) {
                    _muestrasDisponibles.value = emptyList()
                }
                current.copy(
                    numeroOrdenFabricacion = value,
                    codigoProducto = if (value.isEmpty()) "" else current.codigoProducto,
                    descripcionProducto = if (value.isEmpty()) "" else current.descripcionProducto,
                    maquina = if (value.isEmpty()) "" else current.maquina,
                    numeroMuestra = if (value.isEmpty()) "" else current.numeroMuestra
                )
            }
            "numeroMuestra" -> current.copy(numeroMuestra = value)
            "descripcionProducto" -> current.copy(descripcionProducto = value)
            "maquina" -> current.copy(maquina = value)
            else -> current
        }
        _registroLlegadaFormState.value = newState
        validateRegistroLlegadaForm()
    }

    // Funciones de validación
    private fun validateCabeceraForm() {
        val current = _cabeceraFormState.value
        
        // En modo edición, los campos ya están llenos, así que siempre es válido
        // En modo creación, validar que todos los campos estén llenos
        val isValid = if (_isEditMode.value) {
            true // En modo edición, siempre es válido
        } else {
            current.fechaRegistro.isNotEmpty() &&
            current.codigo.isNotEmpty() &&
            current.lote.isNotEmpty() &&
            current.embalaje.isNotEmpty() &&
            current.maquina.isNotEmpty() &&
            current.turno.isNotEmpty() &&
            current.ot.isNotEmpty() &&
            current.producto.isNotEmpty() &&
            current.auxiliar.isNotEmpty() &&
            current.encargadoProduccion.isNotEmpty()
        }
        
        _cabeceraFormState.value = current.copy(isFormValid = isValid)
    }

    private fun validateMaterialForm() {
        val current = _materialFormState.value
        
        // En modo edición, no validar campos vacíos para formularios de agregar
        // En modo creación, validar que todos los campos estén llenos
        val isValid = if (_isEditMode.value) {
            true // En modo edición, siempre es válido para agregar items
        } else {
            current.material.isNotEmpty() &&
            current.marca.isNotEmpty() &&
            current.codigo.isNotEmpty() &&
            current.lote.isNotEmpty()
        }
        
        _materialFormState.value = current.copy(isFormValid = isValid)
    }

    private fun validateInspeccionForm() {
        val current = _inspeccionFormState.value
        
        // En modo edición, no validar campos vacíos para formularios de agregar
        // En modo creación, validar que todos los campos estén llenos
        val isValid = if (_isEditMode.value) {
            true // En modo edición, siempre es válido para agregar items
        } else {
            current.horaInspeccion.isNotEmpty() &&
            current.temperaturaChiller.isNotEmpty() &&
            current.temperaturaCiclo.isNotEmpty() &&
            current.numeroCavidad.isNotEmpty() &&
            current.peso.isNotEmpty()
        }
        
        _inspeccionFormState.value = current.copy(isFormValid = isValid)
    }

    private fun validateCheckListForm() {
        val current = _checkListFormState.value
        
        // En modo edición, no validar campos vacíos para formularios de agregar
        // En modo creación, validar que todos los campos estén llenos
        val isValid = if (_isEditMode.value) {
            true // En modo edición, siempre es válido para agregar items
        } else {
            current.horaCheckList.isNotEmpty() &&
            current.testeado.isNotEmpty() &&
            current.estabilidad.isNotEmpty() &&
            current.tonalidad.isNotEmpty() &&
            current.visorUniforme.isNotEmpty() &&
            current.correctaCostura.isNotEmpty() &&
            current.libreOvulamiento.isNotEmpty() &&
            current.libreContaminacion.isNotEmpty()
        }
        
        _checkListFormState.value = current.copy(isFormValid = isValid)
    }

    private fun validateEvaluacionForm() {
        val current = _evaluacionFormState.value
        
        // En modo edición, no validar campos vacíos para formularios de agregar
        // En modo creación, validar que todos los campos estén llenos
        val isValid = if (_isEditMode.value) {
            true // En modo edición, siempre es válido para agregar items
        } else {
            current.equipo.isNotEmpty() &&
            // Validar que al menos un campo de paletas tenga valor
            (current.paletasAprobadas.isNotEmpty() || current.paletasObservadas.isNotEmpty() || current.paletasRechazadas.isNotEmpty()) &&
            // Validar que al menos un campo de bolsas tenga valor
            (current.bolsasAprobadas.isNotEmpty() || current.bolsasObservadas.isNotEmpty() || current.bolsasRechazadas.isNotEmpty())
        }
        
        _evaluacionFormState.value = current.copy(isFormValid = isValid)
    }

    private fun validateRegistroLlegadaForm() {
        val current = _registroLlegadaFormState.value
        val isValid = current.numeroOrdenFabricacion.isNotEmpty() &&
                current.numeroMuestra.isNotEmpty() &&
                current.codigoProducto.isNotEmpty() &&
                current.descripcionProducto.isNotEmpty()
        _registroLlegadaFormState.value = current.copy(isFormValid = isValid)
    }

    // Función para crear un CheckList
    fun crearCheckList(muestraId: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CREANDO CHECKLIST ===")
            android.util.Log.d("MuestraViewModel", "DocEntry (currentMuestraId): $_currentMuestraId.value")
            
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                // Usar el docEntry de la muestra actual
                val docEntry = _currentMuestraId.value ?: muestraId
                
                // VALIDACIÓN CRÍTICA: Verificar que la muestra no esté completada
                if (isMuestraCompletada(docEntry)) {
                    _errorMessage.value = "No se pueden agregar más checklists. La muestra está completada y solo permite visualización."
                    _isCreating.value = false
                    return@launch
                }
                
                val checkList = _checkListFormState.value
                val fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                
                android.util.Log.d("MuestraViewModel", "DocEntry a usar: $docEntry")
                
                val request = CheckListCreateRequest(
                    muestraId = muestraId,
                    horaCheckList = fechaHora,
                    testeado = checkList.testeado,
                    estabilidad = checkList.estabilidad,
                    tonalidad = checkList.tonalidad,
                    visorUniforme = checkList.visorUniforme,
                    correctaCostura = checkList.correctaCostura,
                    libreOvulamiento = checkList.libreOvulamiento,
                    libreContaminacion = checkList.libreContaminacion,
                    observacion = checkList.observacion
                )
                
                val result = muestraRepository.crearCheckList(docEntry, request)
                
                android.util.Log.d("MuestraViewModel", "Request enviado al repository")
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Message: ${response.message}")
                        
                        if (response.success) {
                            _successMessage.value = response.message
                            // Limpiar el formulario después de crear
                            resetCheckListForm()
                            // Agregar a la lista temporal
                            val nuevoCheckList = CheckListInspeccion(
                                //id = UUID.randomUUID().toString(),
                                muestraId = muestraId,
                                horaCheckList = request.horaCheckList,
                                testeado = request.testeado,
                                estabilidad = request.estabilidad,
                                tonalidad = request.tonalidad,
                                visorUniforme = request.visorUniforme,
                                correctaCostura = request.correctaCostura,
                                libreOvulamiento = request.libreOvulamiento,
                                libreContaminacion = request.libreContaminacion,
                                observacion = request.observacion
                            )
                            val listaActual = _checkListsTemporales.value.toMutableList()
                            listaActual.add(nuevoCheckList)
                            _checkListsTemporales.value = listaActual
                            
                            // Actualizar estado a "En Proceso" si hay al menos 1 CheckList
                            actualizarEstadoMuestra(docEntry)
                            
                            // Refrescar el detalle de la muestra
                            android.util.Log.d("MuestraViewModel", "Refrescando detalle de muestra")
                            obtenerMuestraCompletaPorId(docEntry)
                        } else {
                            android.util.Log.e("MuestraViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al crear CheckList: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isCreating.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN CREAR CHECKLIST ===")
        }
    }

    // Función para crear una Inspección Dimensional
    fun crearInspeccionDimensional(muestraId: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CREANDO INSPECCION DIMENSIONAL ===")
            android.util.Log.d("MuestraViewModel", "DocEntry (currentMuestraId): $_currentMuestraId.value")
            
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                // Usar el docEntry de la muestra actual
                val docEntry = _currentMuestraId.value ?: muestraId
                
                // VALIDACIÓN CRÍTICA: Verificar que la muestra no esté completada
                if (isMuestraCompletada(docEntry)) {
                    _errorMessage.value = "No se pueden agregar más inspecciones. La muestra está completada y solo permite visualización."
                    _isCreating.value = false
                    return@launch
                }
                
                val inspeccion = _inspeccionFormState.value
                val fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                
                android.util.Log.d("MuestraViewModel", "DocEntry a usar: $docEntry")
                
                val request = InspeccionDimensionalCreateRequest(
                    muestraId = muestraId,
                    horaInspeccion = fechaHora,
                    temperaturaChiller = inspeccion.temperaturaChiller,
                    temperaturaCiclo = inspeccion.temperaturaCiclo,
                    numeroCavidad = inspeccion.numeroCavidad,
                    peso = inspeccion.peso,
                    diametroRoscaMedida1 = inspeccion.diametroRoscaMedida1,
                    diametroRoscaMedida2 = inspeccion.diametroRoscaMedida2,
                    alturaBocaMedida1 = inspeccion.alturaBocaMedida1,
                    alturaBocaMedida2 = inspeccion.alturaBocaMedida2,
                    alturaBocaMedida3 = inspeccion.alturaBocaMedida3,
                    alturaBocaMedida4 = inspeccion.alturaBocaMedida4,
                    diametroPrecintoMedida1 = inspeccion.diametroPrecintoMedida1,
                    diametroPrecintoMedida2 = inspeccion.diametroPrecintoMedida2,
                    diametroPrecintoMedida3 = inspeccion.diametroPrecintoMedida3,
                    alturaTotalMedida1 = inspeccion.alturaTotalMedida1,
                    alturaTotalMedida2 = inspeccion.alturaTotalMedida2,
                    diametroInternoMedida1 = inspeccion.diametroInternoMedida1,
                    diametroInternoMedida2 = inspeccion.diametroInternoMedida2,
                    observacion = inspeccion.observacion
                )
                
                val result = muestraRepository.crearInspeccionDimensional(docEntry, request)
                
                android.util.Log.d("MuestraViewModel", "Request enviado al repository")
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Message: ${response.message}")
                        
                        if (response.success) {
                            _successMessage.value = response.message
                            
                            // Agregar a la lista temporal primero
                            val nuevaInspeccion = InspeccionDimensional(
                                id = UUID.randomUUID().toString(),
                                muestraId = muestraId,
                                horaInspeccion = request.horaInspeccion,
                                temperaturaChiller = request.temperaturaChiller,
                                temperaturaCiclo = request.temperaturaCiclo,
                                numeroCavidad = request.numeroCavidad,
                                peso = request.peso,
                                diametroRoscaMedida1 = request.diametroRoscaMedida1,
                                diametroRoscaMedida2 = request.diametroRoscaMedida2,
                                alturaBocaMedida1 = request.alturaBocaMedida1,
                                alturaBocaMedida2 = request.alturaBocaMedida2,
                                alturaBocaMedida3 = request.alturaBocaMedida3,
                                alturaBocaMedida4 = request.alturaBocaMedida4,
                                diametroPrecintoMedida1 = request.diametroPrecintoMedida1,
                                diametroPrecintoMedida2 = request.diametroPrecintoMedida2,
                                diametroPrecintoMedida3 = request.diametroPrecintoMedida3,
                                alturaTotalMedida1 = request.alturaTotalMedida1,
                                alturaTotalMedida2 = request.alturaTotalMedida2,
                                diametroInternoMedida1 = request.diametroInternoMedida1,
                                diametroInternoMedida2 = request.diametroInternoMedida2,
                                observacion = request.observacion
                            )
                            val listaActual = _inspeccionesTemporales.value.toMutableList()
                            listaActual.add(nuevaInspeccion)
                            _inspeccionesTemporales.value = listaActual
                            
                            // Limpiar el formulario después de crear (esto calculará el siguiente número de cavidad)
                            resetInspeccionForm()
                            
                            // Actualizar estado a "En Proceso" si hay al menos 1 Inspección
                            actualizarEstadoMuestra(docEntry)
                            
                            // Refrescar el detalle de la muestra
                            android.util.Log.d("MuestraViewModel", "Refrescando detalle de muestra")
                            obtenerMuestraCompletaPorId(docEntry)
                        } else {
                            android.util.Log.e("MuestraViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al crear Inspección Dimensional: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isCreating.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN CREAR INSPECCION DIMENSIONAL ===")
        }
    }

    // Función para crear un Material Empleado
    fun crearMaterialEmpleado(muestraId: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CREANDO MATERIAL EMPLEADO ===")
            android.util.Log.d("MuestraViewModel", "DocEntry (currentMuestraId): $_currentMuestraId.value")
            
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                val material = _materialFormState.value
                val fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                
                // Usar el docEntry de la muestra actual
                val docEntry = _currentMuestraId.value ?: muestraId
                android.util.Log.d("MuestraViewModel", "DocEntry a usar: $docEntry")
                
                val request = MaterialEmpleadoCreateRequest(
                    material = material.material,
                    marca = material.marca,
                    codigo = material.codigo,
                    lote = material.lote,
                    fecReg = fechaHora
                )
                
                val result = muestraRepository.crearMaterialEmpleado(docEntry, request)
                
                android.util.Log.d("MuestraViewModel", "Request enviado al repository")
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Message: ${response.message}")
                        
                        if (response.success) {
                            _successMessage.value = response.message
                            // Limpiar el formulario después de crear
                            resetMaterialForm()
                            // Agregar a la lista temporal
                            val nuevoMaterial = MaterialEmpleado(
                                id = UUID.randomUUID().toString(),
                                muestraId = muestraId,
                                material = request.material,
                                marca = request.marca,
                                codigo = request.codigo,
                                lote = request.lote
                            )
                            val listaActual = _materialesTemporales.value.toMutableList()
                            listaActual.add(nuevoMaterial)
                            _materialesTemporales.value = listaActual
                            
                            // Refrescar el detalle de la muestra
                            android.util.Log.d("MuestraViewModel", "Refrescando detalle de muestra")
                            obtenerMuestraCompletaPorId(docEntry)
                        } else {
                            android.util.Log.e("MuestraViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al crear Material Empleado: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isCreating.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN CREAR MATERIAL EMPLEADO ===")
        }
    }

    // Función para crear una Evaluación de Producción
    fun crearEvaluacionProduccion(muestraId: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CREANDO EVALUACION PRODUCCION ===")
            android.util.Log.d("MuestraViewModel", "DocEntry (currentMuestraId): $_currentMuestraId.value")
            
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                // Validar que tenemos un muestraId
                val docEntry = _currentMuestraId.value ?: muestraId
                if (docEntry.isEmpty()) {
                    _errorMessage.value = "Error: No se encontró el ID de la muestra"
                    _isCreating.value = false
                    return@launch
                }
                
                // Validar que la muestra no esté completada
                if (isMuestraCompletada(docEntry)) {
                    _errorMessage.value = "No se puede enviar la evaluación. La muestra ya está completada."
                    _isCreating.value = false
                    return@launch
                }
                
                val evaluacion = _evaluacionFormState.value
                val fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                
                android.util.Log.d("MuestraViewModel", "DocEntry a usar: $docEntry")
                
                val request = EvaluacionProduccionCreateRequest(
                    equipo = evaluacion.equipo,
                    estado = "completado", // Dejar vacío según requerimiento
                    paletasAprobadas = evaluacion.paletasAprobadas.ifEmpty { "0" },
                    paletasObservadas = evaluacion.paletasObservadas.ifEmpty { "0" },
                    paletasRechazadas = evaluacion.paletasRechazadas.ifEmpty { "0" },
                    bolsasAprobadas = evaluacion.bolsasAprobadas.ifEmpty { "0" },
                    bolsasObservadas = evaluacion.bolsasObservadas.ifEmpty { "0" },
                    bolsasRechazadas = evaluacion.bolsasRechazadas.ifEmpty { "0" },
                    criteriosEvaluacion = evaluacion.criteriosEvaluacion,
                    fecReg = fechaHora
                )
                
                val result = muestraRepository.crearEvaluacionProduccion(docEntry, request)
                
                android.util.Log.d("MuestraViewModel", "Request enviado al repository")
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Message: ${response.message}")
                        
                        if (response.success) {
                            _successMessage.value = response.message ?: "Evaluación registrada exitosamente"
                            // Limpiar el formulario después de crear
                            resetEvaluacionForm()
                            
                            // Actualizar estado a "Completado" cuando se envía la evaluación
                            actualizarEstadoMuestra(docEntry, esCompletado = true)
                            
                            // Refrescar el detalle de la muestra
                            android.util.Log.d("MuestraViewModel", "Refrescando detalle de muestra")
                            obtenerMuestraCompletaPorId(docEntry)
                        } else {
                            android.util.Log.e("MuestraViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al crear Evaluación de Producción: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isCreating.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN CREAR EVALUACION PRODUCCION ===")
        }
    }

    // Funciones para agregar elementos a las listas temporales
    fun agregarMaterial() {
        // Validar que tenemos un muestraId
        val muestraId = _currentMuestraId.value
        if (muestraId.isNullOrEmpty()) {
            _errorMessage.value = "Error: No se encontró el ID de la muestra"
            return
        }
        
        // Llamar a la función que usa el API
        crearMaterialEmpleado(muestraId)
    }

    fun agregarInspeccion() {
        // Validar que tenemos un muestraId
        val muestraId = _currentMuestraId.value
        if (muestraId.isNullOrEmpty()) {
            _errorMessage.value = "Error: No se encontró el ID de la muestra"
            return
        }
        
        // Validar que la muestra no esté completada
        if (isMuestraCompletada(muestraId)) {
            _errorMessage.value = "No se pueden agregar más inspecciones. La muestra está completada."
            return
        }
        
        // Llamar a la función que usa el API
        crearInspeccionDimensional(muestraId)
    }

    fun agregarCheckList() {
        // Validar que tenemos un muestraId
        val muestraId = _currentMuestraId.value
        if (muestraId.isNullOrEmpty()) {
            _errorMessage.value = "Error: No se encontró el ID de la muestra"
            return
        }
        
        // Validar que la muestra no esté completada
        if (isMuestraCompletada(muestraId)) {
            _errorMessage.value = "No se pueden agregar más checklists. La muestra está completada."
            return
        }
        
        // Llamar a la función que usa el API
        crearCheckList(muestraId)
    }

    // Funciones para eliminar elementos
    fun eliminarMaterial(id: String) {
        val listaActual = _materialesTemporales.value.toMutableList()
        listaActual.removeAll { it.id == id }
        _materialesTemporales.value = listaActual
        
        // Guardar automáticamente si está en modo edición
        guardarCambiosAutomaticamente()
    }

    fun eliminarInspeccion(id: String) {
        val listaActual = _inspeccionesTemporales.value.toMutableList()
        listaActual.removeAll { it.id == id }
        _inspeccionesTemporales.value = listaActual
        
        // Guardar automáticamente si está en modo edición
        guardarCambiosAutomaticamente()
    }

    fun eliminarCheckList(id: String) {
        val listaActual = _checkListsTemporales.value.toMutableList()
        listaActual.removeAll { it.id == id }
        _checkListsTemporales.value = listaActual
        
        // Guardar automáticamente si está en modo edición
        guardarCambiosAutomaticamente()
    }

    // Funciones para resetear formularios
    fun resetMaterialForm() {
        _materialFormState.value = MaterialFormState()
    }

    fun resetInspeccionForm() {
        // No resetear numeroCavidad si hay inspecciones previas
        val numeroCavidadActual = _inspeccionFormState.value.numeroCavidad
        _inspeccionFormState.value = InspeccionDimensionalFormState()
        
        // Si hay inspecciones, mantener el siguiente número de cavidad
        if (_inspeccionesTemporales.value.isNotEmpty()) {
            val ultimoNumero = _inspeccionesTemporales.value.lastOrNull()?.numeroCavidad?.toIntOrNull()
            if (ultimoNumero != null) {
                _inspeccionFormState.value = _inspeccionFormState.value.copy(
                    numeroCavidad = (ultimoNumero + 1).toString()
                )
            }
        }
    }

    fun resetCheckListForm() {
        _checkListFormState.value = CheckListFormState()
    }

    fun resetEvaluacionForm() {
        _evaluacionFormState.value = EvaluacionFormState()
    }

    fun resetRegistroLlegadaForm() {
        _registroLlegadaFormState.value = RegistroLlegadaFormState()
        _muestrasDisponibles.value = emptyList() // Limpiar las muestras disponibles
    }

    fun agregarRegistroLlegada(currentUser: UserResponse? = null) {
        viewModelScope.launch {
            val formState = _registroLlegadaFormState.value
            if (!formState.isFormValid) {
                _errorMessage.value = "Por favor complete todos los campos"
                return@launch
            }

            _isCreating.value = true
            _errorMessage.value = null

            try {
                // Formatear fecha en formato ISO 8601 con timezone: "2026-02-06T09:27:58-05:00"
                val fechaHora = java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Lima"))
                val fechaRegistro = fechaHora.format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME)

                // Extraer solo el número antes del guion del número de muestra
                // Ejemplo: "2-Muestra 2" -> "2"
                val numeroMuestraLimpio = formState.numeroMuestra.split("-").firstOrNull()?.trim() ?: formState.numeroMuestra

                val request = RegistroLlegadaCreateRequest(
                    ordenFabricacion = formState.numeroOrdenFabricacion,
                    nMuestra = numeroMuestraLimpio,
                    codProducto = formState.codigoProducto,
                    desProducto = formState.descripcionProducto,
                    fechaRegistro = fechaRegistro,
                    userRegister = currentUser?.dni ?: "70131373"
                )

                android.util.Log.d("MuestraViewModel", "=== CREANDO REGISTRO DE LLEGADA ===")
                android.util.Log.d("MuestraViewModel", "Request: $request")

                val result = muestraRepository.crearRegistroLlegada(request)

                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Message: ${response.message}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Data: ${response.data}")

                        if (response.statusCode == 201) {
                            _successMessage.value = response.message ?: "Registro de llegada creado exitosamente"
                            
                            // Limpiar formulario
                            resetRegistroLlegadaForm()
                            
                            // Recargar la lista de registros
                            val fechaInicio = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            val fechaFin = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            obtenerRegistrosLlegada(fechaInicio, fechaFin, currentUser?.dni)
                        } else {
                            _errorMessage.value = response.message ?: "Error al crear registro"
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al crear registro de llegada: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }

            _isCreating.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN CREAR REGISTRO DE LLEGADA ===")
        }
    }

    fun obtenerRegistrosLlegada(fechaInicio: String? = null, fechaFin: String? = null, code: String? = null) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== OBTENIENDO REGISTROS DE LLEGADA ===")
            android.util.Log.d("MuestraViewModel", "FechaInicio: $fechaInicio, FechaFin: $fechaFin, Code: $code")
            
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val result = muestraRepository.obtenerRegistrosLlegada(fechaInicio, fechaFin, code)
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Cantidad de registros: ${response.data.size}")
                        
                        if (response.success) {
                            _registrosLlegada.value = response.data
                            android.util.Log.d("MuestraViewModel", "Registros actualizados en el estado")
                        } else {
                            android.util.Log.e("MuestraViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al obtener registros de llegada: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isLoading.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN OBTENIENDO REGISTROS DE LLEGADA ===")
        }
    }

    fun resetAllForms() {
        _cabeceraFormState.value = CabeceraFormState()
        _materialFormState.value = MaterialFormState()
        _inspeccionFormState.value = InspeccionDimensionalFormState()
        _checkListFormState.value = CheckListFormState()
        _evaluacionFormState.value = EvaluacionFormState()
        _registroLlegadaFormState.value = RegistroLlegadaFormState()
        _materialesTemporales.value = emptyList()
        _inspeccionesTemporales.value = emptyList()
        _checkListsTemporales.value = emptyList()
        _pasoActual.value = 1
        
        // Desactivar modo edición al resetear
        _isEditMode.value = false
        _currentMuestraId.value = null
    }

    // Navegación del wizard
    fun siguientePaso() {
        if (_pasoActual.value < 5) {
            _pasoActual.value = _pasoActual.value + 1 // HASTA HABILITAR LOS MODULOS
        }
    }

    fun pasoAnterior() {
        if (_pasoActual.value > 1) {
            _pasoActual.value = _pasoActual.value - 1 // HASTA HABILITAR LOS MODULOS
        }
    }

    fun irAPaso(paso: Int) {
        if (paso in 1..5) {
            _pasoActual.value = paso
        }
    }

    // Función para crear una nueva muestra (solo cabecera)
    fun crearMuestra(currentUser: UserResponse) {
        viewModelScope.launch {
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                val cabeceraForm = _cabeceraFormState.value
                val fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                // Crear el request con el formato requerido por el API
                val request = MuestraCreateRequest(
                    codeProd = cabeceraForm.lote,
                    producto = cabeceraForm.producto,
                    lote = cabeceraForm.codigo,
                    embalaje = cabeceraForm.embalaje, // NO SE ENVIA
                    userRegister = currentUser.dni,
                    fecRegister = fechaActual,
                    turno = cabeceraForm.turno,
                    maquina = cabeceraForm.maquina,
                    codigoMaquina = cabeceraForm.codigoMaquina,
                    encargadoProd = cabeceraForm.encargadoProduccion, // NO SE ENVIA
                    estado = "NUEVO"
                )
                
                val result = muestraRepository.crearMuestra(request)
                
                result.fold(
                    onSuccess = { response ->
                        if (response.success) {
                            // Guardar el ID de la muestra creada
                            _currentMuestraId.value = response.data
                            _isEditMode.value = true
                            
                            _successMessage.value = "Muestra creada exitosamente. Continúa completando los módulos."
                            
                            // Refrescar la lista de muestras
                            obtenerMuestras(currentUser.dni)
                        } else {
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        _errorMessage.value = "Error al crear muestra: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isCreating.value = false
        }
    }

    // Función para activar modo edición
    fun activarModoEdicion(muestraId: String) {
        android.util.Log.d("MuestraViewModel", "Activando modo edición para muestra: $muestraId")
        _isEditMode.value = true
        _currentMuestraId.value = muestraId
        // El estado se cargará cuando se obtenga el detalle
    }
    
    // Función para cargar una muestra para edición
    fun cargarMuestraParaEdicion(muestra: MuestraCompleta) {
        viewModelScope.launch {
            // Activar modo edición
            _isEditMode.value = true
            _currentMuestraId.value = muestra.cabecera.id
            
            // Cargar los datos de la muestra en los formularios
            _cabeceraFormState.value = CabeceraFormState(
                fechaRegistro = muestra.cabecera.fechaRegistro,
                codigo = muestra.cabecera.codigo,
                lote = muestra.cabecera.lote,
                codigoMaquina = muestra.cabecera.codigoMaquina,
                embalaje = muestra.cabecera.embalaje,
                maquina = muestra.cabecera.maquina,
                turno = muestra.cabecera.turno,
                producto = muestra.cabecera.producto,
                auxiliar = muestra.cabecera.auxiliar,
                encargadoProduccion = muestra.cabecera.encargadoProduccion
            )
            
            // Cargar materiales existentes
            _materialesTemporales.value = muestra.materiales
            
            // Cargar inspecciones existentes
            _inspeccionesTemporales.value = muestra.inspeccionesDimensionales
            
            // Cargar checklists existentes
            _checkListsTemporales.value = muestra.checkLists
            
            // Cargar evaluación existente
            muestra.evaluacionProduccion?.let { evaluacion ->
                _evaluacionFormState.value = EvaluacionFormState(
                    equipo = evaluacion.equipo,
                    estado = evaluacion.estado,
                    paletasAprobadas = evaluacion.paletasAprobadas,
                    paletasObservadas = evaluacion.paletasObservadas,
                    paletasRechazadas = evaluacion.paletasRechazadas,
                    bolsasAprobadas = evaluacion.bolsasAprobadas,
                    bolsasObservadas = evaluacion.bolsasObservadas,
                    bolsasRechazadas = evaluacion.bolsasRechazadas,
                    criteriosEvaluacion = evaluacion.criteriosEvaluacion
                )
            }
            
            // Forzar validación después de cargar los datos
            validateCabeceraForm()
            validateMaterialForm()
            validateInspeccionForm()
            validateCheckListForm()
            validateEvaluacionForm()
        }
    }
    
    // Función para actualizar una muestra existente
    /*fun actualizarMuestra(muestraId: String) {
        viewModelScope.launch {
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                // Simular llamada a API
                kotlinx.coroutines.delay(2000)
                
                // Actualizar la muestra en la lista local
                val listaActual = _muestras.value.toMutableList()
                val indice = listaActual.indexOfFirst { it.cabecera.id == muestraId }
                
                if (indice != -1) {
                    // Calcular completitud de módulos
                    val materialesCompletado = _materialesTemporales.value.isNotEmpty()
                    val inspeccionesCompletado = _inspeccionesTemporales.value.isNotEmpty()
                    val checkListsCompletado = _checkListsTemporales.value.isNotEmpty()
                    // La evaluación está completada cuando tiene equipo y al menos un valor en paletas o bolsas
                    val evaluacionCompletado = _evaluacionFormState.value.equipo.isNotEmpty() &&
                        ((_evaluacionFormState.value.paletasAprobadas.isNotEmpty() || 
                          _evaluacionFormState.value.paletasObservadas.isNotEmpty() || 
                          _evaluacionFormState.value.paletasRechazadas.isNotEmpty()) ||
                         (_evaluacionFormState.value.bolsasAprobadas.isNotEmpty() || 
                          _evaluacionFormState.value.bolsasObservadas.isNotEmpty() || 
                          _evaluacionFormState.value.bolsasRechazadas.isNotEmpty()))
                    
                    // Determinar el estado de la cabecera basado en la nueva lógica:
                    // - "Nuevo": Solo cuando se crea la muestra (solo Información General)
                    // - "En Proceso": Cuando hay al menos 1 Inspección Dimensional O al menos 1 CheckList
                    // - "Completado": Cuando se envía la Evaluación final
                    val estadoCabecera = when {
                        evaluacionCompletado -> "Completado"
                        inspeccionesCompletado || checkListsCompletado -> "En Proceso"
                        else -> "Nuevo"
                    }
                    
                    val muestraActualizada = listaActual[indice].copy(
                        cabecera = listaActual[indice].cabecera.copy(estado = estadoCabecera),
                        materiales = _materialesTemporales.value,
                        inspeccionesDimensionales = _inspeccionesTemporales.value,
                        checkLists = _checkListsTemporales.value,
                        evaluacionProduccion = if (evaluacionCompletado) {
                            EvaluacionProduccion(
                                id = UUID.randomUUID().toString(),
                                muestraId = muestraId,
                                estado = _evaluacionFormState.value.estado,
                                paletasAprobadas = _evaluacionFormState.value.paletasAprobadas,
                                paletasObservadas = _evaluacionFormState.value.paletasObservadas,
                                paletasRechazadas = _evaluacionFormState.value.paletasRechazadas,
                                bolsasAprobadas = _evaluacionFormState.value.bolsasAprobadas,
                                bolsasObservadas = _evaluacionFormState.value.bolsasObservadas,
                                bolsasRechazadas = _evaluacionFormState.value.bolsasRechazadas,
                                criteriosEvaluacion = _evaluacionFormState.value.criteriosEvaluacion
                            )
                        } else null,
                        // Actualizar campos de completitud
                        materialesCompletado = materialesCompletado,
                        inspeccionesCompletado = inspeccionesCompletado,
                        checkListsCompletado = checkListsCompletado,
                        evaluacionCompletado = evaluacionCompletado,
                        fechaUltimaActualizacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    )
                    
                    listaActual[indice] = muestraActualizada
                    _muestras.value = listaActual
                    
                    _successMessage.value = "Muestra actualizada exitosamente"
                }
                
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar la muestra: ${e.message}"
            }
            
            _isCreating.value = false
        }
    }*/
    
    // Funciones de utilidad
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
        _productoEncontrado.value = null
    }

    fun resetForm() {
        // Resetear todos los estados del formulario
        _cabeceraFormState.value = CabeceraFormState()
        _materialFormState.value = MaterialFormState()
        _inspeccionFormState.value = InspeccionDimensionalFormState()
        _checkListFormState.value = CheckListFormState()
        _evaluacionFormState.value = EvaluacionFormState()
        
        // Limpiar listas temporales
        _materialesTemporales.value = emptyList()
        _inspeccionesTemporales.value = emptyList()
        _checkListsTemporales.value = emptyList()
        
        // Resetear paso actual
        _pasoActual.value = 1
        
        // Desactivar modo edición
        _isEditMode.value = false
        _currentMuestraId.value = null
        
        // Limpiar mensajes
        clearMessages()
    }
    
    // Función para guardar automáticamente cambios
    private fun guardarCambiosAutomaticamente() {
        if (_isEditMode.value && _currentMuestraId.value != null) {
            viewModelScope.launch {
                try {
                    // Simular delay mínimo para evitar guardados excesivos
                    kotlinx.coroutines.delay(500)
                    //actualizarMuestra(_currentMuestraId.value!!)
                } catch (e: Exception) {
                    // Silenciar errores en guardado automático
                }
            }
        }
    }

    // Función para actualizar el estado de la muestra
    private fun actualizarEstadoMuestra(muestraId: String, esCompletado: Boolean = false) {
        viewModelScope.launch {
            val listaActual = _muestras.value.toMutableList()
            val indice = listaActual.indexOfFirst { it.id == muestraId }
            
            if (indice != -1) {
                val muestra = listaActual[indice]
                val nuevoEstado = when {
                    esCompletado -> "completado"
                    _inspeccionesTemporales.value.isNotEmpty() || _checkListsTemporales.value.isNotEmpty() -> "en proceso"
                    else -> "nuevo"
                }
                
                listaActual[indice] = muestra.copy(estado = nuevoEstado)
                _muestras.value = listaActual
            }
        }
    }
    
    // Función para verificar si la muestra está completada
    fun isMuestraCompletada(muestraId: String?): Boolean {
        if (muestraId.isNullOrEmpty()) return false
        
        // Buscar en la lista de muestras
        val muestra = _muestras.value.firstOrNull { it.id == muestraId }
        if (muestra != null) {
            return muestra.estado == "completado"
        }
        
        // Si no está en _muestras, buscar en _muestrasCompletas
        val muestraCompleta = _muestrasCompletas.value.firstOrNull { it.cabecera.id == muestraId }
        return muestraCompleta?.cabecera?.estado == "completado"
    }
    
    // Función para obtener una muestra completa por ID
    fun obtenerMuestraCompletaPorId(id: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== OBTENIENDO MUESTRA COMPLETA ===")
            android.util.Log.d("MuestraViewModel", "ID: $id")
            
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val result = muestraRepository.obtenerMuestraDetalle(id)
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - DocEntry: ${response.data[0].docEntry}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - CheckLists: ${response.data[0].checkList.size}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Materiales: ${response.data[0].materialEmpleado.size}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Inspecciones: ${response.data[0].inspeccionDimencional.size}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Evaluaciones: ${response.data[0].evaluacion.size}")
                        
                        if (response.success) {
                            // Mapear la respuesta del API a MuestraCompleta
                            val muestraDetalle = response.data[0]
                            
                            // Crear la cabecera
                            val cabecera = MuestraCabecera(
                                id = muestraDetalle.docEntry,
                                fechaRegistro = muestraDetalle.fecRegister,
                                codigo = muestraDetalle.itemCode,
                                lote = muestraDetalle.lote,
                                embalaje = muestraDetalle.embalaje,
                                maquina = muestraDetalle.maquina,
                                turno = muestraDetalle.turno,
                                producto = muestraDetalle.descripcion,
                                auxiliar = muestraDetalle.userRegister,
                                encargadoProduccion = muestraDetalle.encargadorProd,
                                estado = muestraDetalle.estado
                            )
                            
                            // Mapear checkLists
                            val checkLists = muestraDetalle.checkList.map { checkListAPI ->
                                CheckListInspeccion(
                                    id = "", // No viene del API
                                    muestraId = id,
                                    horaCheckList = checkListAPI.horaCheckList ?: "",
                                    testeado = checkListAPI.testeado ?: "",
                                    estabilidad = checkListAPI.estabilidad ?: "",
                                    tonalidad = checkListAPI.tonalidad ?: "",
                                    visorUniforme = checkListAPI.visorUniforme ?: "",
                                    correctaCostura = checkListAPI.correctaCostura ?: "",
                                    libreOvulamiento = checkListAPI.libreOvulamiento ?: "",
                                    libreContaminacion = checkListAPI.libreContaminacion ?: "",
                                    observacion = checkListAPI.observacion ?: ""
                                )
                            }
                            
                            // Mapear Materiales Empleados
                            val materiales = muestraDetalle.materialEmpleado.map { materialAPI ->
                                MaterialEmpleado(
                                    id = UUID.randomUUID().toString(),
                                    muestraId = id,
                                    material = materialAPI.material ?: "",
                                    marca = materialAPI.marca ?: "",
                                    codigo = materialAPI.codigo ?: "",
                                    lote = materialAPI.lote ?: ""
                                )
                            }
                            
                            // Mapear Inspecciones Dimensionales
                            val inspecciones = muestraDetalle.inspeccionDimencional.map { inspeccionAPI ->
                                InspeccionDimensional(
                                    id = UUID.randomUUID().toString(),
                                    muestraId = id,
                                    horaInspeccion = inspeccionAPI.horaInspeccion ?: "",
                                    temperaturaChiller = inspeccionAPI.temperaturaChiller ?: "",
                                    temperaturaCiclo = inspeccionAPI.temperaturaCiclo ?: "",
                                    numeroCavidad = inspeccionAPI.numeroCavidad ?: "",
                                    peso = inspeccionAPI.peso ?: "",
                                    diametroRoscaMedida1 = inspeccionAPI.diametroRoscaMedida1 ?: "",
                                    diametroRoscaMedida2 = inspeccionAPI.diametroRoscaMedida2 ?: "",
                                    alturaBocaMedida1 = inspeccionAPI.alturaBocaMedida1 ?: "",
                                    alturaBocaMedida2 = inspeccionAPI.alturaBocaMedida2 ?: "",
                                    alturaBocaMedida3 = inspeccionAPI.alturaBocaMedida3 ?: "",
                                    alturaBocaMedida4 = inspeccionAPI.alturaBocaMedida4 ?: "",
                                    diametroPrecintoMedida1 = inspeccionAPI.diametroPrecintoMedida1 ?: "",
                                    diametroPrecintoMedida2 = inspeccionAPI.diametroPrecintoMedida2 ?: "",
                                    diametroPrecintoMedida3 = inspeccionAPI.diametroPrecintoMedida3 ?: "",
                                    alturaTotalMedida1 = inspeccionAPI.alturaTotalMedida1 ?: "",
                                    alturaTotalMedida2 = inspeccionAPI.alturaTotalMedida2 ?: "",
                                    diametroInternoMedida1 = inspeccionAPI.diametroInternoMedida1 ?: "",
                                    diametroInternoMedida2 = inspeccionAPI.diametroInternoMedida2 ?: "",
                                    observacion = inspeccionAPI.observacion ?: ""
                                )
                            }
                            
                            // Mapear Evaluación (tomar solo el primero)
                            val evaluacion = muestraDetalle.evaluacion.firstOrNull()?.let { evaluacionAPI ->
                                EvaluacionProduccion(
                                    id = UUID.randomUUID().toString(),
                                    muestraId = id,
                                    equipo = evaluacionAPI.equipo ?: "",
                                    estado = evaluacionAPI.estado ?: "",
                                    paletasAprobadas = evaluacionAPI.paletasAprobadas ?: "",
                                    paletasObservadas = evaluacionAPI.paletasObservadas ?: "",
                                    paletasRechazadas = evaluacionAPI.paletasRechazadas ?: "",
                                    bolsasAprobadas = evaluacionAPI.bolsasAprobadas ?: "",
                                    bolsasObservadas = evaluacionAPI.bolsasObservadas ?: "",
                                    bolsasRechazadas = evaluacionAPI.bolsasRechazadas ?: "",
                                    criteriosEvaluacion = evaluacionAPI.criteriosEvaluacion ?: ""
                                )
                            }
                            
                            // Crear MuestraCompleta
                            val muestraCompleta = MuestraCompleta(
                                cabecera = cabecera,
                                materiales = materiales,
                                inspeccionesDimensionales = inspecciones,
                                checkLists = checkLists,
                                evaluacionProduccion = evaluacion,
                                materialesCompletado = materiales.isNotEmpty(),
                                inspeccionesCompletado = inspecciones.isNotEmpty(),
                                checkListsCompletado = checkLists.isNotEmpty(),
                                evaluacionCompletado = evaluacion != null,
                                fechaUltimaActualizacion = muestraDetalle.fecRegister
                            )
                            
                            // Guardar el docEntry para usar en checkList
                            _currentMuestraId.value = muestraDetalle.docEntry
                            
                            // Si está en modo edición, cargar los datos en los formularios
                            if (_isEditMode.value) {
                                android.util.Log.d("MuestraViewModel", "Cargando datos en formularios para edición")
                                
                                // Cargar cabecera
                                _cabeceraFormState.value = CabeceraFormState(
                                    fechaRegistro = cabecera.fechaRegistro,
                                    codigo = cabecera.codigo,
                                    lote = cabecera.lote,
                                    codigoMaquina = cabecera.codigoMaquina,
                                    embalaje = cabecera.embalaje,
                                    maquina = cabecera.maquina,
                                    turno = cabecera.turno,
                                    ot = "", // No viene del API
                                    producto = cabecera.producto,
                                    auxiliar = cabecera.auxiliar,
                                    encargadoProduccion = cabecera.encargadoProduccion,
                                    isFormValid = true // En modo edición siempre es válido
                                )

                                // Cargar materiales
                                _materialesTemporales.value = materiales
                                
                                // Cargar inspecciones
                                _inspeccionesTemporales.value = inspecciones

                                // Cargar checkLists
                                _checkListsTemporales.value = checkLists.toMutableList()
                                
                                // Cargar evaluación (solo el primero, en el formulario)
                                evaluacion?.let { eval ->
                                    android.util.Log.d("MuestraViewModel", "Cargando evaluación en formulario:")
                                    android.util.Log.d("MuestraViewModel", "  - Equipo: ${eval.equipo}")
                                    android.util.Log.d("MuestraViewModel", "  - Paletas Aprobadas: ${eval.paletasAprobadas}")
                                    android.util.Log.d("MuestraViewModel", "  - Paletas Observadas: ${eval.paletasObservadas}")
                                    android.util.Log.d("MuestraViewModel", "  - Paletas Rechazadas: ${eval.paletasRechazadas}")
                                    android.util.Log.d("MuestraViewModel", "  - Bolsas Aprobadas: ${eval.bolsasAprobadas}")
                                    android.util.Log.d("MuestraViewModel", "  - Bolsas Observadas: ${eval.bolsasObservadas}")
                                    android.util.Log.d("MuestraViewModel", "  - Bolsas Rechazadas: ${eval.bolsasRechazadas}")
                                    android.util.Log.d("MuestraViewModel", "  - Criterios: ${eval.criteriosEvaluacion}")
                                    
                                    _evaluacionFormState.value = EvaluacionFormState(
                                        equipo = eval.equipo,
                                        estado = eval.estado,
                                        paletasAprobadas = eval.paletasAprobadas,
                                        paletasObservadas = eval.paletasObservadas,
                                        paletasRechazadas = eval.paletasRechazadas,
                                        bolsasAprobadas = eval.bolsasAprobadas,
                                        bolsasObservadas = eval.bolsasObservadas,
                                        bolsasRechazadas = eval.bolsasRechazadas,
                                        criteriosEvaluacion = eval.criteriosEvaluacion,
                                        isFormValid = true
                                    )
                                } ?: run {
                                    android.util.Log.d("MuestraViewModel", "No hay evaluación para cargar")
                                    // Si no hay evaluación, limpiar el formulario
                                    _evaluacionFormState.value = EvaluacionFormState()
                                }

                                android.util.Log.d("MuestraViewModel", "Datos cargados en todos los formularios")
                            }
                            
                            // Agregar o actualizar en el cache
                            val listaActual = _muestrasCompletas.value.toMutableList()
                            val indice = listaActual.indexOfFirst { it.cabecera.id == id }
                            
                            if (indice != -1) {
                                listaActual[indice] = muestraCompleta
                            } else {
                                listaActual.add(muestraCompleta)
                            }
                            
                            _muestrasCompletas.value = listaActual
                            
                            // Actualizar también la lista de muestras para mantener sincronizado el estado
                            val listaMuestras = _muestras.value.toMutableList()
                            val indiceMuestra = listaMuestras.indexOfFirst { it.id == id }
                            if (indiceMuestra != -1) {
                                listaMuestras[indiceMuestra] = listaMuestras[indiceMuestra].copy(estado = muestraCompleta.cabecera.estado)
                                _muestras.value = listaMuestras
                            }
                            
                            android.util.Log.d("MuestraViewModel", "Muestra actualizada en el cache")
                        } else {
                            android.util.Log.e("MuestraViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al obtener muestra: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isLoading.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN OBTENER MUESTRA COMPLETA ===")
        }
    }

    fun obtenerMuestraPorId(id: String): MuestraCompleta? {
        return _muestrasCompletas.value.find { it.cabecera.id == id }
    }
    
    fun cargarProductos() {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CARGANDO PRODUCTOS ===")
            
            _isLoadingProductos.value = true
            
            try {
                val result = muestraRepository.obtenerProductos()
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Cantidad de productos: ${response.data.size}")
                        
                        if (response.success) {
                            _productos.value = response.data
                            android.util.Log.d("MuestraViewModel", "Productos actualizados en el estado")
                        } else {
                            android.util.Log.e("MuestraViewModel", "ERROR - Response.message: ${response.message}")
                            _errorMessage.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        _errorMessage.value = "Error al obtener productos: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                _errorMessage.value = "Error inesperado: ${e.message}"
            }
            
            _isLoadingProductos.value = false
            android.util.Log.d("MuestraViewModel", "=== FIN CARGAR PRODUCTOS ===")
        }
    }
    
    fun consultarProducto(codigo: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CONSULTANDO PRODUCTO ===")
            android.util.Log.d("MuestraViewModel", "Código a consultar: $codigo")
            
            try {
                val result = muestraRepository.consultarProducto(codigo)
                
                result.fold(
                    onSuccess = { response ->
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Response.success: ${response.success}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - Cantidad de productos: ${response.data.size}")
                        
                        if (response.success && response.data.isNotEmpty()) {
                            val ordenFabricacion = response.data.first()
                            android.util.Log.d("MuestraViewModel", "Producto encontrado: $ordenFabricacion")
                            
                            // Actualizar el campo producto en el formulario
                            val current = _cabeceraFormState.value
                            _cabeceraFormState.value = current.copy(
                                codigo = ordenFabricacion.lote,
                                producto = ordenFabricacion.descripcion,
                                lote = ordenFabricacion.codigo,
                                codigoMaquina = ordenFabricacion.codMaquina,
                                maquina = ordenFabricacion.maquina,
                            )
                            validateCabeceraForm()
                        } else {
                            android.util.Log.w("MuestraViewModel", "No se encontró producto para código: $codigo")
                            // Limpiar el campo producto si no se encuentra
                            val current = _cabeceraFormState.value
                            _cabeceraFormState.value = current.copy(producto = "", lote = "", maquina = "")
                            validateCabeceraForm()
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "FAILURE - Excepción: ${exception.message}", exception)
                        // Limpiar el campo producto en caso de error
                        val current = _cabeceraFormState.value
                        _cabeceraFormState.value = current.copy(producto = "", lote = "", maquina = "")
                        validateCabeceraForm()
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "EXCEPCIÓN INESPERADA", e)
                val current = _cabeceraFormState.value
                _cabeceraFormState.value = current.copy(producto = "", lote = "", maquina = "")
                validateCabeceraForm()
            }
            
            android.util.Log.d("MuestraViewModel", "=== FIN CONSULTAR PRODUCTO ===")
        }
    }

    fun consultarProductoRegistroLlegada(codigo: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CONSULTANDO PRODUCTO REGISTRO LLEGADA ===")
            android.util.Log.d("MuestraViewModel", "Código a consultar: $codigo")
            
            try {
                val result = muestraRepository.consultarProductoMuestra(codigo)
                
                result.fold(
                    onSuccess = { response ->
                        if (response.success && response.data.isNotEmpty()) {
                            val ordenFabricacion = response.data.first()
                            
                            // Actualizar las muestras disponibles desde el API
                            _muestrasDisponibles.value = ordenFabricacion.muestras
                            android.util.Log.d("MuestraViewModel", "Muestras disponibles: ${ordenFabricacion.muestras}")
                            
                            // Mostrar alert de producto encontrado
                            _productoEncontrado.value = "Orden de fabricación encontrada: ${ordenFabricacion.descripcion}"
                            
                            // Actualizar el formulario de RegistroLlegada
                            val current = _registroLlegadaFormState.value
                            _registroLlegadaFormState.value = current.copy(
                                codigoProducto = ordenFabricacion.codigo,
                                descripcionProducto = ordenFabricacion.descripcion,
                                maquina = ordenFabricacion.maquina,
                                numeroMuestra = "" // Limpiar la muestra seleccionada cuando cambia la orden
                            )
                            validateRegistroLlegadaForm()
                        } else {
                            // Limpiar campos si no se encuentra
                            _muestrasDisponibles.value = emptyList()
                            _productoEncontrado.value = null
                            val current = _registroLlegadaFormState.value
                            _registroLlegadaFormState.value = current.copy(
                                codigoProducto = "",
                                descripcionProducto = "",
                                maquina = "",
                                numeroMuestra = ""
                            )
                        }
                    },
                    onFailure = { exception ->
                        android.util.Log.e("MuestraViewModel", "Error consultando producto", exception)
                        _muestrasDisponibles.value = emptyList()
                        _productoEncontrado.value = null
                        val current = _registroLlegadaFormState.value
                        _registroLlegadaFormState.value = current.copy(
                            codigoProducto = "",
                            descripcionProducto = "",
                            maquina = "",
                            numeroMuestra = ""
                        )
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MuestraViewModel", "Excepción consultando producto", e)
                _muestrasDisponibles.value = emptyList()
            }
        }
    }

    /*fun eliminarMuestra(id: String) {
        val listaActual = _muestras.value.toMutableList()
        listaActual.removeAll { it.cabecera.id == id }
        _muestras.value = listaActual
    }*/
}
