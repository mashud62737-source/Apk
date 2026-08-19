package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.model.Comment
import com.example.model.HashtagItem
import com.example.model.NotificationItem
import com.example.model.SoundItem
import com.example.model.User
import com.example.model.Video
import com.example.ui.components.ScreenTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    private val _currentTab = MutableStateFlow(ScreenTab.FEED)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _inspectedUserId = MutableStateFlow<String?>(null)
    val inspectedUserId: StateFlow<String?> = _inspectedUserId.asStateFlow()

    private val _initialSearchQuery = MutableStateFlow("")
    val initialSearchQuery: StateFlow<String> = _initialSearchQuery.asStateFlow()

    val currentUser: StateFlow<User?> = repository.currentUser
    val allVideos: StateFlow<List<Video>> = repository.allVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val followingVideos: StateFlow<List<Video>> = repository.followingVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val likedVideos: StateFlow<List<Video>> = repository.likedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedVideos: StateFlow<List<Video>> = repository.savedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<User>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.notifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val trendingHashtags: List<HashtagItem> = repository.getTrendingHashtags()
    val popularSounds: List<SoundItem> = repository.getPopularSounds()

    fun selectTab(tab: ScreenTab) {
        if (tab != ScreenTab.PROFILE) {
            _inspectedUserId.value = null
        }
        _currentTab.value = tab
    }

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
    }

    fun viewUserProfile(userId: String) {
        _inspectedUserId.value = userId
        _currentTab.value = ScreenTab.PROFILE
    }

    fun searchHashtag(tag: String) {
        _initialSearchQuery.value = if (tag.startsWith("#")) tag else "#$tag"
        _currentTab.value = ScreenTab.SEARCH
    }

    fun toggleLike(video: Video) {
        viewModelScope.launch {
            repository.toggleVideoLike(video)
        }
    }

    fun toggleSave(video: Video) {
        viewModelScope.launch {
            repository.toggleVideoSave(video)
        }
    }

    fun toggleFollow(creatorId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFollowCreator(creatorId, currentStatus)
        }
    }

    fun sendComment(videoId: String, text: String) {
        viewModelScope.launch {
            repository.addComment(videoId, text)
        }
    }

    fun toggleCommentLike(comment: Comment) {
        viewModelScope.launch {
            repository.toggleCommentLike(comment)
        }
    }

    fun shareVideo(video: Video) {
        viewModelScope.launch {
            repository.shareVideo(video.id)
        }
    }

    fun reportVideo(video: Video) {
        viewModelScope.launch {
            repository.toggleReportVideo(video.id, true)
        }
    }

    fun incrementViews(videoId: String) {
        viewModelScope.launch {
            repository.incrementViewCount(videoId)
        }
    }

    fun uploadVideo(
        videoUrl: String,
        thumbnailUrl: String,
        caption: String,
        hashtags: List<String>,
        musicTitle: String,
        musicAuthor: String
    ) {
        viewModelScope.launch {
            repository.uploadVideo(videoUrl, thumbnailUrl, caption, hashtags, musicTitle, musicAuthor)
        }
    }

    fun deleteVideo(videoId: String) {
        viewModelScope.launch {
            repository.deleteVideo(videoId)
        }
    }

    fun togglePinVideo(videoId: String, isPinned: Boolean) {
        viewModelScope.launch {
            repository.togglePinVideo(videoId, isPinned)
        }
    }

    fun updateProfile(displayName: String, username: String, bio: String, avatarUrl: String) {
        viewModelScope.launch {
            repository.updateProfile(displayName, username, bio, avatarUrl)
        }
    }

    fun switchUser(userId: String) {
        repository.switchUser(userId)
        _inspectedUserId.value = null
    }

    fun registerUser(username: String, displayName: String, email: String, bio: String) {
        viewModelScope.launch {
            repository.registerUser(username, displayName, email, bio)
        }
    }

    fun markNotificationRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }
}
