package com.vistony.app.Service

import com.vistony.app.Entidad.Activity2Dto
import com.vistony.app.Entidad.ActivityRequest
import com.vistony.app.Entidad.DetaleResponse
import com.vistony.app.Entidad.OTRequest
import com.vistony.app.Entidad.OTResponse
import com.vistony.app.Entidad.ProductoResponse
import com.vistony.app.Entidad.ResponseCreated
import com.vistony.app.Entidad.ResponseUpdate
import com.vistony.app.Entidad.UpdateActividadRequest
import com.vistony.app.Entidad.listActivityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ActividadService {
    @GET("Activity")
    suspend fun obtenerActividadesFiltradas(
        @Query("id") id: Int,
        @Query("role") role: String,
        @Query("fecha_inicio") fechaInicio: String,
        @Query("fecha_fin") fechaFin: String
    ): Response<listActivityResponse>

    @GET("Activity/Detalle")
    suspend fun obtenerActividadByDocEntry(@Query("DocEntry") docEntry: String): Response<DetaleResponse>

    @POST("Activity")
    suspend fun registrarActividad(@Body request: ActivityRequest): Response<ResponseCreated>


    @POST("Palet")
    suspend fun getCodigoBarra(@Body request: OTRequest): Response<OTResponse>

    @PATCH("Activity")
    suspend fun updateActividad(
        @Query("DocEntry") docEntry: String,
        @Body request: UpdateActividadRequest
    ): Response<ResponseUpdate>

}