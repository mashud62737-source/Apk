package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserByIdOnce(userId: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users ORDER BY followerCount DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE username LIKE '%' || :query || '%' OR displayName LIKE '%' || :query || '%'")
    fun searchUsers(query: String): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    @Query("UPDATE users SET isBanned = :isBanned WHERE id = :userId")
    suspend fun setUserBanned(userId: String, isBanned: Boolean)

    @Query("UPDATE users SET isVerified = :isVerified, nidStatus = :nidStatus WHERE id = :userId")
    suspend fun setUserVerified(userId: String, isVerified: Boolean, nidStatus: String)

    @Query("UPDATE users SET isAdmin = :isAdmin WHERE id = :userId")
    suspend fun setUserAdmin(userId: String, isAdmin: Boolean)

    @Query("UPDATE users SET followerCount = followerCount + :boost WHERE id = :userId")
    suspend fun boostFollowers(userId: String, boost: Int)

    @Query("SELECT * FROM users WHERE nidStatus = 'PENDING'")
    fun getPendingNidUsers(): Flow<List<User>>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}
