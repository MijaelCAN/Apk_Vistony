package com.vistony.app.Entidad

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import coil.disk.DiskCache
import java.time.LocalDateTime

data class Actividad(
    val id: String,
    val equipo: String, // ID de la máquina asociada a la actividad
    val tipoFalla: String, // "mecanico", "electrico", "operativo"
    val descripcionActividad: String,
    val causaParada: String,
    val fechaInicio: String,
    val fechaFin: String? = null,
    val listaImagenes: List<Uri>, // Lista de URIs de las imágenes
    //val listaImagenes: List<String>, // Lista de URIs de los imagenes String
    val statusActividad: Boolean,
    val tecnico: String, // ID del usuario que registra la actividad
    val paradaDocEntry: String
)

data class Activity2 @RequiresApi(Build.VERSION_CODES.O) constructor(
    val startTime: LocalDateTime? = LocalDateTime.now(),
    val OT: String = "",
    val description_OT: String = "",
    val unidad_medida_OT: String = "",
    val cantidad_OT: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPosition: String = "",
    var area: String = "",
    val machine: Machine = Machine(),
    val equipment: Equipment = Equipment(),

    val reason: FailureType = FailureType(),
    val description: String = "",
    val actionTaken: String = "",
    val evidences: List<Uri> = emptyList(),
    val observations: String = "",
    val endTime: LocalDateTime? = null,
    val paradaDocEntry: String = ""
)


data class FailureType(
    val id: Int = 0,
    val name: String = ""
)

data class Equipment(
    val id: Int = 0,
    val name: String = ""
)

data class Machine(
    val id: Int = 0,
    val name: String = "",
    val line: String = ""
)

data class FailureReport(
    val line: String,
    val machine: String,
    val equipment: String,
    val failureType: String
)

object StaticData {
    // Líneas de producción
    val productionLines = listOf("Grasa", "Aceite", "Acuosos", "Soplado")

    // Tipos de fallas
    val failureTypes = listOf(
        FailureType(1, "Electrico"),
        FailureType(2, "Mecanico"),
        FailureType(3, "Neumatico"),
        FailureType(4, "Operacional"),
        FailureType(5, "Hidraulico"),
        FailureType(6, "Material"),
        FailureType(7, "Otro")
    )

    // Equipos por línea
    val greaseEquipment = listOf(
        Equipment(1, "Agitador"),
        Equipment(2, "Molino"),
        Equipment(3, "Bomba"),
        Equipment(4, "Extractor"),
        Equipment(5, "Otro")
    )

    val oilEquipment = listOf(
        Equipment(1, "Tanque"),
        Equipment(2, "Bomba"),
        Equipment(3, "Etiquetadora"),
        Equipment(4, "Llenadora"),
        Equipment(5, "Tapadora"),
        Equipment(6, "Selladora de induccion"),
        Equipment(7, "Codificadora"),
        Equipment(8, "Otro")
    )

    val aqueousEquipment = listOf(
        Equipment(1, "Tanque"),
        Equipment(2, "Bomba"),
        Equipment(3, "Etiquetadora"),
        Equipment(4, "Llenadora"),
        Equipment(5, "Tapadora"),
        Equipment(6, "Selladora de induccion"),
        Equipment(7, "Codificadora"),
        Equipment(8, "Otro")
    )

    val blowingEquipment = listOf(
        Equipment(1, "Tablero electrico principal"),
        Equipment(2, "Sistema hidraulico de open y close de moldes"),
        Equipment(3, "Sistema hidraulico de parison"),
        Equipment(4, "Sistema de rebarbador derecho"),
        Equipment(5, "Sistema de rebarbador izquierdo"),
        Equipment(6, "Pin de soplo derecho"),
        Equipment(7, "Pin de soplo izquierdo"),
        Equipment(8, "Sistema de robot derecho"),
        Equipment(9, "Sistema de robot izquierdo"),
        Equipment(10, "Sistema de cabezal de extruder principal"),
        Equipment(11, "Sistema de extruder de linea visora"),
        Equipment(12, "Molino trituradora de plastico"),
        Equipment(13, "Blower de 3hp"),
        Equipment(14, "Blower de 1hp"),
        Equipment(15, "Faja transportadora de recojo de rebarba"),
        Equipment(16, "Faja transportadora de alimentacion al molino"),
        Equipment(17, "Faja transportadora auxiliares"),
        Equipment(18, "Otro")
    )

