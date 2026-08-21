package com.example.algorithm

import com.example.model.User
import com.example.model.Video
import kotlin.math.max
import kotlin.math.min

/**
 * Data model representing real-time telemetry metrics for a video playback session.
 */
data class PlaybackTelemetry(
    val videoId: String,
    val watchTimeMs: Long,
    val durationMs: Long,
    val isCompleted: Boolean,
    val loopCount: Int = 0,
    val isLiked: Boolean = false,
    val isCommented: Boolean = false,
    val isShared: Boolean = false,
    val isSaved: Boolean = false,
    val isSkipped: Boolean = false
) {
    val completionRatio: Float
        get() = if (durationMs > 0) (watchTimeMs.toFloat() / durationMs.toFloat()) + (loopCount * 1.0f) else 0f
}

/**
 * Cold-Start Distribution Tier for content discovery.
 */
enum class ColdStartTier(val label: String, val testPoolSize: Int, val boostMultiplier: Float) {
    TIER_1_INITIAL_TEST("Initial Test Pool (100 users)", 100, 1.4f),
    TIER_2_EXPANDED("Expanded Community Pool (5k users)", 5000, 1.25f),
    TIER_3_GLOBAL_VIRAL("Global Viral FYP", 500000, 1.0f)
}

/**
 * Detailed Algorithmic Insights explaining why a post was recommended to a user.
 */
data class AlgoRecommendationInsight(
    val videoId: String,
    val finalScore: Float,
    val interestMatchScore: Float,
    val topMatchingCategory: String,
    val completionScore: Float,
    val engagementScore: Float,
    val coldStartTier: ColdStartTier,
    val officialAccountBoost: Float,
    val reasons: List<String>
)

/**
 * Smart Recommendation Algorithm (Feed Engine)
 *
 * Implements:
 * 1. Real-time User Profiling (Interest affinity vector, watch time, completion rates)
 * 2. Automated Content Categorization & Tagging
 * 3. Content-Based & Collaborative Interest Matching
 * 4. Multi-Stage Viral Cold-Start System (Test Pools)
 * 5. Official & Verified Account Distribution (System Boost)
 */
object RecommendationEngine {

    // User Category Affinity Map: UserId -> Map<Category, Score> (0.0 to 100.0)
    private val userAffinityProfiles = mutableMapOf<String, MutableMap<String, Float>>()

    // Video Cold-Start Status Map: VideoId -> ColdStartTier
    private val videoColdStartTiers = mutableMapOf<String, ColdStartTier>()

    // Video Average Completion Rates: VideoId -> AvgCompletionRate (0.0 to 2.0)
    private val videoAvgCompletionRates = mutableMapOf<String, Float>()

    // Standard platform category taxonomy
    val PLATFORM_CATEGORIES = listOf(
        "tech", "coding", "ai", "gadgets", "future",
        "travel", "japan", "nature", "wanderlust",
        "foodie", "cooking", "recipe", "chef",
        "musicproducer", "synthwave", "beats", "dj", "electronic",
        "dancechallenge", "lifestyle", "fitness", "comedy"
    )

    init {
        // Seed default affinities for standard demo users
        getUserAffinity("user_me")["tech"] = 85f
        getUserAffinity("user_me")["coding"] = 90f
        getUserAffinity("user_me")["travel"] = 65f
        getUserAffinity("user_me")["musicproducer"] = 70f
        getUserAffinity("user_me")["foodie"] = 50f
    }

    /**
     * Retrieves or initializes the interest affinity profile for a user.
     */
    fun getUserAffinity(userId: String): MutableMap<String, Float> {
        return userAffinityProfiles.getOrPut(userId) {
            mutableMapOf(
                "tech" to 50f,
                "coding" to 50f,
                "travel" to 50f,
                "foodie" to 50f,
                "musicproducer" to 50f,
                "dancechallenge" to 40f
            )
        }
    }

    /**
     * Records real-time user interaction and updates the user's affinity profile.
     */
    fun recordInteraction(userId: String, video: Video, telemetry: PlaybackTelemetry) {
        val affinity = getUserAffinity(userId)
        val videoTags = extractCategories(video)

        // Calculate interaction delta based on weights
        var affinityDelta = 0f

        // Watch time and completion
        if (telemetry.completionRatio >= 1.0f) {
            affinityDelta += 6.0f * min(telemetry.completionRatio, 3.0f) // Bonus for rewatches/loops
        } else if (telemetry.completionRatio >= 0.5f) {
            affinityDelta += 3.0f
        } else if (telemetry.isSkipped || telemetry.completionRatio < 0.2f) {
            affinityDelta -= 2.5f // Penalty for early skip
        }

        // Active engagements
        if (telemetry.isLiked) affinityDelta += 8.0f
        if (telemetry.isCommented) affinityDelta += 10.0f
        if (telemetry.isShared) affinityDelta += 12.0f
        if (telemetry.isSaved) affinityDelta += 9.0f

        // Update tags in user affinity profile
        for (tag in videoTags) {
            val currentScore = affinity.getOrDefault(tag, 40f)
            val newScore = (currentScore + affinityDelta).coerceIn(5f, 100f)
            affinity[tag] = newScore
        }

        // Update video cold-start distribution tier based on aggregate performance
        val prevAvg = videoAvgCompletionRates.getOrDefault(video.id, 0.5f)
        val updatedAvg = (prevAvg * 0.7f) + (telemetry.completionRatio * 0.3f)
        videoAvgCompletionRates[video.id] = updatedAvg

        updateColdStartTier(video.id, updatedAvg, video.likesCount + if (telemetry.isLiked) 1 else 0)
    }

