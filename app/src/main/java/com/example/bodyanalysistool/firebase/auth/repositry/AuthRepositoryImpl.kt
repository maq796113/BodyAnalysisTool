package com.example.bodyanalysistool.firebase.auth.repositry

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.bodyanalysistool.R
import com.example.bodyanalysistool.data.AuthResult
import com.example.bodyanalysistool.data.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient,
) : AuthRepository {
    private val auth = Firebase.auth

    override val currentUser: User?
        get() = auth.currentUser?.run {
            User(
                userId = uid,
                userEmail = email!!,
                userName = displayName!!
            )
        }

    override suspend fun loginUsingEmailPassword(email: String, password: String): AuthResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            AuthResult(
                user = user?.run {
                    User(
                        userId = uid,
                        userEmail = email,
                        userName = displayName!!
                    )
                },
                errorMessage = null
            )

        }catch(e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            AuthResult(
                user = null,
                errorMessage = e.message
            )
        }
    }

    override suspend fun loginUsingGoogleWithIntent(intent: Intent): AuthResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            AuthResult(
                user = user?.run {
                    User(
                        userId = uid,
                        userEmail = email!!,
                        userName = displayName!!
                    )
                },
                errorMessage = null
            )
        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            AuthResult(
                user = null,
                errorMessage = e.message
            )
        }
    }

    override suspend fun loginGoogleIntentSender(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
            result.pendingIntent.intentSender
        }catch(e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): AuthResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            Log.d("User", user?.uid.toString())
            user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            )?.await()
            AuthResult(
                user = user?.run {
                    User(
                        userId = uid,
                        userEmail = email,
                        userName = name
                    )
                },
                errorMessage = null
            )
        }catch(e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            AuthResult(
                user = null,
                errorMessage = e.message
            )
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest
            .Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    override suspend fun logout() {
        try {
            oneTapClient.signOut().await()
        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    override fun deleteUser() {
        try {
            auth.currentUser?.delete()
        }catch (e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }
}