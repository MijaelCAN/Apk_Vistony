package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

data class Parada(
    @SerializedName("docEntry") var DocEntry: String = "0",
    @SerializedName("maquina") var Maquina: String = "",
    @SerializedName("fechaHoraInicio") var FechaHoraInicio: String = "",
    @SerializedName("fechaHoraFin") var FechaHoraFin: String? = null,
    @SerializedName("area") var Area: String = "",
    @SerializedName("comentario") var Comentario: String = "",
    @SerializedName("motivo") var Motivo: String = "",
    @SerializedName("usuario") var Usuario: String = "",
    var UserMantemiento: String = "", // Campo interno para lógica de UI
)
data class ParadaRequest(
    @SerializedName("U_Fecha")val U_Fecha: String,
    @SerializedName("U_Maquina")val U_Maquina: String,
    @SerializedName("U_Area")val U_Area: String,
    @SerializedName("U_OrdenMezcla")val U_OrdenMezcla: String,
    @SerializedName("U_Comentario")val U_Comentario: String,
    @SerializedName("U_Estado")val U_Estado: String,
    @SerializedName("U_FechaIni")val U_FechaIni: String,
    //@SerializedName("U_FechaFin")val U_FechaFin: String,
    @SerializedName("U_HoraIni")val U_HoraIni: String,
    //@SerializedName("U_HoraFin")val U_HoraFin: String,
    @SerializedName("U_Usuario")val U_Usuario: String,
    @SerializedName("U_MotivoParaMaq")val U_MotivoParaMaq: String
)

data class ListaRequest(
    @SerializedName("FechaIni")val FechaIni: String?,
    @SerializedName("FechaFin")val FechaFin: String?,
    @SerializedName("Estado")val Estado: String,
    @SerializedName("DNI")val DNI: String? = null,
)

data class ParadaResponse(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") var data: List<Parada> = emptyList()
)
data class PostParada(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: String = "Cargando..."
)

data class ParadaStopResponse(
    @SerializedName("statusCode") var statusCode: Int = 0,
    @SerializedName("success") var success: Boolean = false,
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: String = ""
)
data class Area(
    @SerializedName("code") val Code: String,
    @SerializedName("name") val Name: String
)
data class AreaResponse(
    val statusCode: Int = 0,
    val success: Boolean = false,
    val message: String = "",
    val data: List<Area> = listOf()
)

data class Maquina(
    @SerializedName("code") val Code: String,
    @SerializedName("name") val Name: String
)
data class MaquinaResponse(
    val statusCode: Int = 0,
    val data: List<Maquina> = listOf()
)

data class Motivo(
    @SerializedName("code") val Code: String,
    @SerializedName("name") val Name: String
)
data class MotivoResponse(
    val statusCode: Int = 0,
    val data: List<Motivo> = listOf()
)