package com.vistony.app.Extras

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.vistony.app.Entidad.ConsultaNuevaMuestra
import com.vistony.app.Entidad.ConsultaNuevaMuestraResponse
import com.vistony.app.Entidad.UltimoIntentoMuestra
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.snapshots.SnapshotStateList

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(formatter) ?: "")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(json?.asString, formatter)
    }
}

class SnapshotStateListAdapter<T> : JsonSerializer<SnapshotStateList<T>> {
    override fun serialize(
        src: SnapshotStateList<T>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonArray().apply {
            src?.forEach { item ->
                add(context?.serialize(item))
            }
        }
    }
}

// El backend a veces responde "data" como una lista y a veces como un solo objeto
// (cuando hay un único resultado). Este adapter normaliza ambos casos a List<ConsultaNuevaMuestra>.
class ConsultaNuevaMuestraResponseDeserializer : JsonDeserializer<ConsultaNuevaMuestraResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ConsultaNuevaMuestraResponse {
        val jsonObject = json?.asJsonObject
        val statusCode = jsonObject?.get("statusCode")?.takeIf { !it.isJsonNull }?.asInt ?: 0
        val success = jsonObject?.get("success")?.takeIf { !it.isJsonNull }?.asBoolean ?: false
        val message = jsonObject?.get("message")?.takeIf { !it.isJsonNull }?.asString
        val dataElement = jsonObject?.get("data")

        val data: List<ConsultaNuevaMuestra> = when {
            dataElement == null || dataElement.isJsonNull -> emptyList()
            dataElement.isJsonArray -> context?.deserialize<List<ConsultaNuevaMuestra>>(
                dataElement,
                object : TypeToken<List<ConsultaNuevaMuestra>>() {}.type
            ) ?: emptyList()
            else -> listOfNotNull(
                context?.deserialize<ConsultaNuevaMuestra>(dataElement, ConsultaNuevaMuestra::class.java)
            )
        }

        return ConsultaNuevaMuestraResponse(statusCode, success, message, data)
    }
}

// "ultimoIntento" también llega inconsistente: a veces un objeto único, a veces una lista
// de intentos (se toma el primero, que es el único caso visto hasta ahora).
class ConsultaNuevaMuestraDeserializer : JsonDeserializer<ConsultaNuevaMuestra> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ConsultaNuevaMuestra {
        val jsonObject = json?.asJsonObject
        val docEntry = jsonObject?.get("DocEntry")?.takeIf { !it.isJsonNull }?.asString ?: ""
        val codePreview = jsonObject?.get("codePreview")?.takeIf { !it.isJsonNull }?.asString
        val counterSiguiente = jsonObject?.get("counterSiguiente")?.takeIf { !it.isJsonNull }?.asInt
        val descripcion = jsonObject?.get("descripcion")?.takeIf { !it.isJsonNull }?.asString ?: ""
        val lote = jsonObject?.get("lote")?.takeIf { !it.isJsonNull }?.asString ?: ""
        val numEn = jsonObject?.get("numEn")?.takeIf { !it.isJsonNull }?.asString
        val numOf = jsonObject?.get("numOf")?.takeIf { !it.isJsonNull }?.asString ?: ""
        val type = jsonObject?.get("type")?.takeIf { !it.isJsonNull }?.asString ?: ""
        val versionSiguiente = jsonObject?.get("versionSiguiente")?.takeIf { !it.isJsonNull }?.asString

        val ultimoIntentoElement = jsonObject?.get("ultimoIntento")
        val ultimoIntento: UltimoIntentoMuestra? = when {
            ultimoIntentoElement == null || ultimoIntentoElement.isJsonNull -> null
            ultimoIntentoElement.isJsonArray -> ultimoIntentoElement.asJsonArray
                .firstOrNull { !it.isJsonNull }
                ?.let { context?.deserialize<UltimoIntentoMuestra>(it, UltimoIntentoMuestra::class.java) }
            else -> context?.deserialize<UltimoIntentoMuestra>(ultimoIntentoElement, UltimoIntentoMuestra::class.java)
        }

        return ConsultaNuevaMuestra(
            docEntry = docEntry,
            codePreview = codePreview,
            counterSiguiente = counterSiguiente,
            descripcion = descripcion,
            lote = lote,
            numEn = numEn,
            numOf = numOf,
            type = type,
            ultimoIntento = ultimoIntento,
            versionSiguiente = versionSiguiente
        )
    }
}