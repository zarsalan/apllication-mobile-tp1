package com.example.tp1_epicerie.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp1_epicerie.R

// Composant de champ de texte personnalisé
@Composable
fun CustomTextField(
    label: String,
    labelColor: Color = Color.Black,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label, color = labelColor) },
        modifier = Modifier.fillMaxWidth().padding(start = 6.dp, end = 6.dp, top = 3.dp, bottom = 3.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = labelColor,
            focusedBorderColor = colorResource(id = R.color.app_bar),
        )
    )
}

@Preview
@Composable
fun CustomTextFieldPrev(){
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(label = "text", value = "text", onValueChanged = {})
        CustomTextField(label = "text", value = "text", onValueChanged = {})
    }
}
