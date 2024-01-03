package com.example.bodyanalysistool.data

data class BodyAnalysisResponse(
    val messageOnShoulder: String,
    val shoulderCoordinate: Int,
    val messageOnWaist: String,
    val waistCoordinate: Int
)
