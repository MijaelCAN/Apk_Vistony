package com.vistony.app.Service

import com.vistony.app.Entidad.ConsultaNuevaMuestraResponse
import com.vistony.app.Entidad.CrearMuestraProduccionRequest
import com.vistony.app.Entidad.CrearMuestraProduccionResponse
import com.vistony.app.Entidad.FinalizarAnalisisRequest
import com.vistony.app.Entidad.FinalizarAnalisisResponse
import com.vistony.app.Entidad.IniciarAnalisisRequest
import com.vistony.app.Entidad.IniciarAnalisisResponse
import com.vistony.app.Entidad.MuestraProduccionDetalle
import com.vistony.app.Entidad.MuestraProduccionResponse
import com.vistony.app.Entidad.ResponseMuestraDetalle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Interfaz para los endpoints de la nueva implementación de muestras de producción.
// Backend real: http://192.168.254.27:8036/api/Laboratorio/... (mismo host que el resto de
// Laboratorio/Inspeccion, vía RetrofitInstance.retrofitNew). Ya no usa el túnel ngrok temporal.
interface MuestraProduccionService {
    @GET("Laboratorio/muestras")
    suspend fun obtenerMuestrasProduccion(
        @Query("FechaInicio") fechaInicio: String,
        @Query("fechaFin") fechaFin: String
    ): Response<MuestraProduccionResponse>

    @GET("Laboratorio/muestras/{docEntry}")
    suspend fun obtenerMuestraProduccionDetalle(
        @Path("docEntry") docEntry: String
    ): Response<ResponseMuestraDetalle>

    @GET("Laboratorio/muestras/consultaProducto")
    suspend fun consultarNuevaMuestra(
        @Query("numOf") numOf: String,
        @Query("numEn") numEn: String?,
        @Query("type") type: String
    ): Response<ConsultaNuevaMuestraResponse>

    @POST("Laboratorio/muestras")
    suspend fun crearMuestraProduccion(
        @Body request: CrearMuestraProduccionRequest
    ): Response<CrearMuestraProduccionResponse>

    @PATCH("Laboratorio/muestras/{docEntry}/iniciar-analisis")
    suspend fun iniciarAnalisis(
        @Path("docEntry") docEntry: String,
        @Body request: IniciarAnalisisRequest
    ): Response<IniciarAnalisisResponse>

    @PATCH("Laboratorio/muestras/{docEntry}/finalizar-analisis")
    suspend fun finalizarAnalisis(
        @Path("docEntry") docEntry: String,
        @Body request: FinalizarAnalisisRequest
    ): Response<FinalizarAnalisisResponse>
}
