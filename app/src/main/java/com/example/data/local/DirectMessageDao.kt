package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.DirectMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface DirectMessageDao {
    @Query("SELECT * FROM direct_messages WHERE (senderId = :userId1 AND receiverId = :userId2) OR (senderId = :userId2 AND receiverId = :userId1) ORDER BY timestamp ASC")
    fun getMessagesBetween(userId1: String, userId2: String): Flow<List<DirectMessage>>

    @Query("SELECT * FROM direct_messages WHERE senderId = :userId OR receiverId = :userId ORDER BY timestamp DESC")
    fun getAllMessagesForUser(userId: String): Flow<List<DirectMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: DirectMessage)

    @Query("UPDATE direct_messages SET isRead = 1 WHERE senderId = :otherUserId AND receiverId = :myUserId")
    suspend fun markMessagesAsRead(otherUserId: String, myUserId: String)

    @Query("DELETE FROM direct_messages WHERE (senderId = :userId1 AND receiverId = :userId2) OR (senderId = :userId2 AND receiverId = :userId1)")
    suspend fun deleteConversation(userId1: String, userId2: String)

    @Query("SELECT * FROM direct_messages")
    fun getAllMessages(): Flow<List<DirectMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<DirectMessage>)

    @Query("DELETE FROM direct_messages")
    suspend fun deleteAllMessages()

    @Query("SELECT COUNT(*) FROM direct_messages")
    suspend fun getMessageCount(): Int
}
