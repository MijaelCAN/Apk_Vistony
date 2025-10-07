package com.vistony.app.Screen.Generic.Images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.vistony.app.Screen.Generic.ImagePickerRow
import java.io.File
import kotlin.math.log

@Composable
fun ImagePickerExample(
    images: SnapshotStateList<Uri>,
    onAddImage: (Uri) -> Unit,
    onRemoveImage: (Uri) -> Unit,
    enabled: Boolean = true
) {
    val context = LocalContext.current
    val photoFile = remember {
        File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
    }
    val photoUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    }

    val galleryIntent = remember {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
    }
    val cameraIntent = remember {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    val chooserIntent = remember {
        Intent.createChooser(galleryIntent, "Selecciona imágenes (múltiples) o cámara").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            when {
                data?.clipData != null -> {
                    // Múltiples imágenes seleccionadas
                    val clipData = data.clipData!!
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        onAddImage(uri)
                    }
                    Toast.makeText(context, "${clipData.itemCount} imágenes agregadas", Toast.LENGTH_SHORT).show()
                }
                data?.data != null -> {
                    // Una sola imagen seleccionada
                    onAddImage(data.data!!)
                    Toast.makeText(context, "Imagen agregada", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Imagen de cámara
                    photoUri?.let { 
                        onAddImage(it)
                        Toast.makeText(context, "Foto tomada", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Launcher para solicitar permisos de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso concedido, lanzar el chooser
            launcher.launch(chooserIntent)
        } else {
            // Permiso denegado, mostrar mensaje
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }
    // Función para verificar y solicitar permisos
    val handleImagePickerClick = {

        if (!enabled) {
            Toast.makeText(context, "No se pueden agregar imágenes después de finalizar la actividad", Toast.LENGTH_SHORT).show()
            //return @handleImagePickerClick
        }
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso ya concedido, lanzar chooser
                launcher.launch(chooserIntent)
            }
            else -> {
                // Solicitar permiso
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Column {
        ImagePickerRow(
            images = images,
            itemSize = 90.dp,
            onAddClick = { handleImagePickerClick() },
            //onRemoveImage = { uri -> images.remove(uri) }
            onRemoveImage = { uri -> onRemoveImage(uri) },
            enabled = enabled
        )
    }
}

/*
@Composable
fun ImagePickerExample() {
    val context = LocalContext.current

    // Estado para las imágenes seleccionadas (Uri)
    val images = remember { mutableStateListOf<Uri>() }

    // Launcher para seleccionar imagen de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { images.add(it) }
    }

    // Launcher para tomar foto con cámara
    val photoUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri.value?.let { images.add(it) }
        }
    }

    // Crear Uri para la foto (usando FileProvider)
    fun createImageUri(): Uri? {
        val imageFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    Column {
        ImagePickerRow(
            images = images.mapNotNull { uri ->
                // Convertir Uri a ImageBitmap con Coil AsyncImage es mejor, aquí solo ejemplo
                // Usaremos AsyncImage en ImagePickerRow para Uri
                uri
            },
            itemSize = 90.dp,
            onAddClick = {
                // Mostrar diálogo para elegir entre cámara o galería
                val options = listOf("Galería", "Cámara")
                // Aquí puedes mostrar un dialogo para elegir, o un menú desplegable
                // Por simplicidad, llamamos directo a galería o cámara
                // Ejemplo: abrir galería
                //galleryLauncher.launch("image/*")
                // Para cámara:
                 photoUri.value = createImageUri()
                photoUri.value?.let { cameraLauncher.launch(it) }
            },
            onRemoveImage = { uri ->
                images.remove(uri)
            }
        )
    }
}
*/


 */