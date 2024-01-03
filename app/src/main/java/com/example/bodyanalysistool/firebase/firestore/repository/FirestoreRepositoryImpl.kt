package com.example.bodyanalysistool.firebase.firestore.repository

import com.example.bodyanalysistool.data.AddBitmapResult
import com.example.bodyanalysistool.data.AddNewUserResult
import com.example.bodyanalysistool.data.BitmapUri
import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.data.IsBitmapInCollectionResult
import com.example.bodyanalysistool.data.SendDataResponse
import com.example.bodyanalysistool.data.User
import com.example.bodyanalysistool.data.UserDataResult
import com.example.bodyanalysistool.utils.Constraints
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor()  : FirestoreRepository {

    private val firestoreInstance = FirebaseFirestore.getInstance()


    override fun addBitmap(user: User, bitmapUri: BitmapUri): AddBitmapResult {
        return try {
            firestoreInstance.collection(Constraints.USERS)
                .document("${user.userId}/bitmapUris/${bitmapUri.uriID}")
                .set(bitmapUri)
                .run {
                    AddBitmapResult(
                        isSuccessful = isSuccessful,
                        errorMessage = null
                    )
                }
        } catch (e: Exception) {
            e.printStackTrace()
            AddBitmapResult(
                isSuccessful = false,
                errorMessage = e.message
            )
        }
    }


//    override fun getAllBitmaps(
//        currentUser: User,
//        returnedBitmaps: (List<Bitmap>) -> Unit,
//        onFailure: (Exception) -> Unit,
//    ) {
//        firestoreInstance.collection(Constants.)
//            .whereArrayContainsAny(Constants.PARTICIPANTS_USER_ID, listOf(currentUser.userId))
//            .addSnapshotListener {querySnapshot, error ->
//
//                if(error != null){
//                    onFailure(error)
//                    return@addSnapshotListener
//                }
//
//                val list = mutableListOf<Bitmap>()
//
//                querySnapshot?.forEach { queryDocumentSnapshot ->
//                    list.add( queryDocumentSnapshot.toObject(Chat::class.java) )
//                }
//
//                returnedBitmaps(list)
//
//            }
//    }



    override fun addNewUser(user: User): AddNewUserResult {
        return try {
            firestoreInstance.collection(Constraints.USERS)
                .document(user.userId)
                .set(user)
                .run {
                    AddNewUserResult(
                        isSuccessful = isSuccessful,
                        errorMessage = null
                    )
                }
        } catch (e: Exception) {
            e.printStackTrace()
            AddNewUserResult(
                isSuccessful = false,
                errorMessage = e.message
            )
        }

    }

    override fun isBitmapInCollection(user: User, bitmapUri: BitmapUri): IsBitmapInCollectionResult {
        return try {
            firestoreInstance.collection(Constraints.USERS)
                .document("${user.userId}/bitmapUris/${bitmapUri.uriID}")
                .get()
                .run {
                    IsBitmapInCollectionResult(
                        found = this.isSuccessful && (this.result.data.toString() == bitmapUri.uri),
                        errorMessage = null
                    )
                }
        } catch (e: Exception) {
            e.printStackTrace()
            IsBitmapInCollectionResult(
                found = false,
                errorMessage = e.message
            )
        }
    }


    override fun getData(user: User, bitmapUri: BitmapUri): UserDataResult {
        return try {
            firestoreInstance.collection(Constraints.USERS)
                .document("${user.userId}/bitmapUris/${bitmapUri.uriID}/bodyanalysis")
                .get()
                .run {
                    UserDataResult(
                        bodyAnalysisResponse = result.toObject<BodyAnalysisResponse>(),
                        isSuccessful = isSuccessful,
                        errorMessage = null
                    )
                }
        } catch (e: Exception) {
            UserDataResult(
                bodyAnalysisResponse = null,
                isSuccessful = false,
                errorMessage = e.message
            )
        }
    }

    override fun sendData(user: User, bitmapUri: BitmapUri, bodyAnalysisResponse: BodyAnalysisResponse): SendDataResponse {
        return try {
            firestoreInstance.collection(Constraints.USERS)
                .document("${user.userId}/bitmapUris/${bitmapUri.uriID}/bodyanalysis")
                .set(bodyAnalysisResponse)
                .run {
                    SendDataResponse(
                        isSuccessful = isSuccessful,
                        errorMessage = null
                    )
                }
        } catch (e: Exception) {
            SendDataResponse(
                isSuccessful = false,
                errorMessage = e.message
            )
        }
    }


}