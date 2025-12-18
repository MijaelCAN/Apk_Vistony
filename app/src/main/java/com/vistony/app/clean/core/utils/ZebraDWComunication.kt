package com.vistony.app.clean.core.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

class ZebraDWComunication()
{
    companion object {

        const val DATAWEDGE_SEND_ACTION = "com.symbol.datawedge.api.ACTION"
        const val DATAWEDGE_RETURN_ACTION = "com.symbol.datawedge.api.RESULT_ACTION"
        const val DATAWEDGE_RETURN_CATEGORY = "android.intent.category.DEFAULT"
        const val DATAWEDGE_EXTRA_SEND_RESULT = "SEND_RESULT"
        const val DATAWEDGE_EXTRA_RESULT = "RESULT"
        const val DATAWEDGE_EXTRA_COMMAND = "COMMAND"
        const val DATAWEDGE_EXTRA_RESULT_INFO = "RESULT_INFO"
        const val DATAWEDGE_EXTRA_RESULT_CODE = "RESULT_CODE"

        const val DATAWEDGE_SCAN_EXTRA_DATA_STRING = "com.symbol.datawedge.data_string"
        const val DATAWEDGE_SCAN_EXTRA_LABEL_TYPE = "com.symbol.datawedge.label_type"

        const val DATAWEDGE_SEND_CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE"

        const val DATAWEDGE_SEND_GET_VERSION = "com.symbol.datawedge.api.GET_VERSION_INFO"
        const val DATAWEDGE_RETURN_VERSION = "com.symbol.datawedge.api.RESULT_GET_VERSION_INFO"
        const val DATAWEDGE_RETURN_VERSION_DATAWEDGE = "DATAWEDGE"

        const val DATAWEDGE_SEND_GET_ENUMERATE_SCANNERS = "com.symbol.datawedge.api.ENUMERATE_SCANNERS"
        const val DATAWEDGE_RETURN_ENUMERATE_SCANNERS = "com.symbol.datawedge.api.RESULT_ENUMERATE_SCANNERS"

        const val DATAWEDGE_SEND_GET_CONFIG = "com.symbol.datawedge.api.GET_CONFIG"
        const val DATAWEDGE_RETURN_GET_CONFIG = "com.symbol.datawedge.api.RESULT_GET_CONFIG"
        const val DATAWEDGE_SEND_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG"

        const val DATAWEDGE_SEND_GET_ACTIVE_PROFILE = "com.symbol.datawedge.api.GET_ACTIVE_PROFILE"
        const val DATAWEDGE_RETURN_GET_ACTIVE_PROFILE = "com.symbol.datawedge.api.RESULT_GET_ACTIVE_PROFILE"

        const val DATAWEDGE_SEND_SWITCH_SCANNER = "com.symbol.datawedge.api.SWITCH_SCANNER"

        const val DATAWEDGE_SEND_SET_SCANNER_INPUT = "com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN"
        const val DATAWEDGE_SEND_SET_SCANNER_INPUT_ENABLE = "ENABLE_PLUGIN"
        const val DATAWEDGE_SEND_SET_SCANNER_INPUT_DISABLE = "DISABLE_PLUGIN"

        const val DATAWEDGE_SEND_SET_SOFT_SCAN = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER"
        // Agregar esta constante para resolver el error:
        const val DATAWEDGE_SEND_SWITCH_TO_PROFILE = "com.symbol.datawedge.api.SWITCH_TO_PROFILE"
    }

    /*fun sendCommandString(context: Context, command: String, parameter: String, sendResult: Boolean = false)
    {
        val dwIntent = Intent()
        dwIntent.action = DATAWEDGE_SEND_ACTION
        dwIntent.putExtra(command, parameter)
        if (sendResult)
            dwIntent.putExtra(DATAWEDGE_EXTRA_SEND_RESULT, "true")
        context.sendBroadcast(dwIntent)
    }*/
    fun sendCommandString(context: Context, command: String, parameter: String, sendResult: Boolean = false) {
        val dwIntent = Intent(DATAWEDGE_SEND_ACTION).apply {
            putExtra(command, parameter)
            if (sendResult) putExtra(DATAWEDGE_EXTRA_SEND_RESULT, "true")
        }
        context.sendBroadcast(dwIntent)
    }

    /*fun sendCommandBundle(context: Context, command: String, parameter: Bundle)
    {
        Log.e("REOS","ZebraDWComunication - sendCommandBundle - command: $command - parameter: $parameter")
        val dwIntent = Intent()
        dwIntent.action = DATAWEDGE_SEND_ACTION
        dwIntent.putExtra(command, parameter)
        context.sendBroadcast(dwIntent)
    }*/
    fun sendCommandBundle(context: Context, command: String, parameter: Bundle) {
        Log.e("REOS","ZebraDWComunication - sendCommandBundle - command: $command - parameter: $parameter")
        val dwIntent = Intent(DATAWEDGE_SEND_ACTION).apply {
            putExtra(command, parameter)
        }
        context.sendBroadcast(dwIntent)
    }

}