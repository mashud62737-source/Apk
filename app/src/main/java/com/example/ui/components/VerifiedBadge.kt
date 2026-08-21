package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TokTokCyan

@Composable
fun VerifiedBadge(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    badgeColor: Color = TokTokCyan
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(badgeColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Verified Account",
            tint = Color.White,
            modifier = Modifier
                .size(size * 0.75f)
                .padding(1.dp)
        )
    }
}
