package com.vistony.app.Entidad

data class Actividad(
    val id: String,
    val tipoFalla: String, // "mecanico", "electrico", "operativo"
    val tipoParada: String,
    val correctivo: Boolean,
    val comentario: String,
    val fecha: String,
    val usuario: String // ID del usuario que registra la actividad
)