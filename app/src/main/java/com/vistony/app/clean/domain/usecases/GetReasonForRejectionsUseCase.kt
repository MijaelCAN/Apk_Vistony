package com.vistony.app.clean.domain.usecases

import com.vistony.app.clean.domain.model.ReasonForRejectionsResponseModel
import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository
import javax.inject.Inject

class GetReasonForRejectionsUseCase @Inject constructor(
    private val repository: ManufacturingOrderRepository
) {
    suspend operator fun invoke(): ReasonForRejectionsResponseModel {
        return repository.getReasonForRejections()
    }
}