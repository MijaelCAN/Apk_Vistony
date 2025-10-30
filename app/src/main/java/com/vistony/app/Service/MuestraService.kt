package com.vistony.app.Service

import com.vistony.app.Entidad.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MuestraService {
    @POST("Inspeccion/muestra/list")
    suspend fun obtenerMuestras(@Body request: CabeceraRequest): Response<MuestraResponse>

    @POST("Inspeccion/muestra_create")
    suspend fun crearMuestra(@Body request: MuestraCreateRequest): Response<MuestraCreateResponse>

    @POST("Inspeccion/checkList_create")
    suspend fun crearCheckList(
        @Query("DocEntry") docEntry: String,
        @Body request: CheckListCreateRequest
    ): Response<CheckListCreateResponse>

    @POST("muestra/create")
    suspend fun registrarMuestra(@Body request: MuestraRequest): Response<PostMuestraResponse>

    @GET("muestra/{id}")
    suspend fun obtenerMuestraPorId(@Query("id") id: String): Response<MuestraCompletaResponse>
    
    @GET("Inspeccion/muestra/detalle")
    suspend fun obtenerMuestraDetalle(@Query("DocEntry") docEntry: String): Response<com.vistony.app.Entidad.MuestraDetalleResponse>
    
    @GET("Inspeccion/muestra/productos")
    suspend fun obtenerProductos(): Response<ProductoResponseTemp>
    
    @GET("Inspeccion/consultaProducto")
    suspend fun consultarProducto(@Query("Code") code: String): Response<ConsultaProductoResponse>
}



