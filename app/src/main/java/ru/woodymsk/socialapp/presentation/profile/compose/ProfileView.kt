package ru.woodymsk.socialapp.presentation.profile.compose

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import ru.woodymsk.socialapp.presentation.profile.ProfileViewModel
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents.LoadingState
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents.ShowProfile
import ru.woodymsk.socialapp.presentation.profile.model.ProfileEvents.ProfileError

@Composable
fun ProfileView(profileViewModel: ProfileViewModel) {
    val state: ProfileEvents by profileViewModel.profile.collectAsState()

    Box(Modifier.fillMaxSize()) {

        val view = LocalView.current
        val window = (view.context as Activity).window
        window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()

        when (val event = state) {
            is ShowProfile -> {
                Profile(
                    user = event.user,
                    myPostList = event.posts,
                    onLogoutClick = profileViewModel::logout,
                    onLike = profileViewModel::onLikeButtonClick,
                    onEdit = profileViewModel::onNewPostClick,
                    onDelete = profileViewModel::onDeleteButtonClick,
                )
            }

            is LoadingState -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.inversePrimary,
                )
            }

            is ProfileError -> {
                Toast.makeText(view.context, event.appError.code, Toast.LENGTH_LONG).show()
            }
        }
    }
}