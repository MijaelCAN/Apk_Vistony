package com.vistony.app.Zxing

import android.app.Activity
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BarcodeScanner(onScanResult: (String) -> Unit) {
    val context = LocalContext.current
    var scanResult by remember { mutableStateOf("") }

    /*AndroidView(
        factory = { ctx ->
            val view = View(ctx)
            ZxingScanner.scanCode(context as Activity){scanResult->
                scanResult = scanResult.contents
            }
            view
        },
        update = {  }
    )

    if (scanResult.isNotEmpty()) {
        onScanResult(scanResult)
    }*/
}