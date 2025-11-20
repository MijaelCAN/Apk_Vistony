package com.vistony.app.clean.core.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log

class ZebraDW() {
    private val dw = ZebraDWComunication()
    companion object {
        const val PROFILE_NAME = "JEPICAME"
        const val PROFILE_INTENT_ACTION = "com.vistony.app.SCAN"
        //const val PROFILE_INTENT_START_ACTIVITY = "0"
        const val PROFILE_INTENT_DELIVERY = "0"
    }

    fun createDataWedgeProfile(activity: Activity) {
        Log.e("REOS", "Crear perfil $PROFILE_NAME")
        dw.sendCommandString(
            activity,
            ZebraDWComunication.DATAWEDGE_SEND_CREATE_PROFILE,
            PROFILE_NAME,
            sendResult = true
        )

        val barcode = Bundle().apply {
            putString("PLUGIN_NAME", "BARCODE")
            putString("RESET_CONFIG", "true")
            putBundle("PARAM_LIST", Bundle())
        }

        val intentPlugin = Bundle().apply {
            putString("PLUGIN_NAME", "INTENT")
            putString("RESET_CONFIG", "true")
            val p = Bundle().apply {
                putString("intent_output_enabled", "true")
                putString("intent_action", PROFILE_INTENT_ACTION)
                putString("intent_delivery", PROFILE_INTENT_DELIVERY)
            }
            putBundle("PARAM_LIST", p)
        }

        val app = Bundle().apply {
            putString("PACKAGE_NAME", activity.packageName)
            putStringArray("ACTIVITY_LIST", arrayOf("*"))
        }

        val cfg = Bundle().apply {
            putString("PROFILE_NAME", PROFILE_NAME)
            putString("PROFILE_ENABLED", "true")
            putString("CONFIG_MODE", "UPDATE")
            putParcelableArray("PLUGIN_CONFIG", arrayOf(barcode, intentPlugin))
            putParcelableArray("APP_LIST", arrayOf(app))
        }

        dw.sendCommandBundle(activity, ZebraDWComunication.DATAWEDGE_SEND_SET_CONFIG, cfg)
        dw.sendCommandString(activity, ZebraDWComunication.DATAWEDGE_SEND_GET_ACTIVE_PROFILE, "", sendResult = true)
    }

}