package com.example.bodyanalysistool.firebase.storage.repository

import android.net.Uri
import com.example.bodyanalysistool.data.StorageRef
import com.example.bodyanalysistool.data.UploadResult

interface StorageRepository {
    val currentStorageRef: StorageRef
    suspend fun uploadUserImage(uri: Uri, email: String): UploadResult
}