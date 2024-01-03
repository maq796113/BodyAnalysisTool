package com.example.bodyanalysistool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bodyanalysistool.presentations.animation.LoadingAnimation
import com.example.bodyanalysistool.ui.theme.BodyAnalysisToolTheme
import com.example.bodyanalysistool.ui.theme.kanitFontFamily
import com.example.bodyanalysistool.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyAnalysisToolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    SignUpScreen()
                }
            }
        }
    }

}


@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel()
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
            NameTextField(viewModel=viewModel)
        }
        item {
            EmailTextField(viewModel= viewModel)
        }
        item {
            PasswordTextField(viewModel=viewModel)
        }
        item {
            ReWritePasswordTextField(viewModel = viewModel)
        }
        item {
            SignUpButton()
        }
    }

}

@Composable
fun SignUpButton() {
    val viewModel = hiltViewModel<AuthViewModel>()
    val state by viewModel.signUpState
    val mContext = LocalContext.current
    val nameState by viewModel.nameState.collectAsStateWithLifecycle()
    val emailState by viewModel.emailState.collectAsStateWithLifecycle()
    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()

    ElevatedButton(
        onClick = {
            viewModel.signup(nameState.name!!, emailState.email!!, passwordState.password!!)
            state.authResult?.let {
                viewModel.onSignUpResult(
                    result = it
                )
            }
            Log.d("InsideView", "AuthValue = ${state.authResult}")
            Log.d("InsideView", "signUpState.isSignUpSuccessful = ${state.isSignUpSuccessful}")
            if (state.isSignUpSuccessful) {
                viewModel.resetSignUpState()
                mContext.startActivity(Intent(mContext, SignInActivity::class.java))
            } else {
                Toast.makeText(mContext, "The Sign Up Was Unsuccessful", Toast.LENGTH_LONG).show()
                Toast.makeText(mContext, "!!Try Again!!", Toast.LENGTH_LONG).show()
            }
        }
    ) {
        Text(text = "Sign Up")
    }

    if (state.isSigningUp) {
        LoadingAnimation()
    }
}

@Composable
fun Title(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        fontFamily = kanitFontFamily,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun IconPlacer(
    modifier: Modifier = Modifier,
    id: Int = R.drawable.anatomy
) {
    Icon(
        painter = painterResource(id = id),
        contentDescription = "Anatomy",
        modifier = modifier
    )
}

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
) {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    var email by rememberSaveable { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    OutlinedTextField(
        value = email,
        onValueChange = {
            email = it
            viewModel.onChangedEmail(email)
            isError = false
            if (email.isEmpty()) {
                isError = true
                errorMessage = "The field is empty"
            }
            if (!email.matches(emailRegex.toRegex())) {
                isError = true
                errorMessage = "Not a valid email format"
            }
        },
        singleLine = true,
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (isError) {
                Icon(Icons.Filled.Warning, "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        leadingIcon = { Icon( imageVector = Icons.Default.AccountCircle, contentDescription = null) },
        label = { Text(
            text = "Email",
            fontFamily = kanitFontFamily
        ) },
        isError = isError,
        modifier = modifier
    )
}




@Composable
fun NameTextField(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    var name by rememberSaveable { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
            viewModel.onChangedName(name)
            isError = false
            if (name.isEmpty()) {
                isError = true
                errorMessage = "The field is empty"
            }
        },
        singleLine = true,
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (isError) {
                Icon(Icons.Filled.Warning, "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        leadingIcon = { Icon( imageVector = Icons.Default.AccountCircle, contentDescription = null) },
        label = { Text(
            text = "Name",
            fontFamily = kanitFontFamily
        ) },
        isError = isError,
        modifier = modifier
    )
}


@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
            viewModel.onChangedPassword(password)
            isError = false
            if (password.isEmpty()) {
                isError = true
                errorMessage = "The field is empty"
            } else {
                if (password.length < 5) {
                    isError = true
                    errorMessage = "The length of the password should be at least 5 characters"
                }
            }
                        },
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        leadingIcon = { Icon( imageVector = Icons.Default.Lock, contentDescription = null) },
        label = { Text(
            text = "Password",
            fontFamily = kanitFontFamily
        ) },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = {
                showPassword = !showPassword
            }) {
                Icon(imageVector  = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "Hide/Reveal Password")
            }
        },
        isError=isError
    )
}

@Composable
fun ReWritePasswordTextField(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    val signUpUIState by viewModel.signUpState
    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
            isError = false
            if (password.isEmpty()) {
                isError = true
                errorMessage = "The field is empty"
            } else {

                if ((signUpUIState.isSigningUp) && (passwordState.password != password)) {
                    isError = true
                    errorMessage = "The password doesn't match"
                }

            }
        },
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
        label = {
            Text(
                text = "Re-type Password",
                fontFamily = kanitFontFamily
            )
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = {
                showPassword = !showPassword
            }) {
                Icon(
                    imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    "Hide/Reveal Password"
                )
            }
        },
        isError = isError
    )
}





@Preview(showBackground = true)
@Composable
fun IconPlacerPreview() {
    BodyAnalysisToolTheme {
        IconPlacer()
    }
}

@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    BodyAnalysisToolTheme {
        Title("Android")
    }
}

