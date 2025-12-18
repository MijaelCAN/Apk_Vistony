package com.vistony.app.clean.domain.usecases

import android.util.Log
import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository

class GetManufacturingOrderUseCase(
    private val repository: ManufacturingOrderRepository
) {
    suspend operator fun invoke(formCode: String): ManufacturingOrderResponseModel {
        Log.e("REOS", "GetManufacturingOrderUseCases-invoke-Obteniendo orden de fabricación para el código: $formCode")
        //Log.e("REOS", "GetManufacturingOrderUseCases-invoke-response:" +repository.getManufacturingOrder(formCode))
        return repository.getManufacturingOrder(formCode)
    }
}