package com.vistony.app.Service

import com.vistony.app.Entidad.Area
import com.vistony.app.Entidad.Maquina
import com.vistony.app.Entidad.Motivo
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.ParadaResponse
import com.vistony.app.Entidad.PostParada
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ParadaService {
    @POST("paradas")
    suspend fun registrarParada(@Body parada: Parada): PostParada

    @GET("paradas")
    suspend fun obtenerParadas(): ParadaResponse

    @PUT("paradas/detener")
    suspend fun detenerParada(@Body parada: Parada): ParadaResponse

    /*@PUT("paradas/{id}/detener")
    suspend fun detenerParada(@Path("id") id: Int, @Query("fechaDetencion") fechaDetencion: String)*/

    @GET("area")
    suspend fun getAreas(): List<Area>

    @POST("listaMaquinas")
    suspend fun getMaquinas(@Body area: String): List<Maquina>

    @GET("motivo")
    suspend fun getMotivos(@Body maquina: String): List<Motivo>

}