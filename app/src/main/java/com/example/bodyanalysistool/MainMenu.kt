package com.example.bodyanalysistool

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bodyanalysistool.ui.theme.BodyAnalysisToolTheme
import com.example.bodyanalysistool.ui.theme.Typography
import com.example.bodyanalysistool.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyAnalysisToolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!hasRequiredPermissions()) {
                        ActivityCompat.requestPermissions(
                            this, CAMERAX_PERMISSIONS, 0
                        )
                    }
                    DashboardWithBottomAppBar()
                }
            }
        }
    }
    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

    }
}

@Composable
fun PartDashboardLayout() {
    val viewModel = hiltViewModel<AuthViewModel>()
    val user = viewModel.getSignedInUser()
    Row {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(70.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(0.03f),
                    shape = CircleShape
                )
                .border(1.dp, Color.LightGray, CircleShape)
                .align(Alignment.Top)
                .padding(12.dp)
        ) {
            IconPlacer()
        }

        if (user != null) {
            Text(
                text = "Greetings ${user.userName}",
                style = Typography.titleLarge,
                modifier = Modifier
                    .padding(12.dp)
                    .offset(y = 5.dp)
            )
        }
    }
}

@Composable
fun DashboardWithBottomAppBar() {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val mContext = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val intent = Intent(mContext, ResultsActivity::class.java)
                intent.putExtra("selectedBitmap", uri.toString())
                mContext.startActivity(intent)
            }
        }
    )
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Localized description",
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                                  authViewModel.logout()
                            mContext.startActivity(Intent(mContext, SignInActivity::class.java))
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Logout, "Localized description")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            PartDashboardLayout()
            IconButton(
                onClick = {
                    mContext.startActivity(Intent(mContext, CameraXActivity::class.java))
                },
                modifier= Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxSize()
            ) {
                Icon(
                    Icons.Filled.Camera,
                    contentDescription = "Localized description",
                    modifier= Modifier
                        .fillMaxSize()
                )
            }

        }

    }
}





