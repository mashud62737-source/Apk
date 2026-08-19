package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.VolumeMute
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Video
import com.example.ui.theme.GoldStar
import com.example.ui.theme.TokTokCyan
import com.example.ui.theme.TokTokPink

@Composable
fun VideoOverlay(
    video: Video,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onCreatorClick: (String) -> Unit,
    onToggleFollow: () -> Unit,
    onToggleLike: () -> Unit,
    onOpenComments: () -> Unit,
    onToggleSave: () -> Unit,
    onOpenShare: () -> Unit,
    onHashtagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "disc_rotate")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_angle"
    )

    val likeScale by animateFloatAsState(
        targetValue = if (video.isLikedByMe) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f),
        label = "like_bounce"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.35f),
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.75f)
                    )
                )
            )
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        // Top right mute toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, end = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { onToggleMute() }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Outlined.VolumeMute else Icons.Outlined.VolumeUp,
                    contentDescription = if (isMuted) "Unmute" else "Mute",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Right side action panel
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Creator Avatar with Follow Badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                AsyncImage(
                    model = video.userAvatarUrl,
                    contentDescription = video.userName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                        .clickable { onCreatorClick(video.userId) }
                )

                // Follow / Plus Badge
                if (!video.isFollowedCreator) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(TokTokPink)
                            .clickable { onToggleFollow() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Follow",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Like Action
            OverlayActionButton(
                icon = if (video.isLikedByMe) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                count = formatCount(video.likesCount),
                tint = if (video.isLikedByMe) TokTokPink else Color.White,
                iconScale = likeScale,
                onClick = onToggleLike
            )

            // Comment Action
            OverlayActionButton(
                icon = Icons.Outlined.ChatBubbleOutline,
                count = formatCount(video.commentsCount),
                tint = Color.White,
                onClick = onOpenComments
            )

            // Bookmark / Save Action
            OverlayActionButton(
                icon = if (video.isSavedByMe) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                count = formatCount(if (video.isSavedByMe) 1420 else 1419),
                tint = if (video.isSavedByMe) GoldStar else Color.White,
                onClick = onToggleSave
            )

            // Share Action
            OverlayActionButton(
                icon = Icons.Default.Share,
                count = formatCount(video.sharesCount),
                tint = Color.White,
                onClick = onOpenShare
            )

            // Rotating Music Vinyl Disc
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1F2A))
                    .border(1.5.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                    .rotate(discRotation),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = video.userAvatarUrl,
                    contentDescription = "Audio track",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Bottom Creator & Video Metadata
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(bottom = 70.dp, start = 4.dp)
        ) {
            // Creator handle & verified badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onCreatorClick(video.userId) }
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = "@${video.userHandle}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = TokTokCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // View count badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Views",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = formatCount(video.viewsCount),
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Caption with interactive highlighted hashtags
            val captionText = buildAnnotatedString {
                val words = video.caption.split(" ")
                words.forEachIndexed { index, word ->
                    if (word.startsWith("#")) {
                        pushStringAnnotation(tag = "HASHTAG", annotation = word.removePrefix("#"))
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                            append(word)
                        }
                        pop()
                    } else if (word.startsWith("@")) {
                        withStyle(style = SpanStyle(color = TokTokCyan, fontWeight = FontWeight.SemiBold)) {
                            append(word)
                        }
                    } else {
                        withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.92f))) {
                            append(word)
                        }
                    }
                    if (index < words.size - 1) append(" ")
                }
            }

            Text(
                text = captionText,
                fontSize = 13.5.sp,
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Audio track ticker with musical note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${video.musicTitle} • ${video.musicAuthor}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun OverlayActionButton(
    icon: ImageVector,
    count: String,
    tint: Color,
    onClick: () -> Unit,
    iconScale: Float = 1.0f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(28.dp)
                    .scale(iconScale)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.shadow(4.dp)
        )
    }
}

fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 10_000 -> String.format("%.1fK", count / 1_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
