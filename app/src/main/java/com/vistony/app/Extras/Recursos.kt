package com.vistony.app.Extras

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
fun formatoFecha(fechaHora: LocalDateTime): LocalDateTime{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(fechaHora.toString(), formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatoServidor(fechaHora: LocalDateTime?): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return fechaHora?.format(formatter) ?: ""
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


fun randomMutedColor(): Color {
    // Genera valores RGB entre 80 y 180 para evitar colores muy claros u oscuros
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)

    // Opcional: para un efecto más mate, mezcla con gris (128,128,128) en proporción 0.5
    val mixFactor = 0.8f
    val r = (red * (1 - mixFactor) + 255  * mixFactor).toInt()
    val g = (green * (1 - mixFactor) + 255  * mixFactor).toInt()
    val b = (blue * (1 - mixFactor) + 255  * mixFactor).toInt()

    return Color(r, g, b)
}
