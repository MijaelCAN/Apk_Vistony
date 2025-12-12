package com.vistony.app.clean.core.network

import com.vistony.app.clean.data.api.ManufacturingOrderResponseDto
import com.vistony.app.clean.data.api.ReasonForRejectionsResponseDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/Laboratorio/ListOF")
    suspend fun getManufacturingOrder(@Query("Parametro") orderCode: String):Response<ManufacturingOrderResponseDto>

    @POST("/api/Laboratorio/CalculoDensidad")
    suspend fun recalculateDensity(@Body params: RequestBody?):Response<ManufacturingOrderResponseDto>

    @PATCH("/api/Laboratorio/Liberacion")
    suspend fun updateApprovalStatus(@Body params: RequestBody?):Response<ManufacturingOrderResponseDto>

    @GET("/api/Laboratorio/MotivoCorreccion")
    suspend fun getReasonForRejections():Response<ReasonForRejectionsResponseDto>


}