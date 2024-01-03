package com.example.bodyanalysistool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.bodyanalysistool.presentations.animation.LoadingAnimation
import com.example.bodyanalysistool.ui.theme.BodyAnalysisToolTheme
import com.example.bodyanalysistool.ui.theme.kanitFontFamily
import com.example.bodyanalysistool.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyAnalysisToolTheme {
                val viewModel = hiltViewModel<AuthViewModel>()
                val signInState by viewModel.signInState
                val emailState by viewModel.emailState.collectAsStateWithLifecycle()
                val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
                val mContext = LocalContext.current
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = viewModel.loginUsingGoogleWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(
                                    result = signInResult
                                )
                            }
                        }
                    }
                )
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    LazyColumn(
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            IconPlacer()
                        }
                        item {
                            EmailTextField(viewModel= viewModel)
                        }
                        item {
                            PasswordTextField(viewModel=viewModel)
                        }
                        item {
                            ElevatedButton(onClick = {
                                viewModel.loginUsingEmailPassword(emailState.email!!, passwordState.password!!)
                                Log.d("SignInDebug", "Is SignInState.authResult null=${signInState.authResult == null}")
                                signInState.authResult?.let {
                                    viewModel.onSignInResult(
                                        result = it
                                    )
                                }
                                Log.d("SignInDebug", "Is SignInState.authResult null=${signInState.authResult == null}")
                                if (signInState.isSignInSuccessful || viewModel.getSignedInUser() != null) {
                                    viewModel.resetSignInState()
                                    mContext.startActivity(Intent(mContext, MainMenu::class.java))
                                } else {
                                    Toast.makeText(mContext, "The Sign In Was Unsuccessful", Toast.LENGTH_LONG).show()
                                }
                            }) {
                                Text(
                                    text = "Sign In",
                                    fontFamily = kanitFontFamily,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Don't have an account?",
                                    fontFamily = kanitFontFamily,
                                    fontSize = 12.sp
                                )
                                TextButton(
                                    onClick = {
                                        mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
                                    }
                                ) {
                                    Text(
                                        text = "Register",
                                        fontFamily = kanitFontFamily,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Login using your Google account",
                                    fontFamily = kanitFontFamily,
                                    fontSize = 12.sp
                                )
                                IconButton(onClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = viewModel.loginGoogleIntentSender()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                        if (viewModel.getSignedInUser() != null) {
                                            mContext.startActivity(Intent(mContext, MainMenu::class.java))
                                        }
                                    }
                                }) {
                                    Icon(
                                        ImageVector.vectorResource(id = R.drawable.google),
                                        contentDescription = null
                                    )

                                }
                            }
                        }
                        item {
                            if (signInState.isSigningIn) {
                                LoadingAnimation()
                            }
                        }
                    }

                }
            }
        }
    }
}

