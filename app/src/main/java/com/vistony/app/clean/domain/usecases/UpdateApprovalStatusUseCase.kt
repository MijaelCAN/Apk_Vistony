package com.vistony.app.clean.domain.usecases

import androidx.compose.ui.text.font.FontWeight
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository

class UpdateApprovalStatusUseCase (
    private val repository: ManufacturingOrderRepository
) {
    suspend operator fun invoke(
        docNum: String, density: String, approvalStatus:String,approvalLine:String,optimalWeight:String,maximunWeight: String
        ,estadoAprobacionCorreccion: String,estadoAprobacionDesaprobadoCalidad: String,motivoCorreccion: String
    ){
        // Lógica para recalcular la densidad usando el repositorio

        return repository.updateApprovalStatus(
            docNum, density,approvalStatus,approvalLine,optimalWeight,maximunWeight
        ,estadoAprobacionCorreccion,estadoAprobacionDesaprobadoCalidad,motivoCorreccion)
    }
}