package com.vistony.app.Service

import com.vistony.app.Repository.FirestoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    
    @Provides
    @Singleton
    fun provideFirestoreRepository(): FirestoreRepository {
        return FirestoreRepository()
    }
    
    @Provides
    @Singleton
    fun provideNotificationService(): NotificationService {
        return NotificationService()
    }
}
