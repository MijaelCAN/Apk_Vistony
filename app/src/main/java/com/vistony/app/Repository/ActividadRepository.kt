package com.vistony.app.Repository

import com.vistony.app.Entidad.ActivityRequest
import com.vistony.app.Entidad.DetaleResponse
import com.vistony.app.Entidad.OTRequest
import com.vistony.app.Entidad.OTResponse
import com.vistony.app.Entidad.ResponseCreated
import com.vistony.app.Entidad.ResponseUpdate
import com.vistony.app.Entidad.UpdateActividadRequest
import com.vistony.app.Entidad.listActivityResponse
import com.vistony.app.Service.RetrofitInstance
import retrofit2.Response
import javax.inject.Inject

class ActividadRepository @Inject constructor() {

    val actividadService = RetrofitInstance.actividadService

    suspend fun getAllActividades(
        id: Int,
        role: String,
        fechaInicio: String,
        fechaFin: String
    ): Response<listActivityResponse> =
        actividadService.obtenerActividadesFiltradas(id, role, fechaInicio, fechaFin)

    suspend fun getActividadByDocEntry(docEntry: String): Response<DetaleResponse> =
        actividadService.obtenerActividadByDocEntry(docEntry)

    suspend fun getCodigoBarra(request: OTRequest): Response<OTResponse> =
        actividadService.getCodigoBarra(request)


    suspend fun registrarActividad(request: ActivityRequest): Response<ResponseCreated> =
        actividadService.registrarActividad(request)

    suspend fun updateActividad(
        docEntry: String,
        request: UpdateActividadRequest
    ): Response<ResponseUpdate> =
        actividadService.updateActividad(docEntry, request)
}