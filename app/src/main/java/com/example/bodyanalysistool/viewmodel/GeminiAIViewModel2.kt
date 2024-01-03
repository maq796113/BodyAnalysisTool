package com.example.bodyanalysistool.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeminiAIViewModel2 @Inject constructor(
    private val sessionCache: SessionCache
): ViewModel() {
    val session get() = sessionCache.getActiveSession()
}