package com.vistony.app.clean.data.api

import com.google.gson.annotations.SerializedName
import com.vistony.app.clean.domain.model.ReasonForRejectionsModel

data class ManufacturingOrderResponseDto(
    val sucess: Boolean = true,
    val message: String = "",
    @SerializedName("data")
    val data: List<ManufacturingOrderDto>? = null // Cambiar a nullable
)

data class ManufacturingOrderDto(
    @SerializedName("docEntryLote")
    val batchCode: String= "",
    @SerializedName("lote")
    val batchName: String = "",
    @SerializedName("descripcion")
    val description: String = "",
    @SerializedName("tipoOF")
    val type: String = "",
    @SerializedName("aprobacion1")
    val approbationName1: String = "",
    @SerializedName("aprobacion2")
    val approbationName2: String = "",
    @SerializedName("aprobacion3")
    val approbationName3: String = "",
    @SerializedName("pesoOptimo")
    val optimumWeight: String= "",
    @SerializedName("pesoMaximo")
    val maximumWeight: String = "",
    @SerializedName("densidad")
    val density: String = "",
    @SerializedName("u_VIS_Adjustment")
    val adjustment: String = "",
    @SerializedName("correccion")
    val correction: String = "",
    @SerializedName("motivo")
    val reason: String = "",
    @SerializedName("u_VIS_ReasonAdjust")
    val reasonAdjustment: String = "",
    @SerializedName("listEnvace")
    val detail : List<ManufacturingOrderDetailDto> = emptyList(),
    @SerializedName("desaprobadoCalidad")
    val qualityDisapproved: String = "",
)

data class ManufacturingOrderDetailDto(
    @SerializedName("docEntryLote")
    val batchCode: String = "",
    @SerializedName("lote")
    val batchName: String = "",
    @SerializedName("descripcion")
    val description: String = "",
    @SerializedName("estado")
    val approbationName1: String = "",
    @SerializedName("estado2")
    val approbationName2: String = "",
    @SerializedName("estado3")
    val approbationName3: String = "",
    @SerializedName("pesoOptimo")
    val optimumWeight: String= "",
    @SerializedName("pesoMaximo")
    val maximumWeight: String = "",
    @SerializedName("estadoAprob")
    val approbationCode1: String = "",
    @SerializedName("estadoAprob2")
    val approbationCode2: String = "",
    @SerializedName("estadoAprob3")
    val approbationCode3: String = "",
)

data class ManufacturingOrderUpdateStatus(
    @SerializedName("nroof")
    val docnum: String = "",
    @SerializedName("densidad")
    val density: String = "",
    @SerializedName("motivoCorreccion")
    val correctionReason: String = "",
    @SerializedName("estadoAprobacion")
    val approvalStatus: String = "",
    @SerializedName("lineaAp")
    val approvalLine: String = "",
    @SerializedName("pesoOptimo")
    val optimumWeight: String= "",
    @SerializedName("pesoMaximo")
    val maximumWeight: String = "",

)

data class ReasonForRejectionsResponseDto(
    @SerializedName("statusCode")
    val statusCode: Int = 0,
    @SerializedName("sucess")
    val sucess: Boolean = false,
    @SerializedName("message")
    val message: String?="",           // Ahora nullable
    @SerializedName("data")
    val data: List<ReasonForRejectionsDto> = emptyList(),
)

data class ReasonForRejectionsDto(
    @SerializedName("FldValue")
    val code: String = "",
    @SerializedName("Descr")
    val name: String = "",
)



