package com.example.bodyanalysistool.mediapipe.repository

import com.example.bodyanalysistool.api.MediaPipeAPI
import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.data.ImageDownloadedResponse
import javax.inject.Inject

class MediaPipeRepositoryImpl @Inject constructor(
    private val mediaPipeAPI: MediaPipeAPI
): MediaPipeRepository {
    override suspend fun getBooleanImageDownloaded(url: String): ImageDownloadedResponse? {
        return mediaPipeAPI.getBooleanImageDownloaded(url).body()?.let {
            ImageDownloadedResponse(
                imageDownloaded = it.imageDownloaded
            )
        }
    }

    override suspend fun getBodyAnalysis(): BodyAnalysisResponse? {
        return mediaPipeAPI.getBodyAnalysis().body()?.let {
            BodyAnalysisResponse(
                messageOnShoulder=it.messageOnShoulder,
                shoulderCoordinate = it.shoulderCoordinate,
                messageOnWaist = it.messageOnWaist,
                waistCoordinate = it.waistCoordinate
            )
        }
    }
}