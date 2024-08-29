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

@RequiresApi(Build.VERSION_CODES.O)
fun formatoFecha(fecha: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return LocalDateTime.parse(fecha, formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertirFecha(fechaOriginal: String): String {
    // Formateador para la fecha original
    val formateadorEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    // Parseamos la cadena a LocalDateTime
    val fechaParseada = LocalDateTime.parse(fechaOriginal, formateadorEntrada)

    // Formateador para la fecha en el nuevo formato
    val formateadorSalida = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Convertimos a la cadena en el nuevo formato
    return fechaParseada.format(formateadorSalida)
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertirFecha2(fechaOriginal: String): LocalDateTime {
    // Formateador para la fecha original
    val fechaLimpia = fechaOriginal.trim().replace("\\s+".toRegex(), " ")
    val formateadorEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    // Parseamos la cadena a LocalDateTime
    return LocalDateTime.parse(fechaLimpia, formateadorEntrada)
}
