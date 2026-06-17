package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

// 1. Información General (Cabecera)
data class MuestraCabecera(
    @SerializedName("DocEntry") val id: String = "",
    @SerializedName("U_Fec_Register") val fechaRegistro: String = "",
    @SerializedName("U_ItemCode") val codigo: String = "",
    @SerializedName("U_CodigoMaquina") val codigoMaquina: String = "",
    @SerializedName("U_Lote") val lote: String = "",
    @SerializedName("U_Embalaje") val embalaje: String = "",
    @SerializedName("U_Maquina") val maquina: String = "",
    @SerializedName("U_Turno") val turno: String = "",
    @SerializedName("U_Descripcion") val producto: String = "",
    @SerializedName("U_User_Register") val auxiliar: String = "",
    @SerializedName("U_EncargadorProd") val encargadoProduccion: String = "",
    @SerializedName("U_Estado") val estado: String = "En Proceso", // En Proceso, Completado, Cancelado
    @SerializedName("U_Tipo") val tipo: String = "Envase" // Envase | Tapa
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
    // Campos Envase
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
    // Campos Tapa
    @SerializedName("diametroInternoRoscaTapa") val diametroInternoRoscaTapa: String? = null,
    @SerializedName("diametroExternoRoscaTapa") val diametroExternoRoscaTapa: String? = null,
    @SerializedName("diametroInternoTrinquete") val diametroInternoTrinquete: String? = null,
    @SerializedName("observacion") val observacion: String = ""
)

// 4. Check List de Inspección
data class CheckListInspeccion(
    @SerializedName("id") val id: String = "",
    @SerializedName("muestraId") val muestraId: String = "",
    @SerializedName("horaCheckList") val horaCheckList: String = "",
    // Criterios Envase
    @SerializedName("testeado") val testeado: String? = null, // Aprobado, Observado, Rechazado
    @SerializedName("estabilidad") val estabilidad: String? = null,
    @SerializedName("tonalidad") val tonalidad: String? = null,
    @SerializedName("visorUniforme") val visorUniforme: String? = null,
    @SerializedName("correctaCostura") val correctaCostura: String? = null,
    @SerializedName("libreOvulamiento") val libreOvulamiento: String? = null,
    @SerializedName("libreContaminacion") val libreContaminacion: String? = null,
    // Criterios Tapa
    @SerializedName("funcionalidadAcabadoTapas") val funcionalidadAcabadoTapas: String? = null,
    @SerializedName("conformidadTroquelado") val conformidadTroquelado: String? = null,
    @SerializedName("correctoEnlainado") val correctoEnlainado: String? = null,
    @SerializedName("observacion") val observacion: String = ""
)

// 5. Evaluación de la Producción
data class EvaluacionProduccion(
    @SerializedName("id") val id: String = "",
    @SerializedName("muestraId") val muestraId: String = "",
    @SerializedName("equipo") val equipo: String = "",
    @SerializedName("estado") val estado: String = "",
    @SerializedName("paletasAprobadas") val paletasAprobadas: String = "",
    @SerializedName("paletasObservadas") val paletasObservadas: String = "",
    @SerializedName("paletasRechazadas") val paletasRechazadas: String = "",
    @SerializedName("bolsasAprobadas") val bolsasAprobadas: String = "",
    @SerializedName("bolsasObservadas") val bolsasObservadas: String = "",
    @SerializedName("bolsasRechazadas") val bolsasRechazadas: String = "",
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
    @SerializedName("codMaquina") val codigoMaquina: String,
    @SerializedName("encargado_prod") val encargadoProd: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("tipo") val tipo: String // "Envase" | "Tapa"
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
    // Criterios Envase (null cuando tipo=Tapa)
    @SerializedName("testeado") val testeado: String?,
    @SerializedName("estabilidad") val estabilidad: String?,
    @SerializedName("tonalidad") val tonalidad: String?,
    @SerializedName("visorUniforme") val visorUniforme: String?,
    @SerializedName("correctaCostura") val correctaCostura: String?,
    @SerializedName("libreOvulamiento") val libreOvulamiento: String?,
    @SerializedName("libreContaminacion") val libreContaminacion: String?,
    // Criterios Tapa (null cuando tipo=Envase)
    @SerializedName("funcionalidadAcabadoTapas") val funcionalidadAcabadoTapas: String?,
    @SerializedName("conformidadTroquelado") val conformidadTroquelado: String?,
    @SerializedName("correctoEnlainado") val correctoEnlainado: String?,
    @SerializedName("observacion") val observacion: String
)

