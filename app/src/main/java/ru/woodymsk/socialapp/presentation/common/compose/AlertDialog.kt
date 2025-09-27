package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.theme.SocialAppTheme
import ru.woodymsk.socialapp.presentation.theme.typography

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    titleTextSize: TextUnit = 14.sp,
    buttonTextSize: TextUnit = 14.sp,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.alert_dialog_icon)
            )
        },
        title = {
            Text(
                text = dialogTitle,
                style = typography().labelLarge,
                fontSize = titleTextSize,
            )
        },
        text = {
            Text(
                text = dialogText,
                style = typography().bodyLarge,
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.positive_answer),
                    style = typography().labelLarge,
                    fontSize = buttonTextSize,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.negative_answer),
                    style = typography().labelLarge,
                    fontSize = buttonTextSize,
                )
            }
        }
    )
}

@Preview
@Composable
fun AlertDialogExamplePreview() {
    SocialAppTheme {
        AlertDialog(
            onDismissRequest = {  },
            onConfirmation = {  },
            dialogTitle = "Отсутствует авторизация",
            dialogText = "Выполнить вход?",
            titleTextSize = 20.sp,
            buttonTextSize = 16.sp,
            icon = Icons.Default.Info,
        )
    }
}