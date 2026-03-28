package com.vistony.app.ViewModel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
//import androidx.compose.ui.unit.Constraints
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
import com.vistony.app.Utils.ImageEvidenceProcessor
import com.vistony.app.Workers.UploadParadaMantenimientoEvidenceWorker
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
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
    val createdActivityId: String? = null,
    val isButtonEnabled: Boolean = false


)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ActividadViewModel @Inject constructor(
    private val actividadRepository: ActividadRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val collectionParadasMantenimiento = "paradas_mantenimiento"

    private var actividadesListener: ListenerRegistration? = null
    private var listeningForUserId: String = ""
    private var rawActividades: List<semiActivity> = emptyList()
    @RequiresApi(Build.VERSION_CODES.O)
    private var currentFechaIni: LocalDateTime = LocalDateTime.now().minusDays(1)
    @RequiresApi(Build.VERSION_CODES.O)
    private var currentFechaFin: LocalDateTime = LocalDateTime.now()

    private companion object {
        const val STATUS_READY_CREATE_ACTIVITY = "ready_create_activity"
        const val STATUS_WAITING_PHOTOS_UPLOAD = "waiting_photos_upload"
        const val STATUS_READY_CLOSE_ACTIVITY = "ready_close_activity"
    }


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
        updateButtonState()
    }

    fun onFinalHourChange(date: LocalDateTime?) {
        Log.d("DATE4", date.toString())
        _uiState.update { it.copy(selectedActividad = it.selectedActividad.copy(finalHour = date?.truncatedTo(
            ChronoUnit.SECONDS))) }
    }

    fun onOTChange(ot: String) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(OT = ot))
        updateButtonState()
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
        updateButtonState()
    }


    fun onAreaChange(area: String) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(area = area))
        updateButtonState()
    }

    fun onMachineChange(maquina: Machine) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(machine = maquina))
        updateButtonState()
    }

    fun onEquipamentChange(equipo: Equipment) {
        _uiState.value =
            _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(equipment = equipo))
        updateButtonState()
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
    fun getAllActividades(
        user: UserResponse,
        fechaIni: LocalDateTime = LocalDateTime.now().minusDays(1),
        fechaFin: LocalDateTime = LocalDateTime.now()
    ) {
        val userId = user.id.toString()
        currentFechaIni = fechaIni
        currentFechaFin = fechaFin

        // Si el listener ya está activo para este usuario, solo re-aplica el filtro de fechas
        // sin hacer ninguna lectura nueva a Firestore
        if (actividadesListener != null && listeningForUserId == userId) {
            applyDateFilter()
            return
        }

        // Cancela listener anterior y registra uno nuevo
        actividadesListener?.remove()
        listeningForUserId = userId
        _uiState.update { it.copy(isLoading = true, error = null) }

        actividadesListener = firestore
            .collection(collectionParadasMantenimiento)
            .whereEqualTo("activityData.userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                    return@addSnapshotListener
                }
                rawActividades = snapshot?.documents?.mapNotNull { doc ->
                    mapDocToSemiActivity(doc)
                } ?: emptyList()
                applyDateFilter()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun applyDateFilter() {
        val filtered = rawActividades
            .filter { actividad ->
                val initialHour = parseLocalDateTime(actividad.U_InitialHour) ?: return@filter false
                !initialHour.isBefore(currentFechaIni) && !initialHour.isAfter(currentFechaFin)
            }
            .sortedByDescending { it.U_InitialHour }
        _uiState.update { it.copy(actividades = filtered, isLoading = false, error = null) }
    }

    private fun mapDocToSemiActivity(doc: DocumentSnapshot): semiActivity? {
        val activityData = doc.get("activityData") as? Map<*, *> ?: return null
        val closeData = doc.get("closeData") as? Map<*, *> ?: emptyMap<Any?, Any?>()
        val initialHourStr = activityData["initialHour"] as? String ?: ""
        val finalHourStr = closeData["finalHour"] as? String ?: ""
        val lineTec = closeData["lineTec"] as? String ?: ""
        val evidencesList = closeData["evidences"] as? List<*> ?: emptyList<Any>()
        val imageUrls = evidencesList.mapNotNull { ev ->
            val map = ev as? Map<*, *> ?: return@mapNotNull null
            map["downloadUrl"] as? String
        }
        return semiActivity(
            DocEntry = doc.id,
            U_OT = activityData["OT"] as? String ?: "",
            U_description_OT = activityData["description_OT"] as? String ?: "",
            U_unidad_medida_OT = activityData["unidad_medida_OT"] as? String ?: "",
            U_cantidad_OT = activityData["cantidad_OT"] as? String ?: "",
            U_userId = activityData["userId"] as? String ?: "",
            U_userName = activityData["userName"] as? String ?: "",
            U_userPosition = activityData["userPosition"] as? String ?: "",
            U_area = activityData["area"] as? String ?: "",
            U_machine = activityData["machine"] as? String ?: "",
            U_equipment = activityData["equipment"] as? String ?: "",
            U_LineTec = lineTec,
            U_InitialHour = initialHourStr,
            U_FinalHour = finalHourStr,
            U_imageUrls = imageUrls
        )
    }

    fun getDetailActivity(docEntry: String, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val doc = firestore.collection(collectionParadasMantenimiento).document(docEntry).get().await()
                if (!doc.exists()) {
                    _uiState.update {
                        it.copy(
                            selectedActividad = Activity2(),
                            isLoading = false,
                            error = "Actividad no encontrada"
                        )
                    }
                    return@launch
                }

                val activityData = doc.get("activityData") as? Map<*, *> ?: emptyMap<Any?, Any?>()
                val closeData = doc.get("closeData") as? Map<*, *> ?: emptyMap<Any?, Any?>()

                val initialHourStr = activityData["initialHour"] as? String ?: ""
                val initialHour = parseLocalDateTime(initialHourStr)

                val endTimeStr = closeData["endTime"] as? String
                val endTime = endTimeStr?.let { parseLocalDateTime(it) }

                val finalHourStr = closeData["finalHour"] as? String
                val finalHour = finalHourStr?.let { parseLocalDateTime(it) }

                val reasonStr = closeData["reason"] as? String ?: ""
                val failureType = if (reasonStr.isNotBlank()) parseFailureType(reasonStr) else FailureType()

                val evidencesList = closeData["evidences"] as? List<*> ?: emptyList<Any>()
                val evidenceUris = evidencesList.mapNotNull { ev ->
                    val map = ev as? Map<*, *> ?: return@mapNotNull null
                    val downloadUrl = map["downloadUrl"] as? String ?: return@mapNotNull null
                    Uri.parse(downloadUrl)
                }

                _uiState.update {
                    it.copy(
                        selectedActividad = Activity2(
                            DocEntry = docEntry,
                            startTime = initialHour,
                            OT = activityData["OT"] as? String ?: "",
                            description_OT = activityData["description_OT"] as? String ?: "",
                            unidad_medida_OT = activityData["unidad_medida_OT"] as? String ?: "",
                            cantidad_OT = activityData["cantidad_OT"] as? String ?: "",
                            userId = activityData["userId"] as? String ?: "",
                            userName = activityData["userName"] as? String ?: "",
                            userPosition = activityData["userPosition"] as? String ?: "",
                            area = activityData["area"] as? String ?: "",
                            machine = Machine(name = activityData["machine"] as? String ?: ""),
                            equipment = Equipment(name = activityData["equipment"] as? String ?: ""),
                            reason = failureType,
                            description = closeData["description"] as? String ?: "",
                            actionTaken = closeData["actionTaken"] as? String ?: "",
                            evidences = evidenceUris.toMutableStateList(),
                            observations = closeData["observations"] as? String ?: "",
                            endTime = endTime,
                            paradaDocEntry = closeData["paradaDocEntry"] as? String ?: "",
                            lineTec = closeData["lineTec"] as? String ?: "",
                            initialHour = initialHour,
                            finalHour = finalHour
                        ),
                        isLoading = false,
                        error = null
                    )
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

                val activityId = UUID.randomUUID().toString()

                val activityData = hashMapOf<String, Any?>(
                    "OT" to actividadRequest.OT,
                    "description_OT" to actividadRequest.description_OT,
                    "unidad_medida_OT" to actividadRequest.unidad_medida_OT,
                    "cantidad_OT" to actividadRequest.cantidad_OT,
                    "userId" to actividadRequest.userId,
                    "userName" to actividadRequest.userName,
                    "userPosition" to actividadRequest.userPosition,
                    "area" to actividadRequest.area,
                    "machine" to actividadRequest.machine,
                    "equipment" to actividadRequest.equipment,
                    "initialHour" to actividadRequest.initialHour
                )

                val docRef = firestore
                    .collection(collectionParadasMantenimiento)
                    .document(activityId)

                docRef.set(
                    mapOf(
                        "status" to STATUS_READY_CREATE_ACTIVITY,
                        "activityData" to activityData,
                        "sap" to mapOf<String, Any?>(),
                        "closeData" to mapOf<String, Any?>(),
                        "updatedAt" to com.google.firebase.Timestamp.now()
                    )
                ).await()

                _uiState.update {
                    it.copy(
                        isCreating = false,
                        createSuccess = true,
                        createdActivityId = activityId,
                        error = null
                    )
                }
                // El listener detecta el nuevo documento automáticamente

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
                val activityId = docEntry

                val otherReasonName =
                    if (selected.reason.name == "Otro") "Otro-"+otherReason.value else selected.reason.name

                val endTimeString =
                    selected.endTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                        ?: getCurrentDateTimeFormatted()


                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val formattedFinalHour = selected.finalHour?.format(formatter)

                // 1) Procesar y guardar las evidencias localmente (offline).
                val localEvidencePaths = copyEvidenceUrisToLocalJpegFiles(
                    context = context,
                    activityId = activityId,
                    evidenceUris = selected.evidences.toList()
                )

                // 2) Persistir los datos de cierre en Firestore, dejando evidencias vacías
                //    hasta que el Worker suba a Storage y complete evidences con downloadUrl+storagePath.
                val closeData = mapOf(
                    "reason" to otherReasonName,
                    "description" to selected.description,
                    "actionTaken" to selected.actionTaken,
                    "observations" to selected.observations,
                    "endTime" to endTimeString,
                    "paradaDocEntry" to selected.paradaDocEntry,
                    "lineTec" to selected.lineTec,
                    "finalHour" to (formattedFinalHour ?: ""),
                    "evidences" to emptyList<Map<String, Any?>>()
                )

                val docRef = firestore
                    .collection(collectionParadasMantenimiento)
                    .document(activityId)

                docRef.set(
                    mapOf(
                        "status" to STATUS_WAITING_PHOTOS_UPLOAD,
                        "closeData" to closeData,
                        "updatedAt" to com.google.firebase.Timestamp.now()
                    ),
                    com.google.firebase.firestore.SetOptions.merge()
                ).await()

                // 3) Encolar el Worker para subir evidencias cuando haya internet
                val localPathsJson = JSONArray(localEvidencePaths).toString()
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val request = OneTimeWorkRequestBuilder<UploadParadaMantenimientoEvidenceWorker>()
                    .setInputData(
                        workDataOf(
                            UploadParadaMantenimientoEvidenceWorker.KEY_ACTIVITY_ID to activityId,
                            UploadParadaMantenimientoEvidenceWorker.KEY_LOCAL_EVIDENCE_PATHS_JSON to localPathsJson
                        )
                    )
                    .setConstraints(constraints)
                    .build()

                WorkManager.getInstance(context).enqueue(request)

                _uiState.update {
                    it.copy(
                        isCreating = false,
                        createSuccess = true,
                        createdActivityId = activityId,
                        createError = null,
                        error = null
                    )
                }
                // El listener detecta el cambio automáticamente

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


    override fun onCleared() {
        super.onCleared()
        actividadesListener?.remove()
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

    private fun parseLocalDateTime(value: String?): LocalDateTime? {
        if (value.isNullOrBlank()) return null
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime.parse(value, formatter)
        } catch (_: Exception) {
            null
        }
    }

    private fun parseFailureType(reason: String): FailureType {
        val parts = reason.split("-", limit = 2)
        val namePart = parts.getOrNull(0)?.trim().orEmpty()
        val descriptionPart = parts.getOrNull(1)?.trim().orEmpty()

        val matched = StaticData.failureTypes.find { it.name.equals(namePart, ignoreCase = true) }

        return if (matched != null) {
            FailureType(
                id = matched.id,
                name = matched.name,
                description = descriptionPart
            )
        } else {
            FailureType(id = 0, name = namePart, description = descriptionPart)
        }
    }

    private fun copyEvidenceUrisToLocalJpegFiles(
        context: Context,
        activityId: String,
        evidenceUris: List<Uri>
    ): List<String> {
        if (evidenceUris.isEmpty()) {
            throw IllegalArgumentException("Se requiere al menos una evidencia (foto)")
        }

        val outDir = File(context.filesDir, "paradas_mantenimiento/$activityId/local_evidences")
        outDir.mkdirs()

        return evidenceUris.mapIndexed { index, uri ->
            val outFile = File(outDir, "evidence_$index.jpg")
            ImageEvidenceProcessor.processUriToVerticalJpeg(
                context = context,
                uri = uri,
                outputFile = outFile
            )
            outFile.absolutePath
        }
    }

    // Función para validar si todos los campos requeridos están llenos
    fun areRequiredFieldsFilled(otraMaquina: String = "", otroEquipo: String = ""): Boolean {
        val selected = _uiState.value.selectedActividad
        val selectedOT = _uiState.value.selectedOT
        
        return selected.OT.isNotBlank() &&
               selectedOT.ItemName.isNotBlank() &&
               selected.area.isNotBlank() &&
               selected.machine.name.isNotBlank() &&
               (selected.machine.name != "Otro" || otraMaquina.isNotBlank()) &&
               selected.equipment.name.isNotBlank() &&
               (selected.equipment.name != "Otro" || otroEquipo.isNotBlank())
    }

    // Función para validar si la hora de inicio es válida (no es menor a la actual)
    @RequiresApi(Build.VERSION_CODES.O)
    fun isInitialHourValid(): Boolean {
        val selected = _uiState.value.selectedActividad
        val currentTime = LocalDateTime.now()
        
        return selected.initialHour == null || !selected.initialHour!!.isBefore(currentTime)
    }

    // Función para validar formulario completo
    @RequiresApi(Build.VERSION_CODES.O)
    fun isFormValid(otraMaquina: String = "", otroEquipo: String = ""): Boolean {
        return areRequiredFieldsFilled(otraMaquina, otroEquipo) //&& isInitialHourValid()
    }

    // Función para actualizar el estado del botón
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateButtonState(otraMaquina: String = "", otroEquipo: String = "") {
        val isValid = isFormValid(otraMaquina, otroEquipo)
        _uiState.update { it.copy(isButtonEnabled = isValid) }
    }

    // Función para actualizar el estado del botón con valores externos
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateButtonStateWithExternalValues(otraMaquina: String, otroEquipo: String) {
        val isValid = isFormValid(otraMaquina, otroEquipo)
        _uiState.update { it.copy(isButtonEnabled = isValid) }
    }
}