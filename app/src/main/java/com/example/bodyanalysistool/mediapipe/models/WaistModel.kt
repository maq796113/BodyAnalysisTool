package com.example.bodyanalysistool.mediapipe.models

import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate

class WaistModel {
    val baseOptionsBuilder: BaseOptions = BaseOptions
        .builder()
        .setDelegate(Delegate.CPU)
        .setModelAssetPath("Waist.pkl")
        .build()
}
