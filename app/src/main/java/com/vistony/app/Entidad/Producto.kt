package com.vistony.app.Entidad

data class Producto(
    val Producto:String = "",
    val UM:String = "",
    val Linea: String = "",
    val CantidadPlanificada: Int = 0
)

data class ProductoResponse(
    val statusCode: Int = 0,
    val data: Producto = Producto()
)