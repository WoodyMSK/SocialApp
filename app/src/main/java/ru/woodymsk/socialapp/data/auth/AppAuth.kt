package ru.woodymsk.socialapp.data.auth

import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.woodymsk.socialapp.data.auth.model.Token
import ru.woodymsk.socialapp.domain.token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val tokenPrefs: SharedPreferences,
) {

    companion object {
        private const val idKey = "id"
    }

    private val _authStateFlow: MutableStateFlow<Token>

    init {
        val id = tokenPrefs.getInt(idKey, 0)
        val token = tokenPrefs.token

        if (id == 0 || token == null) {
            _authStateFlow = MutableStateFlow(Token())
            with(tokenPrefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(Token(id, token))
        }
    }

    val authStateFlow: StateFlow<Token> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Int, token: String) {
        _authStateFlow.value = Token(id, token)
        tokenPrefs.token = token
        with(tokenPrefs.edit()) {
            putInt(idKey, id)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = Token()
        with(tokenPrefs.edit()) {
            clear()
            commit()
        }
    }
}