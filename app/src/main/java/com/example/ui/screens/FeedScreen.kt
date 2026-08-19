package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Comment
import com.example.model.User
import com.example.model.Video
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.HeartBurstAnimation
import com.example.ui.components.HeartEffect
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.VideoOverlay
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.TokTokPink
import kotlinx.coroutines.launch

enum class FeedType {
    FOLLOWING,
    FOR_YOU
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    forYouVideos: List<Video>,
    followingVideos: List<Video>,
    currentComments: List<Comment>,
    currentUser: User?,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onToggleLike: (Video) -> Unit,
    onToggleSave: (Video) -> Unit,
    onToggleFollow: (String, Boolean) -> Unit,
    onSendComment: (String, String) -> Unit,
    onToggleCommentLike: (Comment) -> Unit,
    onShareVideo: (Video) -> Unit,
    onReportVideo: (Video) -> Unit,
    onIncrementViews: (String) -> Unit,
    onCreatorClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onHashtagClick: (String) -> Unit
) {
    var selectedFeedType by remember { mutableStateOf(FeedType.FOR_YOU) }
    val displayVideos = if (selectedFeedType == FeedType.FOR_YOU) forYouVideos else followingVideos

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { displayVideos.size }
    )

    val coroutineScope = rememberCoroutineScope()
    val commentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCommentSheet by remember { mutableStateOf(false) }
    var activeVideoForComments by remember { mutableStateOf<Video?>(null) }

    val shareSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showShareSheet by remember { mutableStateOf(false) }
    var activeVideoForShare by remember { mutableStateOf<Video?>(null) }

    val heartAnimations = remember { mutableStateListOf<HeartEffect>() }

    // Track active video view count
    LaunchedEffect(pagerState.currentPage, displayVideos.size) {
        if (displayVideos.isNotEmpty() && pagerState.currentPage < displayVideos.size) {
            val currentVideo = displayVideos[pagerState.currentPage]
            onIncrementViews(currentVideo.id)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (displayVideos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LiveTv,
                        contentDescription = null,
                        tint = TokTokPink,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (selectedFeedType == FeedType.FOLLOWING) "No videos from followed creators" else "No videos available",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (selectedFeedType == FeedType.FOLLOWING) "Follow some awesome creators to see their latest videos here!" else "Be the first to upload a video!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (selectedFeedType == FeedType.FOLLOWING) {
                        Button(
                            onClick = { selectedFeedType = FeedType.FOR_YOU },
                            colors = ButtonDefaults.buttonColors(containerColor = TokTokPink)
                        ) {
                            Text("Explore For You Feed")
                        }
                    }
                }
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { page ->
                val video = displayVideos[page]
                val isCurrentPage = pagerState.currentPage == page

                Box(modifier = Modifier.fillMaxSize()) {
                    VideoPlayerView(
                        video = video,
                        isPlaying = isCurrentPage,
                        isMuted = isMuted,
                        onSingleTap = { /* Play/Pause is handled internally */ },
                        onDoubleTap = { tapOffset ->
                            onToggleLike(video)
                            heartAnimations.add(
                                HeartEffect(
                                    id = System.currentTimeMillis() + (0..1000).random(),
                                    position = tapOffset
                                )
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    VideoOverlay(
                        video = video,
                        isMuted = isMuted,
                        onToggleMute = onToggleMute,
                        onCreatorClick = { onCreatorClick(video.userId) },
                        onToggleFollow = { onToggleFollow(video.userId, video.isFollowedCreator) },
                        onToggleLike = {
                            onToggleLike(video)
                            if (!video.isLikedByMe) {
                                heartAnimations.add(
                                    HeartEffect(
                                        id = System.currentTimeMillis(),
                                        position = Offset(300f, 600f)
                                    )
                                )
                            }
                        },
                        onOpenComments = {
                            activeVideoForComments = video
                            showCommentSheet = true
                        },
                        onToggleSave = { onToggleSave(video) },
                        onOpenShare = {
                            activeVideoForShare = video
                            onShareVideo(video)
                            showShareSheet = true
                        },
                        onHashtagClick = onHashtagClick
                    )
                }
            }
        }

        // Heart Burst Animations Container
        heartAnimations.forEach { heart ->
            HeartBurstAnimation(
                heart = heart,
                onAnimationEnd = {
                    heartAnimations.remove(heart)
                }
            )
        }

        // Top Navigation Bar ("Following" | "For You") & Search
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Live badge / stream icon
            IconButton(
                onClick = { /* Live stream explore */ },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Default.LiveTv,
                    contentDescription = "Live",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Following | For You Tabs
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Following Tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedFeedType = FeedType.FOLLOWING }
                ) {
                    Text(
                        text = "Following",
                        color = if (selectedFeedType == FeedType.FOLLOWING) Color.White else Color.White.copy(alpha = 0.6f),
                        fontWeight = if (selectedFeedType == FeedType.FOLLOWING) FontWeight.Bold else FontWeight.Normal,
                        fontSize = if (selectedFeedType == FeedType.FOLLOWING) 17.sp else 16.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    if (selectedFeedType == FeedType.FOLLOWING) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(2.5.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(2.5.dp))
                    }
                }

                // For You Tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedFeedType = FeedType.FOR_YOU }
                ) {
                    Text(
                        text = "For You",
                        color = if (selectedFeedType == FeedType.FOR_YOU) Color.White else Color.White.copy(alpha = 0.6f),
                        fontWeight = if (selectedFeedType == FeedType.FOR_YOU) FontWeight.Bold else FontWeight.Normal,
                        fontSize = if (selectedFeedType == FeedType.FOR_YOU) 17.sp else 16.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    if (selectedFeedType == FeedType.FOR_YOU) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(2.5.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(2.5.dp))
                    }
                }
            }

            // Search Icon
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Comments Bottom Sheet
        if (showCommentSheet && activeVideoForComments != null) {
            val video = activeVideoForComments!!
            CommentsBottomSheet(
                sheetState = commentSheetState,
                comments = currentComments.filter { it.videoId == video.id },
                currentUser = currentUser,
                onDismiss = {
                    coroutineScope.launch {
                        commentSheetState.hide()
                        showCommentSheet = false
                    }
                },
                onSendComment = { text ->
                    onSendComment(video.id, text)
                },
                onToggleCommentLike = onToggleCommentLike,
                onUserClick = { userId ->
                    coroutineScope.launch {
                        commentSheetState.hide()
                        showCommentSheet = false
                    }
                    onCreatorClick(userId)
                }
            )
        }

        // Share Bottom Sheet
        if (showShareSheet && activeVideoForShare != null) {
            val video = activeVideoForShare!!
            ShareBottomSheet(
                sheetState = shareSheetState,
                video = video,
                onDismiss = {
                    coroutineScope.launch {
                        shareSheetState.hide()
                        showShareSheet = false
                    }
                },
                onReportVideo = { onReportVideo(video) }
            )
        }
    }
}
