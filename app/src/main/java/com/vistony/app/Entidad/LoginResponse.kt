package com.vistony.app.Entidad

data class LoginResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: UserResponse = UserResponse()
)

data class Data(
    val accessToken: String = "",
    val refreshToken: String = "",
    val expiresIn: Int = 0,
    val user: UserResponse = UserResponse()
)

data class UserResponse(
    val id: Int = 0,
    val dni: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val position: String = "",
    val avatar: String = "",
    val lastLogin: String = ""
)
