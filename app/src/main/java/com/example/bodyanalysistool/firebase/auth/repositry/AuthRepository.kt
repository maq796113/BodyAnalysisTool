package com.example.bodyanalysistool.firebase.auth.repositry

import android.content.Intent
import android.content.IntentSender
import com.example.bodyanalysistool.data.AuthResult
import com.example.bodyanalysistool.data.User

interface AuthRepository {
    val currentUser: User?
    suspend fun loginUsingEmailPassword(email: String, password: String): AuthResult
    suspend fun loginUsingGoogleWithIntent(intent: Intent): AuthResult
    suspend fun loginGoogleIntentSender(): IntentSender?
    suspend fun signup(name: String, email: String, password: String): AuthResult
    suspend fun logout()
    fun deleteUser()
}