package com.vistony.app.clean.di

import com.vistony.app.clean.data.repository.ManufacturingOrderRepositoryImpl
import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindManufacturingOrderRepository(
        impl: ManufacturingOrderRepositoryImpl
    ): ManufacturingOrderRepository
}