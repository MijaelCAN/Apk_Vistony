package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

data class Parada(
    var DocEntry: String = "0",
    var Maquina: String = "",
    var FechaHoraInicio: String = "",
    var FechaHoraFin: String = null.toString(),
    var Area: String = "",
    var Comentario: String ="",
    var Motivo: String =""
)
data class ParadaRequest(
    @SerializedName("U_Fecha")val U_Fecha: String,
    @SerializedName("U_Maquina")val U_Maquina: String,
    @SerializedName("U_Area")val U_Area: String,
    @SerializedName("U_Comentario")val U_Comentario: String,
    @SerializedName("U_Estado")val U_Estado: String,
    @SerializedName("U_FechaIni")val U_FechaIni: String,
    //@SerializedName("U_FechaFin")val U_FechaFin: String,
    @SerializedName("U_HoraIni")val U_HoraIni: String,
    //@SerializedName("U_HoraFin")val U_HoraFin: String,
    @SerializedName("U_Usuario")val U_Usuario: String,
    @SerializedName("U_MotivoParaMaq")val U_MotivoParaMaq: Int
)

data class ListaRequest(
    @SerializedName("FechaIni")val FechaIni: String,
    @SerializedName("FechaFin")val FechaFin: String,
    @SerializedName("Estado")val Estado: String,
)

data class ParadaResponse(
    val statusCode: Int = 0,
    var data: List<Parada> = emptyList()
)
data class PostParada(
    val statusCode: Int = 0,
    val data: String = "Cargando..."
)

data class ParadaStopResponse(
    var statusCode: Int = 0,
    var data: String = ""
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