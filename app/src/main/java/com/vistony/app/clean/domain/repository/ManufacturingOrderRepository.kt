package com.vistony.app.clean.domain.repository

import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel

interface ManufacturingOrderRepository {
    suspend fun getManufacturingOrder(formCode: String): ManufacturingOrderResponseModel
    suspend fun recalculateDensity(formCode: String,density:String ): ManufacturingOrderResponseModel
    suspend fun updateApprovalStatus(docNum: String, density: String, approvalStatus:String,approvalLine:String,optimalWeight:String,maximunWeight: String)
}