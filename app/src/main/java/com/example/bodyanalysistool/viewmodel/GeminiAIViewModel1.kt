package com.example.bodyanalysistool.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodyanalysistool.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GeminiAIViewModel1 @Inject constructor(
    private val sessionCache: SessionCache
): ViewModel() {

    private val _uiState: MutableStateFlow<GeminiAIUiState> = MutableStateFlow(GeminiAIUiState())
    val uiState = _uiState.asStateFlow()

    private val _bitmapState: MutableStateFlow<BitmapState> = MutableStateFlow(BitmapState())
    val bitmapState = _bitmapState.asStateFlow()

    private var generativeModel: GenerativeModel

    sealed class MyEvent {

        data class LoadingEvent(val isLoading: Boolean): MyEvent()
        data class SuccessEvent(val isTrue: Boolean): MyEvent()
    }

    private val eventChannel = Channel<MyEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun triggerEvent() = viewModelScope.launch {
        uiState.value.isLoading?.let { MyEvent.LoadingEvent(it) }?.let { eventChannel.send(it) }
        eventChannel.send(MyEvent.SuccessEvent(uiState.value.isSuccessful))
    }

    fun saveSession() {
        sessionCache.saveSession(
            session = Session(
                bitmapState = bitmapState.value,
                geminiAIUiState = uiState.value
            )
        )
    }

    init {
        val config = generationConfig {
            temperature = 0.50f
        }

        generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = BuildConfig.apiKey,
            generationConfig = config
        )
    }
    fun initializeLoading() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    fun getSelectedBitmap(bitmapUri: Uri)  {
        _bitmapState.value = bitmapState.value.copy(
            bitmapUri = bitmapUri.toString()
        )
    }

    fun prompt(selectedImage: Bitmap) {
        val prompt = "You will be giving out gym exercise advises. Take a look at this image and let me know what body part should be worked on."

        viewModelScope.launch {
            try {
                val content = content {
                    image(
                        selectedImage
                    )
                    text(
                        prompt
                    )
                }
                var output = ""
                generativeModel.generateContentStream(content).collect {
                    output += it.text
                    withContext(Dispatchers.Main.immediate) {
                        _uiState.value = uiState.value.copy(
                            isLoading = false,
                            response = output,
                            isSuccessful = true
                        )

                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main.immediate) {
                    _uiState.value = uiState.value.copy(
                        isLoading = false,
                        response = e.message
                    )
                }
            }
        }
    }
}


