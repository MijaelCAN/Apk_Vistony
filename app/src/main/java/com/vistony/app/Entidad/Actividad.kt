package com.vistony.app.Entidad

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.google.gson.annotations.SerializedName
import com.vistony.app.Screen.Generic.Recursos.Data
import java.io.File
import java.io.FileOutputStream
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
    val DocEntry: String = "",
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
    val evidences: SnapshotStateList<Uri> = mutableStateListOf(),
    val observations: String = "",
    val endTime: LocalDateTime? = null,
    val paradaDocEntry: String = "",
    val lineTec: String = "",
    val initialHour: LocalDateTime? = LocalDateTime.now(),
    val finalHour: LocalDateTime? = LocalDateTime.now()// String momentaneo, si s etiene que cambiar a LocalDate u otro hazlo
)
data class Activity2Dto(
    val u_OT: String,
    val u_description_OT: String,
    val u_unidad_medida_OT: String,
    val u_cantidad_OT: String,
    val u_userId: String,
    val u_userName: String,
    val u_userPosition: String,
    val u_area: String,
    val u_machine: String,
    val u_equipment: String,
    val u_reason: String,
    val u_description: String,
    val u_actionTaken: String,
    val evidencia: List<Evidence>, // or appropriate type
    val u_observations: String,
    val u_endTime: String, // parse to LocalDateTime during mapping
    val u_LineTec: String,
    val u_InitialHour: String,
    val u_FinalHour: String
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun toActivity2(docEntry: String, context: Context): Activity2 {
        val data = Data()
        val reasonParts = u_reason.split("-", limit = 2)
        val namePart = reasonParts[0] // la primera parte antes del "-"
        val descriptionPart = if (reasonParts.size > 1) reasonParts[1] else ""

        return Activity2(
            DocEntry = docEntry,
            OT = u_OT,
            description_OT = u_description_OT,
            unidad_medida_OT = u_unidad_medida_OT,
            cantidad_OT = u_cantidad_OT,
            userId = u_userId,
            userName = u_userName,
            userPosition = u_userPosition,
            area = u_area,
            machine = Machine(name = u_machine), // assuming Machine has a name property
            equipment = Equipment(name = u_equipment), // similarly for Equipment
            reason = if (namePart == "Otro") FailureType(id = 7, name = namePart, description = descriptionPart) else data.optionsFalla.find { it.first.name == namePart }?.first ?: FailureType(),
            description = u_description,
            actionTaken = u_actionTaken,
            evidences = convertirEvidenciasAUri(context,evidencia).toMutableStateList(),
            observations = u_observations,
            endTime = if (u_endTime.isNotBlank()) LocalDateTime.parse(u_endTime) else null, // parse string to LocalDateTime
            lineTec = u_LineTec,
            initialHour = if (u_InitialHour.isNotBlank()) LocalDateTime.parse(u_InitialHour) else null,
            finalHour = if (u_FinalHour.isNotBlank()) LocalDateTime.parse(u_FinalHour) else null
        )
    }
}
fun convertirEvidenciasAUri(context: Context, evidences: List<Evidence>): List<Uri> {
    return evidences.mapNotNull { evidence ->
        try {
            /*val bytes = Base64.decode(evidence.base64, Base64.DEFAULT)
            val file = File(context.cacheDir, evidence.fileName)
            val fos = FileOutputStream(file)
            fos.write(bytes)
            fos.close()
            Uri.fromFile(file)*/
            Uri.parse(evidence.link)
        } catch (e: Exception) {
            null
        }
    }
}

data class Activity3 @RequiresApi(Build.VERSION_CODES.O) constructor(
    @SerializedName("DocEntry") val DocEntry: String = "",
    @SerializedName("u_startTime") val startTime: LocalDateTime? = LocalDateTime.now(),
    @SerializedName("u_OT") val OT: String = "",
    @SerializedName("u_description_OT") val description_OT: String = "",
    @SerializedName("u_unidad_medida_OT") val unidad_medida_OT: String = "",
    @SerializedName("u_cantidad_OT") val cantidad_OT: String = "",
    @SerializedName("u_userId") val userId: String = "",
    @SerializedName("u_userName") val userName: String = "",
    @SerializedName("u_userPosition") val userPosition: String = "",
    @SerializedName("u_area") var area: String = "",
    @SerializedName("u_machine") val machine: Machine = Machine(),
    @SerializedName("u_equipment") val equipment: Equipment = Equipment(),
    @SerializedName("u_reason") val reason: FailureType = FailureType(),
    @SerializedName("u_description") val description: String = "",
    @SerializedName("u_actionTaken") val actionTaken: String = "",
    @SerializedName("evidencia") val evidences: SnapshotStateList<Uri> = mutableStateListOf(),
    @SerializedName("u_observations") val observations: String = "",
    @SerializedName("u_endTime") val endTime: LocalDateTime? = null,
    @SerializedName("u_paradaDocEntry") val paradaDocEntry: String = "",
    @SerializedName("u_lineTec") val lineTec: String = "",
    @SerializedName("u_initialHour") val initialHour: LocalDateTime? = LocalDateTime.now(),
    @SerializedName("u_finalHour") val finalHour: LocalDateTime? = LocalDateTime.now()
)



data class FailureType(
    val id: Int = 0,
    val name: String = "",
    val description: String = ""
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

data class listActivityRequest(
    val id: Int,
    val role: String,
    val fecha_inicio: String,
    val fecha_fin: String
)

data class listActivityResponse(
    val statusCode: Int,
    val message: String,
    val data: List<semiActivity>
)
data class DetaleResponse(
    val success: Boolean,
    val message: String,
    val data: List<Activity2Dto>
)

data class semiActivity(
    val DocEntry: String ="",
    val U_OT: String = "",
    val U_description_OT: String ="",
    val U_unidad_medida_OT: String ="",
    val U_cantidad_OT: String = "",
    val U_userId: String = "",
    val U_userName: String = "",
    val U_userPosition: String = "",
    val U_area: String = "",
    val U_machine: String = "",
    val U_equipment: String = "",
    val U_LineTec: String = "",
    val U_InitialHour: String? = null,
    val U_FinalHour: String? = null,
    val U_imageUrls: List<String> = emptyList()
)

data class ResponseCreated(
    val success: Boolean = false,
    val message: String,
    val data: List<Any>
)

data class ResponseUpdate(
    val success: Boolean = false,
    val message: String,
    val data: String
)

data class DataCreate(
    val DocEntry: String
)


data class ActivityRequest(
    val OT: String,
    val description_OT: String,
    val unidad_medida_OT: String,
    val cantidad_OT: String,
    val userId: String,
    val userName: String,
    val userPosition: String,
    val area: String,
    val machine: String,
    val equipment: String,
    val initialHour: String?,
)
data class UpdateActividadRequest(
    val reason: String,
    val description: String,
    val actionTaken: String,
    val evidences: List<Evidence>,
    val observations: String,
    val endTime: String,
    val paradaDocEntry: String,
    val lineTec: String,
    val finalHour: String?
)

data class Evidence(
    val fileName: String,
    val fileType: String,
    val base64: String,
    val link: String = ""
)


data class OTRequest(
    val barra: String = ""
)

data class OTResponse(
    val success: Boolean,
    val message: String?,
    val data: List<OTItem>
)

data class OTItem(
    val ItemName: String = "",
    val UomName: String = "",
    val PlannedQty: String = ""
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