package com.vistony.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.vistony.app.Repository.FirestoreRepository

class MyFireBaseMessagingserService: FirebaseMessagingService(){
    
    private val firestoreRepository: FirestoreRepository by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            FirebaseEntryPoint::class.java
        ).firestoreRepository()
    }
    
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        // Manejar notificaciones cuando la app está en primer plano
        Log.d(
            "FCM",
            "onMessageReceived: notification=${message.notification} data=${message.data}"
        )

        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]
        
        // Si hay datos adicionales en la notificación
        val maquina = message.data["maquina"]
        val area = message.data["area"]
        val motivo = message.data["motivo"]
        
        sendNotification(title, body, maquina, area, motivo)
    }

    private fun sendNotification(
        title: String?, 
        messageBody: String?,
        maquina: String? = null,
        area: String? = null,
        motivo: String? = null
    ) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.logo)
            .setContentTitle(title ?: "Notificación")
            .setContentText(messageBody ?: "")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))

        // Agregar datos adicionales si existen
        if (maquina != null || area != null || motivo != null) {
            val expandedText = buildString {
                messageBody?.let { append(it) }
                if (maquina != null) {
                    if (isNotEmpty()) append("\n")
                    append("Máquina: $maquina")
                }
                if (area != null) {
                    if (isNotEmpty()) append("\n")
                    append("Área: $area")
                }
                if (motivo != null) {
                    if (isNotEmpty()) append("\n")
                    append("Motivo: $motivo")
                }
            }
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(expandedText))
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Canal de Notificaciones",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notificaciones de paradas de máquina"
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token FCM: $token")
        
        // Guardar el token en SharedPreferences para usarlo después del login
        val prefs = getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()
        
        // Si el usuario ya está logueado, guardar el token en Firestore
        val userPrefs = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val userId = userPrefs.getString("current_user_id", null)
        val userRole = userPrefs.getString("current_user_role", null)
        val userName = userPrefs.getString("current_user_name", null)
        
        if (userId != null && userRole != null && userName != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firestoreRepository.saveUserToken(userId, token, userRole, userName)
                    Log.d("FCM", "Token guardado en Firestore para usuario: $userId")
                } catch (e: Exception) {
                    Log.e("FCM", "Error al guardar token en Firestore", e)
                }
            }
        }
    }
    
    companion object {
        private const val CHANNEL_ID = "parada_maquina_channel"
    }
}