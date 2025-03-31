package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.theme.typography

@Composable
fun TimePickerDialog(
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_typography)),
                onClick = onConfirm
            ) {
                Text(
                    style = typography().labelLarge,
                    color = Color.White,
                    text = stringResource(R.string.ok)
                )
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_typography)),
                onClick = onCancel
            ) {
                Text(
                    style = typography().labelLarge,
                    color = Color.White,
                    text = stringResource(R.string.cancel)
                )
            }
        },
        text = { content() }
    )
}

@Composable
@Preview
fun TimePickerDialogPreview() {
    TimePickerDialog()
}