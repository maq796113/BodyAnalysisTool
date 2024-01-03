package com.example.bodyanalysistool.mediapipe.repository

import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.data.ImageDownloadedResponse

interface MediaPipeRepository {

    suspend fun getBooleanImageDownloaded(url: String): ImageDownloadedResponse?

    suspend fun getBodyAnalysis(): BodyAnalysisResponse?

}