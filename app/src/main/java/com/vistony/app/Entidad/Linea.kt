package com.vistony.app.Entidad

data class Linea(
    val ID:String ="",
    val Descripcion: String = ""
)

data class lineaResponse(
    val statusCode: Int = 0,
    val data: List<Linea> = emptyList()
)
