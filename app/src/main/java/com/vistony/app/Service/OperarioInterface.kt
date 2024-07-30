package com.vistony.app.Service

import com.vistony.app.Entidad.Operario
import com.vistony.app.Entidad.OperarioResponse
import retrofit2.http.GET

interface OperarioInterface {
    @GET("Maquinista")
    suspend fun getOperarios(): OperarioResponse
}