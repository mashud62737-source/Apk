package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.ScreenTab
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.UploadScreen
import com.example.ui.theme.TokTokTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            TokTokTheme(darkTheme = isDarkTheme) {
                TokTokApp(viewModel = viewModel, isDarkTheme = isDarkTheme)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokTokApp(
    viewModel: MainViewModel,
    isDarkTheme: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val currentTab by viewModel.currentTab.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val unreadNotifsCount by viewModel.unreadNotificationsCount.collectAsState()

    val currentUser by viewModel.currentUser.collectAsState()
    val inspectedUserId by viewModel.inspectedUserId.collectAsState()
    val allVideos by viewModel.allVideos.collectAsState()
    val followingVideos by viewModel.followingVideos.collectAsState()
    val likedVideos by viewModel.likedVideos.collectAsState()
    val savedVideos by viewModel.savedVideos.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val initialSearchQuery by viewModel.initialSearchQuery.collectAsState()

    // Auth sheet state
    val authSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showAuthSheet by remember { mutableStateOf(false) }

    // Active profile user determination
    val displayUser = if (inspectedUserId != null) {
        allUsers.firstOrNull { it.id == inspectedUserId } ?: currentUser
    } else {
        currentUser
    }
    val isViewingCurrentUser = inspectedUserId == null || inspectedUserId == currentUser?.id

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentTab != ScreenTab.ADMIN) {
                BottomNavBar(
                    currentTab = currentTab,
                    unreadNotificationsCount = unreadNotifsCount,
                    isDarkTheme = isDarkTheme,
                    onTabSelected = { tab -> viewModel.selectTab(tab) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = if (currentTab == ScreenTab.FEED) androidx.compose.ui.unit.Dp.Unspecified else innerPadding.calculateBottomPadding()
                )
        ) {
            when (currentTab) {
                ScreenTab.FEED -> {
                    FeedScreen(
                        forYouVideos = allVideos,
                        followingVideos = followingVideos,
                        currentComments = emptyList(), // Dynamically queried via repository
                        currentUser = currentUser,
                        isMuted = isMuted,
                        onToggleMute = { viewModel.toggleMute() },
                        onToggleLike = { video -> viewModel.toggleLike(video) },
                        onToggleSave = { video -> viewModel.toggleSave(video) },
                        onToggleFollow = { id, status -> viewModel.toggleFollow(id, status) },
                        onSendComment = { videoId, text -> viewModel.sendComment(videoId, text) },
                        onToggleCommentLike = { comment -> viewModel.toggleCommentLike(comment) },
                        onShareVideo = { video -> viewModel.shareVideo(video) },
                        onReportVideo = { video -> viewModel.reportVideo(video) },
                        onIncrementViews = { videoId -> viewModel.incrementViews(videoId) },
                        onCreatorClick = { userId -> viewModel.viewUserProfile(userId) },
                        onSearchClick = { viewModel.selectTab(ScreenTab.SEARCH) },
                        onHashtagClick = { tag -> viewModel.searchHashtag(tag) }
                    )
                }

                ScreenTab.SEARCH -> {
                    SearchScreen(
                        videos = allVideos,
                        users = allUsers,
                        trendingHashtags = viewModel.trendingHashtags,
                        onVideoClick = {
                            viewModel.selectTab(ScreenTab.FEED)
                        },
                        onUserClick = { userId -> viewModel.viewUserProfile(userId) },
                        onToggleFollow = { id, status -> viewModel.toggleFollow(id, status) },
                        initialQuery = initialSearchQuery
                    )
                }

                ScreenTab.UPLOAD -> {
                    UploadScreen(
                        onPublishSuccess = {
                            viewModel.selectTab(ScreenTab.FEED)
                        },
                        onUploadVideo = { videoUrl, thumbUrl, caption, hashtags, musicTitle, musicAuthor ->
                            viewModel.uploadVideo(videoUrl, thumbUrl, caption, hashtags, musicTitle, musicAuthor)
                        },
                        popularSounds = viewModel.popularSounds
                    )
                }

                ScreenTab.NOTIFICATIONS -> {
                    NotificationsScreen(
                        notifications = notifications,
                        onMarkAllRead = { viewModel.markAllNotificationsRead() },
                        onNotificationClick = { notif ->
                            viewModel.markNotificationRead(notif.id)
                            if (notif.sourceUserId.isNotEmpty()) {
                                viewModel.viewUserProfile(notif.sourceUserId)
                            }
                        },
                        onUserClick = { userId -> viewModel.viewUserProfile(userId) },
                        onFollowBack = { userId -> viewModel.toggleFollow(userId, false) }
                    )
                }

                ScreenTab.PROFILE -> {
                    val userVideos = remember(displayUser?.id, allVideos) {
                        allVideos.filter { it.userId == displayUser?.id }
                    }

                    ProfileScreen(
                        user = displayUser,
                        isCurrentUser = isViewingCurrentUser,
                        userVideos = userVideos,
                        likedVideos = likedVideos,
                        savedVideos = savedVideos,
                        isDarkTheme = isDarkTheme,
                        onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                        onToggleFollow = { id, status -> viewModel.toggleFollow(id, status) },
                        onUpdateProfile = { name, handle, bio, avatar ->
                            viewModel.updateProfile(name, handle, bio, avatar)
                        },
                        onVideoClick = {
                            viewModel.selectTab(ScreenTab.FEED)
                        },
                        onOpenAdmin = { viewModel.selectTab(ScreenTab.ADMIN) },
                        onSwitchAccountClick = { showAuthSheet = true }
                    )
                }

                ScreenTab.ADMIN -> {
                    AdminScreen(
                        videos = allVideos,
                        users = allUsers,
                        onBack = { viewModel.selectTab(ScreenTab.PROFILE) },
                        onDeleteVideo = { videoId -> viewModel.deleteVideo(videoId) },
                        onTogglePin = { videoId, isPinned -> viewModel.togglePinVideo(videoId, isPinned) }
                    )
                }
            }
        }
    }

    // Auth Switcher / Registration Dialog
    if (showAuthSheet) {
        AuthDialog(
            sheetState = authSheetState,
            allUsers = allUsers,
            onDismiss = {
                coroutineScope.launch {
                    authSheetState.hide()
                    showAuthSheet = false
                }
            },
            onSwitchUser = { userId ->
                viewModel.switchUser(userId)
            },
            onRegisterUser = { username, displayName, email, bio ->
                viewModel.registerUser(username, displayName, email, bio)
            }
        )
    }
}
