package com.vistony.app.Repository

import com.vistony.app.Entidad.*
import com.vistony.app.Service.MuestraService
import com.vistony.app.Service.RetrofitInstance
import javax.inject.Inject
import javax.inject.Singleton

class MuestraRepository @Inject constructor() {

    val muestraService =  RetrofitInstance.muestraService

    suspend fun obtenerMuestras(dni: String, fechaInicio: String, fechaFin: String, auxiliar: String?): Result<MuestraResponse> {
        return try {
            val response = muestraService.obtenerMuestras(CabeceraRequest(dni.toInt(), auxiliar?:"soplado",  fechaInicio, fechaFin))
            if (response.isSuccessful) {
                Result.success(response.body() ?: MuestraResponse(400, false, "No data", emptyList()))
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearMuestra(request: MuestraCreateRequest): Result<MuestraCreateResponse> {
        return try {
            val response = muestraService.crearMuestra(request)
            if (response.isSuccessful) {
                Result.success(response.body() ?: MuestraCreateResponse(400, false, "Error desconocido", ""))
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearCheckList(docEntry: String, request: CheckListCreateRequest): Result<CheckListCreateResponse> {
        return try {
            val response = muestraService.crearCheckList(docEntry, request)
            if (response.isSuccessful) {
                Result.success(response.body() ?: CheckListCreateResponse(400, false, "Error desconocido", null))
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearInspeccionDimensional(docEntry: String, request: InspeccionDimensionalCreateRequest): Result<InspeccionDimensionalCreateResponse> {
        return try {
            val response = muestraService.crearInspeccionDimensional(docEntry, request)
            if (response.isSuccessful) {
                Result.success(response.body() ?: InspeccionDimensionalCreateResponse(400, false, "Error desconocido", null))
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrarMuestra(request: MuestraRequest): Result<PostMuestraResponse> {
        return try {
            val response = muestraService.registrarMuestra(request)
            if (response.isSuccessful) {
                Result.success(response.body() ?: PostMuestraResponse(false, "Error desconocido"))
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerMuestraPorId(id: String): Result<MuestraCompleta> {
        return try {
            val response = muestraService.obtenerMuestraPorId(id)
            if (response.isSuccessful) {
                val data = response.body()?.data?.firstOrNull()
                if (data != null) {
                    Result.success(data)
                } else {
                    Result.failure(Exception("Muestra no encontrada"))
                }
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun obtenerMuestraDetalle(docEntry: String): Result<MuestraDetalleResponse> {
        return try {
            android.util.Log.d("MuestraRepository", "Obteniendo detalle de muestra - DocEntry: $docEntry")
            
            val response = muestraService.obtenerMuestraDetalle(docEntry)
            
            android.util.Log.d("MuestraRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}, DocEntry: ${body?.data?.docEntry}")
                Result.success(body ?: MuestraDetalleResponse())
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                android.util.Log.e("MuestraRepository", "Error obteniendo detalle: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al obtener detalle", e)
            Result.failure(e)
        }
    }
    
    suspend fun obtenerProductos(): Result<ProductoResponseTemp> {
        return try {
            android.util.Log.d("MuestraRepository", "Obteniendo productos")
            
            val response = muestraService.obtenerProductos()
            
            android.util.Log.d("MuestraRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}, Cantidad: ${body?.data?.size}")
                Result.success(body ?: ProductoResponseTemp())
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                android.util.Log.e("MuestraRepository", "Error obteniendo productos: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al obtener productos", e)
            Result.failure(e)
        }
    }
    
    suspend fun consultarProducto(code: String): Result<ConsultaProductoResponse> {
        return try {
            android.util.Log.d("MuestraRepository", "Consultando producto - Code: $code")
            
            val response = muestraService.consultarProducto(code)
            
            android.util.Log.d("MuestraRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}, Cantidad: ${body?.data?.size}")
                Result.success(body ?: ConsultaProductoResponse())
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                android.util.Log.e("MuestraRepository", "Error consultando producto: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al consultar producto", e)
            Result.failure(e)
        }
    }
}



