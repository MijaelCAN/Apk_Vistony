package com.vistony.app.Service

import com.vistony.app.Entidad.ConsultaNuevaMuestra
import com.vistony.app.Entidad.CrearMuestraProduccionRequest
import com.vistony.app.Entidad.CrearMuestraProduccionResponse
import com.vistony.app.Entidad.MuestraProduccionDetalle
import com.vistony.app.Entidad.MuestraProduccionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Nueva interfaz (v1) para los endpoints de la nueva implementación de muestras de producción
interface MuestraProduccionService {
    @GET("v1/muestras")
    suspend fun obtenerMuestrasProduccion(
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFin") fechaFin: String
    ): Response<MuestraProduccionResponse>

    @GET("v1/muestras/{docEntry}")
    suspend fun obtenerMuestraProduccionDetalle(
        @Path("docEntry") docEntry: Int
    ): Response<MuestraProduccionDetalle>

    @GET("v1/muestras/consultaProducto")
    suspend fun consultarNuevaMuestra(
        @Query("numOf") numOf: String,
        @Query("numEn") numEn: String?,
        @Query("type") type: String
    ): Response<ConsultaNuevaMuestra>

    @POST("v1/muestras")
    suspend fun crearMuestraProduccion(
        @Body request: CrearMuestraProduccionRequest
    ): Response<CrearMuestraProduccionResponse>
}
