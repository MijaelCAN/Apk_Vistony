package com.vistony.app.Service

import com.vistony.app.Entidad.Evaluacion
import com.vistony.app.Entidad.EvaluacionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EvaluacionService {
    @POST("Evaluacion")
    suspend fun postEvaluacion(@Body request: Evaluacion): Response<EvaluacionResponse>
}