package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TokTokPink
import kotlin.math.roundToInt
import kotlin.random.Random

data class HeartEffect(
    val id: Long,
    val position: Offset,
    val rotation: Float = Random.nextFloat() * 40f - 20f
)

@Composable
fun HeartBurstAnimation(
    heart: HeartEffect,
    onAnimationEnd: () -> Unit
) {
    val scale = remember { Animatable(0.2f) }
    val alpha = remember { Animatable(1f) }
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(heart.id) {
        scale.animateTo(
            targetValue = 1.3f,
            animationSpec = keyframes {
                durationMillis = 650
                0.2f at 0
                1.4f at 200 using FastOutSlowInEasing
                1.0f at 400
                1.2f at 650
            }
        )
    }

    LaunchedEffect(heart.id) {
        offsetY.animateTo(
            targetValue = -120f,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(heart.id) {
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = keyframes {
                durationMillis = 700
                1f at 0
                1f at 450
                0f at 700
            }
        )
        onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Liked Heart",
            tint = TokTokPink.copy(alpha = alpha.value.coerceIn(0f, 1f)),
            modifier = Modifier
                .size(100.dp)
                .offset {
                    IntOffset(
                        x = (heart.position.x - 50.dp.toPx()).roundToInt(),
                        y = (heart.position.y - 50.dp.toPx() + offsetY.value).roundToInt()
                    )
                }
                .rotate(heart.rotation)
                .scale(scale.value)
        )
    }
}
