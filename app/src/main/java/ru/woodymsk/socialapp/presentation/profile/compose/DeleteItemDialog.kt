package ru.woodymsk.socialapp.presentation.profile.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.woodymsk.socialapp.R

@Composable
fun DeleteItemDialog(
    onClick: (Boolean) -> Unit,
) {
    Dialog(onDismissRequest = { onClick(false) }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.remove_post),
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onClick(false) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.negative_answer))
                    }
                    TextButton(
                        onClick = { onClick(true) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.positive_answer))
                    }
                }
            }
        }
    }
}