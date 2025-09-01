package com.vistony.app.clean.domain.model

import com.google.gson.annotations.SerializedName
import com.vistony.app.clean.data.api.ManufacturingOrderDetailDto
import com.vistony.app.clean.data.api.ManufacturingOrderDto

data class ManufacturingOrderResponseModel(
    val sucess: Boolean = false,
    val message: String = "",
    val data: List<ManufacturingOrderModel> = emptyList(),
)


data class ManufacturingOrderModel(
    val batchCode: String= "",
    val batchName: String = "",
    val description: String = "",
    val approbationName1: String = "",
    val approbationName2: String = "",
    val approbationName3: String = "",
    val optimumWeight: String= "",
    val maximumWeight: String = "",
    val density: String = "",
    val adjustment: String = "",
    val correction: String = "",
    val reason: String = "",
    val reasonAdjustment: String = "",
    val detail : List<ManufacturingOrderDetailModel> = emptyList(),
)

data class ManufacturingOrderDetailModel(
    val batchCode: String = "",
    val batchName: String = "",
    val description: String = "",
    val approbationName1: String = "",
    val approbationName2: String = "",
    val approbationName3: String = "",
    val optimumWeight: String= "",
    val maximumWeight: String = "",
    val approbationCode1: String = "",
    val approbationCode2: String = "",
    val approbationCode3: String = "",
)

data class ApprobationModel(
    val code: String = "",
    val name: String = "",
)

object ApprobationDefaults {
    val DEFAULT_APPROBATIONS = listOf(
        ApprobationModel(code = "N", name = "Rechazado"),
        ApprobationModel(code = "S", name = "Aprobado"),
        ApprobationModel(code = "*", name = "Pendiente")
    )
}

data class ManufacturingOrderUpdateStatus(
    val docnum: String = "",
    val density: String = "",
    val correctionReason: String = "",
    val approvalStatus: String = "",
    val approvalLine: String = "",
    val optimumWeight: String= "",
    val maximumWeight: String = "",
)


