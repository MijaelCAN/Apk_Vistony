package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

data class Evaluacion (
    @SerializedName("U_Fecha") val U_Fecha: String,
    @SerializedName("U_Turno") val U_Turno: String,
    @SerializedName("U_OT") val U_OT: String,
    @SerializedName("U_Peso_Check") val U_Peso_Check: String,
    @SerializedName("U_Peso_Comment") val U_Peso_Comment: String,
    @SerializedName("U_Etiq_Check") val U_Etiq_Check: String,
    @SerializedName("U_Etiq_Comment") val U_Etiq_Comment: String,
    @SerializedName("U_Lot_Check") val U_Lot_Check: String,
    @SerializedName("U_Lot_Comment") val U_Lot_Comment: String,
    @SerializedName("U_Limp_Check") val U_Limp_Check: String,
    @SerializedName("U_Limp_Comment") val U_Limp_Comment: String,
    @SerializedName("U_Sell_Check") val U_Sell_Check: String,
    @SerializedName("U_Sell_Comment") val U_Sell_Comment: String,
    @SerializedName("U_Enc_Check") val U_Enc_Check: String,
    @SerializedName("U_Enc_Comment") val U_Enc_Comment: String,
    @SerializedName("U_Rotulo_Check") val U_Rotulo_Check: String,
    @SerializedName("U_Rotulo_Comment") val U_Rotulo_Comment: String,
    @SerializedName("U_Palet_Check") val U_Palet_Check: String,
    @SerializedName("U_Palet_Comment") val U_Palet_Comment: String,
    @SerializedName("U_Conformidad") val U_Conformidad: String,
    @SerializedName("U_Conformidad_Comment") val U_Conformidad_Comment: String
)

data class EvaluacionResponse(
    val statusCode: Int,
    val data: String
)