package com.vistony.app.Service

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.reflect.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    //private const val BASE_URL = "http://190.12.79.135:9004/api/" // free
    //private const val BASE_URL = "http://192.168.254.26:9004/api/"
    private const val BASE_URL = "http://192.168.254.27:8060/api/" // LOCAL
    //private const val BASE_URL_NEW = "http://192.168.254.27:8036/api/" // NUEVA IMPLEMENTACION
    private  const val BASE_URL_NEW = "http://192.168.254.27:8036/OperityCode_Feature/api/"
    private val client = OkHttpClient.Builder()
        .connectTimeout(60,TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Cliente con timeout corto exclusivo para login:
    // si no hay red, falla en 10s y cae al fallback de Firestore rápido
    private val loginClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitNew: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_NEW)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitLogin: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_NEW)
            .client(loginClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginService: AuthService by lazy {
        retrofitLogin.create(AuthService::class.java)
    }

    val operService: OperarioInterface by lazy {
        retrofit.create(OperarioInterface::class.java)
    }
    val otService: OTService by lazy {
        retrofit.create(OTService::class.java)
    }
    val evalService: EvaluacionService by lazy {
        retrofit.create(EvaluacionService::class.java)
    }

    val lineaService: LineaService by lazy {
        retrofit.create(LineaService::class.java)
    }
    val paradaService: ParadaService by lazy {
        retrofitNew.create(ParadaService::class.java)
    }

    val detenerParadaService: DetenerParadaService by lazy {
        retrofit.create(DetenerParadaService::class.java)
    }

    val actividadService: ActividadService by lazy {
        retrofitNew.create(ActividadService::class.java)
    }

    val temperaturaService: TemperaturaService by lazy {
        retrofitNew.create(TemperaturaService::class.java)
    }

    val muestraService: MuestraService by lazy {
        retrofitNew.create(MuestraService::class.java)
    }

}
/*
//Configuracion Clase
class RetrofitConfig {
    private var client2: OkHttpClient? = null
    private var retrofit2: Retrofit? = null
    fun getClient(): Retrofit? {
        var baseUrl: String? = null
        baseUrl =
            when (BuildConfig.FLAVOR) {
                "peru"         -> "http://190.12.79.135:9004/api/"
                else -> ""
            }
        Log.e("REOS", "-RetrofitConfig-getClientLog-baseUrl$baseUrl")
        try {
            if (client2 == null) {
                client2 = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .proxy(java.net.Proxy.NO_PROXY)
                    .build()
            }
            if (retrofit2 == null) {
                retrofit2 = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).client(client2)
                    .build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("REOS", "Config-getClient-error:$e")
        }
        return retrofit2
    }
}*/