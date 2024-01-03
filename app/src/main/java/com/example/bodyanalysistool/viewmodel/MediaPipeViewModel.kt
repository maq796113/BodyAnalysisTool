package com.example.bodyanalysistool.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodyanalysistool.UserUseCases
import com.example.bodyanalysistool.events.UserEvent
import com.example.createzero.viewmodel.BodyAnalysisState
import com.example.createzero.viewmodel.ImageDownloadedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPipeViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {
    private val _imageDownloadedState = MutableStateFlow(ImageDownloadedState())
    val imageDownloadedState = _imageDownloadedState.asStateFlow()

    private val _bodyAnalysisState = MutableStateFlow(BodyAnalysisState())
    val bodyAnalysisState = _bodyAnalysisState.asStateFlow()


    fun onEvent(event: UserEvent) {
        when(event) {
            is UserEvent.CheckImageDownloaded -> {
                viewModelScope.launch {
                    _imageDownloadedState.value = imageDownloadedState.value.copy(
                        imageDownloadedResponse = userUseCases.checkImageDownloaded(event.url)
                    )
                }
            }
            is UserEvent.BodyAnalysis -> {
                viewModelScope.launch {
                    _bodyAnalysisState.value = bodyAnalysisState.value.copy(
                        bodyAnalysisResponse = userUseCases.bodyAnalysis()
                    )
                }
            }

            else -> {
                Log.d("MediaPipeViewModel", "ERROR 505")
            }
        }
    }
}