package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.MediaStorageHelper
import com.example.model.Comment
import com.example.model.DirectMessage
import com.example.model.HashtagItem
import com.example.model.NotificationItem
import com.example.model.SoundItem
import com.example.model.User
import com.example.model.Video
import com.example.model.VideoCategory
import com.example.pipeline.MediaType
import com.example.pipeline.MediaUploadPipeline
import com.example.pipeline.PipelineProgressState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    val currentUser: StateFlow<User?> = repository.currentUser
    val currentUserId: StateFlow<String> = repository.currentUserId

    val allVideos: Flow<List<Video>> = repository.allVideos
    val followingVideos: Flow<List<Video>> = repository.followingVideos
    val likedVideos: Flow<List<Video>> = repository.likedVideos
    val savedVideos: Flow<List<Video>> = repository.savedVideos
    val notifications: Flow<List<NotificationItem>> = repository.notifications

    // Selected user profile for inspection
    private val _inspectedUserId = MutableStateFlow<String?>(null)
    val inspectedUserId: StateFlow<String?> = _inspectedUserId.asStateFlow()

    val inspectedUser: Flow<User?> = _inspectedUserId.combine(repository.allUsers) { id, users ->
        if (id == null) null else users.find { it.id == id }
    }

    // Direct Messaging Active Chat Target
    private val _activeChatUserId = MutableStateFlow<String?>(null)
    val activeChatUserId: StateFlow<String?> = _activeChatUserId.asStateFlow()

    // Algorithm & telemetry inspection state
    private val _algoInspectedVideo = MutableStateFlow<Video?>(null)
    val algoInspectedVideo: StateFlow<Video?> = _algoInspectedVideo.asStateFlow()

    // Upload Pipeline progress state
    private val _uploadProgress = MutableStateFlow<PipelineProgressState?>(null)
    val uploadProgress: StateFlow<PipelineProgressState?> = _uploadProgress.asStateFlow()

    val trendingHashtags: List<HashtagItem> = repository.getTrendingHashtags()
    val popularSounds: List<SoundItem> = repository.getPopularSounds()
    val videoCategories: List<VideoCategory> = repository.getVideoCategories()

    fun inspectUser(userId: String) {
        _inspectedUserId.value = userId
    }

    fun clearInspectedUser() {
        _inspectedUserId.value = null
    }

    fun openChatWith(userId: String) {
        _activeChatUserId.value = userId
        viewModelScope.launch {
            repository.markMessagesAsRead(userId)
        }
    }

    fun closeChat() {
        _activeChatUserId.value = null
    }

    fun getMessagesForUser(otherUserId: String): Flow<List<DirectMessage>> {
        return repository.getMessagesBetween(otherUserId)
    }

    fun sendMessage(receiverId: String, text: String, mediaUrl: String? = null) {
        viewModelScope.launch {
            repository.sendDirectMessage(receiverId, text, mediaUrl)
        }
    }

    fun inspectAlgoForVideo(video: Video) {
        _algoInspectedVideo.value = video
    }

    fun clearAlgoInspection() {
        _algoInspectedVideo.value = null
    }

    fun recordImpression(video: Video) {
        viewModelScope.launch {
            repository.incrementViewCount(video.id)
        }
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

    fun addComment(videoId: String, text: String) {
        viewModelScope.launch {
            repository.addComment(videoId, text)
        }
    }

    fun toggleCommentLike(comment: Comment) {
        viewModelScope.launch {
            repository.toggleCommentLike(comment)
        }
    }

    fun shareVideo(videoId: String) {
        viewModelScope.launch {
            repository.shareVideo(videoId)
        }
    }

    fun getVideosForUser(userId: String): Flow<List<Video>> {
        return repository.getVideosByUser(userId)
    }

    fun getCommentsForVideo(videoId: String): Flow<List<Comment>> {
        return repository.getCommentsForVideo(videoId)
    }

    fun searchVideos(query: String): Flow<List<Video>> {
        return repository.searchVideos(query)
    }

    fun searchUsers(query: String): Flow<List<User>> {
        return repository.searchUsers(query)
    }

    fun uploadVideo(
        videoUrl: String,
        thumbnailUrl: String,
        caption: String,
        category: String = "Trending",
        hashtags: List<String>,
        musicTitle: String,
        musicAuthor: String
    ) {
        viewModelScope.launch {
            repository.uploadVideo(videoUrl, thumbnailUrl, caption, category, hashtags, musicTitle, musicAuthor)
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

    fun updateAvatar(avatarUrl: String) {
        viewModelScope.launch {
            repository.updateAvatar(avatarUrl)
        }
    }

    fun submitNidVerification(
        realName: String,
        nidNumber: String,
        category: String,
        frontUri: String,
        backUri: String,
        autoApprove: Boolean = true
    ) {
        viewModelScope.launch {
            repository.submitNidVerification(realName, nidNumber, category, frontUri, backUri, autoApprove)
        }
    }

    fun setVerifiedBadge(userId: String, isVerified: Boolean) {
        viewModelScope.launch {
            repository.setVerifiedBadge(userId, isVerified)
        }
    }

    fun updateTikTokPrivacySettings(
        isPrivate: Boolean,
        allowDMs: String,
        allowDownloads: Boolean,
        allowDuet: Boolean,
        allowStitch: Boolean,
        filterComments: Boolean
    ) {
        viewModelScope.launch {
            repository.updateTikTokPrivacySettings(isPrivate, allowDMs, allowDownloads, allowDuet, allowStitch, filterComments)
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

class MainViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
