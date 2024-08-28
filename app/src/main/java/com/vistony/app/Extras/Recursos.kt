package com.vistony.app.Extras

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
/*
@RequiresApi(Build.VERSION_CODES.O)
fun formatoFecha(fechaHora: LocalDateTime): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    return LocalDateTime.parse(fechaHora, formatter)
}*/
@RequiresApi(Build.VERSION_CODES.O)
fun formatoFecha(fechaHora: LocalDateTime): LocalDateTime{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(fechaHora.toString(), formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatoServidor(fechaHora: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return fechaHora.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatoUsuario(fechaHora: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return fechaHora.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatoHora(fechaHora: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return fechaHora.format(formatter)
}
