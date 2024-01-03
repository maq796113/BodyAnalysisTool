package com.example.bodyanalysistool.firebase.firestore.repository

import com.example.bodyanalysistool.data.AddBitmapResult
import com.example.bodyanalysistool.data.AddNewUserResult
import com.example.bodyanalysistool.data.BitmapUri
import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.data.IsBitmapInCollectionResult
import com.example.bodyanalysistool.data.SendDataResponse
import com.example.bodyanalysistool.data.User
import com.example.bodyanalysistool.data.UserDataResult


interface FirestoreRepository {


//    fun getAllBitmaps(
//        currentUser: User,
//        returnedBitmaps: (List<Bitmap>) -> Unit,
//        onFailure: (Exception) -> Unit,
//    )

    fun addBitmap(user: User, bitmapUri: BitmapUri): AddBitmapResult
    fun addNewUser(user: User): AddNewUserResult
    fun isBitmapInCollection(user: User, bitmapUri: BitmapUri): IsBitmapInCollectionResult
    fun getData(user: User, bitmapUri: BitmapUri): UserDataResult
    fun sendData(user: User, bitmapUri: BitmapUri, bodyAnalysisResponse: BodyAnalysisResponse): SendDataResponse
}