package com.example.bodyanalysistool.viewmodel

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodyanalysistool.data.AuthResult
import com.example.bodyanalysistool.firebase.auth.repositry.AuthRepository
import com.example.bodyanalysistool.firebase.firestore.repository.FirestoreRepository
import com.example.bodyanalysistool.states.EmailStates
import com.example.bodyanalysistool.states.NameStates
import com.example.bodyanalysistool.states.PasswordStates
import com.example.bodyanalysistool.uistates.SignInUiStates
import com.example.bodyanalysistool.uistates.SignUpUIStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
): ViewModel() {

    private val _signInState = mutableStateOf(SignInUiStates())
    val signInState : State<SignInUiStates> = _signInState


    private val _signUpState = mutableStateOf(SignUpUIStates())
    val signUpState : State<SignUpUIStates> = _signUpState


    private val _nameState = MutableStateFlow(NameStates())
    val nameState = _nameState.asStateFlow()


    private val _emailState = MutableStateFlow(EmailStates())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(PasswordStates())
    val passwordState = _passwordState.asStateFlow()

    fun getSignedInUser() = authRepository.currentUser


    fun onChangedName(name: String) {
        _nameState.update {
            it.copy(
                name = name
            )
        }
    }

    fun onChangedEmail(email: String) {
        _emailState.update {
            it.copy(
                email = email
            )
        }
    }

    fun onChangedPassword(password: String) {
        _passwordState.update {
            it.copy(
                password = password
            )
        }
    }

    suspend fun loginUsingGoogleWithIntent(intent: Intent) = authRepository.loginUsingGoogleWithIntent(intent = intent)



    fun signup(name: String, email: String, password: String) {
        _signUpState.value = signUpState.value.copy(
            isSigningUp = true
        )

        viewModelScope.launch {
            _signUpState.value = signUpState.value.copy(
                authResult=authRepository.signup(name, email, password)
            )
        }

    }

    suspend fun loginGoogleIntentSender(): IntentSender? {
        _signInState.value = signInState.value.copy(
            isSigningIn = true
        )
        return authRepository.loginGoogleIntentSender()
    }

    fun loginUsingEmailPassword(email: String, password: String){
        _signInState.value = signInState.value.copy(
            isSigningIn = true
        )

        viewModelScope.launch {
            _signInState.value = signInState.value.copy(
                    authResult = authRepository.loginUsingEmailPassword(email, password)
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun deleteUser() {
        authRepository.deleteUser()
    }

    fun onSignInResult(result: AuthResult) {

        _signInState.value = signInState.value.copy(
            isSignInSuccessful = result.user != null,
            signInErrorMessage = result.errorMessage
        )
        if(result.user != null){
            Log.d(TAG, "user is not null")

        }
        if(result.errorMessage != null){
            _signInState.value = signInState.value.copy(
                    isSigningIn = false
            )
        }
    }

    fun onSignUpResult(result: AuthResult) {
        _signUpState.value = signUpState.value.copy(
                isSignUpSuccessful = result.user != null,
                signUpErrorMessage = result.errorMessage
        )
        Log.d("InsideViewModel", " signUpState.isSignUpSuccessful = ${signUpState.value.isSignUpSuccessful}")
        if(result.user != null){
            Log.d(TAG, "user is not null")
            firestoreRepository.addNewUser(
                user = result.user
            )
        }
        if(result.errorMessage != null){
            _signUpState.value = signUpState.value.copy(
                    isSigningUp = false
            )
        }
    }


    fun resetSignInState() {
        _signInState.value = SignInUiStates()
    }

    fun resetSignUpState() {
        _signUpState.value = SignUpUIStates()
    }

    companion object{
        private const val TAG = "Authentication ViewModel"
    }

}







