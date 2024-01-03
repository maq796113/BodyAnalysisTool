package com.example.bodyanalysistool

import com.example.bodyanalysistool.usecases.BodyAnalysis
import com.example.bodyanalysistool.usecases.CheckImageDownloaded
import com.example.bodyanalysistool.usecases.CurrentStorageReference
import com.example.bodyanalysistool.usecases.UploadUserImage

data class UserUseCases(
    val checkImageDownloaded: CheckImageDownloaded,
    val bodyAnalysis: BodyAnalysis,
    val currentStorageReference: CurrentStorageReference,
    val uploadUserImage: UploadUserImage
)
