package com.example.bodyanalysistool.mediapipe.imageclassification

import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.imageclassifier.ImageClassifier.ImageClassifierOptions

class ImageClassifier(threshold: Float, maxResults: Int, baseOptions: BaseOptions) {
    val optionsBuilder = ImageClassifierOptions.builder()
        .setScoreThreshold(threshold)
        .setMaxResults(maxResults)
        .setRunningMode(RunningMode.IMAGE)
        .setBaseOptions(baseOptions)
        .build()
}



