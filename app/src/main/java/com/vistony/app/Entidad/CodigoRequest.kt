package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

data class CodigoRequest(
    @SerializedName("Barra") val barra: String
)
