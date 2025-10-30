package com.vistony.app.Service

import com.vistony.app.Entidad.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TemperaturaService {
    @POST("ControlTemperatura/List")
    suspend fun obtenerTemperaturas(@Body request: ListRequest): Response<TemperaturaResponse>

    @POST("ControlTemperatura")
    suspend fun registrarTemperatura(
        @Body request: TemperaturaRequest
    ): Response<PostTemperaturaResponse>
    
    @GET("ControlTemperatura/ConsultaOT")
    suspend fun consultarOT(@Query("Barra") ot: String): Response<ConsultaOTResponse>
}





