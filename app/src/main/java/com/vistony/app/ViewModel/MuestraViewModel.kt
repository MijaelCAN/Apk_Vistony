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
        var numeroCavidad: String = "",
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
        var estado: String = "",
        var paletas: String = "",
        var bolsas: String = "",
        var criteriosEvaluacion: String = "",
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

    // Listas temporales para cada sección
    private val _materialesTemporales = MutableStateFlow<List<MaterialEmpleado>>(emptyList())
    val materialesTemporales: StateFlow<List<MaterialEmpleado>> = _materialesTemporales.asStateFlow()

    private val _inspeccionesTemporales = MutableStateFlow<List<InspeccionDimensional>>(emptyList())
    val inspeccionesTemporales: StateFlow<List<InspeccionDimensional>> = _inspeccionesTemporales.asStateFlow()

    private val _checkListsTemporales = MutableStateFlow<List<CheckListInspeccion>>(emptyList())
    val checkListsTemporales: StateFlow<List<CheckListInspeccion>> = _checkListsTemporales.asStateFlow()

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

    // Funciones para actualizar formularios
    fun updateCabecera(field: String, value: String) {
        val current = _cabeceraFormState.value
        val newState = when (field) {
            "fechaRegistro" -> current.copy(fechaRegistro = value)
            "codigo" -> current.copy(codigo = value)
            "lote" -> current.copy(lote = value)
            "embalaje" -> current.copy(embalaje = value)
            "maquina" -> current.copy(maquina = value)
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
            "estado" -> current.copy(estado = value)
            "paletas" -> current.copy(paletas = value)
            "bolsas" -> current.copy(bolsas = value)
            "criteriosEvaluacion" -> current.copy(criteriosEvaluacion = value)
            else -> current
        }
        _evaluacionFormState.value = newState
        validateEvaluacionForm()
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
            current.estado.isNotEmpty() &&
            current.paletas.isNotEmpty() &&
            current.bolsas.isNotEmpty()
        }
        
        _evaluacionFormState.value = current.copy(isFormValid = isValid)
    }

    // Función para crear un CheckList
    fun crearCheckList(muestraId: String) {
        viewModelScope.launch {
            android.util.Log.d("MuestraViewModel", "=== CREANDO CHECKLIST ===")
            android.util.Log.d("MuestraViewModel", "DocEntry (currentMuestraId): $_currentMuestraId.value")
            
            _isCreating.value = true
            _errorMessage.value = null
            
            try {
                val checkList = _checkListFormState.value
                val fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                
                // Usar el docEntry de la muestra actual
                val docEntry = _currentMuestraId.value ?: muestraId
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
                val inspeccion = _inspeccionFormState.value
                val fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                
                // Usar el docEntry de la muestra actual
                val docEntry = _currentMuestraId.value ?: muestraId
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
                            // Limpiar el formulario después de crear
                            resetInspeccionForm()
                            // Agregar a la lista temporal
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

    // Funciones para agregar elementos a las listas temporales
    fun agregarMaterial() {
        val material = MaterialEmpleado(
            id = UUID.randomUUID().toString(),
            muestraId = "",
            material = _materialFormState.value.material,
            marca = _materialFormState.value.marca,
            codigo = _materialFormState.value.codigo,
            lote = _materialFormState.value.lote
        )
        val listaActual = _materialesTemporales.value.toMutableList()
        listaActual.add(material)
        _materialesTemporales.value = listaActual
        resetMaterialForm()
        
        // Guardar automáticamente si está en modo edición
        guardarCambiosAutomaticamente()
    }

    fun agregarInspeccion() {
        // Validar que tenemos un muestraId
        val muestraId = _currentMuestraId.value
        if (muestraId.isNullOrEmpty()) {
            _errorMessage.value = "Error: No se encontró el ID de la muestra"
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
        _inspeccionFormState.value = InspeccionDimensionalFormState()
    }

    fun resetCheckListForm() {
        _checkListFormState.value = CheckListFormState()
    }

    fun resetEvaluacionForm() {
        _evaluacionFormState.value = EvaluacionFormState()
    }

    fun resetAllForms() {
        _cabeceraFormState.value = CabeceraFormState()
        _materialFormState.value = MaterialFormState()
        _inspeccionFormState.value = InspeccionDimensionalFormState()
        _checkListFormState.value = CheckListFormState()
        _evaluacionFormState.value = EvaluacionFormState()
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
            _pasoActual.value = _pasoActual.value + 3 // HASTA HABILITAR LOS MODULOS
        }
    }

    fun pasoAnterior() {
        if (_pasoActual.value > 1) {
            _pasoActual.value = _pasoActual.value - 3 // HASTA HABILITAR LOS MODULOS
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
                    estado = evaluacion.estado,
                    paletas = evaluacion.paletas,
                    bolsas = evaluacion.bolsas,
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
                    val evaluacionCompletado = _evaluacionFormState.value.estado.isNotEmpty()
                    
                    // Determinar el estado de la cabecera basado en la completitud
                    val estadoCabecera = when {
                        materialesCompletado && inspeccionesCompletado && checkListsCompletado && evaluacionCompletado -> "Completado"
                        materialesCompletado || inspeccionesCompletado || checkListsCompletado || evaluacionCompletado -> "En Proceso"
                        else -> "Nuevo"
                    }
                    
                    val muestraActualizada = listaActual[indice].copy(
                        cabecera = listaActual[indice].cabecera.copy(estado = estadoCabecera),
                        materiales = _materialesTemporales.value,
                        inspeccionesDimensionales = _inspeccionesTemporales.value,
                        checkLists = _checkListsTemporales.value,
                        evaluacionProduccion = if (_evaluacionFormState.value.estado.isNotEmpty()) {
                            EvaluacionProduccion(
                                id = UUID.randomUUID().toString(),
                                muestraId = muestraId,
                                estado = _evaluacionFormState.value.estado,
                                paletas = _evaluacionFormState.value.paletas,
                                bolsas = _evaluacionFormState.value.bolsas,
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
                        android.util.Log.d("MuestraViewModel", "SUCCESS - DocEntry: ${response.data.docEntry}")
                        android.util.Log.d("MuestraViewModel", "SUCCESS - CheckLists: ${response.data.checkList.size}")
                        
                        if (response.success) {
                            // Mapear la respuesta del API a MuestraCompleta
                            val muestraDetalle = response.data
                            
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
                            
                            // Crear MuestraCompleta
                            val muestraCompleta = MuestraCompleta(
                                cabecera = cabecera,
                                materiales = emptyList(), // Por ahora vacío
                                inspeccionesDimensionales = emptyList(), // Por ahora vacío
                                checkLists = checkLists,
                                evaluacionProduccion = null,
                                materialesCompletado = false,
                                inspeccionesCompletado = false,
                                checkListsCompletado = checkLists.isNotEmpty(),
                                evaluacionCompletado = false,
                                fechaUltimaActualizacion = muestraDetalle.fecRegister
                            )
                            
                            // Guardar el docEntry para usar en checkList
                            _currentMuestraId.value = muestraDetalle.docEntry
                            
                            // Si está en modo edición, cargar los datos en el formulario de cabecera
                            if (_isEditMode.value) {
                                android.util.Log.d("MuestraViewModel", "Cargando datos en formulario de cabecera para edición")
                                _cabeceraFormState.value = CabeceraFormState(
                                    fechaRegistro = cabecera.fechaRegistro,
                                    codigo = cabecera.codigo,
                                    lote = cabecera.lote,
                                    embalaje = cabecera.embalaje,
                                    maquina = cabecera.maquina,
                                    turno = cabecera.turno,
                                    ot = "", // No viene del API
                                    producto = cabecera.producto,
                                    auxiliar = cabecera.auxiliar,
                                    encargadoProduccion = cabecera.encargadoProduccion,
                                    isFormValid = true // En modo edición siempre es válido
                                )

                                val listaActual = checkLists.toMutableList()
                                _checkListsTemporales.value = listaActual

                                android.util.Log.d("MuestraViewModel", "Datos de cabecera cargados en el formulario")
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

    /*fun eliminarMuestra(id: String) {
        val listaActual = _muestras.value.toMutableList()
        listaActual.removeAll { it.cabecera.id == id }
        _muestras.value = listaActual
    }*/
}
