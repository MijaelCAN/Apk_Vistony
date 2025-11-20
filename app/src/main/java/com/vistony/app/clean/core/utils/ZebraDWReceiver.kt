package com.vistony.app.clean.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ZebraDWReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (action == ZebraDW.PROFILE_INTENT_ACTION) {
            val scannedData = intent.getStringExtra(ZebraDWComunication.DATAWEDGE_SCAN_EXTRA_DATA_STRING)
            val labelType = intent.getStringExtra(ZebraDWComunication.DATAWEDGE_SCAN_EXTRA_LABEL_TYPE)

            Log.e("REOS", "ZebraDWReceiver - Datos escaneados: $scannedData, Tipo: $labelType")

            // Si necesitas procesar los datos escaneados
            if (!scannedData.isNullOrEmpty()) {
                val intentData = Intent().apply {
                    putExtra("SCAN_DATA", scannedData)
                    putExtra("LABEL_TYPE", labelType)
                }
                ObservableObject.instance.updateValue(intentData)
            }
        }
    }
}

