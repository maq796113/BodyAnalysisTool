package com.example.bodyanalysistool.presentations.animation


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFFF4B400),
        Color(0xFF0F9D58),
        Color(0xFFDB4437),
        Color(0xFF4285F4)
    ),
    strokeWidth: Dp = 4.dp
) {
    val expansionDuration by remember { mutableIntStateOf(700) }
    val infiniteTransition = rememberInfiniteTransition(label = "")


    val currentColorIndex by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = colors.size,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = 2*expansionDuration*colors.size,
                easing = LinearEasing
            )
        ), label = ""
    )

    val progress by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(
                durationMillis = expansionDuration,
                easing = LinearEasing
            )
        ), label = ""
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = expansionDuration,
                easing = LinearEasing
            )
        ), label = ""
    )

    CircularProgressIndicator(
        modifier = modifier
            .rotate(rotation),
        progress = progress,
        color = colors[currentColorIndex],
        strokeWidth = strokeWidth
    )
}