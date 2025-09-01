package com.vistony.app.clean.data.mappers

import com.vistony.app.clean.data.api.ManufacturingOrderDetailDto
import com.vistony.app.clean.data.api.ManufacturingOrderDto
import com.vistony.app.clean.data.api.ManufacturingOrderResponseDto
import com.vistony.app.clean.domain.model.ManufacturingOrderDetailModel
import com.vistony.app.clean.domain.model.ManufacturingOrderModel
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import kotlin.String


fun ManufacturingOrderResponseDto.toModel() = ManufacturingOrderResponseModel (
    sucess = this.sucess,
    message = this.message,
    data = this.data.map { it.toModel() }
)

fun ManufacturingOrderDto.toModel() = ManufacturingOrderModel(
    batchCode = this.batchCode,
    batchName = this.batchName,
    description = this.description,
    approbationName1 = this.approbationName1,
    approbationName2 = this.approbationName2,
    approbationName3 = this.approbationName3,
    optimumWeight = this.optimumWeight ?: "0",
    maximumWeight = this.maximumWeight ?: "0",
    density = this.density,
    adjustment = this.adjustment,
    correction = this.correction,
    reason = this.reason,
    reasonAdjustment = this.reasonAdjustment,
    detail = this.detail.map { it.toModel() }

)

fun ManufacturingOrderDetailDto.toModel() = ManufacturingOrderDetailModel(
    batchCode = this.batchCode,
    batchName = this.batchName,
    description = this.description,
    approbationName1 = this.approbationName1,
    approbationName2 = this.approbationName2,
    approbationName3 = this.approbationName3,
    optimumWeight = this.optimumWeight ?: "0",
    maximumWeight = this.maximumWeight ?: "0",
    approbationCode1 = this.approbationCode1?: "",
    approbationCode2 = this.approbationCode2?: "",
    approbationCode3 = this.approbationCode3?: "",
)