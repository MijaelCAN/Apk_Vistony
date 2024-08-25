package com.vistony.app.Repository

import com.vistony.app.Entidad.Maquina
import com.vistony.app.Entidad.Parada
import com.vistony.app.Service.ParadaService
import com.vistony.app.Service.RetrofitInstance
import javax.inject.Inject

class ParadaRepository @Inject constructor() {
    private val paradaService = RetrofitInstance.paradaService

    suspend fun registrarParada(parada: Parada) = paradaService.registrarParada(parada)
    suspend fun obtenerParadas() = paradaService.obtenerParadas()
    suspend fun detenerParada(parada: Parada) = paradaService.detenerParada(parada)
    suspend fun getAreas() = paradaService.getAreas()
    suspend fun getMaquinas(nameArea: String) = paradaService.getMaquinas(nameArea)
    suspend fun getMotivos(nameMaquina: String) = paradaService.getMotivos(nameMaquina)
}