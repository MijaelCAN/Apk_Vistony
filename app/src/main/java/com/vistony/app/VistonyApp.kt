package com.vistony.app

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VistonyApp:Application() {
    override fun onCreate() {
        super.onCreate()

        // Habilitar persistencia offline en Firestore para que la app lea/escriba aunque no haya internet.
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
    }
}