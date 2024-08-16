package com.vistony.app.Entidad

data class Inspeccion(
    val OT: String = "",
    val Fecha: String = "",
    val Turno: String = "",
    val Usuario: String = ""
)

data class InspecionResponse(
    val statusCode: Int = 0,
    val data: List<Inspeccion> = emptyList()
)

data class InspecionRequest(
    val FechaIni: String,
    val FechaFin: String,
)
