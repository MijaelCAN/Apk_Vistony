package com.vistony.app.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vistony.app.Entidad.LoginRequest
import com.vistony.app.Entidad.LoginResponse
import com.vistony.app.Service.AuthService
import com.vistony.app.Service.RetrofitConfig
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val authService = RetrofitInstance.loginService

    var _loginstate by mutableStateOf(ResponseState())
        private set

    fun validar(user: String, pass: String) {

        viewModelScope.launch {
            try {
                val response = authService.login(LoginRequest(user, pass))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.data.equals("OK")) {
                        _loginstate = ResponseState(
                            state = true,
                            loginResponse = body,
                            message = "Autorizado"
                        )
                        Log.e("rurta", "entro aui")
                    } else {
                        _loginstate = ResponseState(
                            state = false,
                            message = "No autorizado"
                        )
                        Log.e("erre", "no esroo")
                    }
                } else {
                    _loginstate = ResponseState(
                        state = false,
                        message = "Error en la Respuesta"
                    )
                    Log.e("LoginViewModel", "Error en la Respuesta: ${response?.message()}")
                }

            } catch (e: Exception) {
                _loginstate = ResponseState(
                    state = false,
                    message = "Error de Comunicación"
                )
                Log.e("LoginViewModel", "Error de Comunicacion: $e")
            }
        }
    }
}

data class ResponseState(
    val state: Boolean = false,
    val loginResponse: LoginResponse? = null,
    val message: String? = null
)