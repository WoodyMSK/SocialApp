package ru.woodymsk.socialapp.presentation.new_event.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.EventType
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.domain.isValidDate
import ru.woodymsk.socialapp.domain.parseDate
import ru.woodymsk.socialapp.presentation.common.DateTransformation
import ru.woodymsk.socialapp.presentation.common.compose.TimePickerDialog
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventEvents
import ru.woodymsk.socialapp.presentation.theme.typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// Ссылка на экран в Figma: https://www.figma.com/design/8z1sV6KIf6Sc1y02TrY2XS/Nmedia?node-id=33-8385&t=FGNxHroHn1Ruq1No-1
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewEventDateTimeBottomSheet(
    state: Event = Event(),
    onEvent: (NewEventEvents) -> Unit = {},
) {

    val interactionSource = remember { MutableInteractionSource() }
    var eventTypeState by remember { mutableStateOf(state.type) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Datetime update function in ViewModel
    fun updateDateTime(dateTime: LocalDateTime) {
        val digits = buildString {
            append(dateTime.dayOfMonth.toString().padStart(2, '0'))
            append(dateTime.monthValue.toString().padStart(2, '0'))
            append(dateTime.year)
            append(dateTime.hour.toString().padStart(2, '0'))
            append(dateTime.minute.toString().padStart(2, '0'))
        }
        onEvent(NewEventEvents.DataTimeUpdated(digits))
    }

    // date selection handler
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val selectedDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            updateDateTime(selectedDate)
        }
    }

    // time selection handler
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        if (showTimePicker) {
            val current = parseDate(state.datetime) ?: LocalDateTime.now()
            val updated = current
                .withHour(timePickerState.hour)
                .withMinute(timePickerState.minute)
            updateDateTime(updated)
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 31.dp)
        ) {
            // Date and time selection field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = state.datetime,
                onValueChange = { newValue ->
                    // Фильтруем только цифры и обрезаем до 12 символов
                    val cleaned = newValue.filter { it.isDigit() }.take(12)
                    if (state.datetime != cleaned) {
                        onEvent(NewEventEvents.DataTimeUpdated(cleaned))
                    }
                },
                visualTransformation = DateTransformation(),
                isError = !isValidDate(state.datetime),
                label = {
                    Text(
                        text = stringResource(R.string.event_date),
                        style = typography().bodySmall,
                    )
                },
                supportingText = {
                    Text(
                        text = stringResource(R.string.event_date_format),
                        style = typography().bodySmall,
                    )
                },
                textStyle = typography().bodyLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.purple_typography),
                    unfocusedBorderColor = colorResource(id = R.color.purple_typography),
                    errorBorderColor = Color.Red,
                    focusedSupportingTextColor = colorResource(id = R.color.purple_typography),
                    disabledSupportingTextColor = colorResource(id = R.color.purple_typography),
                    errorSupportingTextColor = Color.Red,
                    focusedLabelColor = colorResource(id = R.color.purple_typography),
                    unfocusedLabelColor = colorResource(id = R.color.purple_typography),
                    disabledLabelColor = colorResource(id = R.color.purple_typography),
                    errorLabelColor = Color.Red,
                ),
                // datetime selection button
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar_24),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        contentDescription = stringResource(id = R.string.event_timing_button),
                        modifier = Modifier
                            .padding(horizontal = 14.dp)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource,
                            ) {
                                showDatePicker = true
                            }
                    )

                },
            )
            // Event type switcher
            Column(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.type),
                    style = typography().labelLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                )
                NewEventDateRadioButton(
                    eventType = EventType.ONLINE,
                    title = stringResource(id = R.string.event_format_online),
                    state = eventTypeState,
                    onEventType = {
                        eventTypeState = EventType.ONLINE
                        onEvent(NewEventEvents.TypeUpdated(eventTypeState))
                    },
                )
                NewEventDateRadioButton(
                    eventType = EventType.OFFLINE,
                    title = stringResource(id = R.string.event_format_offline),
                    state = eventTypeState,
                    onEventType = {
                        eventTypeState = EventType.OFFLINE
                        onEvent(NewEventEvents.TypeUpdated(eventTypeState))
                    },
                )
            }
            // time selection dialog
            if (showTimePicker) {
                // Ссылка на скриншот экрана:  https://github.com/WoodyMSK/SocialApp/pull/39#issuecomment-2797973677
                TimePickerDialog(
                    onCancel = { showTimePicker = false },
                    onConfirm = { showTimePicker = false },
                ) {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            selectorColor = colorResource(id = R.color.purple_typography),
                            timeSelectorSelectedContainerColor = colorResource(id = R.color.purple_typography),
                            timeSelectorSelectedContentColor = Color.White,
                            clockDialSelectedContentColor = Color.White,
                            periodSelectorSelectedContentColor = Color.White,
                            periodSelectorSelectedContainerColor = colorResource(id = R.color.purple_typography),
                        )
                    )
                }
            }
            // date selection dialog
            if (showDatePicker) {
                // Ссылка на скриншот экрана: https://github.com/WoodyMSK/SocialApp/pull/39#issuecomment-2797976944
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_typography)),
                            onClick = {
                                showDatePicker = false
                                showTimePicker = true
                            }
                        ) {
                            Text(
                                style = typography().labelLarge,
                                color = Color.White,
                                text = stringResource(R.string.choose_time)
                            )
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = colorResource(id = R.color.purple_typography),
                            selectedYearContainerColor = colorResource(id = R.color.purple_typography),
                            todayDateBorderColor = colorResource(id = R.color.purple_typography),
                            todayContentColor = colorResource(id = R.color.purple_typography),
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun NewEventDateRadioButton(
    eventType: EventType,
    title: String,
    state: EventType?,
    onEventType: (EventType) -> Unit,
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(id = R.color.purple_typography),
                unselectedColor = MaterialTheme.colorScheme.onSurface,
            ),
            selected = state == eventType,
            onClick = {
                onEventType(eventType)
            }
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = title,
            style = typography().labelLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        )
    }
}

@Composable
@Preview
fun NewEventDateTimePreview() {
    NewEventDateTimeBottomSheet()
}