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
import kotlinx.coroutines.flow.update
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

    fun onResetStateLogin(){
        _isLoading.value = EstadoLogin.Idle
    }


    fun validar(user: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = EstadoLogin.Cargando
            try {
                val response = authService.login(LoginRequest(user, pass))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.success) {
                            _loginstate = ResponseState(
                                state = true,
                                loginResponse = body,
                                message = "Autorizado"
                            )
                            _userData.value = body.data
                            _isLoading.value = EstadoLogin.Exitoso

                        } else { handleError("Usuario no registrado") }
                    }else { handleError("Error en la respuesta del servidor") }
                } else { handleError("Error en la respuesta: ${response.message()}")}
            } catch (e: Exception) {handleError("Error de comunicación: $e")}
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