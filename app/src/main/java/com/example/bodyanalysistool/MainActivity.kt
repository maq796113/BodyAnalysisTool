package com.example.bodyanalysistool

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bodyanalysistool.ui.theme.BodyAnalysisToolTheme
import com.example.bodyanalysistool.ui.theme.Brown
import com.example.bodyanalysistool.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        setContent {
            BodyAnalysisToolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WelcomeScreen()
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.davinci),
            contentDescription = "The Vitruvian Man",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.anatomy),
                contentDescription = null,
                tint = Brown,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = stringResource(id = R.string.welcome_text1),
                style = Typography.titleLarge,
                color = Color.Black,
                modifier = Modifier
                    .padding(10.dp)
            )


            val mContext = LocalContext.current
            ElevatedButton(
                onClick = {
                mContext.startActivity(Intent(mContext, SignInActivity::class.java))
            },
                modifier = Modifier
                    .width(190.dp)
                    .padding(10.dp)
            ) {
                Text(text = "Sign In")
            }

            ElevatedButton(
                onClick = {
                    mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
                },
                modifier = Modifier
                    .width(190.dp)
                    .padding(10.dp)
            ) {
                Text(text = "Sign Up")
            }

        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun WelcomeScreenPreview() {
    BodyAnalysisToolTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WelcomeScreen()
        }
    }
}




