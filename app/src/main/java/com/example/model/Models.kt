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
    val isVerified: Boolean = false,
    val isAdmin: Boolean = false,
    val email: String = "",
    val joinedDate: String = "August 2026"
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
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
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