    /**
     * Automatically extracts relevant taxonomy categories and hashtags from a video.
     */
    fun extractCategories(video: Video): List<String> {
        val tags = mutableSetOf<String>()
        tags.addAll(video.hashtags.map { it.lowercase().trim('#') })

        val fullText = "${video.caption} ${video.musicTitle} ${video.musicAuthor}".lowercase()
        for (category in PLATFORM_CATEGORIES) {
            if (fullText.contains(category)) {
                tags.add(category)
            }
        }
        return if (tags.isEmpty()) listOf("lifestyle") else tags.toList()
    }

    /**
     * Determines and updates the viral cold-start stage for a video.
     */
    private fun updateColdStartTier(videoId: String, avgCompletion: Float, totalLikes: Int) {
        val currentTier = videoColdStartTiers.getOrDefault(videoId, ColdStartTier.TIER_1_INITIAL_TEST)
        val newTier = when {
            avgCompletion >= 0.85f || totalLikes > 50000 -> ColdStartTier.TIER_3_GLOBAL_VIRAL
            avgCompletion >= 0.60f || totalLikes > 5000 -> ColdStartTier.TIER_2_EXPANDED
            else -> currentTier
        }
        videoColdStartTiers[videoId] = newTier
    }

    /**
     * Computes the algorithmic ranking score and detailed insight for a video given a user.
     */
    fun evaluateVideo(userId: String, video: Video, creator: User?): AlgoRecommendationInsight {
        val userAffinity = getUserAffinity(userId)
        val videoTags = extractCategories(video)

        // 1. Interest Matching Score (0 - 100)
        var matchedAffinitySum = 0f
        var topTag = "For You"
        var maxTagScore = 0f

        for (tag in videoTags) {
            val score = userAffinity.getOrDefault(tag, 35f)
            matchedAffinitySum += score
            if (score > maxTagScore) {
                maxTagScore = score
                topTag = "#$tag"
            }
        }
        val interestMatchScore = if (videoTags.isNotEmpty()) matchedAffinitySum / videoTags.size else 40f

        // 2. Completion & Engagement Ratio
        val avgCompletion = videoAvgCompletionRates.getOrDefault(video.id, 0.72f)
        val completionScore = (avgCompletion * 50f).coerceIn(0f, 100f)

        val totalViews = max(video.viewsCount, 1)
        val engagementRatio = ((video.likesCount * 1.0f + video.commentsCount * 2.0f + video.sharesCount * 3.0f) / totalViews.toFloat()).coerceIn(0f, 1f)
        val engagementScore = engagementRatio * 100f

        // 3. Cold Start Boost
        val coldStartTier = videoColdStartTiers.getOrDefault(video.id, when {
            video.viewsCount > 1000000 -> ColdStartTier.TIER_3_GLOBAL_VIRAL
            video.viewsCount > 50000 -> ColdStartTier.TIER_2_EXPANDED
            else -> ColdStartTier.TIER_1_INITIAL_TEST
        })
        val coldStartBonus = (coldStartTier.boostMultiplier - 1.0f) * 25f

        // 4. Official & Verified Creator Boost
        var officialBoost = 1.0f
        if (creator?.isAdmin == true) {
            officialBoost += 0.50f // Admin / Platform announcement boost
        }
        if (creator?.isVerified == true) {
            officialBoost += 0.25f // Verified creator boost
        }
        if (video.isPinned) {
            officialBoost += 0.30f // Pinned creator highlight
        }

        // 5. Final Multi-Factor Weighted Recommendation Score
        val baseScore = (interestMatchScore * 0.35f) +
                (completionScore * 0.25f) +
                (engagementScore * 0.20f) +
                coldStartBonus

        val finalScore = baseScore * officialBoost

        // Generate explainability reasons
        val reasons = mutableListOf<String>()
        if (interestMatchScore >= 65f) {
            reasons.add("🎯 High affinity for $topTag (${interestMatchScore.toInt()}% match)")
        }
        if (coldStartTier == ColdStartTier.TIER_1_INITIAL_TEST) {
            reasons.add("🧪 Cold-Start Test Pool (Discovering fresh content)")
        } else if (coldStartTier == ColdStartTier.TIER_2_EXPANDED) {
            reasons.add("🔥 Trending in Community (${avgCompletion.times(100).toInt()}% Avg Completion)")
        } else {
            reasons.add("🌟 Viral Hit across TokTok FYP")
        }
        if (creator?.isAdmin == true) {
            reasons.add("🛡️ Official TokTok Featured Post")
        } else if (creator?.isVerified == true) {
            reasons.add("✨ Verified Creator Quality Boost")
        }
        if (video.isFollowedCreator) {
            reasons.add("👥 From a creator you follow")
        }

        return AlgoRecommendationInsight(
            videoId = video.id,
            finalScore = finalScore,
            interestMatchScore = interestMatchScore,
            topMatchingCategory = topTag,
            completionScore = completionScore,
            engagementScore = engagementScore,
            coldStartTier = coldStartTier,
            officialAccountBoost = officialBoost,
            reasons = reasons
        )
    }

    /**
     * Ranks and sorts a list of videos for a specific user using the multi-factor algorithm.
     */
    fun rankForYouFeed(userId: String, videos: List<Video>, usersMap: Map<String, User>): List<Video> {
        if (videos.isEmpty()) return emptyList()

        return videos.sortedByDescending { video ->
            val creator = usersMap[video.userId]
            evaluateVideo(userId, video, creator).finalScore
        }
    }
}
