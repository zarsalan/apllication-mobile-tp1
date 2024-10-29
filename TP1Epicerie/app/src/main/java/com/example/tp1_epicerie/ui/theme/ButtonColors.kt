package com.example.tp1_epicerie.ui.theme

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.tp1_epicerie.R

// Couleurs par défaut pour les boutons de type sauvegarder
@Composable
fun ButtonDefaults.submitButtonColors(): ButtonColors {
    return buttonColors(
        containerColor = colorResource(id = R.color.app_bar),
        contentColor = Color.White,
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.White
    )
}
