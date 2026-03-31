package com.vistony.app.Workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import java.io.File

class UploadParadaMantenimientoEvidenceWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val activityId = inputData.getString(KEY_ACTIVITY_ID) ?: return Result.failure()
        val localEvidencePathsJson =
            inputData.getString(KEY_LOCAL_EVIDENCE_PATHS_JSON) ?: return Result.failure()

        val localPaths = parseJsonArray(localEvidencePathsJson)
        if (localPaths.isEmpty()) return Result.failure()

        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance().reference

        val evidencesForFirestore = mutableListOf<Map<String, Any?>>()

        return try {
            localPaths.forEachIndexed { index, localPath ->
                val file = File(localPath)
                if (!file.exists()) {
                    throw IllegalStateException("Local evidence file not found: $localPath")
                }

                val fileName = "evidence_${index}.jpg"
                val storagePath =
                    "paradas_mantenimiento/$activityId/evidences/$fileName"

                val bytes = file.readBytes()
                val uploadRef = storage.child(storagePath)

                // Subir como JPEG de calidad 100% ya procesado por la app (vertical).
                uploadRef.putBytes(bytes).await()

                val downloadUrl = uploadRef.downloadUrl.await().toString()

                evidencesForFirestore.add(
                    mapOf(
                        "fileName" to fileName,
                        "contentType" to "image/jpeg",
                        "downloadUrl" to downloadUrl,
                        "storagePath" to storagePath
                    )
                )
            }

            val docRef = firestore.collection(COLLECTION_PARADAS_MANTENIMIENTO).document(activityId)
            docRef.update(
                mapOf(
                    "closeData.evidences" to evidencesForFirestore,
                    "status" to READY_CLOSE_ACTIVITY,
                    "closeData.updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "UploadParadaMantenimientoEvidenceWorker failed", e)
            // Dejar estado de error para visualización.
            val docRef = firestore.collection(COLLECTION_PARADAS_MANTENIMIENTO).document(activityId)
            docRef.update(
                mapOf(
                    "status" to "error",
                    "error" to (e.message ?: "Error al subir evidencias"),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.failure()
        }
    }

    private fun parseJsonArray(json: String): List<String> {
        val arr = JSONArray(json)
        return List(arr.length()) { i -> arr.getString(i) }
    }

    companion object {
        private const val TAG = "EvidenceUploadWorker"
        private const val COLLECTION_PARADAS_MANTENIMIENTO = "paradas_mantenimiento"
        private const val READY_CLOSE_ACTIVITY = "completed"

        const val KEY_ACTIVITY_ID = "activityId"
        const val KEY_LOCAL_EVIDENCE_PATHS_JSON = "localEvidencePathsJson"
    }
}

