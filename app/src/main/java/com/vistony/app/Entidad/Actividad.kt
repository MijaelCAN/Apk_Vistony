package com.vistony.app.Entidad

import android.net.Uri

data class Actividad(
    val id: String,
    val maquina: String, // ID de la máquina asociada a la actividad
    val tipoFalla: String, // "mecanico", "electrico", "operativo"
    val descripcionActividad: String,
    val causaParada: String,
    val fecha: String,
    val listaImagenes: List<Uri>, // Lista de URIs de las imágenes
    //val listaImagenes: List<String>, // Lista de URIs de los imagenes String
    val statusActividad: Boolean,
    val usuario: String, // ID del usuario que registra la actividad
    val paradaDocEntry: String
)