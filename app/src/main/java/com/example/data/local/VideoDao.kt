package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.Video
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY isPinned DESC, createdAt DESC")
    fun getAllVideos(): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE userId = :userId ORDER BY createdAt DESC")
    fun getVideosByUser(userId: String): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE isLikedByMe = 1 ORDER BY createdAt DESC")
    fun getLikedVideos(): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE isSavedByMe = 1 ORDER BY createdAt DESC")
    fun getSavedVideos(): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE isFollowedCreator = 1 ORDER BY createdAt DESC")
    fun getFollowingVideos(): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE id = :videoId")
    suspend fun getVideoById(videoId: String): Video?

    @Query("SELECT * FROM videos WHERE caption LIKE '%' || :query || '%' OR userHandle LIKE '%' || :query || '%' OR userName LIKE '%' || :query || '%'")
    fun searchVideos(query: String): Flow<List<Video>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<Video>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Update
    suspend fun updateVideo(video: Video)

    @Query("DELETE FROM videos WHERE id = :videoId")
    suspend fun deleteVideo(videoId: String)

    @Query("UPDATE videos SET viewsCount = viewsCount + 1 WHERE id = :videoId")
    suspend fun incrementViews(videoId: String)

    @Query("UPDATE videos SET likesCount = likesCount + :delta, isLikedByMe = :isLiked WHERE id = :videoId")
    suspend fun updateLike(videoId: String, isLiked: Boolean, delta: Int)

    @Query("UPDATE videos SET isSavedByMe = :isSaved WHERE id = :videoId")
    suspend fun updateSave(videoId: String, isSaved: Boolean)

    @Query("UPDATE videos SET isFollowedCreator = :isFollowed WHERE userId = :userId")
    suspend fun updateCreatorFollowStatus(userId: String, isFollowed: Boolean)

    @Query("UPDATE videos SET commentsCount = commentsCount + 1 WHERE id = :videoId")
    suspend fun incrementCommentCount(videoId: String)

    @Query("UPDATE videos SET sharesCount = sharesCount + 1 WHERE id = :videoId")
    suspend fun incrementShareCount(videoId: String)

    @Query("UPDATE videos SET isPinned = :isPinned WHERE id = :videoId")
    suspend fun togglePin(videoId: String, isPinned: Boolean)

    @Query("UPDATE videos SET isReported = :isReported WHERE id = :videoId")
    suspend fun toggleReport(videoId: String, isReported: Boolean)

    @Query("DELETE FROM videos WHERE userId = :userId")
    suspend fun deleteVideosByUser(userId: String)

    @Query("UPDATE videos SET isPrivate = :isPrivate WHERE id = :videoId")
    suspend fun updateVideoPrivacy(videoId: String, isPrivate: Boolean)

    @Query("UPDATE videos SET isReported = 0 WHERE id = :videoId")
    suspend fun clearVideoReport(videoId: String)

    @Query("UPDATE videos SET viewsCount = :views, likesCount = :likes WHERE id = :videoId")
    suspend fun updateVideoMetrics(videoId: String, views: Int, likes: Int)

    @Query("SELECT * FROM videos WHERE isReported = 1")
    fun getReportedVideos(): Flow<List<Video>>

    @Query("DELETE FROM videos")
    suspend fun deleteAllVideos()

    @Query("SELECT COUNT(*) FROM videos")
    suspend fun getVideoCount(): Int
}
