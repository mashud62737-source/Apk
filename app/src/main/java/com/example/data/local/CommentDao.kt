package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE videoId = :videoId ORDER BY createdAt DESC")
    fun getCommentsForVideo(videoId: String): Flow<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<Comment>)

    @Query("DELETE FROM comments WHERE id = :commentId")
    suspend fun deleteComment(commentId: String)

    @Query("UPDATE comments SET likesCount = likesCount + :delta, isLikedByMe = :isLiked WHERE id = :commentId")
    suspend fun toggleCommentLike(commentId: String, isLiked: Boolean, delta: Int)

    @Query("SELECT * FROM comments")
    fun getAllComments(): Flow<List<Comment>>

    @Query("DELETE FROM comments")
    suspend fun deleteAllComments()

    @Query("SELECT COUNT(*) FROM comments")
    suspend fun getCommentCount(): Int
}
