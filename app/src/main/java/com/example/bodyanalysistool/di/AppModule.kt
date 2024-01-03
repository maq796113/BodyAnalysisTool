package com.example.bodyanalysistool.di

import android.content.Context
import android.content.SharedPreferences
import com.example.bodyanalysistool.UserUseCases
import com.example.bodyanalysistool.api.MediaPipeAPI
import com.example.bodyanalysistool.firebase.auth.repositry.AuthRepository
import com.example.bodyanalysistool.firebase.auth.repositry.AuthRepositoryImpl
import com.example.bodyanalysistool.firebase.firestore.repository.FirestoreRepository
import com.example.bodyanalysistool.firebase.firestore.repository.FirestoreRepositoryImpl
import com.example.bodyanalysistool.firebase.storage.repository.StorageRepository
import com.example.bodyanalysistool.firebase.storage.repository.StorageRepositoryImpl
import com.example.bodyanalysistool.mediapipe.repository.MediaPipeRepository
import com.example.bodyanalysistool.mediapipe.repository.MediaPipeRepositoryImpl
import com.example.bodyanalysistool.usecases.BodyAnalysis
import com.example.bodyanalysistool.usecases.CheckImageDownloaded
import com.example.bodyanalysistool.usecases.CurrentStorageReference
import com.example.bodyanalysistool.usecases.UploadUserImage
import com.example.bodyanalysistool.utils.Util
import com.example.bodyanalysistool.viewmodel.SessionCache
import com.example.bodyanalysistool.viewmodel.SessionCacheImpl
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSessionCache(sharedPreferences: SharedPreferences): SessionCache {
        return SessionCacheImpl(sharedPreferences)
    }


    @Provides
    @Singleton
    fun provideFirestoreRepository(impl: FirestoreRepositoryImpl): FirestoreRepository = impl


    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context.applicationContext

    @Provides
    @Singleton
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient = Identity.getSignInClient(context)

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Util.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMediaPipeAPI(retrofit: Retrofit) : MediaPipeAPI {
        return retrofit.create(MediaPipeAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMediaPipeRepository(impl: MediaPipeRepositoryImpl): MediaPipeRepository = impl

    @Singleton
    @Provides
    fun provideStorageRepository(impl: StorageRepositoryImpl): StorageRepository = impl

    @Singleton
    @Provides
    fun provideUserUseCases(mediaRepository: MediaPipeRepository, storageRepository: StorageRepository): UserUseCases {
        return UserUseCases(
            checkImageDownloaded = CheckImageDownloaded(mediaRepository),
            bodyAnalysis = BodyAnalysis(mediaRepository),
            currentStorageReference = CurrentStorageReference(storageRepository),
            uploadUserImage = UploadUserImage(storageRepository)
        )
    }

}