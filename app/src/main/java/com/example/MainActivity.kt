package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import com.example.algorithm.RecommendationEngine
import com.example.data.AppRepository
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.components.BottomNavBar
import com.example.ui.components.ScreenTab
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.DirectMessagesScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.UploadScreen
import com.example.ui.theme.TokTokTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = AppRepository(applicationContext)
        val factory = MainViewModelFactory(repository)

        setContent {
            val viewModel: MainViewModel = viewModel(factory = factory)
            var isDarkTheme by remember { mutableStateOf(true) }

            TokTokTheme(darkTheme = isDarkTheme) {
                TokTokApp(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onToggleDarkTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokTokApp(
    viewModel: MainViewModel,
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(ScreenTab.FEED) }
    var isMuted by remember { mutableStateOf(false) }

    val currentUser by viewModel.currentUser.collectAsState()
    val inspectedUserId by viewModel.inspectedUserId.collectAsState()
    val activeChatUserId by viewModel.activeChatUserId.collectAsState()

    val allVideos by viewModel.allVideos.collectAsState(initial = emptyList())
    val followingVideos by viewModel.followingVideos.collectAsState(initial = emptyList())
    val likedVideos by viewModel.likedVideos.collectAsState(initial = emptyList())
    val savedVideos by viewModel.savedVideos.collectAsState(initial = emptyList())
    val allUsers by viewModel.searchUsers("").collectAsState(initial = emptyList())
    val notifications by viewModel.notifications.collectAsState(initial = emptyList())

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

    // Active chat user
    val activeChatUser = remember(activeChatUserId, allUsers) {
        allUsers.firstOrNull { it.id == activeChatUserId }
    }

    // Handle back button on sub-screens
    BackHandler(enabled = currentTab != ScreenTab.FEED || inspectedUserId != null || activeChatUserId != null) {
        if (activeChatUserId != null) {
            viewModel.closeChat()
        } else if (inspectedUserId != null) {
            viewModel.clearInspectedUser()
        } else if (currentTab == ScreenTab.SETTINGS || currentTab == ScreenTab.DIRECT_MESSAGES || currentTab == ScreenTab.ADMIN) {
            currentTab = ScreenTab.PROFILE
        } else {
            currentTab = ScreenTab.FEED
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentTab != ScreenTab.ADMIN && currentTab != ScreenTab.SETTINGS && (currentTab != ScreenTab.DIRECT_MESSAGES || activeChatUser == null)) {
                BottomNavBar(
                    currentTab = currentTab,
                    unreadNotificationsCount = notifications.count { !it.isRead },
                    isDarkTheme = isDarkTheme,
                    onTabSelected = { tab ->
                        if (inspectedUserId != null) {
                            viewModel.clearInspectedUser()
                        }
                        currentTab = tab
                    }
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
                        currentComments = emptyList(),
                        currentUser = currentUser,
                        isMuted = isMuted,
                        onToggleMute = { isMuted = !isMuted },
                        onToggleLike = { video -> viewModel.toggleLike(video) },
                        onToggleSave = { video -> viewModel.toggleSave(video) },
                        onToggleFollow = { id, status -> viewModel.toggleFollow(id, status) },
                        onSendComment = { videoId, text -> viewModel.addComment(videoId, text) },
                        onToggleCommentLike = { comment -> viewModel.toggleCommentLike(comment) },
                        onShareVideo = { video -> viewModel.shareVideo(video.id) },
                        onReportVideo = { _ -> },
                        onIncrementViews = { videoId ->
                            allVideos.firstOrNull { it.id == videoId }?.let { viewModel.recordImpression(it) }
                        },
                        onCreatorClick = { userId ->
                            viewModel.inspectUser(userId)
                            currentTab = ScreenTab.PROFILE
                        },
                        onSearchClick = { currentTab = ScreenTab.SEARCH },
                        onHashtagClick = { tag ->
                            currentTab = ScreenTab.SEARCH
                        },
                        onNavigateToUpload = { currentTab = ScreenTab.UPLOAD },
                        onGetAlgoInsight = { video ->
                            RecommendationEngine.evaluateVideo(currentUser?.id ?: "user_me", video, null)
                        },
                        onRecordTelemetry = { video, _ -> viewModel.recordImpression(video) }
                    )
                }

                ScreenTab.SEARCH -> {
                    SearchScreen(
                        videos = allVideos,
                        users = allUsers,
                        trendingHashtags = viewModel.trendingHashtags,
                        onVideoClick = {
                            currentTab = ScreenTab.FEED
                        },
                        onUserClick = { userId ->
                            viewModel.inspectUser(userId)
                            currentTab = ScreenTab.PROFILE
                        },
                        onToggleFollow = { id, status -> viewModel.toggleFollow(id, status) },
                        initialQuery = ""
                    )
                }

                ScreenTab.UPLOAD -> {
                    UploadScreen(
                        onPublishSuccess = {
                            currentTab = ScreenTab.FEED
                        },
                        onUploadVideo = { videoUrl, thumbUrl, caption, category, hashtags, musicTitle, musicAuthor ->
                            viewModel.uploadVideo(videoUrl, thumbUrl, caption, category, hashtags, musicTitle, musicAuthor)
                        },
                        popularSounds = viewModel.popularSounds,
                        videoCategories = viewModel.videoCategories
                    )
                }

                ScreenTab.NOTIFICATIONS -> {
                    NotificationsScreen(
                        notifications = notifications,
                        onMarkAllRead = { viewModel.markAllNotificationsRead() },
                        onNotificationClick = { notif ->
                            viewModel.markNotificationRead(notif.id)
                            if (notif.sourceUserId.isNotEmpty()) {
                                viewModel.inspectUser(notif.sourceUserId)
                                currentTab = ScreenTab.PROFILE
                            }
                        },
                        onUserClick = { userId ->
                            viewModel.inspectUser(userId)
                            currentTab = ScreenTab.PROFILE
                        },
                        onFollowBack = { userId -> viewModel.toggleFollow(userId, false) },
                        onOpenDirectMessages = { currentTab = ScreenTab.DIRECT_MESSAGES }
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
                        onToggleDarkTheme = onToggleDarkTheme,
                        onToggleFollow = { id, status -> viewModel.toggleFollow(id, status) },
                        onUpdateProfile = { name, handle, bio, avatar ->
                            viewModel.updateProfile(name, handle, bio, avatar)
                        },
                        onVideoClick = {
                            currentTab = ScreenTab.FEED
                        },
                        onOpenAdmin = { currentTab = ScreenTab.ADMIN },
                        onSwitchAccountClick = { showAuthSheet = true },
                        onOpenSettings = { currentTab = ScreenTab.SETTINGS },
                        onOpenChat = { targetUserId ->
                            viewModel.openChatWith(targetUserId)
                            currentTab = ScreenTab.DIRECT_MESSAGES
                        }
                    )
                }

                ScreenTab.DIRECT_MESSAGES -> {
                    DirectMessagesScreen(
                        viewModel = viewModel,
                        activeChatUser = activeChatUser,
                        onBack = {
                            if (activeChatUserId != null) {
                                viewModel.closeChat()
                            } else {
                                currentTab = ScreenTab.NOTIFICATIONS
                            }
                        },
                        onOpenUserProfile = { userId ->
                            viewModel.inspectUser(userId)
                            currentTab = ScreenTab.PROFILE
                        }
                    )
                }

                ScreenTab.SETTINGS -> {
                    SettingsScreen(
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme,
                        onToggleDarkTheme = onToggleDarkTheme,
                        onBack = { currentTab = ScreenTab.PROFILE },
                        onSwitchAccount = { showAuthSheet = true }
                    )
                }

                ScreenTab.ADMIN -> {
                    AdminScreen(
                        videos = allVideos,
                        users = allUsers,
                        onBack = { currentTab = ScreenTab.PROFILE },
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
