package com.vistony.app.Entidad

data class Parada(
    var id: Int = 0,
    var fechaInicio: String = "",// tiene que ser un LocaDateTime var fechaInicio: LocalDateTime? = null,
    var fechaFinal: String = "", // tiene que ser un LocaDateTime var fechaInicio: LocalDateTime? = null,
    var maquina: String = "",
    var area: String = "",
    var motivoParada: String = "",
    var comentarios: String = ""
)

data class ParadaResponse(
    val statusCode: Int = 0,
    var data: List<Parada> = emptyList()
)
data class PostParada(
    val statusCode: Int = 0,
    val data: String
)

data class ParadaStopRequest(
    var id: Int = 0,
    var fechaFinal: String = ""
)
data class Area(
    val id: Int,
    val nomArea: String
)
data class AreaResponse(
    val statusCode: Int = 0,
    val data: List<Area> = listOf()
)

data class Maquina(
    val id: Int,
    val maquina: String
)
data class MaquinaResponse(
    val statusCode: Int = 0,
    val data: List<Maquina> = listOf()
)

data class Motivo(
    val id: Int,
    val motivo: String
)
data class MotivoResponse(
    val statusCode: Int = 0,
    val data: List<Motivo> = listOf()
)