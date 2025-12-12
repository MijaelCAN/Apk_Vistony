package com.vistony.app.clean.di

import com.vistony.app.clean.core.network.ApiService
import dagger.Module
import dagger.Provides
import com.vistony.app.clean.data.datasources.ManufacturingOrderRemoteDataSource
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideManufacturingOrderRemoteDataSource(
        api: ApiService
    ): ManufacturingOrderRemoteDataSource = ManufacturingOrderRemoteDataSource(api)
}