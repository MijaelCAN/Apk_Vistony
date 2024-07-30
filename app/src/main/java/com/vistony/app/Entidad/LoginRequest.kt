package com.vistony.app.Entidad

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("User") val user: String,
    @SerializedName("PassWord") val password: String
)
