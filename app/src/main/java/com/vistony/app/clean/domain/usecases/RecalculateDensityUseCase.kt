package com.vistony.app.clean.domain.usecases

import com.vistony.app.clean.domain.model.ManufacturingOrderResponseModel
import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository

class RecalculateDensityUseCase(
    private val repository: ManufacturingOrderRepository
) {
    suspend operator fun invoke(orderCode: String, density: String): ManufacturingOrderResponseModel {
        // Lógica para recalcular la densidad usando el repositorio
       return repository.recalculateDensity(orderCode, density)
    }
}