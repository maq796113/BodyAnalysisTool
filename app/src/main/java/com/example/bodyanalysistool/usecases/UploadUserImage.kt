package com.example.bodyanalysistool.usecases

import android.net.Uri
import com.example.bodyanalysistool.data.UploadResult
import com.example.bodyanalysistool.firebase.storage.repository.StorageRepository

class UploadUserImage(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(uri: Uri, email: String): UploadResult? {
        return repository.uploadUserImage(uri, email)
    }
}