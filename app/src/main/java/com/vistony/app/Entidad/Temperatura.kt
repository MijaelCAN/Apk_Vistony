package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

data class Temperatura(
    @SerializedName("DocEntry") var id: String = "",
    @SerializedName("U_fecRegistro") var fecha: String = "",
    @SerializedName("U_OT") var ot: String = "",
    @SerializedName("U_descripcion") var descripcion: String = "",
    @SerializedName("U_Temperatura") var temperatura: String = "",
    @SerializedName("U_Dni") var dni: String = "",
    @SerializedName("U_UserName") var auxiliar: String = "",
    @SerializedName("U_Observacion") var observaciones: String = ""
)

data class TemperaturaRequest(
    @SerializedName("OT") var ot: String = "",
    @SerializedName("description_OT") var descripcion: String = "",
    @SerializedName("dni") var dni: String = "",
    @SerializedName("userName") var userName: String = "",
    @SerializedName("temperature") var temperatura: Double = 0.0,
    @SerializedName("controlTemperatura") var controlTemperatura: String = "",
    @SerializedName("fec_regsiter") var fechaRegistro: String = "",
    @SerializedName("observation") var observacion: String = ""
)


data class TemperaturaResponse(
    @SerializedName("statusCode") var statusCode: Int = 0,
    @SerializedName("success") var success: Boolean = false,
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: List<Temperatura> = emptyList()
)

data class ListRequest(
    val id: Int,
    val role: String?,
    val fecha_inicio: String?,
    val fecha_fin: String?,
)

data class PostTemperaturaResponse(
    @SerializedName("statusCode") var statusCode: Int = 0,
    @SerializedName("success") var success: Boolean = false,
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: List<Any> = emptyList()
)

data class ConsultaOTItem(
    @SerializedName("OT") var ot: String = "",
    @SerializedName("Producto") var producto: String = ""
)

data class ConsultaOTResponse(
    @SerializedName("statusCode") var statusCode: Int = 0,
    @SerializedName("success") var success: Boolean = false,
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: List<ConsultaOTItem> = emptyList()
)





