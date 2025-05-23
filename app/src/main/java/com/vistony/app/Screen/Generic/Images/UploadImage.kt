package com.vistony.app.Screen.Generic.Images

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.vistony.app.Screen.Generic.ImagePickerRow
import java.io.File

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
@Composable
fun ImagePickerExample(images: SnapshotStateList<Uri>) {
    val context = LocalContext.current

    val photoFile = remember {
        File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
    }
    val photoUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    }

    val galleryIntent = remember {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
    }
    val cameraIntent = remember {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
    }
    val chooserIntent = remember {
        Intent.createChooser(galleryIntent, "Selecciona imagen o cámara").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val selectedImageUri = data?.data ?: photoUri
            selectedImageUri?.let { images.add(it) }
        }
    }

    Column {
        ImagePickerRow(
            images = images,
            itemSize = 90.dp,
            onAddClick = { launcher.launch(chooserIntent) },
            onRemoveImage = { uri -> images.remove(uri) }
        )
    }
}
