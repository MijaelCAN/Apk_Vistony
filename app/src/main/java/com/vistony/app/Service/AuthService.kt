package com.vistony.app.Service

import com.vistony.app.Entidad.LoginRequest
import com.vistony.app.Entidad.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("Auth")
    suspend fun login(@Body request: LoginRequest):Response<LoginResponse>
}