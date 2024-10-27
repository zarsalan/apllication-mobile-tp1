package com.example.tp1_epicerie.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String = "",
    customDropdownMenus: CustomDropdownMenus = CustomDropdownMenus(
        menus = listOf(),
        containerColor = Color.Gray
    ),
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(value) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(end = 8.dp)
            )

            Box(modifier = Modifier.padding(start = 8.dp)) {
                Box(
                    modifier = Modifier
                        .background(
                            if (expanded) Color.Gray else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        ) // Rounded corners
                        .clickable { expanded = !expanded }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .defaultMinSize(minWidth = 120.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = selectedText,
                                fontSize = 16.sp,
                                color = Color.Black,
                            )
                        }
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown Arrow",
                            tint = Color.White,
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(0.dp, 0.dp),
                    modifier = Modifier
                        .heightIn(max = 256.dp)
                ) {
                    customDropdownMenus.menus.forEach { menu ->
                        DropdownMenuItem(
                            text = { Text(menu.text) },
                            onClick = {
                                selectedText = menu.text
                                menu.onClick()
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

data class CustomDropdownMenu(
    val text: String,
    val onClick: () -> Unit = {},
)

data class CustomDropdownMenus(
    val menus: List<CustomDropdownMenu>,
    val containerColor: Color
)

@Preview(showBackground = true)
@Composable
fun CustomDropdownMenuPreview() {
    CustomDropdownMenu(
        modifier = Modifier.width(300.dp),
        label = "Catégorie:",
        value = "Test",
        CustomDropdownMenus(
            containerColor = Color.Gray,
            menus = listOf(
                CustomDropdownMenu(
                    text = "Fruits",
                ),
                CustomDropdownMenu(
                    text = "Légumes",
                ),
                CustomDropdownMenu(
                    text = "Viandes",
                ),
                CustomDropdownMenu(
                    text = "Poissons",
                )
            )
        )
    )
}