package com.vistony.app.Entidad


data class Operario(
    var ID: String,
    var Nonbre:String
)

data class OperarioResponse(
    val statusCode: Int,
    val data: List<Operario>
)
