package com.vistony.app.ViewModel

import android.app.Application
import android.content.Context
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
import com.google.firebase.messaging.FirebaseMessaging
import com.vistony.app.Repository.FirestoreRepository
import com.vistony.app.Service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

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

    private val sharedPreferences = application.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    // Función para guardar credenciales
    fun saveCredentials(username: String, password: String, remember: Boolean) {
        if (remember) {
            sharedPreferences.edit()
                .putString("saved_username", username)
                .putString("saved_password", password)
                .putBoolean("remember_credentials", true)
                .apply()
        } else {
            clearSavedCredentials()
        }
    }

    // Función para recuperar credenciales guardadas
    fun getSavedCredentials(): Triple<String, String, Boolean> {
        val username = sharedPreferences.getString("saved_username", "") ?: ""
        val password = sharedPreferences.getString("saved_password", "") ?: ""
        val remember = sharedPreferences.getBoolean("remember_credentials", false)
        return Triple(username, password, remember)
    }

    // Función para limpiar credenciales guardadas
    private fun clearSavedCredentials() {
        sharedPreferences.edit()
            .remove("saved_username")
            .remove("saved_password")
            .putBoolean("remember_credentials", false)
            .apply()
    }
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
                            
                            // Guardar información del usuario en SharedPreferences para FCM
                            sharedPreferences.edit()
                                .putString("current_user_id", body.data.dni)
                                .putString("current_user_role", body.data.role)
                                .putString("current_user_name", body.data.name)
                                .apply()
                            
                            // Obtener y guardar el token FCM en Firestore
                            launch {
                                try {
                                    val fcmPrefs = application.getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
                                    var token = fcmPrefs.getString("fcm_token", null)
                                    
                                    // Si no hay token guardado, obtenerlo de Firebase
                                    if (token == null) {
                                        token = FirebaseMessaging.getInstance().token.await()
                                        fcmPrefs.edit().putString("fcm_token", token).apply()
                                    }
                                    
                                    // Guardar token en Firestore
                                    if (token != null) {
                                        firestoreRepository.saveUserToken(
                                            userId = body.data.dni,
                                            token = token,
                                            userRole = body.data.role,
                                            userName = body.data.name
                                        ).onSuccess {
                                            Log.d("LoginViewModel", "Token FCM guardado exitosamente")
                                        }.onFailure { error ->
                                            Log.e("LoginViewModel", "Error al guardar token FCM", error)
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("LoginViewModel", "Error al obtener/guardar token FCM", e)
                                }
                            }

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
        // Obtener userId antes de limpiar SharedPreferences
        val userId = sharedPreferences.getString("current_user_id", null)
        
        _userData.value = null
        
        // Limpiar información del usuario de SharedPreferences
        sharedPreferences.edit()
            .remove("current_user_id")
            .remove("current_user_role")
            .remove("current_user_name")
            .apply()
        
        // Opcional: Eliminar token de Firestore al cerrar sesión
        if (userId != null) {
            viewModelScope.launch {
                firestoreRepository.deleteUserToken(userId)
            }
        }
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