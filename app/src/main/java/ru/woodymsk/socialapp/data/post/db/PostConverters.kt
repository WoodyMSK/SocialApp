package ru.woodymsk.socialapp.data.post.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.woodymsk.socialapp.data.model.AttachmentType

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
}