package com.vistony.app.clean.data.repository

import android.content.Context
import com.vistony.app.clean.data.datasources.ManufacturingOrderRemoteDataSource
import com.vistony.app.clean.data.mappers.toModel
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.model.ReasonForRejectionsResponseModel
import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManufacturingOrderRepositoryImpl  @Inject constructor(
    private val remote: ManufacturingOrderRemoteDataSource
) : ManufacturingOrderRepository {

    override suspend fun getManufacturingOrder(orderCode: String): ManufacturingOrderResponseModel {
        return  remote.getManufacturingOrder(orderCode).toModel()
    }

    override suspend fun recalculateDensity(orderCode: String, density: String): ManufacturingOrderResponseModel {
        return  remote.recalculateDensity(orderCode,density).toModel()
    }

    override suspend fun updateApprovalStatus(
        docNum: String, density: String, approvalStatus:String,approvalLine:String,optimalWeight:String,maximunWeight: String
        ,estadoAprobacionCorreccion: String,estadoAprobacionDesaprobadoCalidad: String,motivoCorreccion: String
    ) {
        return  remote.updateApprovalStatus(docNum, density, approvalStatus,approvalLine,optimalWeight,maximunWeight,
            estadoAprobacionCorreccion,estadoAprobacionDesaprobadoCalidad,motivoCorreccion)
    }

    override suspend fun getReasonForRejections(): ReasonForRejectionsResponseModel {
        return  remote.getReasonForRejections().toModel()
    }

}