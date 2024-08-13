package com.vistony.app.ui.theme.Screen.Parada

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.zxing.integration.android.IntentIntegrator

@Composable
fun QrCodeScanner() {
    var scanResult by remember { mutableStateOf("") }
    val context = LocalContext.current
    val integrator = IntentIntegrator(context as Activity)

    integrator.setOrientationLocked(true)

    integrator.setDesiredBarcodeFormats(IntentIntegrator.CODE_128)

    integrator.setPrompt("Escanea un código QR")
    integrator.setBeepEnabled(true)

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
        if (intentResult != null) {
            if (intentResult.contents == null) {
                // Cancelado
            } else {
                scanResult = intentResult.contents
            }
        }
    }

    Button(onClick = { launcher.launch(integrator.createScanIntent()) }) {
        Text("Escanear")
    }

    Text(text = "Resultado: $scanResult")
}