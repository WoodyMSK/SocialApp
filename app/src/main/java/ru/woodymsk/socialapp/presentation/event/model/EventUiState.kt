package ru.woodymsk.socialapp.presentation.event.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.error.AppError

data class EventUiState(
    val pagingDataFlow: Flow<PagingData<Event>> = emptyFlow(),
    val isAuth: Boolean = false,
    val error: AppError? = null,
    val showAuthDialog: Boolean = false,
)