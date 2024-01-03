package com.example.bodyanalysistool.data

data class UserDataResult(
    val bodyAnalysisResponse: BodyAnalysisResponse? = null,
    val isSuccessful: Boolean = false,
    val errorMessage: String? = null
)