// Response para crear CheckList
data class CheckListCreateResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any?
)

// Request para crear Inspeccion Dimensional
data class InspeccionDimensionalCreateRequest(
    @SerializedName("muestraId") val muestraId: String,
    @SerializedName("horaInspeccion") val horaInspeccion: String,
    // Campos comunes (siempre presentes)
    @SerializedName("temperaturaChiller") val temperaturaChiller: String,
    @SerializedName("temperaturaCiclo") val temperaturaCiclo: String,
    @SerializedName("numeroCavidad") val numeroCavidad: String,
    @SerializedName("peso") val peso: String,
    // Campos Envase (null cuando tipo=Tapa)
    @SerializedName("diametroRoscaMedida1") val diametroRoscaMedida1: String?,
    @SerializedName("diametroRoscaMedida2") val diametroRoscaMedida2: String?,
    @SerializedName("alturaBocaMedida1") val alturaBocaMedida1: String?,
    @SerializedName("alturaBocaMedida2") val alturaBocaMedida2: String?,
    @SerializedName("alturaBocaMedida3") val alturaBocaMedida3: String?,
    @SerializedName("alturaBocaMedida4") val alturaBocaMedida4: String?,
    @SerializedName("diametroPrecintoMedida1") val diametroPrecintoMedida1: String?,
    @SerializedName("diametroPrecintoMedida2") val diametroPrecintoMedida2: String?,
    @SerializedName("diametroPrecintoMedida3") val diametroPrecintoMedida3: String?,
    @SerializedName("alturaTotalMedida1") val alturaTotalMedida1: String?,
    @SerializedName("alturaTotalMedida2") val alturaTotalMedida2: String?,
    @SerializedName("diametroInternoMedida1") val diametroInternoMedida1: String?,
    @SerializedName("diametroInternoMedida2") val diametroInternoMedida2: String?,
    // Campos Tapa (null cuando tipo=Envase)
    @SerializedName("diametroInternoRoscaTapa") val diametroInternoRoscaTapa: String?,
    @SerializedName("diametroExternoRoscaTapa") val diametroExternoRoscaTapa: String?,
    @SerializedName("diametroInternoTrinquete") val diametroInternoTrinquete: String?,
    @SerializedName("observacion") val observacion: String
)

// Response para crear Inspeccion Dimensional
data class InspeccionDimensionalCreateResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any?
)

// Request para crear Material Empleado
data class MaterialEmpleadoCreateRequest(
    @SerializedName("material") val material: String,
    @SerializedName("marca") val marca: String,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("lote") val lote: String,
    @SerializedName("fec_reg") val fecReg: String
)

// Response para crear Material Empleado
data class MaterialEmpleadoCreateResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any?
)

// Request para crear Evaluación de Producción
data class EvaluacionProduccionCreateRequest(
    @SerializedName("equipo") val equipo: String,
    @SerializedName("estado") val estado: String = "",
    @SerializedName("paletasAprobadas") val paletasAprobadas: String,
    @SerializedName("paletasObservadas") val paletasObservadas: String,
    @SerializedName("paletasRechazadas") val paletasRechazadas: String,
    @SerializedName("bolsasAprobadas") val bolsasAprobadas: String,
    @SerializedName("bolsasObservadas") val bolsasObservadas: String,
    @SerializedName("bolsasRechazadas") val bolsasRechazadas: String,
    @SerializedName("criteriosEvaluacion") val criteriosEvaluacion: String,
    @SerializedName("fec_reg") val fecReg: String
)

