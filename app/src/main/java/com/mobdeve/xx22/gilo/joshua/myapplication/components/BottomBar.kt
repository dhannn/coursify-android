package com.mobdeve.xx22.gilo.joshua.myapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onFabClick: () -> Unit,
    barHeight: Dp = 60.dp,
    fabColor: Color = Color(0xFF7980FF),
    fabSize: Dp = 64.dp,
    fabIconSize: Dp = 32.dp,
    cardTopCornerSize: Dp = 24.dp,
    cardElevation: Dp = 8.dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight + fabSize / 2)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
            shape = RoundedCornerShape(
                topStart = cardTopCornerSize,
                topEnd = cardTopCornerSize,
                bottomEnd = 0.dp,
                bottomStart = 0.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomBarItem(
                    itemData = BottomBarItemData(
                        icon = Icons.Default.Home,
                        selected = selectedIndex == 0,
                        onClick = { onItemSelected(0) }
                    )
                )
                BottomBarItem(
                    itemData = BottomBarItemData(
                        icon = Icons.Default.Favorite,
                        selected = selectedIndex == 1,
                        onClick = { onItemSelected(1) }
                    )
                )
                Spacer(modifier = Modifier.size(fabSize))
                BottomBarItem(
                    itemData = BottomBarItemData(
                        icon = Icons.Default.LocationOn,
                        selected = selectedIndex == 2,
                        onClick = { onItemSelected(2) }
                    )
                )
                BottomBarItem(
                    itemData = BottomBarItemData(
                        icon = Icons.Default.Person,
                        selected = selectedIndex == 3,
                        onClick = { onItemSelected(3) }
                    )
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .size(fabSize)
                .align(Alignment.TopCenter),
            onClick = onFabClick,
            shape = CircleShape,
            containerColor = fabColor,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add New Plan",
                tint = Color.White,
                modifier = Modifier.size(fabIconSize)
            )
        }
    }
}
