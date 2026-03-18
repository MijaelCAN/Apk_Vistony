package com.vistony.app.Service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Servicio para enviar notificaciones push a través de Firebase Cloud Messaging
 * Nota: Para enviar notificaciones desde el cliente, se requiere usar Cloud Functions
 * o un servidor backend. Este servicio prepara los datos para ser enviados.
 * 
 * Para producción, se recomienda implementar Cloud Functions que escuchen
 * cuando se registre una parada y envíe las notificaciones automáticamente.
 */
@Singleton
class NotificationService @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val COLLECTION_TOKENS = "fcm_tokens"
    private val COLLECTION_NOTIFICATIONS = "notifications"

    /**
     * Prepara una notificación para ser enviada a usuarios de mantenimiento
     * cuando se registra una parada de máquina.
     * 
     * En producción, esto debería ser manejado por Cloud Functions.
     * Por ahora, guardamos la notificación en Firestore para que Cloud Functions la procese.
     */
    suspend fun sendParadaNotification(
        maquina: String,
        area: String,
        motivo: String,
        usuario: String,
        docEntry: String? = null
    ): Result<Unit> {
        return try {
            // Obtener tokens de usuarios de mantenimiento
            val tokensSnapshot = db.collection(COLLECTION_TOKENS)
                .whereEqualTo("role", "mantenimiento")
                .get()
                .await()

            val tokens = tokensSnapshot.documents.mapNotNull { doc ->
                doc.getString("token")
            }

            if (tokens.isEmpty()) {
                Log.w("NotificationService", "No hay tokens de mantenimiento disponibles")
                return Result.success(Unit)
            }

            // Crear el payload de la notificación
            val notificationData = hashMapOf(
                "type" to "parada_maquina",
                "title" to "Nueva Parada de Máquina",
                "body" to "Máquina: $maquina - Área: $area - Motivo: $motivo",
                "maquina" to maquina,
                "area" to area,
                "motivo" to motivo,
                "usuario" to usuario,
                "docEntry" to (docEntry ?: ""),
                "tokens" to tokens,
                "createdAt" to com.google.firebase.Timestamp.now(),
                "status" to "pending"
            )

            // Guardar en Firestore para que Cloud Functions lo procese
            // O alternativamente, puedes llamar a tu backend API que envíe las notificaciones
            db.collection(COLLECTION_NOTIFICATIONS)
                .add(notificationData)
                .await()

            Log.d("NotificationService", "Notificación de parada guardada para ${tokens.size} usuarios de mantenimiento")
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NotificationService", "Error al enviar notificación", e)
            Result.failure(e)
        }
    }

    /**
     * Obtiene el token FCM del dispositivo actual
     */
    suspend fun getCurrentToken(): Result<String> {
        return try {
            val token = FirebaseMessaging.getInstance().token.await()
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