// Response para crear Evaluación de Producción
data class EvaluacionProduccionCreateResponse(
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

// Evaluación de la Producción del API
data class EvaluacionProduccionAPI(
    @SerializedName("U_equipo") val equipo: String? = "",
    @SerializedName("U_estado") val estado: String? = "",
    @SerializedName("U_paletasAprobadas") val paletasAprobadas: String? = "",
    @SerializedName("U_paletasObservadas") val paletasObservadas: String? = "",
    @SerializedName("U_paletasRechazadas") val paletasRechazadas: String? = "",
    @SerializedName("U_bolsasAprobadas") val bolsasAprobadas: String? = "",
    @SerializedName("U_bolsasObservadas") val bolsasObservadas: String? = "",
    @SerializedName("U_bolsasRechazadas") val bolsasRechazadas: String? = "",
    @SerializedName("U_criteriosEvaluacion") val criteriosEvaluacion: String? = "",
    @SerializedName("U_fec_reg") val fecReg: String? = ""
)



// Inspeccion Dimensional del API
data class InspeccionDimensionalAPI(
    @SerializedName("U_horaInspeccion") val horaInspeccion: String? = "",
    @SerializedName("U_temperaturaChiller") val temperaturaChiller: String? = "",
    @SerializedName("U_temperaturaCiclo") val temperaturaCiclo: String? = "",
    @SerializedName("U_numeroCavidad") val numeroCavidad: String? = "",
    @SerializedName("U_peso") val peso: String? = "",
    // Campos Envase
    @SerializedName("U_alturaBocaMedida1") val alturaBocaMedida1: String? = null,
    @SerializedName("U_alturaBocaMedida2") val alturaBocaMedida2: String? = null,
    @SerializedName("U_alturaBocaMedida3") val alturaBocaMedida3: String? = null,
    @SerializedName("U_alturaBocaMedida4") val alturaBocaMedida4: String? = null,
    @SerializedName("U_alturaTotalMedida1") val alturaTotalMedida1: String? = null,
    @SerializedName("U_alturaTotalMedida2") val alturaTotalMedida2: String? = null,
    @SerializedName("U_diametroInternoMedida1") val diametroInternoMedida1: String? = null,
    @SerializedName("U_diametroInternoMedida2") val diametroInternoMedida2: String? = null,
    @SerializedName("U_diametroPrecintoMedida1") val diametroPrecintoMedida1: String? = null,
    @SerializedName("U_diametroPrecintoMedida2") val diametroPrecintoMedida2: String? = null,
    @SerializedName("U_diametroPrecintoMedida3") val diametroPrecintoMedida3: String? = null,
    @SerializedName("U_diametroRoscaMedida1") val diametroRoscaMedida1: String? = null,
    @SerializedName("U_diametroRoscaMedida2") val diametroRoscaMedida2: String? = null,
    // Campos Tapa
    @SerializedName("U_diametroInternoRoscaTapa") val diametroInternoRoscaTapa: String? = null,
    @SerializedName("U_diametroExternoRoscaTapa") val diametroExternoRoscaTapa: String? = null,
    @SerializedName("U_diametroInternoTrinquete") val diametroInternoTrinquete: String? = null,
    @SerializedName("U_observacion") val observacion: String? = ""
)

// Material Empleado del API
data class MaterialEmpleadoAPI(
    @SerializedName("U_material") val material: String? = "",
    @SerializedName("U_marca") val marca: String? = "",
    @SerializedName("U_codigo") val codigo: String? = "",
    @SerializedName("U_lote") val lote: String? = "",
    @SerializedName("U_fec_reg") val fecReg: String? = ""
)

// CheckList del API
data class CheckListAPI(
    @SerializedName("U_horaCheckList") val horaCheckList: String? = "",
    // Criterios Envase
    @SerializedName("U_testeado") val testeado: String? = null,
    @SerializedName("U_estabilidad") val estabilidad: String? = null,
    @SerializedName("U_tonalidad") val tonalidad: String? = null,
    @SerializedName("U_visorUniforme") val visorUniforme: String? = null,
    @SerializedName("U_correctaCostura") val correctaCostura: String? = null,
    @SerializedName("U_libreOvulamiento") val libreOvulamiento: String? = null,
    @SerializedName("U_libreContaminacion") val libreContaminacion: String? = null,
    // Criterios Tapa
    @SerializedName("U_funcionalidadAcabadoTapas") val funcionalidadAcabadoTapas: String? = null,
    @SerializedName("U_conformidadTroquelado") val conformidadTroquelado: String? = null,
    @SerializedName("U_correctoEnlainado") val correctoEnlainado: String? = null,
    @SerializedName("U_observacion") val observacion: String? = ""
)

// Response del detalle de muestra del API
data class MuestraDetalleResponse(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: List<MuestraDetalleData> = emptyList()
)

data class MuestraDetalleData(
    @SerializedName("DocEntry") val docEntry: String = "",
    @SerializedName("U_ItemCode") val itemCode: String = "",
    @SerializedName("U_Descripcion") val descripcion: String = "",
    @SerializedName("U_Lote") val lote: String = "",
    @SerializedName("U_CodigoMaquina") val codigoMaquina: String = "",
    @SerializedName("U_Embalaje") val embalaje: String = "",
    @SerializedName("U_User_Register") val userRegister: String = "",
    @SerializedName("U_Fec_Register") val fecRegister: String = "",
    @SerializedName("U_Turno") val turno: String = "",
    @SerializedName("U_Maquina") val maquina: String = "",
    @SerializedName("U_EncargadorProd") val encargadorProd: String = "",
    @SerializedName("U_Estado") val estado: String = "",
    @SerializedName("U_Tipo") val tipo: String = "Envase",
    @SerializedName("CheckList") val checkList: List<CheckListAPI> = emptyList(),
    @SerializedName("MaterialEmpleado") val materialEmpleado: List<MaterialEmpleadoAPI> = emptyList(),
    @SerializedName("InspeccionDimencional") val inspeccionDimencional: List<InspeccionDimensionalAPI> = emptyList(),
    @SerializedName("Evaluacion") val evaluacion: List<EvaluacionProduccionAPI> = emptyList()
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

// Consulta de producto por código (OT)
data class ConsultaProductoItem(
    @SerializedName("CodProd") val codigo: String = "",
    @SerializedName("Producto") val descripcion: String = "",
    @SerializedName("Lote") val lote: String = "",
    @SerializedName("CodMaquina") val codMaquina: String = "",
    @SerializedName("Maquina") val maquina: String = "",
    @SerializedName("Embalaje") val embalaje: String,
    @SerializedName("Tipo") val tipo: String = "Envase" // "Envase" | "Tapa" — viene del backend
)

data class ConsultaProductoResponse(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: List<ConsultaProductoItem> = emptyList()
)

// Consulta de producto por código
data class ConsultaProductoMuestraItem(
    @SerializedName("CodProd") val codigo: String = "",
    @SerializedName("Producto") val descripcion: String = "",
    @SerializedName("Lote") val lote: String = "",
    @SerializedName("Maquina") val maquina: String = "",
    @SerializedName("Embalaje") val embalaje: String,
    @SerializedName("Encargado_produccion") val encargadoProduccion: String = "",
    @SerializedName("Muestras") val muestras: List<String> = emptyList()
)

data class ConsultaProductoMuestraResponse(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: List<ConsultaProductoMuestraItem> = emptyList()
)

// Especificaciones de Soplado
data class EspecificacionSopladoData(
    @SerializedName("U_Producto") val producto: String = "",
    @SerializedName("U_Codigo") val codigo: String = "",
    // Campos comunes (Envase y Tapa)
    @SerializedName("U_PesoMin") val pesoMin: String = "",
    @SerializedName("U_PesoMax") val pesoMax: String = "",
    @SerializedName("U_AlturaTotalMin") val alturaTotalMin: String = "",
    @SerializedName("U_AlturaTotalMax") val alturaTotalMax: String = "",
    // Campos solo Envase
    @SerializedName("U_DRoscaMain") val diametroRoscaMin: String = "",
    @SerializedName("U_DRoscaMax") val diametroRoscaMax: String = "",
    @SerializedName("U_AlturaBocaMin") val alturaBocaMin: String = "",
    @SerializedName("U_AlturaBocaMax") val alturaBocaMax: String = "",
    @SerializedName("U_DPrecintoMin") val diametroPrecintoMin: String = "",
    @SerializedName("U_DPrecintoMax") val diametroPrecintoMax: String = "",
    @SerializedName("U_DInternoMin") val diametroInternoMin: String = "",
    @SerializedName("U_DInternoMax") val diametroInternoMax: String = "",
    // Campos solo Tapa
    @SerializedName("U_DIntRoscaTapaMin") val diametroInternoRoscaTapaMin: String = "",
    @SerializedName("U_DIntRoscaTapaMax") val diametroInternoRoscaTapaMax: String = "",
    @SerializedName("U_DExtRoscaTapaMin") val diametroExternoRoscaTapaMin: String = "",
    @SerializedName("U_DExtRoscaTapaMax") val diametroExternoRoscaTapaMax: String = "",
    @SerializedName("U_DIntTrinqueteMin") val diametroInternoTrinqueteMin: String = "",
    @SerializedName("U_DIntTrinqueteMax") val diametroInternoTrinqueteMax: String = ""
)

data class EspecificacionSopladoResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: EspecificacionSopladoData?
)

// Registro de Llegada
data class RegistroLlegada(
    @SerializedName("DocEntry") val id: String = "",
    @SerializedName("U_OrdenFabricacion") val numeroOrdenFabricacion: String = "",
    @SerializedName("U_NMuestra") val numeroMuestra: String = "",
    @SerializedName("U_CodProducto") val codigoProducto: String = "",
    @SerializedName("U_DesProducto") val descripcionProducto: String = "",
    @SerializedName("U_FechaRegistro") val fechaRegistro: String = "",
    @SerializedName("U_UserRegister") val userRegister: String = "",
    @SerializedName("U_Name") val nombre: String = "",
    @SerializedName("U_Estado") val estado: String = "PENDIENTE",
    @SerializedName("FechaApro") val fechaAprobacion: String? = null,
    @SerializedName("U_FechaConfirmacion") val fechaConfirmacion: String? = null
)

// Request para crear Registro de Llegada
data class RegistroLlegadaCreateRequest(
    @SerializedName("u_OrdenFabricacion") val ordenFabricacion: String,
    @SerializedName("u_NMuestra") val nMuestra: String,
    @SerializedName("u_CodProducto") val codProducto: String,
    @SerializedName("u_DesProducto") val desProducto: String,
    @SerializedName("u_FechaRegistro") val fechaRegistro: String,
    @SerializedName("u_UserRegister") val userRegister: String
)

// Response para crear Registro de Llegada
data class RegistroLlegadaCreateResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any?
)

