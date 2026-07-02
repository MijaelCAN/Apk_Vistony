package com.vistony.app.clean.data.mappers

import com.vistony.app.clean.data.api.DensityResponseDto
import com.vistony.app.clean.data.api.ManufacturingOrderDetailDto
import com.vistony.app.clean.data.api.ManufacturingOrderDto
import com.vistony.app.clean.data.api.ManufacturingOrderResponseDto
import com.vistony.app.clean.data.api.ReasonForRejectionsDto
import com.vistony.app.clean.data.api.ReasonForRejectionsResponseDto
import com.vistony.app.clean.domain.model.DensityResponseModel
import com.vistony.app.clean.domain.model.ManufacturingOrderDetailModel
import com.vistony.app.clean.domain.model.ManufacturingOrderModel
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.model.ReasonForRejectionsModel
import com.vistony.app.clean.domain.model.ReasonForRejectionsResponseModel
import kotlin.String
import kotlin.text.orEmpty


fun ManufacturingOrderResponseDto.toModel() = ManufacturingOrderResponseModel(
    sucess = this.sucess,
    message = this.message,
    data = this.data?.map { it.toModel() } ?: emptyList() // Validar null
)

fun ManufacturingOrderDto.toModel() = ManufacturingOrderModel(
    batchCode = this.batchCode ?: "",
    batchName = this.batchName ?: "",
    description = this.description ?: "",
    approbationName1 = this.approbationName1 ?: "",
    approbationName2 = this.approbationName2 ?: "",
    approbationName3 = this.approbationName3 ?: "",
    optimumWeight = this.optimumWeight?.takeIf { it != 0.0 }?.let { "%.3f".format(it).trimEnd('0').trimEnd('.') } ?: "0",
    maximumWeight = this.maximumWeight?.takeIf { it != 0.0 }?.let { "%.3f".format(it).trimEnd('0').trimEnd('.') } ?: "0",
    density = this.density ?: "",
    adjustment = this.adjustment ?: "",
    correction = this.correction ?: "",
    reason = this.reason ?: "",
    reasonAdjustment = this.reasonAdjustment ?: "",
    detail = this.detail?.map { it.toModel() } ?: emptyList(), // Validar null
    qualityDisapproved = this.qualityDisapproved ?: "",
    observations = this.observations ?: ""
)

fun ManufacturingOrderDetailDto.toModel() = ManufacturingOrderDetailModel(
    batchCode = this.batchCode ?: "",
    batchName = this.batchName ?: "",
    description = this.description ?: "",
    approbationName1 = this.approbationName1 ?: "",
    approbationName2 = this.approbationName2 ?: "",
    approbationName3 = this.approbationName3 ?: "",
    optimumWeight = this.optimumWeight?.takeIf { it != 0.0 }?.let { "%.3f".format(it).trimEnd('0').trimEnd('.') } ?: "0",
    maximumWeight = this.maximumWeight?.takeIf { it != 0.0 }?.let { "%.3f".format(it).trimEnd('0').trimEnd('.') } ?: "0",
    approbationCode1 = this.approbationCode1 ?: "",
    approbationCode2 = this.approbationCode2 ?: "",
    approbationCode3 = this.approbationCode3 ?: ""
)

fun ReasonForRejectionsResponseDto.toModel() = ReasonForRejectionsResponseModel(
    statusCode = statusCode ?: 0,
    success = sucess ?: false,     // usa el valor de la API
    message = message ?: "",
    data = data?.map { it.toModel() }.orEmpty()
)
fun ReasonForRejectionsDto.toModel() = ReasonForRejectionsModel(
    code = this.code ?: "",
    name = this.name ?: ""
)

fun DensityResponseDto.toModel() = DensityResponseModel(
    statusCode = statusCode ?: 0,
    success = success ?: false,     // usa el valor de la API
    message = message ?: "",
    data = data ?: ""
)