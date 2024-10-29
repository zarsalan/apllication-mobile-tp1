package com.example.tp1_epicerie.ui.common

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.tp1_epicerie.R

// Composant de boîte de dialogue personnalisée avec boutons Oui et Non
@Composable
fun CustomYesNoDialog(
    visible: Boolean = false,
    onDismissRequest: () -> Unit,
    onYesWithContext: ((Context) -> Unit)? = null,
    onYes: (() -> Unit)? = null,
    onNo: () -> Unit = {},
    title: String = "",
    message: String = "",
) {
    if (visible) {
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = { onYesWithContext?.invoke(context) ?: onYes?.invoke() }) {
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