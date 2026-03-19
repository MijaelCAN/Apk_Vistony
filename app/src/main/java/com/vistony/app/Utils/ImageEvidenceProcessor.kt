package com.vistony.app.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageEvidenceProcessor {

    /**
     * Convierte la imagen a JPG (calidad 100) y la deja en orientación vertical según EXIF.
     * El archivo final se guarda en [outputFile].
     */
    fun processUriToVerticalJpeg(
        context: Context,
        uri: Uri,
        outputFile: File
    ): File {
        outputFile.parentFile?.mkdirs()

        val resolver = context.contentResolver

        // 1) Leer EXIF orientation
        val orientation = try {
            val input = resolver.openInputStream(uri)
            if (input == null) {
                ExifInterface.ORIENTATION_NORMAL
            } else {
                val exif = ExifInterface(input)
                input.close()
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            }
        } catch (e: Exception) {
            Log.w("ImageEvidenceProcessor", "EXIF read failed: ${e.message}")
            ExifInterface.ORIENTATION_NORMAL
        }

        // 2) Decodificar bitmap
        val bitmap = decodeBitmapFromUri(resolver, uri)
            ?: throw IllegalStateException("Could not decode bitmap from uri=$uri")

        // 3) Aplicar rotación según EXIF
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> Unit
        }

        val rotatedBitmap = if (!matrix.isIdentity) {
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }

        // 4) Guardar como JPG calidad 100
        FileOutputStream(outputFile).use { out ->
            val ok = rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            if (!ok) throw IllegalStateException("JPEG compress failed")
        }

        // Libera memoria
        if (rotatedBitmap != bitmap) {
            bitmap.recycle()
        }

        return outputFile
    }

    private fun decodeBitmapFromUri(resolver: android.content.ContentResolver, uri: Uri): Bitmap? {
        // Decodificar sin downscale agresivo; si es muy grande, Android puede OOM.
        // Si necesitas control fino, ajustamos con inSampleSize.
        val input: InputStream? = resolver.openInputStream(uri)
        input ?: return null
        return input.use { BitmapFactory.decodeStream(it) }
    }
}

