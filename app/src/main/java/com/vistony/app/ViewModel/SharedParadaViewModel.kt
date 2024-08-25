package com.vistony.app.ViewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import javax.inject.Inject
@HiltViewModel
class SharedParadaViewModel @Inject constructor():ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _paradaShared = MutableStateFlow(Parada())
    @RequiresApi(Build.VERSION_CODES.O)
    val paradaShared: StateFlow<Parada> = _paradaShared.asStateFlow()
    @RequiresApi(Build.VERSION_CODES.O)
    fun setParadaShared(paradaShared: Parada) {
        _paradaShared.value = paradaShared
    }
}

data class Parada @RequiresApi(Build.VERSION_CODES.O) constructor(
    var id: Int = 0,
    var fechaInicio: String = "",// tiene que ser un LocaDateTime var fechaInicio: LocalDateTime? = null,
    var fechaFinal: String = "", // tiene que ser un LocaDateTime var fechaInicio: LocalDateTime? = null,
    var maquina: String = "",
    var area: String = "",
    var motivoParada: String = "",
    var comentarios: String = ""
)