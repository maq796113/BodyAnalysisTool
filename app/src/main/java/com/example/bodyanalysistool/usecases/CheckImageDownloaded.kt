package com.example.bodyanalysistool.usecases

import com.example.bodyanalysistool.data.ImageDownloadedResponse
import com.example.bodyanalysistool.mediapipe.repository.MediaPipeRepository

class CheckImageDownloaded(
    private val repository: MediaPipeRepository
) {
    suspend operator fun invoke(url: String): ImageDownloadedResponse? {
        return repository.getBooleanImageDownloaded(url)
    }

}