package com.example.bodyanalysistool.usecases

import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.mediapipe.repository.MediaPipeRepository


class BodyAnalysis(
    private val repository: MediaPipeRepository
) {
    suspend operator fun invoke(): BodyAnalysisResponse? {
        return repository.getBodyAnalysis()
    }
}