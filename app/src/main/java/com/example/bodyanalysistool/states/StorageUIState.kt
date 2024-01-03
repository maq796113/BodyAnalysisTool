package com.example.bodyanalysistool.states

import com.example.bodyanalysistool.data.StorageRef
import com.example.bodyanalysistool.data.UploadResult

data class StorageUIState(
    val uploadResult: UploadResult? = null,
    val currentStorageRef: StorageRef? = null
)