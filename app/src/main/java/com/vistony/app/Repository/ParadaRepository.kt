package com.vistony.app.Repository

import com.vistony.app.Entidad.AreaResponse
import com.vistony.app.Entidad.ListaRequest
import com.vistony.app.Entidad.Maquina
import com.vistony.app.Entidad.MaquinaResponse
import com.vistony.app.Entidad.MotivoResponse
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.ParadaRequest
import com.vistony.app.Entidad.ParadaResponse
import com.vistony.app.Entidad.PostParada
import com.vistony.app.Service.ParadaService
import com.vistony.app.Service.RetrofitInstance
import retrofit2.Response
import javax.inject.Inject

class ParadaRepository @Inject constructor() {
    private val paradaService = RetrofitInstance.paradaService

    suspend fun registrarParada(request: ParadaRequest): Response<PostParada> = paradaService.registrarParada(request)
    suspend fun obtenerParadas(request: ListaRequest): Response<ParadaResponse> = paradaService.obtenerParadas(request)
    suspend fun detenerParada(DocEntry: Int):Response<PostParada> = paradaService.detenerParada(DocEntry)
    suspend fun getAreas(): Response<AreaResponse> = paradaService.getAreas()
    suspend fun getMaquinas(): Response<MaquinaResponse> = paradaService.getMaquinas()
    suspend fun getMotivoParada(areaId: Int): Response<MotivoResponse> = paradaService.getMotivoParada(areaId)
}