    // Máquinas por línea
    val greaseMachines = listOf(
        Machine(1, "TGR-201", "Grasa"),
        Machine(2, "TGR-202", "Grasa"),
        Machine(3, "TGR-203", "Grasa"),
        Machine(4, "TGR-204", "Grasa"),
        Machine(5, "TGR-205", "Grasa"),
        Machine(6, "TGR-206", "Grasa"),
        Machine(7, "TGR-207", "Grasa"),
        Machine(8, "Otro", "Grasa")
    )

    val oilMachines = listOf(
        Machine(1, "Española", "Aceite"),
        Machine(2, "Italiana 01", "Aceite"),
        Machine(3, "Italiana 02", "Aceite"),
        Machine(4, "Volpack 02", "Aceite"),
        Machine(5, "Mezpack", "Aceite"),
        Machine(6, "Balde y Cilindro", "Aceite"),
        Machine(7, "Otro", "Aceite")
    )

    val aqueousMachines = listOf(
        Machine(1, "Freno", "Acuosos"),
        Machine(2, "Urea", "Acuosos"),
        Machine(3, "Silicona", "Acuosos"),
        Machine(4, "Radiador", "Acuosos"),
        Machine(5, "Acuosos", "Acuosos"),
        Machine(6, "Otro", "Acuosos")
    )

    val blowingMachines = listOf(
        Machine(1, "Sopladora 01", "Soplado"),
        Machine(2, "Sopladora 02", "Soplado"),
        Machine(3, "Sopladora 03", "Soplado"),
        Machine(4, "Sopladora 04", "Soplado"),
        Machine(5, "Sopladora 05", "Soplado"),
        Machine(6, "Inyectora 01", "Soplado"),
        Machine(7, "Inyectora 02", "Soplado"),
        Machine(8, "Otro", "Soplado")
    )

    // Datos de ejemplo para reports
    val sampleFailureReports = listOf(
        FailureReport("Grasa", "TGR-201", "Agitador", "Electrico"),
        FailureReport("Grasa", "TGR-202", "Molino", "Mecanico"),
        FailureReport("Grasa", "TGR-203", "Bomba", "Neumatico"),
        FailureReport("Grasa", "TGR-204", "Extractor", "Operacional"),
        FailureReport("Grasa", "TGR-205", "Otros (Texto)", "Hidraulico"),
        FailureReport("Aceite", "Española", "Tanque", "Material"),
        FailureReport("Aceite", "Italiana 01", "Bomba", "Electrico"),
        FailureReport("Acuosos", "Freno", "Tanque", "Mecanico"),
        FailureReport("Soplado", "Sopladora 01", "Tablero electrico principal", "Electrico")
    )

    // Función para obtener equipos por línea
    fun getEquipmentByLine(line: String): List<Equipment> {
        return when (line) {
            "Grasa" -> greaseEquipment
            "Aceite" -> oilEquipment
            "Acuosos" -> aqueousEquipment
            "Soplado" -> blowingEquipment
            else -> emptyList()
        }
    }

    // Función para obtener máquinas por línea
    fun getMachinesByLine(line: String): List<Machine> {
        return when (line) {
            "Grasa" -> greaseMachines
            "Aceite" -> oilMachines
            "Acuosos" -> aqueousMachines
            "Soplado" -> blowingMachines
            else -> emptyList()
        }
    }
}