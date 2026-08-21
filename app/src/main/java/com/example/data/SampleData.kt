package com.example.data

import com.example.model.Comment
import com.example.model.HashtagItem
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.model.SoundItem
import com.example.model.User
import com.example.model.Video

object SampleData {
    // Default starter creators
    val initialUsers = listOf(
        User(
            id = "user_me",
            username = "mashud_creator",
            displayName = "Mashud Official 🇧🇩",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
            bio = "👑 BDTOK Master Creator & Owner | Welcome to the official platform! ✨",
            followerCount = 125000,
            followingCount = 14,
            likesCount = 890000,
            isFollowedByMe = false,
            isVerified = true,
            verifiedCategory = "Master Creator",
            nidStatus = "VERIFIED",
            isAdmin = true,
            email = "mashud62737@gmail.com"
        ),
        User(
            id = "creator_dhaka",
            username = "dhaka_vibes",
            displayName = "Dhaka City Explorer 🏙️",
            avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
            bio = "Exploring Dhaka city life, street foods and culture 🇧🇩",
            followerCount = 48200,
            followingCount = 120,
            likesCount = 310000,
            isFollowedByMe = false,
            isVerified = true,
            isAdmin = false,
            email = "dhaka@bdtok.app"
        ),
        User(
            id = "creator_tech",
            username = "bangla_techie",
            displayName = "Bangla Tech & AI 💡",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            bio = "Daily tech reviews, gadget tips and coding guides 🚀",
            followerCount = 84000,
            followingCount = 45,
            likesCount = 620000,
            isFollowedByMe = false,
            isVerified = true,
            isAdmin = false,
            email = "tech@bdtok.app"
        )
    )

    // Vibrant starter videos for immediate playback on feed
    val initialVideos = listOf(
        Video(
            id = "video_1",
            userId = "user_me",
            userHandle = "mashud_creator",
            userName = "Mashud Official 🇧🇩",
            userAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80",
            caption = "Welcome to BDTOK! 🇧🇩 The next generation short-video platform for creators. Double tap to like! 🔥 #bdtok #trending #creator #bangladesh",
            category = "Trending / FYP",
            hashtags = listOf("bdtok", "trending", "creator", "bangladesh"),
            musicTitle = "Original Sound - @mashud_creator",
            musicAuthor = "Mashud Official",
            likesCount = 28400,
            commentsCount = 1420,
            sharesCount = 3890,
            viewsCount = 154000,
            isLikedByMe = true,
            isPinned = true
        ),
        Video(
            id = "video_2",
            userId = "creator_dhaka",
            userHandle = "dhaka_vibes",
            userName = "Dhaka City Explorer 🏙️",
            userAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=80",
            caption = "Sunset vibes and amazing energy across the city! 🌇 Who loves evening strolls? #dhaka #travel #vibes #citylife",
            category = "Travel & Vlog",
            hashtags = listOf("dhaka", "travel", "vibes", "citylife"),
            musicTitle = "Lofi Beats & Sunset Chill",
            musicAuthor = "Chill Hop Studio",
            likesCount = 19200,
            commentsCount = 890,
            sharesCount = 1200,
            viewsCount = 98000
        ),
        Video(
            id = "video_3",
            userId = "creator_tech",
            userHandle = "bangla_techie",
            userName = "Bangla Tech & AI 💡",
            userAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800&auto=format&fit=crop&q=80",
            caption = "Top 3 smartphone tips you probably did not know in 2026! 📱💡 Save this video for later. #tech #tips #gadgets #techtok",
            category = "Tech & Coding",
            hashtags = listOf("tech", "tips", "gadgets", "techtok"),
            musicTitle = "Cyber Wave Synth 2026",
            musicAuthor = "Aria Nova",
            likesCount = 34500,
            commentsCount = 1630,
            sharesCount = 4500,
            viewsCount = 210000
        )
    )

    // Initial comments & notifications
    val initialComments = listOf(
        Comment(
            id = "c_1",
            videoId = "video_1",
            userId = "creator_dhaka",
            userName = "Dhaka City Explorer 🏙️",
            userHandle = "dhaka_vibes",
            userAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
            text = "Awesome launch of BDTOK! Looking forward to creating great content here 🚀🇧🇩",
            isCreatorVerified = true,
            likesCount = 342,
            isLikedByMe = true
        ),
        Comment(
            id = "c_2",
            videoId = "video_1",
            userId = "creator_tech",
            userName = "Bangla Tech & AI 💡",
            userHandle = "bangla_techie",
            userAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            text = "Great platform UI and smooth feed! 🔥",
            isCreatorVerified = true,
            likesCount = 188
        )
    )

    val initialNotifications = listOf(
        NotificationItem(
            id = "notif_welcome",
            type = NotificationType.SYSTEM,
            sourceUserId = "system_bdtok",
            sourceUserName = "BDTOK Team",
            sourceUserAvatar = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&auto=format&fit=crop&q=80",
            message = "Welcome to BDTOK! You have full Master Creator controls in your Admin Panel 👑",
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
    )

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

    val videoCategories = listOf(
        com.example.model.VideoCategory("cat_trending", "Trending / FYP", "🔥", "Top viral moments & trending sounds"),
        com.example.model.VideoCategory("cat_comedy", "Comedy & Fun", "😂", "Memes, skits, pranks and funny moments"),
        com.example.model.VideoCategory("cat_tech", "Tech & Coding", "💡", "Gadgets, apps, AI development & tech reviews"),
        com.example.model.VideoCategory("cat_gaming", "Gaming & Esports", "🎮", "Gameplay, highlights, streams & tips"),
        com.example.model.VideoCategory("cat_music", "Music & Dance", "🎵", "Original songs, choreography & covers"),
        com.example.model.VideoCategory("cat_food", "Food & Cooking", "🍕", "Delicious recipes, restaurant vlogs & snacks"),
        com.example.model.VideoCategory("cat_beauty", "Fashion & Beauty", "👗", "Outfits, makeup tutorials & aesthetics"),
        com.example.model.VideoCategory("cat_travel", "Travel & Vlog", "✈️", "Daily life, scenery, destinations & culture"),
        com.example.model.VideoCategory("cat_fitness", "Sports & Fitness", "🏋️", "Workouts, soccer, motivation & health"),
        com.example.model.VideoCategory("cat_edu", "Education & Life", "📚", "Quick tips, tutorials, facts & language"),
        com.example.model.VideoCategory("cat_art", "Art & Animation", "🎨", "Digital drawing, 3D animations & crafts"),
        com.example.model.VideoCategory("cat_pets", "Animals & Pets", "🐾", "Cute cats, dogs, wildlife & pet moments")
    )
}
