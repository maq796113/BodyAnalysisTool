package com.example.bodyanalysistool

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bodyanalysistool.ui.theme.BodyAnalysisToolTheme
import com.example.bodyanalysistool.viewmodel.GeminiAIViewModel2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeminiAIActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyAnalysisToolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier, geminiAIViewModel: GeminiAIViewModel2 = hiltViewModel()) {


    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Title(
                title = "Results",
                modifier=modifier.padding(10.dp)
            )
        }
        item {
            AsyncImage(
                model = Uri.parse(geminiAIViewModel.session?.bitmapState?.bitmapUri ?: "https://www.freeiconspng.com/uploads/error-icon-15.png"),
                contentDescription = null,
                modifier = modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        item {
            if (geminiAIViewModel.session?.geminiAIUiState?.isSuccessful == true) {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    geminiAIViewModel.session!!.geminiAIUiState.response?.let { Text(text = it) }
                }
            } else {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    geminiAIViewModel.session!!.geminiAIUiState.response?.let { Text(text = it) }
                }
            }
        }


    }
}

