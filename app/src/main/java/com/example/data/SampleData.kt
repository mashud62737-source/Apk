package com.example.data

import com.example.model.Comment
import com.example.model.HashtagItem
import com.example.model.NotificationItem
import com.example.model.SoundItem
import com.example.model.User
import com.example.model.Video

object SampleData {
    // Single clean default profile for the user, 0 stats, no fake demo creators
    val initialUsers = listOf(
        User(
            id = "user_me",
            username = "my_channel",
            displayName = "My Channel",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
            bio = "Welcome to my TokTok profile ✨ Tap edit to customize!",
            followerCount = 0,
            followingCount = 0,
            likesCount = 0,
            isFollowedByMe = false,
            isVerified = false,
            isAdmin = true,
            email = "user@toktok.app"
        )
    )

    // Empty video list - clean feed on first launch like TikTok
    val initialVideos = emptyList<Video>()

    // Empty comments & notifications on first launch
    val initialComments = emptyList<Comment>()
    val initialNotifications = emptyList<NotificationItem>()

    val trendingHashtags = listOf(
        HashtagItem("fyp", 120000, "2.4B views", true),
        HashtagItem("trending", 98000, "1.8B views", true),
        HashtagItem("viral", 85000, "1.2B views", true),
        HashtagItem("techtok", 45000, "820M views", true),
        HashtagItem("travel", 38000, "650M views", true),
        HashtagItem("foodie", 32000, "520M views", true),
        HashtagItem("music", 29000, "410M views", true),
        HashtagItem("dance", 25000, "340M views", true)
    )

    val popularSounds = listOf(
        SoundItem("s_1", "Original Sound - TokTok Trend", "TokTok Sounds", 30, "10.2K videos", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_2", "Lofi Beats & Sunset Chill", "Chill Hop Studio", 45, "8.4K videos", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_3", "Cyber Wave Synth 2026", "Aria Nova", 60, "5.5K videos", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_4", "Funky Cooking Groove", "Marcus Beats", 30, "4.2K videos", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_5", "Summer EDM Drop", "TokTok Viral", 35, "3.8K videos", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=200&auto=format&fit=crop&q=80")
    )
}
