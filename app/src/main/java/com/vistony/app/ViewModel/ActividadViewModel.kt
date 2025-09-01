package com.vistony.app.ViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.Activity2
import com.vistony.app.Entidad.Equipment
import com.vistony.app.Entidad.FailureReport
import com.vistony.app.Entidad.FailureType
import com.vistony.app.Entidad.Machine
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.StaticData
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.Extras.LocalDateTimeAdapter
import com.vistony.app.Extras.SnapshotStateListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import javax.inject.Inject

data class ActivityUi_State @RequiresApi(Build.VERSION_CODES.O) constructor(
    val actividades: List<Activity2> = emptyList(),
    val listAreas: List<String> = StaticData.productionLines,
    val listMaquinas: List<Machine> = emptyList(),
    val listEquipos: List<Equipment> = emptyList(),
    val listMotivos: List<FailureType> = StaticData.failureTypes,


    val expandedArea: Boolean = false,
    val expandedMotivo: Boolean = false,
    val expandedEquipo: Boolean = false,
    val expandedMaquina: Boolean = false,

    val showDialogDateIni: Boolean = false,
    val showDialogDateFin: Boolean = false,

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
    val selectedActividad: Activity2 = Activity2()
)

@HiltViewModel
class ActividadViewModel @Inject constructor(): ViewModel() {


    private  val _uiState = MutableStateFlow(ActivityUi_State())
    val uiState: MutableStateFlow<ActivityUi_State> = _uiState

    private val _actividades = MutableStateFlow<List<Actividad>>(emptyList())
    val actividades: MutableStateFlow<List<Actividad>> = _actividades

    @RequiresApi(Build.VERSION_CODES.O)
    fun onInitialChange(currentUser: UserResponse ) { // images: SnapshotStateList<Uri>
        val registroAnterior = _uiState.value.selectedActividad
        _uiState.value = _uiState.value.copy(
            selectedActividad = registroAnterior.copy(
                startTime = LocalDateTime.now(),
                userId = currentUser.id.toString(),
                userName = currentUser.name,
                userPosition = currentUser.position,
            )
        )
    }
    fun onShowDialogDateIniChange(showDialog: Boolean) {
        _uiState.value = _uiState.value.copy(showDialogDateIni = showDialog)
    }
    fun onShowDialogDateFinChange(showDialog: Boolean) {
        _uiState.value = _uiState.value.copy(showDialogDateFin = showDialog)
    }
    fun onStartTimeChange(date: LocalDateTime?) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(startTime = date))
    }

    fun onOTChange(ot: String) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(OT = ot))
    }

    fun onAreaChange(area: String) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(area = area))
    }

    fun onMachineChange(maquina: Machine) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(machine = maquina))
    }
    fun onEquipamentChange(equipo: Equipment) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(equipment = equipo))
    }
    fun onReasonChange(motivo: FailureType) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(reason = motivo))
    }
    fun onDescriptionChange(descripcion: String) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(description = descripcion))
    }

    fun onActionChange(accion: String) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(actionTaken = accion))
    }

    fun onObservationChange(observacion: String) {
        _uiState.value = _uiState.value.copy(selectedActividad = _uiState.value.selectedActividad.copy(observations = observacion))
    }


    fun onExpandedAreaChange(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(expandedArea = expanded)
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
    fun crearActividad(otraMaquina: MutableState<String>, otroEquipo: MutableState<String>) {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .registerTypeAdapter(SnapshotStateList::class.java, SnapshotStateListAdapter<Any>())
            .setPrettyPrinting()
            .create()

        val selected = _uiState.value.selectedActividad
        val machineName = if (selected.machine.name == "Otro") otraMaquina.value else selected.machine.name
        val EquipmentName = if (selected.equipment.name == "Otro") otroEquipo.value else selected.equipment.name

        val nuevaActividad = selected.copy(
            machine = selected.machine.copy(name = machineName),
            equipment = selected.equipment.copy(name = EquipmentName)
        )



        // Obtén la lista actual de actividades y le agregas la nueva
        val actividadesActualizadas = _uiState.value.actividades + nuevaActividad
        val jsonActividad = gson.toJson(nuevaActividad)

        // También, si tienes otra variable que mantiene la lista independiente (como _actividades), actualízala también si es necesario:
        //_actividades.value = actividadesActualizadas
        Log.e("MDCR", "Entidad Actividad: $nuevaActividad")
        Log.e("MDCR", "Actividad creada en Json: $jsonActividad")

        // Actualiza el estado con la lista de actividades actualizada
        _uiState.value = _uiState.value.copy(actividades = actividadesActualizadas, selectedActividad = Activity2())
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
        val currentActividades = _actividades.value.toMutableList()
        //val index = currentActividades.indexOfFirst { it.id == actividad.id }
        if (indexOf != -1) {
            // Crear una copia con statusActividad modificado a 'Y'
            val updatedActividad = currentActividades[indexOf].copy(statusActividad = !_actividades.value[indexOf].statusActividad)
            currentActividades[indexOf] = updatedActividad
            _actividades.value = currentActividades
        }
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
    fun getMachinesByLine(line: String){
        val machines = when (line) {
            "Grasa" -> StaticData.greaseMachines
            "Aceite" -> StaticData.oilMachines
            "Acuosos" -> StaticData.aqueousMachines
            "Soplado" -> StaticData.blowingMachines
            else -> emptyList()
        }
        _uiState.value = _uiState.value.copy(listMaquinas = machines)

    }



}