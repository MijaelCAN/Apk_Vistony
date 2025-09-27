package com.vistony.app.ViewModel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.Activity2
import com.vistony.app.Entidad.Activity2Dto
import com.vistony.app.Entidad.ActivityRequest
import com.vistony.app.Entidad.Equipment
import com.vistony.app.Entidad.Evidence
import com.vistony.app.Entidad.FailureReport
import com.vistony.app.Entidad.FailureType
import com.vistony.app.Entidad.Machine
import com.vistony.app.Entidad.OTItem
import com.vistony.app.Entidad.OTRequest
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.StaticData
import com.vistony.app.Entidad.UpdateActividadRequest
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.Entidad.semiActivity
import com.vistony.app.Repository.ActividadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class ActivityUi_State @RequiresApi(Build.VERSION_CODES.O) constructor(
    val userCurrent: UserResponse = UserResponse(),
    val actividades: List<semiActivity> = emptyList(),
    val listAreas: List<String> = StaticData.productionLines,
    val listMaquinas: List<Machine> = emptyList(),
    val listEquipos: List<Equipment> = emptyList(),
    val listMotivos: List<FailureType> = StaticData.failureTypes,
    val listOT: List<OTItem> = emptyList(),


    val expandedArea: Boolean = false,
    val expandedMotivo: Boolean = false,
    val expandedEquipo: Boolean = false,
    val expandedMaquina: Boolean = false,
    val expandedOT: Boolean = false,

    val showDialogDateIni: Boolean = false,
    val showDialogDateFin: Boolean = false,
    val showDialogTimeIni: Boolean = false,
    val showDialogTimeFin: Boolean = false,

    val listEquiposGrasas: List<Equipment> = StaticData.greaseEquipment,
    val listEquiposAceite: List<Equipment> = StaticData.oilEquipment,
    val listEquiposAcuosos: List<Equipment> = StaticData.aqueousEquipment,
    val listEquiposSoplado: List<Equipment> = StaticData.blowingEquipment,
    val listMaquinasGrasas: List<Machine> = StaticData.greaseMachines,
    val listMaquinasAceite: List<Machine> = StaticData.oilMachines,
    val listMaquinasAcuosos: List<Machine> = StaticData.aqueousMachines,
    val listMaquinasSoplado: List<Machine> = StaticData.blowingMachines,

    val listExampleReport: List<FailureReport> = StaticData.sampleFailureReports,
    val selectedArea: String = "",
    val selectedMachine: String = "",
    val selectedMotivo: String = "",
    val selectedEquipo: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedActividad: Activity2 = Activity2(),
    val selectedOT: OTItem = OTItem(),


    val isCreating: Boolean = false,
    val createSuccess: Boolean = false,
    val createError: String? = null,
    val createdActivityId: String? = null


)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ActividadViewModel @Inject constructor(
    private val actividadRepository: ActividadRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(ActivityUi_State())
    val uiState: MutableStateFlow<ActivityUi_State> = _uiState

    private val _actividades = MutableStateFlow<List<Actividad>>(emptyList())
    val actividades: MutableStateFlow<List<Actividad>> = _actividades

    init {
        //getAllActividades()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onInitialChange(currentUser: UserResponse) { // images: SnapshotStateList<Uri>
        val registroAnterior = _uiState.value.selectedActividad
        _uiState.value = _uiState.value.copy(
            selectedActividad = registroAnterior.copy(
                startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                userId = currentUser.id.toString(),
                userName = currentUser.name,
                userPosition = currentUser.position,
            )
        )
    }
    fun onUserChange(user: UserResponse) {
        Log.e("Current User => ", user.toString())
        _uiState.update { it.copy(userCurrent = user) }
    }

    fun onShowDialogDateIniChange(showDialog: Boolean) {
        _uiState.value = _uiState.value.copy(showDialogDateIni = showDialog)
    }

    fun onShowDialogDateFinChange(showDialog: Boolean) {
        _uiState.value = _uiState.value.copy(showDialogDateFin = showDialog)
    }

    fun onShowDialogTimeIniChange(showDialog: Boolean) {
        _uiState.value = _uiState.value.copy(showDialogTimeIni = showDialog)
    }

    fun onShowDialogTimeFinChange(showDialog: Boolean) {
        _uiState.update { it.copy(showDialogTimeFin = showDialog) }
    }

    fun onStartTimeChange(date: LocalDateTime?) {
        _uiState.update { it.copy(selectedActividad = it.selectedActividad.copy(startTime = date)) }
    }

    fun onEndTimeChange(date: LocalDateTime?) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(endTime = date))
    }

    fun onInitialHourChange(date: LocalDateTime?) {
        Log.d("DATE3", date.toString())
        _uiState.update { it.copy(selectedActividad = it.selectedActividad.copy(initialHour = date)) }
    }

    fun onFinalHourChange(date: LocalDateTime?) {
        Log.d("DATE4", date.toString())
        _uiState.update { it.copy(selectedActividad = it.selectedActividad.copy(finalHour = date?.truncatedTo(
            ChronoUnit.SECONDS))) }
    }

    fun onOTChange(ot: String) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(OT = ot))
    }

    fun onOTItemChange(otItem: OTItem) {
        _uiState.update {
            it.copy(
                selectedActividad = it.selectedActividad.copy(
                    description_OT = otItem.ItemName,
                    unidad_medida_OT = otItem.UomName,
                    cantidad_OT = otItem.PlannedQty
                ), selectedOT = otItem
            )
        }
    }


    fun onAreaChange(area: String) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(area = area))
    }

    fun onMachineChange(maquina: Machine) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(machine = maquina))
    }

    fun onEquipamentChange(equipo: Equipment) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(equipment = equipo))
    }

    fun onReasonChange(motivo: FailureType) {
        if(_uiState.value.selectedActividad.endTime == null){
            _uiState.value =
                _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(reason = motivo))
        }

    }

    fun onDescriptionChange(descripcion: String) {
        _uiState.value = _uiState.value.copy(
            selectedActividad = _uiState.value.selectedActividad.copy(description = descripcion)
        )
    }

    fun onActionChange(accion: String) {
        _uiState.value = _uiState.value.copy(
            selectedActividad = _uiState.value.selectedActividad.copy(actionTaken = accion)
        )
    }

    fun onObservationChange(observacion: String) {
        _uiState.value = _uiState.value.copy(
            selectedActividad = _uiState.value.selectedActividad.copy(observations = observacion)
        )
    }

    fun onLineTecChange(lineTec: String) {
        _uiState.update { it.copy(selectedActividad = it.selectedActividad.copy(lineTec = lineTec)) }
    }

    fun onImagesChange(images: SnapshotStateList<Uri>) {
        val updateActividad = uiState.value.selectedActividad.copy(evidences = images)
        _uiState.update { it.copy(selectedActividad = updateActividad) }
    }

    fun onImagesChange(newImages: List<Uri>) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedActividad = currentState.selectedActividad.copy(
                    evidences = newImages.toMutableStateList()
                )
            )
        }
    }

    fun addImage(uri: Uri) {
        _uiState.update { currentState ->
            val currentImages = currentState.selectedActividad.evidences.toMutableStateList()
            currentImages.add(uri)

            val nuevaActividad = currentState.selectedActividad.copy(evidences = currentImages)
            currentState.copy(selectedActividad = nuevaActividad)
        }
    }

    fun removeImage(uri: Uri) {
        _uiState.update { currentState ->
            val currentImages = currentState.selectedActividad.evidences.toMutableStateList()
            currentImages.remove(uri)

            val nuevaActividad = currentState.selectedActividad.copy(evidences = currentImages)
            currentState.copy(selectedActividad = nuevaActividad)
        }
    }


    fun onExpandedAreaChange(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(expandedArea = expanded)
    }

    fun onExpandedOTChange(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(expandedOT = expanded)
    }

    fun onExpandedMaquinaChange(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(expandedMaquina = expanded)
    }

    fun onExpandedEquipoChange(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(expandedEquipo = expanded)
    }

    fun onExpandedMotivoChange(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(expandedMotivo = expanded)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllActividades(user: UserResponse, fechaIni: LocalDateTime = LocalDateTime.now().minusDays(1), fechaFin: LocalDateTime = LocalDateTime.now()) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {

                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val formattedStartDate = fechaIni.format(formatter)
                val formattedEndDate = fechaFin.format(formatter)

                Log.d("formattedStartDate", user.toString())


                val response = actividadRepository.getAllActividades(
                    user.id,
                    user.position,
                    formattedStartDate,
                    formattedEndDate
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _uiState.update {
                            it.copy(
                                actividades = body.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        val errorMessage = response.message() ?: "Error desconocido del servidor"
                        _uiState.update {
                            it.copy(
                                actividades = emptyList(),
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        actividades = emptyList(),
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun getDetailActivity(docEntry: String, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = actividadRepository.getActividadByDocEntry(docEntry)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.success) {
                            Log.d("body", body.data[0].toString())
                            _uiState.update {
                                it.copy(
                                    selectedActividad = Activity2Dto(
                                        u_OT = body.data[0].u_OT,
                                        u_description_OT = body.data[0].u_description_OT,
                                        u_unidad_medida_OT = body.data[0].u_unidad_medida_OT,
                                        u_cantidad_OT = body.data[0].u_cantidad_OT,
                                        u_userId = body.data[0].u_userId,
                                        u_userName = body.data[0].u_userName,
                                        u_userPosition = body.data[0].u_userPosition,
                                        u_area = body.data[0].u_area,
                                        u_machine = body.data[0].u_machine,
                                        u_equipment = body.data[0].u_equipment,
                                        u_reason = body.data[0].u_reason,
                                        u_description = body.data[0].u_description,
                                        u_actionTaken = body.data[0].u_actionTaken,
                                        evidencia = body.data[0].evidencia,
                                        u_observations = body.data[0].u_observations,
                                        u_endTime = body.data[0].u_endTime,
                                        u_LineTec = body.data[0].u_LineTec,
                                        u_InitialHour = body.data[0].u_InitialHour,
                                        u_FinalHour = body.data[0].u_FinalHour
                                    ).toActivity2(docEntry,context),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }else{
                            val errorMessage = response.message() ?: "Error desconocido del servidor"
                            _uiState.update {
                                it.copy(
                                    selectedActividad = Activity2(),
                                    isLoading = false,
                                    error = errorMessage
                                )
                            }
                            Log.d("ELSE", body.toString());
                        }
                    }else{
                        val errorMessage = response.message() ?: "Error desconocido del servidor"
                        _uiState.update {
                            it.copy(
                                selectedActividad = Activity2(),
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                        Log.e("Actividad", "Error HTTP2: ${response.code()} - $errorMessage")
                    }
                }else{
                    val errorMessage = response.message() ?: "Error desconocido del servidor"
                    _uiState.update {
                        it.copy(
                            selectedActividad = Activity2(),
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    Log.e("Actividad", "Error HTTP1: ${response.code()} - $errorMessage")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        selectedActividad = Activity2(),
                        isLoading = false,
                        error = e.message
                    )
                }
                Log.e("Actividad", "Excepción: ${e.message}", e)
            }
        }
    }


    fun getAllOT(new_ot: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val selected = _uiState.value.selectedActividad
                Log.d("OT", new_ot)
                Log.d("selected_OT", selected.OT)

                val response = actividadRepository.getCodigoBarra(OTRequest(selected.OT))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _uiState.update {
                            it.copy(
                                listOT = body.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        val errorMessage = response.message() ?: "Error desconocido del servidor"
                        _uiState.update {
                            it.copy(
                                listOT = emptyList(),
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        listOT = emptyList(),
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
    fun onResetSelectedActividad() {
        Log.d("reset", "reset")
        _uiState.update { it.copy(selectedActividad = Activity2()) }
    }

    fun resetCreateState() {

        Log.d("reset", "resetCreateState")
        _uiState.update { currentState ->
            currentState.copy(
                isCreating = false,
                createSuccess = false,
                createError = null,
                createdActivityId = null,
                selectedActividad = Activity2(),
                selectedOT = OTItem()
            )
        }
    }

    fun crearActividad(otraMaquina: MutableState<String>, otroEquipo: MutableState<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, createError = null) }
            try {
                val selected = _uiState.value.selectedActividad
                val machineName =
                    if (selected.machine.name == "Otro") otraMaquina.value else selected.machine.name
                val EquipmentName =
                    if (selected.equipment.name == "Otro") otroEquipo.value else selected.equipment.name

                val nuevaActividad = selected.copy(
                    machine = selected.machine.copy(name = machineName),
                    equipment = selected.equipment.copy(name = EquipmentName)
                )
                val formatter = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                //val formattedInitialHour = nuevaActividad.initialHour?.format(formatter)

                val actividadRequest = ActivityRequest(
                    OT = nuevaActividad.OT,
                    description_OT = nuevaActividad.description_OT,
                    unidad_medida_OT = nuevaActividad.unidad_medida_OT,
                    cantidad_OT = nuevaActividad.cantidad_OT,
                    userId = nuevaActividad.userId,
                    userName = nuevaActividad.userName,
                    userPosition = nuevaActividad.userPosition,
                    area = nuevaActividad.area,
                    machine = nuevaActividad.machine.name,
                    equipment = nuevaActividad.equipment.name,
                    initialHour = formatter,
                )
                Log.e("MDCR", "Entidad Actividad: $actividadRequest")

                val response = actividadRepository.registrarActividad(actividadRequest)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.success) {
                            _uiState.update {
                                it.copy(
                                    isCreating = false,
                                    createSuccess = true,
                                    createdActivityId = "12345",
                                    error = null
                                )
                            }
                            getAllActividades(user = _uiState.value.userCurrent)
                            //Log.d("Actividad", body.toString())
                        } else {
                            val errorMessage = body?.message ?: "Error desconocido del servidor"
                            _uiState.update {
                                it.copy(
                                    isCreating = false,
                                    createError = errorMessage
                                )
                            }
                            Log.d("ELSE", body.toString());
                        }
                    }
                } else {
                    val errorMessage = response.message() ?: "Error de conexion"
                    _uiState.update { it.copy(isCreating = false, createError = errorMessage) }
                    Log.e("Actividad", "Error HTTP: ${response.code()} - $errorMessage")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        createSuccess = false,
                        createError = e.message
                    )
                }
                Log.e("Actividad", "Excepción: ${e.message}", e)
            }
        }
    }

    fun UpdateActividad(context: Context, docEntry: String, otherReason: MutableState<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, createError = null) }
            try {

                //val docEntry = "20"
                val selected = _uiState.value.selectedActividad
                val otherReasonName =
                    if (selected.reason.name == "Otro") "Otro-"+otherReason.value else selected.reason.name

                val endTimeString =
                    selected.endTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                        ?: getCurrentDateTimeFormatted()


                val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                val formattedFinalHour = selected.finalHour?.format(formatter)

                val request = UpdateActividadRequest(
                    reason = otherReasonName,
                    description = selected.description,
                    actionTaken = selected.actionTaken,
                    evidences = convertirUrisAEvidencias(context = context, selected.evidences),
                    observations = selected.observations,
                    endTime = endTimeString,
                    paradaDocEntry = selected.paradaDocEntry,
                    lineTec = selected.lineTec,
                    finalHour = formattedFinalHour
                )
                Log.d("evidences", selected.evidences.toString())
                Log.d(
                    "Evidencias",
                    convertirUrisAEvidencias(context, selected.evidences).toString()
                )
                Log.d("request", request.toString())

                val response = actividadRepository.updateActividad(docEntry, request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.success) {
                            _uiState.update {
                                it.copy(
                                    isCreating = false,
                                    createSuccess = true,
                                    createdActivityId = body.data,
                                    error = null
                                )
                            }
                        }
                        Log.d("Actividad", body.toString())
                    }
                } else {
                    val errorMessage = response.message() ?: "Error desconocido del servidor"
                    _uiState.update { it.copy(isCreating = false, createError = errorMessage) }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        createSuccess = false,
                        createError = e.message
                    )
                }
            }

        }
    }


    fun actualizarActividad(parada: Parada) {

    }

    fun eliminarActividad(parada: Parada) {

    }

    fun obtenerActividadPorId(id: String) {

    }

    /*fun setStatusActividad(actividad: Actividad){
        var currentActividad = _actividades.value.toMutableList()
        currentActividad
        _actividades.value = currentActividad
    }*/

    fun setStatusActividad(indexOf: Int) {
        /*val currentActividades = _actividades.value.toMutableList()
        //val index = currentActividades.indexOfFirst { it.id == actividad.id }
        if (indexOf != -1) {
            // Crear una copia con statusActividad modificado a 'Y'
            val updatedActividad = currentActividades[indexOf].copy(statusActividad = !_actividades.value[indexOf].statusActividad)
            currentActividades[indexOf] = updatedActividad
            _actividades.value = currentActividades
        }*/
    }

    // Función para obtener equipos por línea
    fun getEquipmentByLine(line: String) {
        val equipment = when (line) {
            "Grasa" -> StaticData.greaseEquipment
            "Aceite" -> StaticData.oilEquipment
            "Acuosos" -> StaticData.aqueousEquipment
            "Soplado" -> StaticData.blowingEquipment
            else -> emptyList()
        }
        _uiState.value = _uiState.value.copy(listEquipos = equipment)
    }

    // Función para obtener máquinas por línea
    fun getMachinesByLine(line: String) {
        val machines = when (line) {
            "Grasa" -> StaticData.greaseMachines
            "Aceite" -> StaticData.oilMachines
            "Acuosos" -> StaticData.aqueousMachines
            "Soplado" -> StaticData.blowingMachines
            else -> emptyList()
        }
        _uiState.value = _uiState.value.copy(listMaquinas = machines)

    }

    fun convertirUrisAEvidencias(context: Context, uris: List<Uri>): List<Evidence> {
        Log.d("uris", uris.toString())

        return uris.mapNotNull { uri ->
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) {
                    val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                    val fileName = uri.lastPathSegment ?: "archivo_desconocido"
                    val fileType =
                        context.contentResolver.getType(uri) ?: "application/octet-stream"
                    Evidence(fileName, fileType, base64)
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getCurrentDateTimeFormatted(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return now.format(formatter)
    }
}