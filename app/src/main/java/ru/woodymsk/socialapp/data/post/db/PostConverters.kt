package ru.woodymsk.socialapp.data.post.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.woodymsk.socialapp.data.model.AttachmentType
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.data.model.UserPreview

class PostConverters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)
    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
    @TypeConverter
    fun fromLikeOwnerIdsList(value: List<Int>): String {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().toJson(value, type)
    }
    @TypeConverter
    fun toLikeOwnerIdsList(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, type)
    }
    @TypeConverter
    fun fromCoords(value: Coords?): String? {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun toCoords(value: String?): Coords? {
        if (value == null) return null
        return Gson().fromJson(value, Coords::class.java)
    }
    @TypeConverter
    fun fromUsersMap(value: Map<Int, UserPreview>?): String? {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun toUsersMap(value: String?): Map<Int, UserPreview>? {
        if (value == null) return null
        val type = object : TypeToken<Map<Int, UserPreview>>() {}.type
        return Gson().fromJson(value, type)
    }
}