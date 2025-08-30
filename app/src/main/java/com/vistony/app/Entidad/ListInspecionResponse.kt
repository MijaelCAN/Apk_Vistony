package com.vistony.app.Entidad

data class Inspeccion(
    val OT: String = "",
    val U_Fecha: String = "",
    val U_Turno: String = "",
    val U_Peso_Check: String = "",
    val U_Peso_Comment: String = "",
    val U_Etiq_Check: String = "",
    val U_Etiq_Comment: String = "",
    val U_Lot_Check: String = "",
    val U_Lot_Comment: String = "",
    val U_Limp_Check: String = "",
    val U_Limp_Comment: String = "",
    val U_Sell_Check: String = "",
    val U_Sell_Comment: String = "",
    val U_Enc_Check: String = "",
    val U_Enc_Comment: String = "",
    val U_Rotulo_Check: String = "",
    val U_Rotulo_Comment: String = "",
    val U_Palet_Check: String = "",
    val U_Palet_Comment: String = "",
    val U_Conformidad: String = "",
    val U_Conformidad_Comment: String = "",
    val U_Usuario: String = "",
    val U_Cantidad: String = "",
    val U_Maquinista: String = ""
)

data class InspecionResponse(
    val statusCode: Int = 0,
    val data: List<Inspeccion> = emptyList()
)

data class InspecionRequest(
    val FechaIni: String,
    val FechaFin: String,
)
