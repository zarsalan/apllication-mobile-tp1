package com.example.tp1_epicerie.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.tp1_epicerie.R

@Composable
fun CustomYesNoDialog(
    visible: Boolean = false,
    onDismissRequest: () -> Unit,
    onYes: () -> Unit,
    onNo: () -> Unit = {},
    title: String = "",
    message: String = "",
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = { onYes() }) {
                    Text((stringResource(R.string.text_yes)))
                }
            },
            dismissButton = {
                Button(
                    onClick = { onNo() }) {
                    Text((stringResource(R.string.text_no)))
                }
            }
        )
    }
}