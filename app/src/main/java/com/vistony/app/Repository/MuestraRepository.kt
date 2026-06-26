package com.vistony.app.Repository

import android.util.Log
import com.google.gson.Gson
import com.vistony.app.Entidad.*
import com.vistony.app.Service.MuestraService
import com.vistony.app.Service.RetrofitInstance
import javax.inject.Inject
import javax.inject.Singleton

class MuestraRepository @Inject constructor() {

    val muestraService =  RetrofitInstance.muestraService
    val muestraProduccionService = RetrofitInstance.muestraProduccionService

    // Las respuestas de error de la API v1 vienen como { "error": { "code", "details", "message" } }
    private fun mensajeErrorApi(response: retrofit2.Response<*>): String {
        val cuerpoError = response.errorBody()?.string()
        val mensaje = try {
            Gson().fromJson(cuerpoError, ApiErrorResponse::class.java)?.error?.message
        } catch (e: Exception) {
            null
        }
        return mensaje ?: "Error del servidor: ${response.message()}"
    }

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

    suspend fun crearMaterialEmpleado(docEntry: String, request: MaterialEmpleadoCreateRequest): Result<MaterialEmpleadoCreateResponse> {
        return try {
            val response = muestraService.crearMaterialEmpleado(docEntry, request)
            if (response.isSuccessful) {
                Result.success(response.body() ?: MaterialEmpleadoCreateResponse(400, false, "Error desconocido", null))
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearEvaluacionProduccion(docEntry: String, request: EvaluacionProduccionCreateRequest): Result<EvaluacionProduccionCreateResponse> {
        return try {
            val response = muestraService.crearEvaluacionProduccion(docEntry, request)
            if (response.isSuccessful) {
                Result.success(response.body() ?: EvaluacionProduccionCreateResponse(400, false, "Error desconocido", null))
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
                //android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}, DocEntry: ${body?.data[0]?.docEntry}")
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
    suspend fun consultarProductoMuestra(code: String): Result<ConsultaProductoMuestraResponse> {
        return try {
            android.util.Log.d("MuestraRepository", "Consultando producto muestra - Code: $code")

            val response = muestraService.consultarProductoMuestra(code)

            android.util.Log.d("MuestraRepository", "Respuesta muetsra - Código: ${response.code()}, Éxito: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}, Cantidad: ${body?.data?.size}")
                Result.success(body ?: ConsultaProductoMuestraResponse())
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                android.util.Log.e("MuestraRepository", "Error consultando producto muestra: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al consultar producto muestra", e)
            Result.failure(e)
        }
    }
    
    suspend fun obtenerEspecificacionSoplado(codProducto: String): Result<EspecificacionSopladoResponse> {
        return try {
            android.util.Log.d("MuestraRepository", "Obteniendo especificación soplado - CodProducto: $codProducto")

            val response = muestraService.obtenerEspecificacionSoplado(codProducto)
            
            android.util.Log.d("MuestraRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}")
                Result.success(body ?: EspecificacionSopladoResponse(400, false, "Error desconocido", null))
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                android.util.Log.e("MuestraRepository", "Error obteniendo especificación: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al obtener especificación", e)
            Result.failure(e)
        }
    }
    
    suspend fun obtenerRegistrosLlegada(fechaInicio: String? = null, fechaFin: String? = null, code: String? = null): Result<RegistroLlegadaResponse> {
        return try {
            android.util.Log.d("MuestraRepository", "Obteniendo registros de llegada")
            android.util.Log.d("MuestraRepository", "FechaInicio: $fechaInicio, FechaFin: $fechaFin, Code: $code")
            
            val response = muestraService.obtenerRegistrosLlegada(fechaInicio, fechaFin, code)
            
            android.util.Log.d("MuestraRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}, Cantidad: ${body?.data?.size}")
                Result.success(body ?: RegistroLlegadaResponse(400, false, "Error desconocido", emptyList()))
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                android.util.Log.e("MuestraRepository", "Error obteniendo registros de llegada: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al obtener registros de llegada", e)
            Result.failure(e)
        }
    }
    
    suspend fun crearRegistroLlegada(request: RegistroLlegadaCreateRequest): Result<RegistroLlegadaCreateResponse> {
        return try {
            android.util.Log.d("MuestraRepository", "Creando registro de llegada")
            android.util.Log.d("MuestraRepository", "Request: $request")

            val response = muestraService.crearRegistroLlegada(request)

            android.util.Log.d("MuestraRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - StatusCode: ${body?.statusCode}, Success: ${body?.success}, Message: ${body?.message}")
                Result.success(body ?: RegistroLlegadaCreateResponse(400, false, "Error desconocido", null))
            } else {
                val errorMessage = "Error del servidor: ${response.message()}"
                android.util.Log.e("MuestraRepository", "Error creando registro de llegada: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al crear registro de llegada", e)
            Result.failure(e)
        }
    }

    suspend fun confirmarRecepcion(request: ConfirmarRecepcionRequest): Result<ConfirmarRecepcionResponse> {
        return try {
            android.util.Log.d("MuestraRepository", "Confirmando recepción - Orden: ${request.ordenFabricacion}, Muestra: ${request.nMuestra}")

            val response = muestraService.confirmarRecepcion(request)

            android.util.Log.d("MuestraRepository", "Respuesta - Código: ${response.code()}, Éxito: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val body = response.body()
                android.util.Log.d("MuestraRepository", "Body - Success: ${body?.success}, Message: ${body?.message}")
                Result.success(body ?: ConfirmarRecepcionResponse(400, false, "Error desconocido", null))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = when (response.code()) {
                    400 -> "La muestra ya fue confirmada anteriormente"
                    404 -> "Registro no encontrado"
                    else -> "Error del servidor: ${response.message()}"
                }
                android.util.Log.e("MuestraRepository", "Error confirmando recepción (${ response.code()}): $errorBody")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("MuestraRepository", "Excepción al confirmar recepción", e)
            Result.failure(e)
        }
    }

    suspend fun obtenerMuestrasProduccion(fechaInicio: String, fechaFin: String): Result<MuestraProduccionResponse> {
        return try {
            val response = muestraProduccionService.obtenerMuestrasProduccion(fechaInicio, fechaFin)
            if (response.isSuccessful) {
                Result.success(response.body() ?: MuestraProduccionResponse(emptyList()))
            } else {
                Result.failure(Exception("Error del servidor: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerMuestraProduccionDetalle(docEntry: String): Result<MuestraProduccionDetalle> {
        return try {
            val response = muestraProduccionService.obtenerMuestraProduccionDetalle(docEntry)
            Log.d("MuestraRepository", "Response: $response")
            if (response.isSuccessful) {
                Log.d("MuestraRepository", "Response: $response")
                val body = response.body()
                Log.d("MuestraRepository", "Body0: $body")
                if (body != null) {
                    Log.d("MuestraRepository", "Data: ${body.data}")
                    Result.success(body.data)
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

    suspend fun consultarNuevaMuestra(numOf: String, numEn: String?, type: String): Result<List<ConsultaNuevaMuestra>> {
        return try {
            val response = muestraProduccionService.consultarNuevaMuestra(numOf, numEn, type)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception(mensajeErrorApi(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearMuestraProduccion(request: CrearMuestraProduccionRequest): Result<CrearMuestraProduccionResponse> {
        return try {
            val response = muestraProduccionService.crearMuestraProduccion(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception(mensajeErrorApi(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun iniciarAnalisis(docEntry: String, userStartAnalysis: String): Result<IniciarAnalisisResponse> {
        return try {
            val response = muestraProduccionService.iniciarAnalisis(docEntry, IniciarAnalisisRequest(userStartAnalysis))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception(mensajeErrorApi(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun finalizarAnalisis(docEntry: String, request: FinalizarAnalisisRequest): Result<FinalizarAnalisisResponse> {
        return try {
            val response = muestraProduccionService.finalizarAnalisis(docEntry, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception(mensajeErrorApi(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
