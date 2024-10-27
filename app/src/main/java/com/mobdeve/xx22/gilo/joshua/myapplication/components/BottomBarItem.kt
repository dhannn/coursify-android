package com.mobdeve.xx22.gilo.joshua.myapplication.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobdeve.xx22.gilo.joshua.myapplication.ui.theme.PrimaryColor

data class BottomBarItemData(
    val icon: ImageVector,
    val selected: Boolean = false,
    val onClick: () -> Unit = {}
)

@Composable
fun BottomBarItem(
    itemData: BottomBarItemData,
    selectedColor: Color = PrimaryColor,
    nonSelectedColor: Color = PrimaryColor.copy(alpha = 0.7f),
    iconSize: Dp = 24.dp
) {
    IconButton(onClick = { itemData.onClick() }) {
        Icon(
            imageVector = itemData.icon,
            contentDescription = null,
            tint = if (itemData.selected) selectedColor else nonSelectedColor,
            modifier = Modifier.size(iconSize)
        )
    }
}