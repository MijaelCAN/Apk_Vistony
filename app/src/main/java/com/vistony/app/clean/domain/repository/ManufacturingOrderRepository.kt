package com.vistony.app.clean.domain.repository

import com.vistony.app.clean.data.api.ManufacturingOrderResponseDto
import com.vistony.app.clean.domain.model.DensityResponseModel
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.model.ReasonForRejectionsResponseModel

interface ManufacturingOrderRepository {
    suspend fun getManufacturingOrder(formCode: String): ManufacturingOrderResponseModel
    suspend fun recalculateDensity(formCode: String,density:String ): ManufacturingOrderResponseModel
    suspend fun updateApprovalStatus(
        docNum: String, density: String, approvalStatus:String,approvalLine:String
        ,optimalWeight:String,maximunWeight: String,estadoAprobacionCorreccion: String
        ,estadoAprobacionDesaprobadoCalidad: String,motivoCorreccion: String,observations:String)

    suspend fun getReasonForRejections(): ReasonForRejectionsResponseModel
}