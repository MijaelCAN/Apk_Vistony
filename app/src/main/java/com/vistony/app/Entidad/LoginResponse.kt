package com.vistony.app.Entidad

data class LoginResponse(
    val statusCode: Int = 0,
    val message: String = "",
    val data: Any? = null
)

data class Data(
    val accessToken: String = "",
    val refreshToken: String = "",
    val expiresIn: Int = 0,
    val user: UserResponse = UserResponse()
)

data class UserResponse(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val role: String = "mantenimiento",
    val position: String = "",
    val avatar: String = "",
    val lastLogin: String = ""
)
