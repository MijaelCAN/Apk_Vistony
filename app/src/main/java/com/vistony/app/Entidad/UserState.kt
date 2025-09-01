package com.vistony.app.Entidad

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class UserState {
    var currentUser: UserResponse by mutableStateOf(UserResponse())
        private set

    fun setUser(user: UserResponse) {
        currentUser = user
    }

    fun clearUser() {
        currentUser = UserResponse()
    }

    fun hasRole(role: String): Boolean {
        return currentUser?.role?.equals(role, ignoreCase = true) ?: false
    }

    fun hasAnyRole(vararg roles: String): Boolean {
        return roles.any { hasRole(it) }
    }
}