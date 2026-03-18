package com.vistony.app

import com.vistony.app.Repository.FirestoreRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FirebaseEntryPoint {
    fun firestoreRepository(): FirestoreRepository
}
