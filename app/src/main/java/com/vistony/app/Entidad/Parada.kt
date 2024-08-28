package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

data class Parada(
    var id: Int = 0,
    var maquina: String = "",
    var fechaInicio: String = "",// tiene que ser un LocaDateTime var fechaInicio: LocalDateTime? = null,
    var fechaFinal: String = "", // tiene que ser un LocaDateTime var fechaInicio: LocalDateTime? = null,
    var area: String = "",
    var motivoParada: String = "",
    var comentarios: String = ""
)
data class ParadaRequest(
    @SerializedName("U_Fecha")val U_Fecha: String,
    @SerializedName("U_Maquina")val U_Maquina: String,
    @SerializedName("U_Area")val U_Area: String,
    @SerializedName("U_Comentario")val U_Comentario: String,
    @SerializedName("U_Estado")val U_Estado: String,
    @SerializedName("U_FechaIni")val U_FechaIni: String,
    @SerializedName("U_FechaFin")val U_FechaFin: String,
    @SerializedName("U_HoraIni")val U_HoraIni: String,
    @SerializedName("U_HoraFin")val U_HoraFin: String,
    @SerializedName("U_Usuario")val U_Usuario: String,
    @SerializedName("U_MotivoParaMaq")val U_MotivoParaMaq: Int
)
data class ParadaResponse(
    val statusCode: Int = 0,
    var data: List<Parada> = emptyList()
)
data class PostParada(
    val statusCode: Int = 0,
    val data: String = "Cargando..."
)

data class ParadaStopRequest(
    var id: Int = 0,
    var fechaFinal: String = ""
)
data class Area(
    val Code: String,
    val Name: String
)
data class AreaResponse(
    val statusCode: Int = 0,
    val data: List<Area> = listOf()
)

data class Maquina(
    val Code: String,
    val Name: String
)
data class MaquinaResponse(
    val statusCode: Int = 0,
    val data: List<Maquina> = listOf()
)

data class Motivo(
    val Code: Int,
    val Name: String
)
data class MotivoResponse(
    val statusCode: Int = 0,
    val data: List<Motivo> = listOf()
)