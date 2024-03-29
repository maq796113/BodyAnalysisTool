package com.example.bodyanalysistool.presentations

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bodyanalysistool.GeminiAIActivity
import com.example.bodyanalysistool.viewmodel.GeminiAIViewModel1
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun PhotoBottomSheetContent(
    bitmaps: List<Bitmap>,
    geminiAIViewModel: GeminiAIViewModel1 = hiltViewModel(),
    modifier: Modifier,
) {
    val context = LocalContext.current
    val uiState by geminiAIViewModel.uiState.collectAsStateWithLifecycle()
    val bitmapState by geminiAIViewModel.bitmapState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    LaunchedEffect(uiState) {
        if (uiState.isLoading == false) {

            val intent = Intent(context, GeminiAIActivity::class.java)
            println("Im here")
            geminiAIViewModel.saveSession()
            context.startActivity(intent)
        }
    }


    if (bitmaps.isEmpty()) {
        Box(
            modifier = modifier
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("There are no photos yet")
        }
    } else {

        //recomposition

        if (uiState.isLoading == true) { //if isLoading true, the progress indicator should pop up on the sheet
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier.fillMaxSize())
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp,
                contentPadding = PaddingValues(16.dp),
                modifier = modifier
            ) {
                items(bitmaps) { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                geminiAIViewModel.initializeLoading()
                                //I want recomposition from this point

                                //I want a progress indicator working

                                //the following line of code should run after isLoading is true
                                scope.launch {
                                    geminiAIViewModel.prompt(bitmap)
                                   if (bitmapState.bitmapUri == null) {
                                       val path = saveImage(bitmap, context)
                                       geminiAIViewModel.getSelectedBitmap(path)
                                   }
                                }
                            }
                    )
                }
            }

        }

    }
}

fun saveImage(bitmap: Bitmap, context: Context): String {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
    return MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap,
        "SelectedImage",
        null
    )
}
