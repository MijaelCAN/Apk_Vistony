package com.vistony.app.Screen.Parada

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.zxing.integration.android.IntentIntegrator
import com.vistony.app.R

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



@Composable
fun Prueba(){
    var isChecked by remember { mutableStateOf(false) }
    Switch(
        checked = isChecked,
        onCheckedChange = { isChecked = !isChecked },
        thumbContent = {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.switch_check),
                tint = Color(0xFF0054A3)
            )
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color(0xFFFFFFEF),
            checkedTrackColor = Color(0xFF0054A3),
            uncheckedThumbColor = Color(0xFFA30062),
            uncheckedTrackColor = Color(0xFFFFFFEF)
        )
    )
}