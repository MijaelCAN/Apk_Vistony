package com.vistony.app.Service

import com.vistony.app.Entidad.Area
import com.vistony.app.Entidad.AreaResponse
import com.vistony.app.Entidad.ListaRequest
import com.vistony.app.Entidad.Maquina
import com.vistony.app.Entidad.MaquinaResponse
import com.vistony.app.Entidad.Motivo
import com.vistony.app.Entidad.MotivoResponse
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.ParadaRequest
import com.vistony.app.Entidad.ParadaResponse
import com.vistony.app.Entidad.PostParada
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ParadaService {
    @POST("ParadaMaquina")
    suspend fun registrarParada(@Body request: ParadaRequest): Response<PostParada>

    @POST("ParadaMaquina/ListaParadaMaquina")
    suspend fun obtenerParadas(@Body request: ListaRequest): Response<ParadaResponse>

    @PUT("paradas/detener")
    suspend fun detenerParada(@Body parada: Parada): Response<PostParada>

    /*@PUT("paradas/{id}/detener")
    suspend fun detenerParada(@Path("id") id: Int, @Query("fechaDetencion") fechaDetencion: String)*/

    @GET("ParadaMaquina/Area")
    suspend fun getAreas(): Response<AreaResponse>

    @GET("ParadaMaquina/Maquina")
    suspend fun getMaquinas(): Response<MaquinaResponse>

    /*@GET("ParadaMaquina/MotivoParada?Area=1")
    suspend fun getMotivos(@Body maquina: String): List<Motivo>*/

    @GET("ParadaMaquina/MotivoParada")
    suspend fun getMotivoParada(@Query("Area") Area: Int): Response<MotivoResponse>

}