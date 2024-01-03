package com.example.bodyanalysistool.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodyanalysistool.UserUseCases
import com.example.bodyanalysistool.events.UserEvent
import com.example.bodyanalysistool.states.StorageUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {
    private val _uploadUserImageState = MutableStateFlow(StorageUIState())
    val uploadUserImageState = _uploadUserImageState.asStateFlow()


    fun onEvent(event: UserEvent) {
        when(event) {
            is UserEvent.UploadUserImage -> {
                viewModelScope.launch {
                    _uploadUserImageState.value = uploadUserImageState.value.copy(
                        uploadResult = userUseCases.uploadUserImage(event.uri, event.email)
                    )
                }
            }
            is UserEvent.CurrentStorageReference -> {
                _uploadUserImageState.value = uploadUserImageState.value.copy(
                    currentStorageRef = userUseCases.currentStorageReference()
                )
            }

            else -> {
                Log.d("StorageViewModel", "Error 505")
            }
        }
    }
}