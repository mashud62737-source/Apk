package com.example.data

import com.example.model.Comment
import com.example.model.HashtagItem
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.model.SoundItem
import com.example.model.User
import com.example.model.Video

object SampleData {
    val initialUsers = listOf(
        User(
            id = "user_me",
            username = "alex_creative",
            displayName = "Alex Rivera",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
            bio = "✨ Creating short cinematic visuals & coding cool stuff 🚀 | NextGen Creator",
            followerCount = 42800,
            followingCount = 312,
            likesCount = 389000,
            isFollowedByMe = false,
            isVerified = true,
            isAdmin = true,
            email = "alex@toktok.app"
        ),
        User(
            id = "user_2",
            username = "maya_travels",
            displayName = "Maya Chen ✈️",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&auto=format&fit=crop&q=80",
            bio = "Exploring hidden gems around the world 🌍 45+ countries and counting! 📸",
            followerCount = 1250000,
            followingCount = 184,
            likesCount = 18400000,
            isFollowedByMe = true,
            isVerified = true,
            isAdmin = false,
            email = "maya@travel.com"
        ),
        User(
            id = "user_3",
            username = "chef_marcus",
            displayName = "Marcus Culinary 🍳",
            avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
            bio = "Michelin star secrets in under 60 seconds 🔥 New recipes daily!",
            followerCount = 890000,
            followingCount = 95,
            likesCount = 9200000,
            isFollowedByMe = false,
            isVerified = true,
            isAdmin = false,
            email = "marcus@kitchen.io"
        ),
        User(
            id = "user_4",
            username = "neon_beats",
            displayName = "Aria Nova 🎧",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            bio = "Music producer & synth wizard ⚡️ Making beats that hit different",
            followerCount = 640000,
            followingCount = 240,
            likesCount = 5400000,
            isFollowedByMe = true,
            isVerified = true,
            isAdmin = false,
            email = "aria@neonbeats.com"
        ),
        User(
            id = "user_5",
            username = "tech_pulse",
            displayName = "Liam Tech Reviewer 🤖",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80",
            bio = "Daily future tech, AI breakthroughs & gadget teardowns 📱⚡️",
            followerCount = 980000,
            followingCount = 310,
            likesCount = 12000000,
            isFollowedByMe = false,
            isVerified = true,
            isAdmin = false,
            email = "liam@techpulse.org"
        ),
        User(
            id = "user_admin",
            username = "toktok_official",
            displayName = "TokTok Team 🛡️",
            avatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&auto=format&fit=crop&q=80",
            bio = "Official TokTok Community & Creator Hub 🌟 #TokTokCreators",
            followerCount = 5600000,
            followingCount = 12,
            likesCount = 45000000,
            isFollowedByMe = true,
            isVerified = true,
            isAdmin = true,
            email = "admin@toktok.app"
        )
    )

