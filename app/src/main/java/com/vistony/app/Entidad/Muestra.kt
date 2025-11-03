package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

// 1. Información General (Cabecera)
data class MuestraCabecera(
    @SerializedName("DocEntry") val id: String = "",
    @SerializedName("U_Fec_Register") val fechaRegistro: String = "",
    @SerializedName("U_ItemCode") val codigo: String = "",
    @SerializedName("U_Lote") val lote: String = "",
    @SerializedName("U_Embalaje") val embalaje: String = "",
    @SerializedName("U_Maquina") val maquina: String = "",
    @SerializedName("U_Turno") val turno: String = "",
    @SerializedName("U_Descripcion") val producto: String = "",
    @SerializedName("U_User_Register") val auxiliar: String = "",
    @SerializedName("U_EncargadorProd") val encargadoProduccion: String = "",
    @SerializedName("U_Estado") val estado: String = "En Proceso" // En Proceso, Completado, Cancelado
)

// 2. Formulación de Material Empleado
data class MaterialEmpleado(
    @SerializedName("id") val id: String = "",
    @SerializedName("muestraId") val muestraId: String = "",
    @SerializedName("material") val material: String = "",
    @SerializedName("marca") val marca: String = "",
    @SerializedName("codigo") val codigo: String = "",
    @SerializedName("lote") val lote: String = ""
)

// 3. Inspección Dimensional
data class InspeccionDimensional(
    @SerializedName("id") val id: String = "",
    @SerializedName("muestraId") val muestraId: String = "",
    @SerializedName("horaInspeccion") val horaInspeccion: String = "",
    @SerializedName("temperaturaChiller") val temperaturaChiller: String = "",
    @SerializedName("temperaturaCiclo") val temperaturaCiclo: String = "",
    @SerializedName("numeroCavidad") val numeroCavidad: String = "",
    @SerializedName("peso") val peso: String = "",
    @SerializedName("diametroRoscaMedida1") val diametroRoscaMedida1: String = "",
    @SerializedName("diametroRoscaMedida2") val diametroRoscaMedida2: String = "",
    @SerializedName("alturaBocaMedida1") val alturaBocaMedida1: String = "",
    @SerializedName("alturaBocaMedida2") val alturaBocaMedida2: String = "",
    @SerializedName("alturaBocaMedida3") val alturaBocaMedida3: String = "",
    @SerializedName("alturaBocaMedida4") val alturaBocaMedida4: String = "",
    @SerializedName("diametroPrecintoMedida1") val diametroPrecintoMedida1: String = "",
    @SerializedName("diametroPrecintoMedida2") val diametroPrecintoMedida2: String = "",
    @SerializedName("diametroPrecintoMedida3") val diametroPrecintoMedida3: String = "",
    @SerializedName("alturaTotalMedida1") val alturaTotalMedida1: String = "",
    @SerializedName("alturaTotalMedida2") val alturaTotalMedida2: String = "",
    @SerializedName("diametroInternoMedida1") val diametroInternoMedida1: String = "",
    @SerializedName("diametroInternoMedida2") val diametroInternoMedida2: String = "",
    @SerializedName("observacion") val observacion: String = ""
)

// 4. Check List de Inspección
data class CheckListInspeccion(
    @SerializedName("id") val id: String = "",
    @SerializedName("muestraId") val muestraId: String = "",
    @SerializedName("horaCheckList") val horaCheckList: String = "",
    @SerializedName("testeado") val testeado: String = "", // Aprobado, Observado, Rechazado
    @SerializedName("estabilidad") val estabilidad: String = "",
    @SerializedName("tonalidad") val tonalidad: String = "",
    @SerializedName("visorUniforme") val visorUniforme: String = "",
    @SerializedName("correctaCostura") val correctaCostura: String = "",
    @SerializedName("libreOvulamiento") val libreOvulamiento: String = "",
    @SerializedName("libreContaminacion") val libreContaminacion: String = "",
    @SerializedName("observacion") val observacion: String = ""
)

