package com.vistony.app.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vistony.app.Entidad.UserResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val COLLECTION_USERS = "users"
    private val COLLECTION_TOKENS = "fcm_tokens"

    /**
     * Guarda o actualiza el token FCM de un usuario en Firestore
     * @param userId ID del usuario (DNI)
     * @param token Token FCM
     * @param userRole Rol del usuario
     * @param userName Nombre del usuario
     */
    suspend fun saveUserToken(
        userId: String,
        token: String,
        userRole: String,
        userName: String
    ): Result<Unit> {
        return try {
            val tokenData = hashMapOf(
                "token" to token,
                "userId" to userId,
                "role" to userRole.lowercase(),
                "userName" to userName,
                "lastUpdated" to com.google.firebase.Timestamp.now()
            )

            // Guardar en la colección de tokens con el userId como documento
            db.collection(COLLECTION_TOKENS)
                .document(userId)
                .set(tokenData)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene todos los tokens FCM de usuarios con rol "mantenimiento"
     */
    suspend fun getMantenimientoTokens(): Result<List<String>> {
        return try {
            val snapshot = db.collection(COLLECTION_TOKENS)
                .whereEqualTo("role", "mantenimiento")
                .get()
                .await()

            val tokens = snapshot.documents.mapNotNull { doc ->
                doc.getString("token")
            }

            Result.success(tokens)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina el token de un usuario (útil al cerrar sesión)
     */
    suspend fun deleteUserToken(userId: String): Result<Unit> {
        return try {
            db.collection(COLLECTION_TOKENS)
                .document(userId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