    val initialVideos = listOf(
        Video(
            id = "vid_1",
            userId = "user_2",
            userHandle = "maya_travels",
            userName = "Maya Chen ✈️",
            userAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80",
            caption = "Witnessing the breathtaking sunrise over Mount Fuji 🌄✨ Still can't believe this view was real! Who would you take here? #travel #japan #fuji #wanderlust #fyp",
            hashtags = listOf("travel", "japan", "fuji", "wanderlust", "fyp"),
            musicTitle = "Golden Hour - Lofi Sunset Mix",
            musicAuthor = "Aria Nova 🎧",
            likesCount = 245800,
            commentsCount = 1842,
            sharesCount = 12300,
            viewsCount = 1450000,
            isLikedByMe = true,
            isSavedByMe = true,
            isFollowedCreator = true,
            createdAt = System.currentTimeMillis() - 3600000 * 2,
            isPinned = true
        ),
        Video(
            id = "vid_2",
            userId = "user_3",
            userHandle = "chef_marcus",
            userName = "Marcus Culinary 🍳",
            userAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop&q=80",
            caption = "Crispy Garlic Butter Steak Bites with truffle parmesan dip 🔥🥩 Save this recipe for tonight! #foodie #steak #cooking #recipe #chef #delicious",
            hashtags = listOf("foodie", "steak", "cooking", "recipe", "chef", "delicious"),
            musicTitle = "Cooking Groove - Upbeat Funk",
            musicAuthor = "Chef Marcus Beats",
            likesCount = 189200,
            commentsCount = 945,
            sharesCount = 38400,
            viewsCount = 890000,
            isLikedByMe = false,
            isSavedByMe = true,
            isFollowedCreator = false,
            createdAt = System.currentTimeMillis() - 3600000 * 5
        ),
        Video(
            id = "vid_3",
            userId = "user_4",
            userHandle = "neon_beats",
            userName = "Aria Nova 🎧",
            userAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80",
            caption = "Dropping the bass with analog synths 🎹⚡️ When the beat drops at 0:15... Headphones recommended! 🎧 #musicproducer #synthwave #beats #dj #electronic",
            hashtags = listOf("musicproducer", "synthwave", "beats", "dj", "electronic"),
            musicTitle = "Cyber City Neon - Original Track",
            musicAuthor = "Aria Nova 🎧",
            likesCount = 412000,
            commentsCount = 3120,
            sharesCount = 52100,
            viewsCount = 2300000,
            isLikedByMe = true,
            isSavedByMe = false,
            isFollowedCreator = true,
            createdAt = System.currentTimeMillis() - 3600000 * 12
        ),
        Video(
            id = "vid_4",
            userId = "user_5",
            userHandle = "tech_pulse",
            userName = "Liam Tech Reviewer 🤖",
            userAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=800&auto=format&fit=crop&q=80",
            caption = "This holographic transparent display is finally here! 🤯 Future of smartphones or just a gimmick? Let me know below! #techtok #gadgets #future #ai #tech",
            hashtags = listOf("techtok", "gadgets", "future", "ai", "tech"),
            musicTitle = "Future Wave Techno Beat",
            musicAuthor = "Tech Beats Pro",
            likesCount = 95400,
            commentsCount = 1430,
            sharesCount = 8900,
            viewsCount = 670000,
            isLikedByMe = false,
            isSavedByMe = false,
            isFollowedCreator = false,
            createdAt = System.currentTimeMillis() - 3600000 * 24
        ),
        Video(
            id = "vid_5",
            userId = "user_me",
            userHandle = "alex_creative",
            userName = "Alex Rivera",
            userAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&auto=format&fit=crop&q=80",
            caption = "Building a full short-video app with Jetpack Compose & Kotlin in 24 hours ⚡️💻 Full source code in bio! #androiddev #coding #kotlin #developer #toktok",
            hashtags = listOf("androiddev", "coding", "kotlin", "developer", "toktok"),
            musicTitle = "Lofi Coding Sessions Vol. 4",
            musicAuthor = "Dev Beats",
            likesCount = 58200,
            commentsCount = 612,
            sharesCount = 4300,
            viewsCount = 320000,
            isLikedByMe = true,
            isSavedByMe = true,
            isFollowedCreator = false,
            createdAt = System.currentTimeMillis() - 3600000 * 36
        )
    )

