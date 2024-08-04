package com.vistony.app.Service

import android.util.Log
import com.google.firebase.BuildConfig
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

    private const val BASE_URL = "http://190.12.79.135:9004/api/"
    private val client = OkHttpClient.Builder()
        .connectTimeout(60,TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
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

}
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
}