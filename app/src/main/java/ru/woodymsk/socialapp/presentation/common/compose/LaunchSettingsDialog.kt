package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import ru.woodymsk.socialapp.R


@Composable
fun LaunchSettingsDialog(
    showDialog: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    fontSize = 22.sp,
                )
            },
            text = { Text(stringResource(R.string.go_to_settings_to_allow_access)) },
            confirmButton = {
                AlertDialogButton(
                    text = stringResource(R.string.go_over),
                    onClick = onConfirm,
                )
            },
            dismissButton = {
                AlertDialogButton(
                    text = stringResource(R.string.cancel),
                    onClick = onCancel,
                )
            }
        )
    }
}