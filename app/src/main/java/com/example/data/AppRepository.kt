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

    private val _adConfig = MutableStateFlow(com.example.model.PlatformAdConfig())
    val adConfig: StateFlow<com.example.model.PlatformAdConfig> = _adConfig.asStateFlow()

    val reportedVideos: Flow<List<Video>> = videoDao.getReportedVideos()
    val pendingNidUsers: Flow<List<User>> = userDao.getPendingNidUsers()

    fun getTrendingHashtags(): List<HashtagItem> = SampleData.trendingHashtags
    fun getPopularSounds(): List<SoundItem> = SampleData.popularSounds
    fun getVideoCategories(): List<VideoCategory> = SampleData.videoCategories

    // === MASTER CREATOR / ADMIN OPERATIONS ===

    suspend fun adminDeleteUser(userId: String) {
        // Delete user's videos and user profile
        videoDao.deleteVideosByUser(userId)
        userDao.deleteUser(userId)
        if (_currentUserId.value == userId) {
            _currentUserId.value = "user_me"
            loadCurrentUser("user_me")
        }
    }

    suspend fun adminUpdateUser(user: User) {
        userDao.updateUser(user)
        if (user.id == _currentUserId.value) {
            _currentUser.value = user
        }
    }

    suspend fun adminReviewNid(userId: String, isApproved: Boolean) {
        val user = userDao.getUserByIdOnce(userId) ?: return
        val updated = user.copy(
            isVerified = isApproved,
            nidStatus = if (isApproved) "VERIFIED" else "REJECTED"
        )
        userDao.updateUser(updated)
        if (userId == _currentUserId.value) {
            _currentUser.value = updated
        }

        // Notify user of decision
        val notif = NotificationItem(
            id = "notif_review_${UUID.randomUUID()}",
            type = NotificationType.VERIFICATION,
            sourceUserId = "system_bdtok",
            sourceUserName = "BDTOK Master Creator Team",
            sourceUserAvatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&auto=format&fit=crop&q=80",
            message = if (isApproved)
                "🎉 Your Blue Tick verification request was approved by the Master Admin! You are now Verified ✅"
            else
                "❌ Your verification request was rejected after review by the Admin Team.",
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        notificationDao.insertNotification(notif)
    }

    suspend fun adminToggleUserVerified(userId: String, isVerified: Boolean) {
        userDao.setUserVerified(userId, isVerified, if (isVerified) "VERIFIED" else "UNVERIFIED")
        if (userId == _currentUserId.value) {
            val user = userDao.getUserByIdOnce(userId)
            _currentUser.value = user
        }
    }

    suspend fun adminToggleUserAdmin(userId: String, isAdmin: Boolean) {
        userDao.setUserAdmin(userId, isAdmin)
        if (userId == _currentUserId.value) {
            val user = userDao.getUserByIdOnce(userId)
            _currentUser.value = user
        }
    }

    suspend fun adminToggleUserBan(userId: String, isBanned: Boolean) {
        userDao.setUserBanned(userId, isBanned)
        if (userId == _currentUserId.value) {
            val user = userDao.getUserByIdOnce(userId)
            _currentUser.value = user
        }
    }

    suspend fun adminBoostFollowers(userId: String, boostCount: Int) {
        userDao.boostFollowers(userId, boostCount)
        if (userId == _currentUserId.value) {
            val user = userDao.getUserByIdOnce(userId)
            _currentUser.value = user
        }
    }

    suspend fun adminSetVideoPrivacy(videoId: String, isPrivate: Boolean) {
        videoDao.updateVideoPrivacy(videoId, isPrivate)
    }

    suspend fun adminClearVideoReport(videoId: String) {
        videoDao.clearVideoReport(videoId)
    }

    suspend fun adminUpdateVideoMetrics(videoId: String, views: Int, likes: Int) {
        videoDao.updateVideoMetrics(videoId, views, likes)
    }

    fun adminUpdateAdConfig(config: com.example.model.PlatformAdConfig) {
        _adConfig.value = config
    }

    suspend fun adminBroadcastSystemAnnouncement(title: String, message: String) {
        val notif = NotificationItem(
            id = "broadcast_${UUID.randomUUID()}",
            type = NotificationType.SYSTEM,
            sourceUserId = "creator_mashud",
            sourceUserName = "BDTOK Master Broadcast 📢",
            sourceUserAvatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&auto=format&fit=crop&q=80",
            message = "$title: $message",
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        notificationDao.insertNotification(notif)
    }

    // === DATABASE DOWNLOAD, EXPORT & BACKUP/RESTORE ENGINE ===

    data class DatabaseStats(
        val usersCount: Int = 0,
        val videosCount: Int = 0,
        val commentsCount: Int = 0,
        val notificationsCount: Int = 0,
        val messagesCount: Int = 0,
        val totalEntities: Int = 0,
        val lastExportTimestamp: Long = System.currentTimeMillis()
    )

    suspend fun getDatabaseStats(): DatabaseStats {
        val uCount = userDao.getUserCount()
        val vCount = videoDao.getVideoCount()
        val cCount = commentDao.getCommentCount()
        val nCount = notificationDao.getNotificationCount()
        val mCount = directMessageDao.getMessageCount()
        val total = uCount + vCount + cCount + nCount + mCount
        return DatabaseStats(
            usersCount = uCount,
            videosCount = vCount,
            commentsCount = cCount,
            notificationsCount = nCount,
            messagesCount = mCount,
            totalEntities = total
        )
    }

    suspend fun exportCompleteDatabaseToJson(): String {
        val root = org.json.JSONObject()
        root.put("app", "BDTOK Android App")
        root.put("version", "34.2.0")
        root.put("timestamp", System.currentTimeMillis())
        root.put("exportDate", java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US).format(java.util.Date()))
        root.put("masterCreator", "Mashud • mashud62737@gmail.com")

        // 1. Users
        val usersList = userDao.getAllUsers().firstOrNull() ?: emptyList()
        val usersArr = org.json.JSONArray()
        for (u in usersList) {
            val uObj = org.json.JSONObject()
            uObj.put("id", u.id)
            uObj.put("username", u.username)
            uObj.put("displayName", u.displayName)
            uObj.put("email", u.email)
            uObj.put("avatarUrl", u.avatarUrl)
            uObj.put("bio", u.bio)
            uObj.put("followerCount", u.followerCount)
            uObj.put("followingCount", u.followingCount)
            uObj.put("likesCount", u.likesCount)
            uObj.put("isVerified", u.isVerified)
            uObj.put("verifiedCategory", u.verifiedCategory)
            uObj.put("isAdmin", u.isAdmin)
            uObj.put("nidStatus", u.nidStatus)
            uObj.put("nidNumber", u.nidNumber ?: "")
            uObj.put("isPrivateAccount", u.isPrivateAccount)
            uObj.put("isBanned", u.isBanned)
            usersArr.put(uObj)
        }
        root.put("users", usersArr)

        // 2. Videos
        val videosList = videoDao.getAllVideos().firstOrNull() ?: emptyList()
        val videosArr = org.json.JSONArray()
        for (v in videosList) {
            val vObj = org.json.JSONObject()
            vObj.put("id", v.id)
            vObj.put("userId", v.userId)
            vObj.put("userName", v.userName)
            vObj.put("userHandle", v.userHandle)
            vObj.put("userAvatarUrl", v.userAvatarUrl)
            vObj.put("videoUrl", v.videoUrl)
            vObj.put("thumbnailUrl", v.thumbnailUrl)
            vObj.put("caption", v.caption)
            vObj.put("musicTitle", v.musicTitle)
            vObj.put("musicAuthor", v.musicAuthor)
            vObj.put("likesCount", v.likesCount)
            vObj.put("commentsCount", v.commentsCount)
            vObj.put("sharesCount", v.sharesCount)
            vObj.put("viewsCount", v.viewsCount)
            vObj.put("category", v.category)
            vObj.put("isPrivate", v.isPrivate)
            vObj.put("isPinned", v.isPinned)
            vObj.put("isReported", v.isReported)
            videosArr.put(vObj)
        }
        root.put("videos", videosArr)

        // 3. Comments
        val commentsList = commentDao.getAllComments().firstOrNull() ?: emptyList()
        val commentsArr = org.json.JSONArray()
        for (c in commentsList) {
            val cObj = org.json.JSONObject()
            cObj.put("id", c.id)
            cObj.put("videoId", c.videoId)
            cObj.put("userId", c.userId)
            cObj.put("userName", c.userName)
            cObj.put("userAvatarUrl", c.userAvatarUrl)
            cObj.put("text", c.text)
            cObj.put("likesCount", c.likesCount)
            cObj.put("createdAt", c.createdAt)
            commentsArr.put(cObj)
        }
        root.put("comments", commentsArr)

        // 4. Notifications
        val notifsList = notificationDao.getAllNotifications().firstOrNull() ?: emptyList()
        val notifsArr = org.json.JSONArray()
        for (n in notifsList) {
            val nObj = org.json.JSONObject()
            nObj.put("id", n.id)
            nObj.put("type", n.type.name)
            nObj.put("sourceUserId", n.sourceUserId)
            nObj.put("sourceUserName", n.sourceUserName)
            nObj.put("message", n.message)
            nObj.put("timestamp", n.timestamp)
            notifsArr.put(nObj)
        }
        root.put("notifications", notifsArr)

        // 5. Ad Config
        val currentAd = _adConfig.value
        val adObj = org.json.JSONObject()
        adObj.put("isEnabled", currentAd.isEnabled)
        adObj.put("sponsorName", currentAd.sponsorName)
        adObj.put("headline", currentAd.headline)
        adObj.put("ctaText", currentAd.ctaText)
        adObj.put("targetUrl", currentAd.targetUrl)
        adObj.put("frequency", currentAd.frequency)
        root.put("platformAdConfig", adObj)

        return root.toString(2)
    }

    suspend fun exportUserPersonalDataToJson(userId: String): String {
        val root = org.json.JSONObject()
        val user = userDao.getUserByIdOnce(userId)
        val userVideos = videoDao.getVideosByUser(userId).firstOrNull() ?: emptyList()
        val userMessages = directMessageDao.getAllMessagesForUser(userId).firstOrNull() ?: emptyList()

        root.put("appName", "BDTOK Social Video")
        root.put("requestDate", java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US).format(java.util.Date()))
        root.put("userStatus", if (user?.isVerified == true) "Verified Creator ✅" else "Standard User")

        if (user != null) {
            val profile = org.json.JSONObject()
            profile.put("id", user.id)
            profile.put("username", user.username)
            profile.put("displayName", user.displayName)
            profile.put("email", user.email)
            profile.put("bio", user.bio)
            profile.put("followers", user.followerCount)
            profile.put("following", user.followingCount)
            profile.put("totalLikes", user.likesCount)
            profile.put("isPrivate", user.isPrivateAccount)
            root.put("profile", profile)
        }

        val videosArr = org.json.JSONArray()
        for (v in userVideos) {
            val vObj = org.json.JSONObject()
            vObj.put("id", v.id)
            vObj.put("caption", v.caption)
            vObj.put("musicTitle", v.musicTitle)
            vObj.put("views", v.viewsCount)
            vObj.put("likes", v.likesCount)
            vObj.put("comments", v.commentsCount)
            vObj.put("videoUrl", v.videoUrl)
            videosArr.put(vObj)
        }
        root.put("myVideos", videosArr)

        val msgsArr = org.json.JSONArray()
        for (m in userMessages) {
            val mObj = org.json.JSONObject()
            mObj.put("id", m.id)
            mObj.put("senderId", m.senderId)
            mObj.put("receiverId", m.receiverId)
            mObj.put("text", m.text)
            mObj.put("timestamp", m.timestamp)
            msgsArr.put(mObj)
        }
        root.put("directMessages", msgsArr)

        return root.toString(2)
    }

    suspend fun importDatabaseFromJson(jsonString: String): Boolean {
        return try {
            val root = org.json.JSONObject(jsonString)

            if (root.has("users")) {
                val usersArr = root.getJSONArray("users")
                val parsedUsers = mutableListOf<User>()
                for (i in 0 until usersArr.length()) {
                    val u = usersArr.getJSONObject(i)
                    parsedUsers.add(
                        User(
                            id = u.optString("id", UUID.randomUUID().toString()),
                            username = u.optString("username", "user_$i"),
                            displayName = u.optString("displayName", "User $i"),
                            email = u.optString("email", ""),
                            avatarUrl = u.optString("avatarUrl", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200"),
                            bio = u.optString("bio", ""),
                            followerCount = u.optInt("followerCount", 0),
                            followingCount = u.optInt("followingCount", 0),
                            likesCount = u.optInt("likesCount", 0),
                            isVerified = u.optBoolean("isVerified", false),
                            verifiedCategory = u.optString("verifiedCategory", "Creator"),
                            isAdmin = u.optBoolean("isAdmin", false),
                            nidStatus = u.optString("nidStatus", "UNVERIFIED"),
                            nidNumber = u.optString("nidNumber", null),
                            isPrivateAccount = u.optBoolean("isPrivateAccount", false),
                            isBanned = u.optBoolean("isBanned", false)
                        )
                    )
                }
                if (parsedUsers.isNotEmpty()) {
                    userDao.deleteAllUsers()
                    userDao.insertUsers(parsedUsers)
                }
            }

            if (root.has("videos")) {
                val videosArr = root.getJSONArray("videos")
                val parsedVideos = mutableListOf<Video>()
                for (i in 0 until videosArr.length()) {
                    val v = videosArr.getJSONObject(i)
                    parsedVideos.add(
                        Video(
                            id = v.optString("id", UUID.randomUUID().toString()),
                            userId = v.optString("userId", "user_me"),
                            userName = v.optString("userName", "Creator"),
                            userHandle = v.optString("userHandle", "creator"),
                            userAvatarUrl = v.optString("userAvatarUrl", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200"),
                            videoUrl = v.optString("videoUrl", "android.resource://com.example/raw/sample_video"),
                            thumbnailUrl = v.optString("thumbnailUrl", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800"),
                            caption = v.optString("caption", "BDTOK clip"),
                            musicTitle = v.optString("musicTitle", "Original Sound - BDTOK"),
                            musicAuthor = v.optString("musicAuthor", "BDTOK Sound"),
                            likesCount = v.optInt("likesCount", 0),
                            commentsCount = v.optInt("commentsCount", 0),
                            sharesCount = v.optInt("sharesCount", 0),
                            viewsCount = v.optInt("viewsCount", 0),
                            category = v.optString("category", "Trending"),
                            isPrivate = v.optBoolean("isPrivate", false),
                            isPinned = v.optBoolean("isPinned", false),
                            isReported = v.optBoolean("isReported", false)
                        )
                    )
                }
                if (parsedVideos.isNotEmpty()) {
                    videoDao.deleteAllVideos()
                    videoDao.insertVideos(parsedVideos)
                }
            }

            loadCurrentUser(_currentUserId.value)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun resetDatabaseToDefaults() {
        userDao.deleteAllUsers()
        videoDao.deleteAllVideos()
        commentDao.deleteAllComments()
        notificationDao.deleteAllNotifications()
        directMessageDao.deleteAllMessages()

        userDao.insertUsers(SampleData.initialUsers)
        videoDao.insertVideos(SampleData.initialVideos)
        commentDao.insertComments(SampleData.initialComments)
        notificationDao.insertNotifications(SampleData.initialNotifications)

        _currentUserId.value = "user_me"
        loadCurrentUser("user_me")
    }
}