// 5. Evaluación de la Producción
data class EvaluacionProduccion(
    @SerializedName("id") val id: String = "",
    @SerializedName("muestraId") val muestraId: String = "",
    @SerializedName("estado") val estado: String = "",
    @SerializedName("paletas") val paletas: String = "", // Aprobado, Observado, Rechazado
    @SerializedName("bolsas") val bolsas: String = "",
    @SerializedName("criteriosEvaluacion") val criteriosEvaluacion: String = ""
)

// Entidad principal que agrupa todo
data class MuestraCompleta(
    @SerializedName("cabecera") val cabecera: MuestraCabecera,
    @SerializedName("materiales") val materiales: List<MaterialEmpleado> = emptyList(),
    @SerializedName("inspeccionesDimensionales") val inspeccionesDimensionales: List<InspeccionDimensional> = emptyList(),
    @SerializedName("checkLists") val checkLists: List<CheckListInspeccion> = emptyList(),
    @SerializedName("evaluacionProduccion") val evaluacionProduccion: EvaluacionProduccion? = null,
    // Campos de completitud
    @SerializedName("materialesCompletado") val materialesCompletado: Boolean = false,
    @SerializedName("inspeccionesCompletado") val inspeccionesCompletado: Boolean = false,
    @SerializedName("checkListsCompletado") val checkListsCompletado: Boolean = false,
    @SerializedName("evaluacionCompletado") val evaluacionCompletado: Boolean = false,
    @SerializedName("fechaUltimaActualizacion") val fechaUltimaActualizacion: String = ""
) {
    // Función para calcular el porcentaje de completitud
    fun getCompletitudPorcentaje(): Int {
        var completado = 0
        var total = 4 // Total de secciones
        
        if (materialesCompletado) completado++
        if (inspeccionesCompletado) completado++
        if (checkListsCompletado) completado++
        if (evaluacionCompletado) completado++
        
        return (completado * 100) / total
    }
    
    // Función para obtener el estado general
    fun getEstadoGeneral(): String {
        // Usar el estado de la cabecera que se actualiza automáticamente
        return cabecera.estado
    }
    
    // Función para obtener secciones pendientes
    fun getSeccionesPendientes(): List<String> {
        val pendientes = mutableListOf<String>()
        if (!materialesCompletado) pendientes.add("Materiales")
        if (!inspeccionesCompletado) pendientes.add("Inspecciones")
        if (!checkListsCompletado) pendientes.add("CheckLists")
        if (!evaluacionCompletado) pendientes.add("Evaluación")
        return pendientes
    }
}

// Enums para criterios de evaluación
enum class CriterioEvaluacion(val displayName: String) {
    APROBADO("Aprobado"),
    OBSERVADO("Observado"),
    RECHAZADO("Rechazado")
}

// Request para crear muestra (solo cabecera)
data class MuestraCreateRequest(
    @SerializedName("code_prod") val codeProd: String,
    @SerializedName("producto") val producto: String,
    @SerializedName("lote") val lote: String,
    @SerializedName("embalaje") val embalaje: String,
    @SerializedName("user_register") val userRegister: String,
    @SerializedName("fec_register") val fecRegister: String,
    @SerializedName("turno") val turno: String,
    @SerializedName("maquina") val maquina: String,
    @SerializedName("encargado_prod") val encargadoProd: String,
    @SerializedName("estado") val estado: String
)

// Response para crear muestra
data class MuestraCreateResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: String // ID de la muestra creada
)