    val initialComments = listOf(
        Comment(
            id = "c_1",
            videoId = "vid_1",
            userId = "user_4",
            userName = "Aria Nova 🎧",
            userHandle = "neon_beats",
            userAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            text = "The lighting in this shot is absolutely surreal! What camera did you use? 😍✨",
            likesCount = 342,
            isLikedByMe = true,
            createdAt = System.currentTimeMillis() - 3600000
        ),
        Comment(
            id = "c_2",
            videoId = "vid_1",
            userId = "user_3",
            userName = "Marcus Culinary 🍳",
            userHandle = "chef_marcus",
            userAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
            text = "Bucket list destination right there! Adding it to my 2026 trip planner 🗻🔥",
            likesCount = 128,
            isLikedByMe = false,
            createdAt = System.currentTimeMillis() - 1800000
        ),
        Comment(
            id = "c_3",
            videoId = "vid_1",
            userId = "user_5",
            userName = "Liam Tech Reviewer 🤖",
            userHandle = "tech_pulse",
            userAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80",
            text = "The audio track syncs so perfectly with the sunrise reveal! 10/10 edit 👏",
            likesCount = 89,
            isLikedByMe = false,
            createdAt = System.currentTimeMillis() - 900000
        ),
        Comment(
            id = "c_4",
            videoId = "vid_2",
            userId = "user_me",
            userName = "Alex Rivera",
            userHandle = "alex_creative",
            userAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
            text = "Making this tonight for dinner, looks unbelievable! 🥩🔥",
            likesCount = 45,
            isLikedByMe = true,
            createdAt = System.currentTimeMillis() - 7200000
        )
    )

    val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            type = NotificationType.LIKE,
            sourceUserId = "user_2",
            sourceUserName = "Maya Chen ✈️",
            sourceUserAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&auto=format&fit=crop&q=80",
            videoId = "vid_5",
            videoThumbnail = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=400&auto=format&fit=crop&q=80",
            message = "liked your video: 'Building a full short-video app...'",
            timestamp = System.currentTimeMillis() - 120000,
            isRead = false
        ),
        NotificationItem(
            id = "notif_2",
            type = NotificationType.FOLLOW,
            sourceUserId = "user_4",
            sourceUserName = "Aria Nova 🎧",
            sourceUserAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            message = "started following you",
            timestamp = System.currentTimeMillis() - 3600000,
            isRead = false
        ),
        NotificationItem(
            id = "notif_3",
            type = NotificationType.COMMENT,
            sourceUserId = "user_3",
            sourceUserName = "Marcus Culinary 🍳",
            sourceUserAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
            videoId = "vid_5",
            videoThumbnail = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=400&auto=format&fit=crop&q=80",
            message = "commented: 'Super clean UI architecture! Great work brother 👏'",
            timestamp = System.currentTimeMillis() - 7200000,
            isRead = true
        ),
        NotificationItem(
            id = "notif_4",
            type = NotificationType.SYSTEM,
            sourceUserId = "user_admin",
            sourceUserName = "TokTok Team 🛡️",
            sourceUserAvatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&auto=format&fit=crop&q=80",
            message = "Welcome to TokTok! Your creator profile is verified and ready.",
            timestamp = System.currentTimeMillis() - 86400000,
            isRead = true
        )
    )

    val trendingHashtags = listOf(
        HashtagItem("fyp", 489000, "98.4B views", true),
        HashtagItem("trending", 312000, "45.2B views", true),
        HashtagItem("techtok", 98000, "12.8B views", true),
        HashtagItem("travel", 145000, "28.5B views", true),
        HashtagItem("foodie", 220000, "34.1B views", true),
        HashtagItem("musicproducer", 64000, "8.9B views", true),
        HashtagItem("dancechallenge", 180000, "31.2B views", true),
        HashtagItem("coding", 42000, "4.7B views", true)
    )

    val popularSounds = listOf(
        SoundItem("s_1", "Original Sound - Maya Chen", "Maya Chen ✈️", 28, "1.2M videos", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_2", "Cyber City Neon (Retro Wave)", "Aria Nova 🎧", 45, "840K videos", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_3", "Golden Hour Lofi Beats", "Chill Hop Studio", 60, "2.5M videos", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_4", "Cooking Groove Funky Beat", "Chef Marcus Beats", 30, "450K videos", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200&auto=format&fit=crop&q=80"),
        SoundItem("s_5", "Summer Vibes EDM Drop 2026", "TokTok Viral Sounds", 35, "3.8M videos", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=200&auto=format&fit=crop&q=80")
    )
}
