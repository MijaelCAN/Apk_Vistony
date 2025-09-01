package com.vistony.app.clean.data.datasources

import android.util.Log
import com.vistony.app.clean.core.network.ApiService
import com.vistony.app.clean.data.api.ManufacturingOrderResponseDto
import com.vistony.app.clean.domain.repository.ManufacturingOrderRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

class ManufacturingOrderRemoteDataSource @Inject constructor(
    private val api: ApiService
)
{
    suspend fun getManufacturingOrder(orderCode: String
    ): ManufacturingOrderResponseDto {
        return try {
            val response = api.getManufacturingOrder(orderCode)
            Log.e("REOS", "ManufacturingOrderRemoteDataSource-getManufacturingOrder-Respuesta del servidor: ${response.body()}")
            if (response.isSuccessful) {
                response.body() ?: ManufacturingOrderResponseDto(data = listOf())
            } else {
                Log.e("REOS", "ManufacturingOrderRemoteDataSource-getManufacturingOrder-Error en la respuesta: ${response.code()} ${response.message()}")
                ManufacturingOrderResponseDto(data = listOf())
            }

        } catch (e: Exception) {
            Log.e("REOS", "ManufacturingOrderRemoteDataSource-getManufacturingOrder-Error al enviar datos: ${e.message}")
            ManufacturingOrderResponseDto(data = listOf())
        }
    }

    suspend fun recalculateDensity(orderCode: String, density:String
    ): ManufacturingOrderResponseDto {
        return try {
            var json: String = ""
            if (orderCode != null) {
                json = "{ \"docNum\":\"${orderCode}\",\"densidad\":\"${density}\"}"
            }
            Log.e("REOS", "QuotationHistoricRepository-getQuotationHistoric-json: " +json)
            val jsonRequest: RequestBody = RequestBody.create(
                ("application/json; charset=utf-8").toMediaTypeOrNull(),
                json
            )
            val response = api.recalculateDensity(jsonRequest)
            Log.e("REOS", "ManufacturingOrderRemoteDataSource-getManufacturingOrder-Respuesta del servidor: ${response.body()}")
            if (response.isSuccessful) {
                response.body() ?: ManufacturingOrderResponseDto(data = listOf())
            } else {
                Log.e("REOS", "ManufacturingOrderRemoteDataSource-getManufacturingOrder-Error en la respuesta: ${response.code()} ${response.message()}")
                ManufacturingOrderResponseDto(data = listOf())
            }

        } catch (e: Exception) {
            Log.e("REOS", "ManufacturingOrderRemoteDataSource-getManufacturingOrder-Error al enviar datos: ${e.message}")
            ManufacturingOrderResponseDto(data = listOf())
        }
    }

    suspend fun updateApprovalStatus(
        docNum: String, density: String, approvalStatus:String,approvalLine:String,optimalWeight:String,maximunWeight: String
    ) {
        try {
            var json: String = ""
            if (docNum != null) {
                json = "{ \"nroof\":\"${docNum}\",\"densidad\":\"${density}\",\"estadoAprobacion\":\"${approvalStatus}\",\"motivoCorreccion\":\"${""}\",\"lineaAp\":\"${approvalLine}\",\"pesoOptimo\":\"${optimalWeight}\",\"pesoMaximo\":\"${maximunWeight}\"}"
            }
            Log.e("REOS", "ManufacturingOrderRemoteDataSource-updateApprovalStatus-json: " +json)
            val jsonRequest: RequestBody = RequestBody.create(
                ("application/json; charset=utf-8").toMediaTypeOrNull(),
                json
            )
            val response = api.updateApprovalStatus(jsonRequest)
            Log.e("REOS", "ManufacturingOrderRemoteDataSource-updateApprovalStatus-Respuesta del servidor: ${response.body()}")
            if (response.isSuccessful) {
                response.body() ?: ManufacturingOrderResponseDto(data = listOf())
            } else {
                Log.e("REOS", "ManufacturingOrderRemoteDataSource-updateApprovalStatus-Error en la respuesta: ${response.code()} ${response.message()}")
                //ManufacturingOrderResponseDto(data = listOf())
            }

        } catch (e: Exception) {
            Log.e("REOS", "ManufacturingOrderRemoteDataSource-updateApprovalStatus-Error al enviar datos: ${e.message}")
            //ManufacturingOrderResponseDto(data = listOf())
        }
    }
}