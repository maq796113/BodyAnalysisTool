package com.example.bodyanalysistool.data


data class UploadResult(
    val isSuccessful: Boolean = false,
    val errorMessage: String? = null,
    val downloadLink: String? = null
)
