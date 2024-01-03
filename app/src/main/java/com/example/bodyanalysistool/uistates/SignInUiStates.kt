package com.example.bodyanalysistool.uistates

import com.example.bodyanalysistool.data.AuthResult

data class SignInUiStates(
    val isSignInSuccessful: Boolean = false,
    val isSigningIn: Boolean = false,
    val signInErrorMessage: String? = null,
    val authResult: AuthResult? = null
)
