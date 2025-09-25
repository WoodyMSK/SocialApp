 package ru.woodymsk.socialapp.data.profile

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.woodymsk.socialapp.domain.profile.model.User
import ru.woodymsk.socialapp.domain.userAvatar
import ru.woodymsk.socialapp.domain.userId
import ru.woodymsk.socialapp.domain.userLogin
import ru.woodymsk.socialapp.domain.userName
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_USER_ID = "user_id"
private const val KEY_USER_LOGIN = "user_login"
private const val KEY_USER_NAME = "user_name"
private const val KEY_USER_AVATAR = "user_avatar"

@Singleton
class ProfilePreferences @Inject constructor(
    private val profilePrefs: SharedPreferences
) {
    private val _profileStateFlow: MutableStateFlow<User>

    init {
        val id = profilePrefs.userId
        val login = profilePrefs.userLogin
        val name = profilePrefs.userName
        val avatar = profilePrefs.userAvatar

        _profileStateFlow = if (id == 0 || login == null || name == null) {
            MutableStateFlow(User(0, "", "", null))
        } else {
            MutableStateFlow(User(id, login, name, avatar))
        }
    }

    val profileStateFlow: StateFlow<User> = _profileStateFlow.asStateFlow()

    @Synchronized
    fun saveProfile(user: User) {
        _profileStateFlow.value = user
        with(profilePrefs.edit()) {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USER_LOGIN, user.login)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_AVATAR, user.avatar)
            apply()
        }
    }

    @Synchronized
    fun clearProfile() {
        _profileStateFlow.value = User(0, "", "", null)
        with(profilePrefs.edit()) {
            clear()
            apply()
        }
    }
}