package com.example.bodyanalysistool.firebase.storage.repository

import android.net.Uri
import com.example.bodyanalysistool.data.StorageRef
import com.example.bodyanalysistool.data.UploadResult
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class StorageRepositoryImpl @Inject constructor() : StorageRepository {

    private val storage = Firebase.storage("gs://body-analysis-fyp.appspot.com")

    override val currentStorageRef: StorageRef
        get() = storage.reference.run {
            StorageRef(
                referencePath = path
            )
        }

    override suspend fun uploadUserImage(uri: Uri, email: String): UploadResult {

        return try {
            val reference = storage.reference.child("$email/images-${UUID.randomUUID()}.jpg")
            reference.putFile(uri)
            UploadResult(
                isSuccessful = true,
                downloadLink = reference.downloadUrl.toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            UploadResult(
                isSuccessful = false,
                errorMessage = e.message
            )
        }
    }
}