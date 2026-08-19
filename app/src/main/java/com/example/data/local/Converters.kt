package com.example.data.local

import androidx.room.TypeConverter
import com.example.model.NotificationType

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(separator = "|||") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split("|||").filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun fromNotificationType(value: NotificationType): String {
        return value.name
    }

    @TypeConverter
    fun toNotificationType(value: String): NotificationType {
        return try {
            NotificationType.valueOf(value)
        } catch (e: Exception) {
            NotificationType.SYSTEM
        }
    }
}
