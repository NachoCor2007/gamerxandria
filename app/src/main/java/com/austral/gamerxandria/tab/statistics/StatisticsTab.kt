package com.austral.gamerxandria.tab.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.austral.gamerxandria.ui.theme.AppSize
import com.austral.gamerxandria.ui.theme.StatisticItemTitle
import com.austral.gamerxandria.ui.theme.TextGray
import com.austral.gamerxandria.ui.theme.TextWhite

@Composable
fun StatisticsTab() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ) {
        // First menu item - "Guess the game stats"
        HorizontalDivider()
        StatsMenu("Guess the game stats", true)

        // Second menu item - "Gamer Career"
        HorizontalDivider()
        StatsMenu("Gamer Career")
    }
}

@Composable
private fun StatsMenu(menuTitle: String = "Stats menu", menuExpandedValue: Boolean = false) {
    var menuExpanded by remember { mutableStateOf(menuExpandedValue) }

    MenuHeader(
        title = menuTitle,
        isExpanded = menuExpanded,
        onToggleExpand = { menuExpanded = !menuExpanded }
    )

    AnimatedVisibility(visible = menuExpanded) {
        Column(modifier = Modifier.padding(start = AppSize.spacingXLarge, top = AppSize.spacingSmall, bottom = AppSize.spacingSmall)) {
            InfoItem("info")
            InfoItem("info")
        }
    }
}

@Composable
fun MenuHeader(
    title: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .padding(AppSize.contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Use X or checkmark based on isChecked
        if (isExpanded) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Opened menu",
                tint = TextWhite,
                modifier = Modifier.size(AppSize.iconSize)
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Closed menu",
                tint = TextWhite,
                modifier = Modifier.size(AppSize.iconSize)
            )
        }

        Spacer(modifier = Modifier.width(AppSize.contentPadding))

        Text(
            text = title,
            style = StatisticItemTitle,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun InfoItem(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(vertical = AppSize.spacingTiny),
        style = MaterialTheme.typography.bodyMedium,
        color = TextGray
    )
}