// Response para obtener lista de Registros de Llegada
data class RegistroLlegadaResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<RegistroLlegada>
)

// Request para confirmar recepción de muestra
data class ConfirmarRecepcionRequest(
    @SerializedName("u_OrdenFabricacion") val ordenFabricacion: String,
    @SerializedName("u_NMuestra") val nMuestra: String,
    @SerializedName("u_FechaConfirmacion") val fechaConfirmacion: String
)

// Data del response de confirmación
data class ConfirmarRecepcionData(
    @SerializedName("U_OrdenFabricacion") val ordenFabricacion: String,
    @SerializedName("U_NMuestra") val nMuestra: String,
    @SerializedName("U_Estado") val estado: String,
    @SerializedName("U_FechaConfirmacion") val fechaConfirmacion: String,
    @SerializedName("U_UserConfirma") val userConfirma: String
)

// Response para confirmar recepción
data class ConfirmarRecepcionResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ConfirmarRecepcionData?
)

// --- NUEVOS MODELOS PARA MUESTRAS DE PRODUCCIÓN ---

data class MuestraProduccion(
    @SerializedName("code") val code: String,
    @SerializedName("counter") val counter: Int,
    @SerializedName("dateRegister") val dateRegister: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("docEntry") val docEntry: Int,
    @SerializedName("isFinish") val isFinish: Boolean,
    @SerializedName("lote") val lote: String,
    @SerializedName("ordenEnvase") val ordenEnvase: String,
    @SerializedName("ordenFabricacion") val ordenFabricacion: String,
    @SerializedName("status") val status: String,
    @SerializedName("userRegister") val userRegister: String,
    @SerializedName("version") val version: String
)

