package com.example.bodyanalysistool

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.bodyanalysistool.data.BitmapUri
import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.data.User
import com.example.bodyanalysistool.events.UserEvent
import com.example.bodyanalysistool.ui.theme.BodyAnalysisToolTheme
import com.example.bodyanalysistool.viewmodel.AuthViewModel
import com.example.bodyanalysistool.viewmodel.FirestoreViewModel
import com.example.bodyanalysistool.viewmodel.MediaPipeViewModel
import com.example.bodyanalysistool.viewmodel.StorageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.getStringExtra("selectedBitmap")
        setContent {
            BodyAnalysisToolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultView(uri = uri)
                }
            }
        }
    }
}

@Composable
fun ResultView(
    modifier: Modifier = Modifier,
    mediaPipeViewModel: MediaPipeViewModel = hiltViewModel(),
    firestoreViewModel: FirestoreViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    storageViewModel: StorageViewModel = hiltViewModel(),
    uri: String?
) {
    val isBitmapInCollectionState by firestoreViewModel.isBitmapInCollectionState.collectAsStateWithLifecycle()
    val getUserDataState by firestoreViewModel.getUserDatastate.collectAsStateWithLifecycle()
    val imageDownloadedState by mediaPipeViewModel.imageDownloadedState.collectAsStateWithLifecycle()
    val bodyAnalysisState by mediaPipeViewModel.bodyAnalysisState.collectAsStateWithLifecycle()
    val uploadUserImageState by storageViewModel.uploadUserImageState.collectAsStateWithLifecycle()
    var _bodyAnalysisState = remember<BodyAnalysisResponse?> { null }
    val bodyAnalysis by rememberUpdatedState(newValue = _bodyAnalysisState)
    Log.d("ResultsView", "SignedInUser: ${authViewModel.getSignedInUser()}")
    firestoreViewModel.isBitmapInCollection(authViewModel.getSignedInUser()!!, BitmapUri(UUID.randomUUID().toString(), uri))
    if (isBitmapInCollectionState.isBitmapInCollectionResult?.found == false) {
        firestoreViewModel.addBitmap(authViewModel.getSignedInUser()!!, BitmapUri(UUID.randomUUID().toString(), uri))
        val user: User? = authViewModel.getSignedInUser()
        if (uri != null && user != null) {
            storageViewModel.onEvent(UserEvent.UploadUserImage(uri.toUri(), user.userEmail))
            uploadUserImageState.uploadResult?.downloadLink?.let {
                UserEvent.CheckImageDownloaded(
                    it
                )
            }?.let { mediaPipeViewModel.onEvent(it) }
            mediaPipeViewModel.onEvent(UserEvent.BodyAnalysis)

            firestoreViewModel.sendData(user, BitmapUri(UUID.randomUUID().toString(), uri), bodyAnalysisState.bodyAnalysisResponse!!)

            _bodyAnalysisState = bodyAnalysisState.bodyAnalysisResponse
        }
    } else {
        firestoreViewModel.getData(authViewModel.getSignedInUser()!!, BitmapUri(UUID.randomUUID().toString(), uri))

        _bodyAnalysisState = getUserDataState.userDataResult?.bodyAnalysisResponse

    }
    Column {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = modifier
                .size(100.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = modifier)
        (if ((imageDownloadedState.imageDownloadedResponse?.imageDownloaded == "True") || (getUserDataState.userDataResult != null)) bodyAnalysis?.messageOnShoulder else "Please Wait....")?.let {
            Text(
                text=it
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    BodyAnalysisToolTheme {

    }
}