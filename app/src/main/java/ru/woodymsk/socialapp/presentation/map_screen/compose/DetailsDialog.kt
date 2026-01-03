package ru.woodymsk.socialapp.presentation.map_screen.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.common.GrammaticalForm
import ru.woodymsk.socialapp.presentation.common.compose.AlertDialogButton
import ru.woodymsk.socialapp.presentation.common.compose.CategoryTitle
import ru.woodymsk.socialapp.presentation.common.compose.CategoryValue
import ru.woodymsk.socialapp.domain.common.model.CopyCategory
import ru.woodymsk.socialapp.presentation.map_screen.model.MapEvents
import ru.woodymsk.socialapp.domain.map_screen.model.ObjectDetails
import ru.woodymsk.socialapp.domain.map_screen.model.TypeSpecificDetails

@Composable
fun DetailsDialog(
    objectDetails: ObjectDetails,
    onEvent: (MapEvents) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        title = {
            Text(
                text = objectDetails.title,
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
            ) {
                // basic information
                Text(
                    text = objectDetails.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                // coordinates
                objectDetails.coordinates.let { coordinates ->
                    CategoryTitle(stringResource(R.string.coordinates))
                    CategoryValue(
                        text = coordinates,
                        onClick = {
                            onEvent(
                                MapEvents.ShowCopyToast(
                                    CopyCategory(
                                        name = R.string.coordinates,
                                        grammaticalForm = GrammaticalForm.PLURAL,
                                    )
                                )
                            )
                        },
                    )
                }
                // URI
                objectDetails.uri?.let { uri ->
                    CategoryTitle(stringResource(R.string.link))
                    CategoryValue(
                        text = uri,
                        onClick = {
                            onEvent(
                                MapEvents.ShowCopyToast(
                                    CopyCategory(
                                        name = R.string.link,
                                        grammaticalForm = GrammaticalForm.FEMININE,
                                    )
                                )
                            )
                        },
                    )
                }
                // detailed information by type
                when (val state = objectDetails.typeSpecificDetails) {
                    is TypeSpecificDetails.Toponym -> {
                        // address
                        state.address.let { address ->
                            CategoryTitle(stringResource(R.string.address))
                            CategoryValue(
                                text = address,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.address,
                                                grammaticalForm = GrammaticalForm.MASCULINE,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                        // components
                        state.components?.let { components ->
                            CategoryTitle(stringResource(R.string.components))
                            CategoryValue(
                                text = components,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.components,
                                                grammaticalForm = GrammaticalForm.PLURAL,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                    }

                    is TypeSpecificDetails.Business -> {
                        // object_name
                        state.name.let { object_name ->
                            CategoryTitle(stringResource(R.string.object_name))
                            CategoryValue(
                                text = object_name,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.object_name,
                                                grammaticalForm = GrammaticalForm.NEUTER,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                        // working hours
                        state.workingHours?.let { hours ->
                            CategoryTitle(stringResource(R.string.working_hours))
                            CategoryValue(
                                text = hours,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.working_hours,
                                                grammaticalForm = GrammaticalForm.PLURAL,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                        // categories
                        state.categories?.let { categories ->
                            CategoryTitle(stringResource(R.string.categories))
                            CategoryValue(
                                text = categories,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.categories,
                                                grammaticalForm = GrammaticalForm.PLURAL,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                        // phones
                        state.phones?.let { phones ->
                            CategoryTitle(stringResource(R.string.phone_numbers))
                            CategoryValue(
                                text = phones,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.phone_numbers,
                                                grammaticalForm = GrammaticalForm.PLURAL,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                        // link
                        state.link?.let { link ->
                            CategoryTitle(stringResource(R.string.link))
                            CategoryValue(
                                text = link,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.link,
                                                grammaticalForm = GrammaticalForm.FEMININE,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                        // address
                        state.address?.let { address ->
                            CategoryTitle(stringResource(R.string.address))
                            CategoryValue(
                                text = address,
                                onClick = {
                                    onEvent(
                                        MapEvents.ShowCopyToast(
                                            CopyCategory(
                                                name = R.string.address,
                                                grammaticalForm = GrammaticalForm.MASCULINE,
                                            )
                                        )
                                    )
                                },
                            )
                        }
                    }

                    is TypeSpecificDetails.Undefined -> {}
                }
            }
        },
        confirmButton = {
            AlertDialogButton(
                text = stringResource(R.string.add),
                onClick = {
                    onConfirm()
                },
            )
        },
        dismissButton = {
            AlertDialogButton(
                text = stringResource(R.string.close),
                onClick = onDismiss,
            )
        },
    )
}