data class MuestraProduccionResponse(
    @SerializedName("data") val data: List<MuestraProduccion>
)

// Detalle de una muestra de producción (pantalla de detalle de "Mis OF")
data class MuestraProduccionDetalle(
    @SerializedName("code") val code: String,
    @SerializedName("counter") val counter: Int,
    @SerializedName("dateEndAnalysis") val dateEndAnalysis: String?,
    @SerializedName("dateRegister") val dateRegister: String,
    @SerializedName("dateStartAnalysis") val dateStartAnalysis: String?,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("docEntry") val docEntry: Int,
    @SerializedName("isFinish") val isFinish: Boolean,
    @SerializedName("lote") val lote: String,
    @SerializedName("observation") val observation: String?,
    @SerializedName("ordenEnvase") val ordenEnvase: String,
    @SerializedName("ordenFabricacion") val ordenFabricacion: String,
    @SerializedName("reason") val reason: String?,
    @SerializedName("status") val status: String,
    @SerializedName("timeline") val timeline: List<MuestraProduccionTimelineItem> = emptyList(),
    @SerializedName("type") val type: String,
    @SerializedName("typeReject") val typeReject: String?,
    @SerializedName("userEndAnalysis") val userEndAnalysis: String?,
    @SerializedName("userRegister") val userRegister: String,
    @SerializedName("userStartAnalysis") val userStartAnalysis: String?,
    @SerializedName("version") val version: String
)