// Request para crear CheckList
data class CheckListCreateRequest(
    @SerializedName("muestraId") val muestraId: String,
    @SerializedName("horaCheckList") val horaCheckList: String,
    @SerializedName("testeado") val testeado: String,
    @SerializedName("estabilidad") val estabilidad: String,
    @SerializedName("tonalidad") val tonalidad: String,
    @SerializedName("visorUniforme") val visorUniforme: String,
    @SerializedName("correctaCostura") val correctaCostura: String,
    @SerializedName("libreOvulamiento") val libreOvulamiento: String,
    @SerializedName("libreContaminacion") val libreContaminacion: String,
    @SerializedName("observacion") val observacion: String
)

// Response para crear CheckList
data class CheckListCreateResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any?
)

// Requests para API
data class MuestraRequest(
    @SerializedName("cabecera") val cabecera: MuestraCabecera,
    @SerializedName("materiales") val materiales: List<MaterialEmpleado>,
    @SerializedName("inspeccionesDimensionales") val inspeccionesDimensionales: List<InspeccionDimensional>,
    @SerializedName("checkLists") val checkLists: List<CheckListInspeccion>,
    @SerializedName("evaluacionProduccion") val evaluacionProduccion: EvaluacionProduccion?
)

data class MuestraResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<MuestraCabecera> // ANTES MuestraCompleta
)
data class CabeceraRequest(
    val id: Int,
    val role: String,
    val fecha_inicio: String,
    val fecha_fin: String,
)

// O TAMBIEN DETALLE
data class MuestraCompletaResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<MuestraCompleta>
)

data class PostMuestraResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)

// CheckList del API
data class CheckListAPI(
    @SerializedName("u_HoraCheckList") val horaCheckList: String? = "",
    @SerializedName("u_Testeado") val testeado: String? = "",
    @SerializedName("u_Estabilidad") val estabilidad: String? = "",
    @SerializedName("u_Tonalidad") val tonalidad: String? = "",
    @SerializedName("u_VisorUniforme") val visorUniforme: String? = "",
    @SerializedName("u_CorrectaCostura") val correctaCostura: String? = "",
    @SerializedName("u_LibreOvulamiento") val libreOvulamiento: String? = "",
    @SerializedName("u_LibreContaminacion") val libreContaminacion: String? = "",
    @SerializedName("u_Observacion") val observacion: String? = ""
)

// Response del detalle de muestra del API
data class MuestraDetalleResponse(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: MuestraDetalleData = MuestraDetalleData()
)

data class MuestraDetalleData(
    @SerializedName("docEntry") val docEntry: String = "",
    @SerializedName("u_ItemCode") val itemCode: String = "",
    @SerializedName("u_Descripcion") val descripcion: String = "",
    @SerializedName("u_Lote") val lote: String = "",
    @SerializedName("u_Embalaje") val embalaje: String = "",
    @SerializedName("u_User_Register") val userRegister: String = "",
    @SerializedName("u_Fec_Register") val fecRegister: String = "",
    @SerializedName("u_Turno") val turno: String = "",
    @SerializedName("u_Maquina") val maquina: String = "",
    @SerializedName("u_EncargadorProd") val encargadorProd: String = "",
    @SerializedName("u_Estado") val estado: String = "",
    @SerializedName("checkList") val checkList: List<CheckListAPI> = emptyList(),
    @SerializedName("materialEmpleado") val materialEmpleado: List<Any> = emptyList()
)

// Productos para el dropdown
data class ProductoItem(
    @SerializedName("OT") val ot: String = "",
    @SerializedName("Producto") val producto: String = ""
)

data class ProductoResponseTemp(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: List<ProductoItem> = emptyList()
)

// Consulta de producto por código
data class ConsultaProductoItem(
    @SerializedName("Codigo") val codigo: String = "",
    @SerializedName("Producto") val descripcion: String = "",
    @SerializedName("Lote") val lote: String = "",
    @SerializedName("Maquina") val maquina: String = "",
    @SerializedName("Embalaje") val embalaje: String,
    @SerializedName("Encargado_produccion") val encargadoProduccion: String = ""
)

data class ConsultaProductoResponse(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: List<ConsultaProductoItem> = emptyList()
)
