package com.vistony.app.Service

import com.vistony.app.Entidad.CodigoRequest
import com.vistony.app.Entidad.ProductoResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OTService {
    @POST("Palet")
    suspend fun getCodigoBarra(@Body request: CodigoRequest ): ProductoResponse
}