data class MuestraProduccionTimelineItem(
    @SerializedName("counter") val counter: Int,
    @SerializedName("dateEndAnalysis") val dateEndAnalysis: String?,
    @SerializedName("dateRegister") val dateRegister: String,
    @SerializedName("esCurrent") val esCurrent: Boolean,
    @SerializedName("reason") val reason: String?,
    @SerializedName("status") val status: String,
    @SerializedName("typeReject") val typeReject: String?,
    @SerializedName("userRegister") val userRegister: String,
    @SerializedName("version") val version: String
)

// Respuesta de la consulta previa al registrar una nueva muestra (GET al abrir "Nueva muestra")
data class ConsultaNuevaMuestra(
    @SerializedName("codePreview") val codePreview: String,
    @SerializedName("counterSiguiente") val counterSiguiente: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("lote") val lote: String,
    @SerializedName("numEn") val numEn: String?,
    @SerializedName("numOf") val numOf: String,
    @SerializedName("type") val type: String,
    @SerializedName("ultimoIntento") val ultimoIntento: Any?,
    @SerializedName("versionSiguiente") val versionSiguiente: String
)

// Request para registrar una nueva muestra (POST)
data class CrearMuestraProduccionRequest(
    @SerializedName("numOf") val numOf: String,
    @SerializedName("numEn") val numEn: String?,
    @SerializedName("type") val type: String,
    @SerializedName("dateRegister") val dateRegister: String,
    @SerializedName("userRegister") val userRegister: String
)

// Respuesta al registrar una nueva muestra (POST)
data class CrearMuestraProduccionResponse(
    @SerializedName("code") val code: String,
    @SerializedName("counter") val counter: Int,
    @SerializedName("dateRegister") val dateRegister: String,
    @SerializedName("docEntry") val docEntry: Int,
    @SerializedName("status") val status: String,
    @SerializedName("userRegister") val userRegister: String,
    @SerializedName("version") val version: String
)

// Envoltorio de error que devuelve la API (v1) en respuestas no exitosas
data class ApiErrorResponse(
    @SerializedName("error") val error: ApiErrorDetail?
)

data class ApiErrorDetail(
    @SerializedName("code") val code: String?,
    @SerializedName("details") val details: Any?,
    @SerializedName("message") val message: String?
)
