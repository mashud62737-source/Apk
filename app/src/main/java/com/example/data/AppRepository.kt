package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.model.Comment
import com.example.model.DirectMessage
import com.example.model.HashtagItem
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.model.SoundItem
import com.example.model.User
import com.example.model.Video
import com.example.model.VideoCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID

class AppRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val userDao = database.userDao()
    private val videoDao = database.videoDao()
    private val commentDao = database.commentDao()
    private val directMessageDao = database.directMessageDao()
    private val notificationDao = database.notificationDao()

    private val _currentUserId = MutableStateFlow("user_me")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    val allVideos: Flow<List<Video>> = videoDao.getAllVideos()
    val followingVideos: Flow<List<Video>> = videoDao.getFollowingVideos()
    val likedVideos: Flow<List<Video>> = videoDao.getLikedVideos()
    val savedVideos: Flow<List<Video>> = videoDao.getSavedVideos()
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val notifications: Flow<List<NotificationItem>> = notificationDao.getAllNotifications()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
            loadCurrentUser(_currentUserId.value)
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val existingUsers = userDao.getAllUsers().firstOrNull()
        if (existingUsers.isNullOrEmpty()) {
            userDao.insertUsers(SampleData.initialUsers)
            if (SampleData.initialVideos.isNotEmpty()) {
                videoDao.insertVideos(SampleData.initialVideos)
            }
            if (SampleData.initialComments.isNotEmpty()) {
                commentDao.insertComments(SampleData.initialComments)
            }
            if (SampleData.initialNotifications.isNotEmpty()) {
                notificationDao.insertNotifications(SampleData.initialNotifications)
            }
        }
    }

    private suspend fun loadCurrentUser(userId: String) {
        val user = userDao.getUserByIdOnce(userId)
        _currentUser.value = user
    }

    fun switchUser(userId: String) {
        _currentUserId.value = userId
        CoroutineScope(Dispatchers.IO).launch {
            loadCurrentUser(userId)
        }
    }

    fun getUser(userId: String): Flow<User?> = userDao.getUserById(userId)

    fun getVideosByUser(userId: String): Flow<List<Video>> = videoDao.getVideosByUser(userId)

    fun searchVideos(query: String): Flow<List<Video>> = videoDao.searchVideos(query)

    fun searchUsers(query: String): Flow<List<User>> = userDao.searchUsers(query)

    fun getCommentsForVideo(videoId: String): Flow<List<Comment>> = commentDao.getCommentsForVideo(videoId)

    fun getMessagesBetween(otherUserId: String): Flow<List<DirectMessage>> {
        return directMessageDao.getMessagesBetween(_currentUserId.value, otherUserId)
    }

    fun getAllMessagesForCurrentUser(): Flow<List<DirectMessage>> {
        return directMessageDao.getAllMessagesForUser(_currentUserId.value)
    }

    suspend fun incrementViewCount(videoId: String) {
        videoDao.incrementViews(videoId)
    }

    suspend fun toggleVideoLike(video: Video) {
        val newLikedState = !video.isLikedByMe
        val delta = if (newLikedState) 1 else -1
        videoDao.updateLike(video.id, newLikedState, delta)

        // If liked, notify creator
        if (newLikedState && video.userId != _currentUserId.value) {
            val user = _currentUser.value ?: return
            val notif = NotificationItem(
                id = "notif_${UUID.randomUUID()}",
                type = NotificationType.LIKE,
                sourceUserId = user.id,
                sourceUserName = user.displayName,
                sourceUserAvatar = user.avatarUrl,
                videoId = video.id,
                videoThumbnail = video.thumbnailUrl,
                message = "liked your video",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            notificationDao.insertNotification(notif)
        }
    }

    suspend fun toggleVideoSave(video: Video) {
        val newSavedState = !video.isSavedByMe
        videoDao.updateSave(video.id, newSavedState)
    }

    suspend fun toggleFollowCreator(creatorId: String, currentFollowStatus: Boolean) {
        val newStatus = !currentFollowStatus
        val creator = userDao.getUserByIdOnce(creatorId) ?: return
        val delta = if (newStatus) 1 else -1
        val updatedCreator = creator.copy(
            isFollowedByMe = newStatus,
            followerCount = (creator.followerCount + delta).coerceAtLeast(0)
        )
        userDao.updateUser(updatedCreator)
        videoDao.updateCreatorFollowStatus(creatorId, newStatus)

        // Update current user following count
        val me = _currentUser.value
        if (me != null) {
            val updatedMe = me.copy(followingCount = (me.followingCount + delta).coerceAtLeast(0))
            userDao.updateUser(updatedMe)
            _currentUser.value = updatedMe
        }

        // Notification if followed
        if (newStatus && creatorId != _currentUserId.value) {
            val meUser = _currentUser.value ?: return
            val notif = NotificationItem(
                id = "notif_${UUID.randomUUID()}",
                type = NotificationType.FOLLOW,
                sourceUserId = meUser.id,
                sourceUserName = meUser.displayName,
                sourceUserAvatar = meUser.avatarUrl,
                message = if (creator.isFollowedByMe) "is now friends with you 👥" else "started following you",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            notificationDao.insertNotification(notif)
        }
    }

    suspend fun addComment(videoId: String, text: String): Boolean {
        val user = _currentUser.value ?: return false
        val comment = Comment(
            id = "c_${UUID.randomUUID()}",
            videoId = videoId,
            userId = user.id,
            userName = user.displayName,
            userHandle = user.username,
            userAvatarUrl = user.avatarUrl,
            text = text.trim(),
            isCreatorVerified = user.isVerified,
            likesCount = 0,
            isLikedByMe = false,
            createdAt = System.currentTimeMillis()
        )
        commentDao.insertComment(comment)
        videoDao.incrementCommentCount(videoId)

        val video = videoDao.getVideoById(videoId)
        if (video != null && video.userId != user.id) {
            val notif = NotificationItem(
                id = "notif_${UUID.randomUUID()}",
                type = NotificationType.COMMENT,
                sourceUserId = user.id,
                sourceUserName = user.displayName,
                sourceUserAvatar = user.avatarUrl,
                videoId = video.id,
                videoThumbnail = video.thumbnailUrl,
                message = "commented: \"${text.take(30)}${if (text.length > 30) "..." else ""}\"",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            notificationDao.insertNotification(notif)
        }
        return true
    }

    suspend fun toggleCommentLike(comment: Comment) {
        val newLiked = !comment.isLikedByMe
        val delta = if (newLiked) 1 else -1
        commentDao.toggleCommentLike(comment.id, newLiked, delta)
    }

    suspend fun shareVideo(videoId: String) {
        videoDao.incrementShareCount(videoId)
    }

    suspend fun toggleReportVideo(videoId: String, isReported: Boolean) {
        videoDao.toggleReport(videoId, isReported)
    }

    suspend fun uploadVideo(
        videoUrl: String,
        thumbnailUrl: String,
        caption: String,
        category: String = "Trending",
        hashtags: List<String>,
        musicTitle: String,
        musicAuthor: String
    ): Video {
        val user = _currentUser.value ?: SampleData.initialUsers[0]
        val newVideo = Video(
            id = "v_${UUID.randomUUID()}",
            userId = user.id,
            userHandle = user.username,
            userName = user.displayName,
            userAvatarUrl = user.avatarUrl,
            videoUrl = videoUrl,
            thumbnailUrl = thumbnailUrl,
            caption = caption,
            category = category,
            hashtags = hashtags,
            musicTitle = musicTitle,
            musicAuthor = musicAuthor,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            viewsCount = 1,
            isLikedByMe = false,
            isSavedByMe = false,
            isFollowedCreator = false,
            createdAt = System.currentTimeMillis(),
            isPinned = false,
            isReported = false
        )
        videoDao.insertVideo(newVideo)
        return newVideo
    }

    suspend fun deleteVideo(videoId: String) {
        videoDao.deleteVideo(videoId)
    }

    suspend fun togglePinVideo(videoId: String, isPinned: Boolean) {
        videoDao.togglePin(videoId, isPinned)
    }

    suspend fun updateProfile(displayName: String, username: String, bio: String, avatarUrl: String) {
        val user = _currentUser.value ?: return
        val updated = user.copy(
            displayName = displayName.trim(),
            username = username.trim().removePrefix("@"),
            bio = bio.trim(),
            avatarUrl = avatarUrl.trim()
        )
        userDao.updateUser(updated)
        _currentUser.value = updated
    }

    suspend fun updateAvatar(newAvatarUrl: String) {
        val user = _currentUser.value ?: return
        val updated = user.copy(avatarUrl = newAvatarUrl)
        userDao.updateUser(updated)
        _currentUser.value = updated
    }

    /**
     * NID Verification for Blue Tick ✅
     */
    suspend fun submitNidVerification(
        realName: String,
        nidNumber: String,
        category: String,
        frontUri: String,
        backUri: String,
        autoApprove: Boolean = true
    ) {
        val user = _currentUser.value ?: return
        val isApproved = autoApprove
        val updated = user.copy(
            realName = realName,
            nidNumber = nidNumber,
            verifiedCategory = category,
            nidFrontUri = frontUri,
            nidBackUri = backUri,
            nidStatus = if (isApproved) "VERIFIED" else "PENDING",
            isVerified = isApproved
        )
        userDao.updateUser(updated)
        _currentUser.value = updated

        // System notification
        val notif = NotificationItem(
            id = "notif_verif_${UUID.randomUUID()}",
            type = NotificationType.VERIFICATION,
            sourceUserId = "system_toktok",
            sourceUserName = "TokTok Verification Center",
            sourceUserAvatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&auto=format&fit=crop&q=80",
            message = if (isApproved)
                "🎉 Congratulations! Your National ID verification was approved. You received the Blue Tick badge ✅!"
            else
                "📄 Your National ID verification request is currently under review.",
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        notificationDao.insertNotification(notif)
    }

    suspend fun setVerifiedBadge(userId: String, isVerified: Boolean) {
        val user = userDao.getUserByIdOnce(userId) ?: return
        val updated = user.copy(isVerified = isVerified, nidStatus = if (isVerified) "VERIFIED" else "UNVERIFIED")
        userDao.updateUser(updated)
        if (userId == _currentUserId.value) {
            _currentUser.value = updated
        }
    }

    suspend fun updateTikTokPrivacySettings(
        isPrivate: Boolean,
        allowDMs: String,
        allowDownloads: Boolean,
        allowDuet: Boolean,
        allowStitch: Boolean,
        filterComments: Boolean
    ) {
        val user = _currentUser.value ?: return
        val updated = user.copy(
            isPrivateAccount = isPrivate,
            allowDirectMessages = allowDMs,
            allowDownloads = allowDownloads,
            allowDuet = allowDuet,
            allowStitch = allowStitch,
            filterComments = filterComments
        )
        userDao.updateUser(updated)
        _currentUser.value = updated
    }

    /**
     * Direct Messages Chat
     */
    suspend fun sendDirectMessage(receiverId: String, text: String, mediaUrl: String? = null): DirectMessage {
        val sender = _currentUser.value ?: SampleData.initialUsers[0]
        val message = DirectMessage(
            id = "msg_${UUID.randomUUID()}",
            senderId = sender.id,
            receiverId = receiverId,
            text = text.trim(),
            mediaUrl = mediaUrl,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        directMessageDao.insertMessage(message)

        // Notification for receiver
        val notif = NotificationItem(
            id = "notif_msg_${UUID.randomUUID()}",
            type = NotificationType.MESSAGE,
            sourceUserId = sender.id,
            sourceUserName = sender.displayName,
            sourceUserAvatar = sender.avatarUrl,
            message = "sent you a direct message: \"${text.take(25)}\"",
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        notificationDao.insertNotification(notif)

        return message
    }

    suspend fun markMessagesAsRead(otherUserId: String) {
        directMessageDao.markMessagesAsRead(otherUserId, _currentUserId.value)
    }

    suspend fun registerUser(username: String, displayName: String, email: String, bio: String): User {
        val newUser = User(
            id = "user_${UUID.randomUUID()}",
            username = username.trim().removePrefix("@"),
            displayName = displayName.trim(),
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
            bio = bio.trim(),
            followerCount = 0,
            followingCount = 0,
            likesCount = 0,
            isFollowedByMe = false,
            isVerified = false,
            isAdmin = false,
            email = email.trim(),
            joinedDate = "August 2026"
        )
        userDao.insertUser(newUser)
        switchUser(newUser.id)
        return newUser
    }

    suspend fun markNotificationAsRead(id: String) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }

    fun getTrendingHashtags(): List<HashtagItem> = SampleData.trendingHashtags
    fun getPopularSounds(): List<SoundItem> = SampleData.popularSounds
    fun getVideoCategories(): List<VideoCategory> = SampleData.videoCategories
}
