package com.vistony.app.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.Parada
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ActividadViewModel @Inject constructor(): ViewModel() {


    private val _actividades = MutableStateFlow<List<Actividad>>(emptyList())
    val actividades: MutableStateFlow<List<Actividad>> = _actividades


    fun crearActividad(actividad: Actividad){
        Log.e("MDCR", "CREAR ACTIVIDAD ${actividad}")
        _actividades.value += actividad
    }

    fun actualizarActividad(parada: Parada) {

    }

    fun eliminarActividad(parada: Parada) {

    }

    fun obtenerActividadPorId(id: String) {

    }

    /*fun setStatusActividad(actividad: Actividad){
        var currentActividad = _actividades.value.toMutableList()
        currentActividad
        _actividades.value = currentActividad
    }*/

    fun setStatusActividad(indexOf: Int) {
        val currentActividades = _actividades.value.toMutableList()
        //val index = currentActividades.indexOfFirst { it.id == actividad.id }
        if (indexOf != -1) {
            // Crear una copia con statusActividad modificado a 'Y'
            val updatedActividad = currentActividades[indexOf].copy(statusActividad = !_actividades.value[indexOf].statusActividad)
            currentActividades[indexOf] = updatedActividad
            _actividades.value = currentActividades
        }
    }


}