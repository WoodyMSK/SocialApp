package ru.woodymsk.socialapp.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.woodymsk.socialapp.domain.event.model.Event

object CustomNavType {

    val EventType = object : NavType<Event?>(
        isNullableAllowed = true
    ) {
        override fun get(bundle: Bundle, key: String): Event? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Event? {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Event?): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Event?) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}