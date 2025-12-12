package com.vistony.app.clean.di

import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository
import com.vistony.app.clean.domain.usecases.GetManufacturingOrderUseCase
import com.vistony.app.clean.domain.usecases.GetReasonForRejectionsUseCase
import com.vistony.app.clean.domain.usecases.RecalculateDensityUseCase
import com.vistony.app.clean.domain.usecases.UpdateApprovalStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetManufacturingOrderUseCases(
        repository: ManufacturingOrderRepository
    ): GetManufacturingOrderUseCase = GetManufacturingOrderUseCase(repository)

    @Provides
    @Singleton
    fun provideRecalculateDensityUseCase(
        repository: ManufacturingOrderRepository
    ): RecalculateDensityUseCase = RecalculateDensityUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateApprovalStatusUseCase(
        repository: ManufacturingOrderRepository
    ): UpdateApprovalStatusUseCase = UpdateApprovalStatusUseCase(repository)

    @Provides
    @Singleton
    fun provideGetReasonForRejectionsUseCase(
        repository: ManufacturingOrderRepository
    ): GetReasonForRejectionsUseCase = GetReasonForRejectionsUseCase(repository)
}