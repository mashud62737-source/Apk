package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String,
    val bio: String,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val likesCount: Int = 0,
    val isFollowedByMe: Boolean = false,
    val followsMe: Boolean = false, // TikTok Mutual Follow-Back / "Friends" state
    val isVerified: Boolean = false, // Blue Tick ✅
    val verifiedCategory: String = "Content Creator",
    val nidStatus: String = "UNVERIFIED", // UNVERIFIED, PENDING, VERIFIED
    val nidFrontUri: String? = null,
    val nidBackUri: String? = null,
    val nidNumber: String? = null,
    val realName: String? = null,
    val isAdmin: Boolean = false,
    val email: String = "",
    val joinedDate: String = "August 2026",
    // TikTok Privacy & Safety Settings
    val isPrivateAccount: Boolean = false,
    val allowDirectMessages: String = "Everyone", // Everyone, Friends, No one
    val allowDownloads: Boolean = true,
    val allowDuet: Boolean = true,
    val allowStitch: Boolean = true,
    val filterComments: Boolean = false
)

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey val id: String,
    val userId: String,
    val userHandle: String,
    val userName: String,
    val userAvatarUrl: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val caption: String,
    val category: String = "Trending",
    val hashtags: List<String> = emptyList(),
    val musicTitle: String = "Original Sound - @$userHandle",
    val musicAuthor: String = userName,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val viewsCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val isSavedByMe: Boolean = false,
    val isFollowedCreator: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isReported: Boolean = false
)

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey val id: String,
    val videoId: String,
    val userId: String,
    val userName: String,
    val userHandle: String,
    val userAvatarUrl: String,
    val text: String,
    val isCreatorVerified: Boolean = false,
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "direct_messages")
data class DirectMessage(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val mediaUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val type: NotificationType,
    val sourceUserId: String,
    val sourceUserName: String,
    val sourceUserAvatar: String,
    val videoId: String? = null,
    val videoThumbnail: String? = null,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION,
    MESSAGE,
    VERIFICATION,
    SYSTEM
}

data class HashtagItem(
    val tag: String,
    val videoCount: Int,
    val viewCount: String,
    val isTrending: Boolean = true
)

data class SoundItem(
    val id: String,
    val title: String,
    val author: String,
    val durationSec: Int,
    val usageCount: String,
    val coverUrl: String
)

data class VideoCategory(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val description: String
)
