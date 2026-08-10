package com.vistony.app.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.vistony.app.Entidad.OTItem
import com.vistony.app.Entidad.UserResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class FirestoreRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val COLLECTION_USERS = "users"
    private val COLLECTION_TOKENS = "fcm_tokens"
    private val COLLECTION_ORDENES_ENVASE = "ordenes_envase"

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
     * Login offline: busca al usuario en la colección "usuarios" (solo mantenimiento)
     * y verifica el hash SHA-256 de la contraseña.
     * Usa el cache local de Firestore si no hay internet.
     * @return UserResponse si las credenciales son correctas, null si no coinciden o no existe
     */
    suspend fun loginFirestore(dni: String, passwordHash: String): Result<UserResponse?> {
        return try {
            val snapshot = db.collection(COLLECTION_USERS)
                .whereEqualTo("dni", dni)
                .limit(1)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Log.w("FirestoreRepository", "Usuario $dni no encontrado en Firestore")
                return Result.success(null)
            }

            val doc = snapshot.documents.first()
            val storedHash = doc.getString("passwordHash")
            if (storedHash == null || storedHash != passwordHash) {
                Log.w("FirestoreRepository", "Contraseña incorrecta para usuario $dni")
                return Result.success(null)
            }

            val user = UserResponse(
                id = doc.getLong("id")?.toInt() ?: 0,
                dni = doc.getString("dni") ?: "",
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: "",
                role = doc.getString("role") ?: "",
                position = doc.getString("position") ?: "",
                avatar = doc.getString("avatar") ?: "",
                lastLogin = doc.getTimestamp("lastLogin")?.toDate()?.toString() ?: ""
            )

            Log.d("FirestoreRepository", "Login Firestore exitoso para $dni")
            Result.success(user)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error al consultar Firestore para login", e)
            Result.failure(e)
        }
    }

    /**
     * Inicia un SnapshotListener para el usuario logueado.
     * - Con internet: descarga el documento y lo mantiene actualizado en cache local.
     * - Sin internet: sirve desde el cache local (del último login online).
     * Retorna el ListenerRegistration para cancelarlo al cerrar sesión.
     */
    fun escucharUsuario(dni: String): ListenerRegistration {
        return db.collection(COLLECTION_USERS)
            .whereEqualTo("dni", dni)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("FirestoreRepository", "Error en SnapshotListener de usuario $dni", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    Log.d("FirestoreRepository", "Cache actualizado para usuario $dni (fuente: ${snapshot.metadata.isFromCache})")
                } else {
                    Log.d("FirestoreRepository", "Usuario $dni no encontrado en colección usuarios (puede no ser mantenimiento)")
                }
            }
    }

    /**
     * Consulta la colección "ordenes_envase" filtrando por OT_Mezcla.
     * Firestore puede almacenar el campo como número o como String, por lo que
     * se lanzan dos queries en paralelo y se fusionan los resultados por ID de documento.
     */
    suspend fun getOTMezcla(otEnvase: Long): Result<List<OTItem>> {
        return try {
            // Firestore almacena OT_Mezcla como String en algunos docs y como número en otros.
            // doc.get() devuelve el valor raw sin castear, evitando el RuntimeException de getLong()
            // cuando el campo es String. Se hacen dos queries para cubrir ambos tipos de almacenamiento.
            val snapshotLong = db.collection(COLLECTION_ORDENES_ENVASE)
                .whereEqualTo("OT_Mezcla", otEnvase)
                .get()
                .await()

            val snapshotString = db.collection(COLLECTION_ORDENES_ENVASE)
                .whereEqualTo("OT_Mezcla", otEnvase.toString())
                .get()
                .await()

            val allDocs = (snapshotLong.documents + snapshotString.documents)
                .distinctBy { it.id }

            val items = allDocs.map { doc ->
                OTItem(
                    ItemName   = doc.getString("ItemName") ?: "",
                    UomName    = doc.getString("UomName") ?: "",
                    PlannedQty = doc.get("PlannedQty")?.toString()
                        ?: doc.get("Qty")?.toString()
                        ?: "0",
                    OT_Mezcla  = doc.get("OT_Mezcla")?.toString() ?: ""
                )
            }

            Log.d("FirestoreRepository", "OT Mezcla encontrados: ${items.size} para OT_Mezcla=$otEnvase")
            Result.success(items)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error al consultar ordenes_envase", e)
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
