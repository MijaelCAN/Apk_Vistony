package com.vistony.app.Service

import com.vistony.app.Entidad.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @POST("Inspeccion/inspeccionDimensional_create")
    suspend fun crearInspeccionDimensional(
        @Query("DocEntry") docEntry: String,
        @Body request: InspeccionDimensionalCreateRequest
    ): Response<InspeccionDimensionalCreateResponse>

    @POST("Inspeccion/materialEmpleado_create")
    suspend fun crearMaterialEmpleado(
        @Query("DocEntry") docEntry: String,
        @Body request: MaterialEmpleadoCreateRequest
    ): Response<MaterialEmpleadoCreateResponse>

    @POST("Inspeccion/evaluacionProduccion_create")
    suspend fun crearEvaluacionProduccion(
        @Query("DocEntry") docEntry: String,
        @Body request: EvaluacionProduccionCreateRequest
    ): Response<EvaluacionProduccionCreateResponse>

    @POST("muestra/create")
    suspend fun registrarMuestra(@Body request: MuestraRequest): Response<PostMuestraResponse>

    @GET("muestra/{id}")
    suspend fun obtenerMuestraPorId(@Query("id") id: String): Response<MuestraCompletaResponse>
    
    @GET("Inspeccion/muestra/detalle")
    suspend fun obtenerMuestraDetalle(@Query("DocEntry") docEntry: String): Response<MuestraDetalleResponse>
    
    @GET("Inspeccion/muestra/productos")
    suspend fun obtenerProductos(): Response<ProductoResponseTemp>
    
    @GET("Inspeccion/consultaProducto")
    suspend fun consultarProducto(@Query("Code") code: String): Response<ConsultaProductoResponse>
    
    @GET("Inspeccion/EspecificacionSoplado")
    suspend fun obtenerEspecificacionSoplado(
        @Query("CodProducto") codProducto: String,
        //@Query("Tipo") tipo: String = "Envase"
    ): Response<EspecificacionSopladoResponse>
    
    @GET("Inspeccion/GetProduccion")
    suspend fun obtenerRegistrosLlegada(
        @Query("FechaInicio") fechaInicio: String? = null,
        @Query("FechaFin") fechaFin: String? = null,
        @Query("Code") role: String? = null
    ): Response<RegistroLlegadaResponse>

    @GET("Inspeccion/consultaProductoMuestra")
    suspend fun consultarProductoMuestra(@Query("Code") code: String): Response<ConsultaProductoMuestraResponse>
    
    @POST("Inspeccion/PostProduccion")
    suspend fun crearRegistroLlegada(@Body request: RegistroLlegadaCreateRequest): Response<RegistroLlegadaCreateResponse>

    @PATCH("Inspeccion/ConfirmarRecepcion")
    suspend fun confirmarRecepcion(@Body request: ConfirmarRecepcionRequest): Response<ConfirmarRecepcionResponse>
}



