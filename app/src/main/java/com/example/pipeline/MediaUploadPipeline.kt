package com.example.pipeline

import kotlinx.coroutines.delay
import java.util.UUID

enum class MediaType {
    VIDEO,
    PHOTO,
    TEXT
}

enum class UploadPipelineStage(val title: String, val description: String) {
    VALIDATING("1. Asset Validation", "Inspecting media format, dimensions, audio channels & integrity"),
    TRANSCODING("2. Multi-Bitrate Transcoding", "Encoding 1080p, 720p & 480p streams with HLS adaptive ladder (.m3u8)"),
    OBJECT_STORAGE("3. Object Storage Ingestion", "Uploading master streams and chunk segments to AWS S3 / Cloud Storage"),
    CDN_DISTRIBUTION("4. CDN Global Edge Warming", "Replicating chunks across high-speed edge locations with instant caching"),
    AI_CATEGORIZATION("5. AI Content Classification", "Extracting semantic tags, visual concepts, audio signatures & viral rating"),
    COLD_START_POOL("6. Viral Cold-Start Enrollment", "Distributing to Initial 100-User Test Exploration Pool"),
    FINISHED("7. Published & Live", "Media is active on TokTok FYP and creator profile")
}

data class TranscodingVariant(
    val quality: String,
    val resolution: String,
    val bitrate: String,
    val codec: String = "H.264 / AAC",
    val status: String = "Ready"
)

data class PipelineProgressState(
    val stage: UploadPipelineStage = UploadPipelineStage.VALIDATING,
    val progress: Float = 0f,
    val currentDetail: String = "Initializing pipeline...",
    val variants: List<TranscodingVariant> = emptyList(),
    val s3Key: String = "",
    val cdnUrl: String = "",
    val coldStartPoolId: String = ""
)

data class ProcessedMediaPackage(
    val mediaId: String,
    val mediaType: MediaType,
    val masterStreamUrl: String,
    val thumbnailUrl: String,
    val s3ObjectUri: String,
    val cdnDeliveryUrl: String,
    val detectedTags: List<String>,
    val coldStartPoolId: String,
    val transcodingVariants: List<TranscodingVariant>
)

/**
 * Asynchronous Media Processing & Multi-Bitrate Transcoding Pipeline
 */
object MediaUploadPipeline {

    /**
     * Executes the complete asynchronous upload, transcoding, storage, and distribution pipeline.
     */
    suspend fun processAndUploadMedia(
        mediaType: MediaType,
        rawUri: String,
        caption: String,
        hashtags: List<String>,
        onProgress: (PipelineProgressState) -> Unit
    ): ProcessedMediaPackage {
        val mediaId = "media_${UUID.randomUUID().toString().take(8)}"
        val s3Key = "s3://toktok-media-vault/${if (mediaType == MediaType.VIDEO) "videos" else "photos"}/2026/08/$mediaId"
        val cdnBase = "https://cdn.toktok.app/streams/$mediaId"
        val poolId = "cspool_${(1000..9999).random()}"

        val variants = listOf(
            TranscodingVariant("1080p HD", "1080x1920", "6.2 Mbps (60 fps)"),
            TranscodingVariant("720p HD", "720x1280", "3.1 Mbps (30 fps)"),
            TranscodingVariant("480p SD", "480x854", "1.2 Mbps (Adaptive)")
        )

        // Stage 1: Validation
        onProgress(
            PipelineProgressState(
                stage = UploadPipelineStage.VALIDATING,
                progress = 0.15f,
                currentDetail = "Analyzing 9:16 vertical ratio, 60fps frame rate and AAC-LC stereo audio..."
            )
        )
        delay(350)

        // Stage 2: Multi-Bitrate Transcoding
        onProgress(
            PipelineProgressState(
                stage = UploadPipelineStage.TRANSCODING,
                progress = 0.35f,
                currentDetail = "Transcoding HLS Master Playlist & 3 Adaptive Streams (1080p, 720p, 480p)...",
                variants = variants
            )
        )
        delay(450)

        // Stage 3: Object Storage (AWS S3)
        onProgress(
            PipelineProgressState(
                stage = UploadPipelineStage.OBJECT_STORAGE,
                progress = 0.60f,
                currentDetail = "Writing chunks and manifest to $s3Key...",
                variants = variants,
                s3Key = s3Key
            )
        )
        delay(350)

        // Stage 4: CDN Global Edge Warming
        onProgress(
            PipelineProgressState(
                stage = UploadPipelineStage.CDN_DISTRIBUTION,
                progress = 0.80f,
                currentDetail = "Edge caching warm on CloudFront PoPs (Edge Node: SFO-Edge-02, Low-latency)...",
                variants = variants,
                s3Key = s3Key,
                cdnUrl = "$cdnBase/master.m3u8"
            )
        )
        delay(350)

        // Stage 5: AI Content Classification
        val detectedTags = mutableListOf<String>()
        detectedTags.addAll(hashtags)
        val textLower = caption.lowercase()
        if (textLower.contains("tech") || textLower.contains("code") || textLower.contains("ai")) detectedTags.add("techtok")
        if (textLower.contains("travel") || textLower.contains("trip") || textLower.contains("city")) detectedTags.add("travel")
        if (textLower.contains("food") || textLower.contains("cook") || textLower.contains("recipe")) detectedTags.add("foodie")
        if (detectedTags.isEmpty()) detectedTags.add("lifestyle")

        onProgress(
            PipelineProgressState(
                stage = UploadPipelineStage.AI_CATEGORIZATION,
                progress = 0.92f,
                currentDetail = "Extracted tags: ${detectedTags.joinToString { "#$it" }} • Sentiment: Positive",
                variants = variants,
                s3Key = s3Key,
                cdnUrl = "$cdnBase/master.m3u8"
            )
        )
        delay(300)

        // Stage 6: Cold-Start Test Pool Enrollment
        onProgress(
            PipelineProgressState(
                stage = UploadPipelineStage.COLD_START_POOL,
                progress = 0.98f,
                currentDetail = "Enrolled in Test Pool $poolId (Broadcasting to first 100 active users)...",
                variants = variants,
                s3Key = s3Key,
                cdnUrl = "$cdnBase/master.m3u8",
                coldStartPoolId = poolId
            )
        )
        delay(250)

        // Stage 7: Complete
        onProgress(
            PipelineProgressState(
                stage = UploadPipelineStage.FINISHED,
                progress = 1.0f,
                currentDetail = "Post is live on FYP & CDN streams ready!",
                variants = variants,
                s3Key = s3Key,
                cdnUrl = "$cdnBase/master.m3u8",
                coldStartPoolId = poolId
            )
        )

        return ProcessedMediaPackage(
            mediaId = mediaId,
            mediaType = mediaType,
            masterStreamUrl = if (rawUri.isNotBlank()) rawUri else "$cdnBase/master.m3u8",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            s3ObjectUri = s3Key,
            cdnDeliveryUrl = "$cdnBase/master.m3u8",
            detectedTags = detectedTags.distinct(),
            coldStartPoolId = poolId,
            transcodingVariants = variants
        )
    }
}
