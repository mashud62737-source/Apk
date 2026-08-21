package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.model.Comment
import com.example.model.DirectMessage
import com.example.model.NotificationItem
import com.example.model.User
import com.example.model.Video

@Database(
    entities = [
        User::class,
        Video::class,
        Comment::class,
        DirectMessage::class,
        NotificationItem::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun videoDao(): VideoDao
    abstract fun commentDao(): CommentDao
    abstract fun directMessageDao(): DirectMessageDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "toktok_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
