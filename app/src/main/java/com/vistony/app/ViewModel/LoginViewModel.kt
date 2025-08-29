package com.vistony.app.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vistony.app.Entidad.Data
import com.vistony.app.Entidad.LoginRequest
import com.vistony.app.Entidad.LoginResponse
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val authService = RetrofitInstance.loginService
    private val gson = Gson()

    var _loginstate by mutableStateOf(ResponseState())
        private set
    private val _isLoading = MutableStateFlow<EstadoLogin>(EstadoLogin.Idle)
    val isLoading: StateFlow<EstadoLogin> = _isLoading.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _userData = MutableStateFlow<UserResponse?>(null)
    val userData: StateFlow<UserResponse?> = _userData.asStateFlow()

    val listaUsuarios = listOf(
        UserResponse(
            id = 1,
            name = "Carlos Méndez",
            email = "carlos.mendez@example.com",
            role =  "mantenimiento",
            position = "Supervisor",
            avatar = "https://randomuser.me/api/portraits/men/32.jpg",
            lastLogin = "2025-08-19T14:30:00"
        ),
        UserResponse(
            id = 2,
            name = "María López",
            email = "maria.lopez@example.com",
            role = "mantenimiento",
            position = "Supervisor",
            avatar = "https://randomuser.me/api/portraits/women/45.jpg",
            lastLogin = "2025-08-20T08:50:00"
        ),
        UserResponse(
            id = 3,
            name = "Juan Pérez",
            email = "juan.perez@example.com",
            role = "operador",
            position = "Supervisor",
            avatar = "https://randomuser.me/api/portraits/men/52.jpg",
            lastLogin = "2025-08-19T22:10:00"
        ),
        UserResponse(
            id = 4,
            name = "Sofía Ramírez",
            email = "sofia.ramirez@example.com",
            role = "operador",
            position = "Supervisor",
            avatar = "https://randomuser.me/api/portraits/women/58.jpg",
            lastLogin = "2025-08-18T17:45:00"
        ),
        UserResponse(
            id = 5,
            name = "Miguel Torres",
            email = "miguel.torres@example.com",
            role = "operador",
            position = "Supervisor",
            avatar = "https://randomuser.me/api/portraits/men/40.jpg",
            lastLogin = "2025-08-20T07:20:00"
        )
    )


    fun validar(user: String, pass: String) {

        Log.e("MDCR", "user: $user pass: $pass")
        viewModelScope.launch {
            _isLoading.value = EstadoLogin.Cargando
            try {
                val response = authService.login(LoginRequest(user, pass))

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {

                        val userResponse = parseUserData(body)

                        if(userResponse != null){
                            _loginstate = ResponseState(
                                state = true,
                                loginResponse = body,
                                message = "Autorizado"
                            )
                            _userData.value = userResponse
                            _isLoading.value = EstadoLogin.Exitoso
                        } else{
                            // Si no hay datos de usuario, usar valor por defecto
                            val defaultUser = listaUsuarios.firstOrNull { it.role == "mantenimiento" }
                            _userData.value = defaultUser
                            _loginstate = ResponseState(
                                state = true,
                                loginResponse = body,
                                message = "Autorizado (rol por defecto)"
                            )
                            _isLoading.value = EstadoLogin.Exitoso
                        }




                        _isLoading.value = EstadoLogin.Exitoso
                        //_userRole.value = body.userRole
                        _userRole.value = "mantenimiento"
                        //_userRole.value = "operador"
                        Log.e("rurta", "entro aui")
                    } else { handleError("Respuesta Vacia") }
                } else { handleError("Error en la respuesta: ${response.message()}")}
            } catch (e: Exception) {handleError("Error de comunicación: $e")}
        }
    }

    private fun parseUserData(loginResponse: LoginResponse): UserResponse? {
        return when (val data = loginResponse.data) {
            is String -> {
                if (data.isBlank()) {
                    null // String vacío
                } else {
                    try {
                        // Intentar parsear si es un JSON string
                        gson.fromJson(data, Data::class.java).user
                    } catch (e: Exception) {
                        null // No es JSON válido
                    }
                }
            }
            is Map<*, *> -> {
                // Si data ya es un objeto mapeado
                try {
                    val json = gson.toJson(data)
                    gson.fromJson(json, Data::class.java).user
                } catch (e: Exception) {
                    null
                }
            }
            else -> null
        }
    }

    private fun handleError(message: String) {
        _loginstate = ResponseState(state = false, message = message)
        _isLoading.value = EstadoLogin.Error(message)
        Log.e("LoginViewModel", message)
    }

    fun clearUserData() {
        _userData.value = null
    }
}
data class ResponseState(
    val state: Boolean = false,
    val loginResponse: LoginResponse = LoginResponse(),
    val message: String = ""
)

sealed class EstadoLogin {
    object Idle : EstadoLogin()
    object Cargando : EstadoLogin()
    object Exitoso : EstadoLogin()
    data class Error(val mensaje: String) : EstadoLogin()
}