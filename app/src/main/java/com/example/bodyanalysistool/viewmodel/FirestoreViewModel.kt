package com.example.bodyanalysistool.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bodyanalysistool.data.BitmapUri
import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.data.User
import com.example.bodyanalysistool.firebase.firestore.repository.FirestoreRepository
import com.example.bodyanalysistool.states.AddBitmapState
import com.example.bodyanalysistool.states.GetDataState
import com.example.bodyanalysistool.states.IsBitmapInCollectionState
import com.example.bodyanalysistool.states.SendDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel() {

    private val _isBitmapInCollectionState = MutableStateFlow(IsBitmapInCollectionState())
    val isBitmapInCollectionState = _isBitmapInCollectionState.asStateFlow()

    private val _addBitmapState = MutableStateFlow(AddBitmapState())
    val addBitmapState = _addBitmapState.asStateFlow()

    private val _getUserDataState = MutableStateFlow(GetDataState())
    val getUserDatastate = _getUserDataState.asStateFlow()

    private val _sendDataState = MutableStateFlow(SendDataState())
    val sendDataState = _sendDataState.asStateFlow()

    fun isBitmapInCollection(user: User, bitmapUri: BitmapUri) {
        _isBitmapInCollectionState.value = isBitmapInCollectionState.value.copy(
            isBitmapInCollectionResult = firestoreRepository.isBitmapInCollection(user, bitmapUri)
        )
    }

    fun addBitmap(user: User, bitmapUri: BitmapUri) {
        _addBitmapState.value = addBitmapState.value.copy(
            addBitmapResult = firestoreRepository.addBitmap(user, bitmapUri)
        )
    }

    fun getData(user: User, bitmapUri: BitmapUri) {
        _getUserDataState.value = getUserDatastate.value.copy(
            userDataResult = firestoreRepository.getData(user, bitmapUri)
        )
    }

    fun sendData(user: User, bitmapUri: BitmapUri, bodyAnalysisResponse: BodyAnalysisResponse) {
        _sendDataState.value = sendDataState.value.copy(
            sendDataResponse = firestoreRepository.sendData(user, bitmapUri, bodyAnalysisResponse)
        )
    }
}