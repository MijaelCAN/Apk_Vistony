package com.vistony.app.Repository

import android.util.Log
import com.vistony.app.Entidad.*
import com.vistony.app.Service.RetrofitInstance
import com.vistony.app.Service.TemperaturaService
import javax.inject.Inject
import javax.inject.Singleton

class TemperaturaRepository @Inject constructor(

) {

    private val temperaturaService = RetrofitInstance.temperaturaService


    suspend fun obtenerTemperaturas(
        id: String,
        fechaInicio: String?,
        fechaFin: String?,
        auxiliar: String?
    ): Result<TemperaturaResponse> {
        return try {
            Log.d("TemperaturaRepository", "Obteniendo temperaturas - ID: $id, Fechas: $fechaInicio - $fechaFin")
            
            val response = temperaturaService.obtenerTemperaturas(
                ListRequest(id.toInt(), auxiliar ?: "soplado", fechaInicio, fechaFin)
            )
            
            Log.d("TemperaturaRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("TemperaturaRepository", "Body - Success: ${body?.success}, Cantidad: ${body?.data?.size}")
                Result.success(body ?: TemperaturaResponse())
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                Log.e("TemperaturaRepository", "Error obteniendo temperaturas: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("TemperaturaRepository", "Excepción al obtener temperaturas", e)
            Result.failure(e)
        }
    }

    suspend fun registrarTemperatura(request: TemperaturaRequest): Result<PostTemperaturaResponse> {
        return try {
            Log.d("TemperaturaRepository", "Iniciando registro de temperatura")
            Log.d("TemperaturaRepository", "Request: OT=${request.ot}, Temperatura=${request.temperatura}, DNI=${request.dni}")
            
            val response = temperaturaService.registrarTemperatura(request)
            
            Log.d("TemperaturaRepository", "Respuesta recibida - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("TemperaturaRepository", "Body no es null - Success: ${body.success}, Message: ${body.message}")
                    Log.d("TemperaturaRepository", "StatusCode: ${body.statusCode}, Data size: ${body.data.size}")
                    Result.success(body)
                } else {
                    Log.w("TemperaturaRepository", "Body es null pero la respuesta es exitosa")
                    // Si la respuesta es exitosa pero el body es null, crear una respuesta por defecto
                    Result.success(PostTemperaturaResponse(
                        statusCode = response.code(),
                        success = true,
                        message = "Temperatura registrada correctamente"
                    ))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("TemperaturaRepository", "Error del servidor - Código: ${response.code()}, ErrorBody: $errorBody")
                Result.failure(Exception("Error del servidor: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("TemperaturaRepository", "Excepción al registrar temperatura", e)
            Result.failure(Exception("Error al registrar temperatura: ${e.message}"))
        }
    }

    suspend fun obtenerTemperaturaPorId(id: String): Result<Temperatura> {
        return try {
            // TODO: Implementar cuando el backend tenga este endpoint
            // Por ahora retornamos un error
            Result.failure(Exception("Endpoint no implementado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun consultarOT(ot: String): Result<ConsultaOTResponse> {
        return try {
            Log.d("TemperaturaRepository", "Consultando OT: $ot")
            
            val response = temperaturaService.consultarOT(ot)
            
            Log.d("TemperaturaRepository", "Respuesta OT - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("TemperaturaRepository", "Body no es null - Success: ${body?.success}, Items: ${body?.data?.size}")
                Result.success(body ?: ConsultaOTResponse())
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                Log.e("TemperaturaRepository", "Error consultando OT: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("TemperaturaRepository", "Excepción al consultar OT", e)
            Result.failure(e)
        }
    }
}





