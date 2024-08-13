package com.vistony.app.Service

import com.vistony.app.Entidad.lineaResponse
import retrofit2.Response
import retrofit2.http.GET

interface LineaService {
    @GET("Maquinista/Line")
    suspend fun getLinea(): Response<lineaResponse>
}