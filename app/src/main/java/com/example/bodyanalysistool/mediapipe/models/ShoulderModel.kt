package com.example.bodyanalysistool.mediapipe.models

import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate

class ShoulderModel {
    val baseOptionsBuilder = BaseOptions
        .builder()
        .setDelegate(Delegate.CPU)
        .setModelAssetPath("Shoulder.pkl")
        .build()
}