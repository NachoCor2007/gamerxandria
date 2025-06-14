package com.austral.gamerxandria.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.austral.gamerxandria.ui.theme.AppSize

@Composable
fun BottomBar(
    onNavigate: (String) -> Unit,
) {
    val libraryTab = TabBarItem(title = GamerxandriaNouns.Library.name, selectedIcon = Icons.Filled.Favorite, unselectedIcon = Icons.Outlined.FavoriteBorder)
    val searchTab = TabBarItem(title = GamerxandriaNouns.Search.name, selectedIcon = Icons.Filled.Search, unselectedIcon = Icons.Outlined.Search)
    val guessTab = TabBarItem(title = GamerxandriaNouns.Guess.name, selectedIcon = Icons.AutoMirrored.Filled.Help, unselectedIcon = Icons.AutoMirrored.Outlined.Help)
    val profileTab = TabBarItem(title = GamerxandriaNouns.Profile.name, selectedIcon = Icons.Filled.AccountCircle, unselectedIcon = Icons.Outlined.AccountCircle)

    val tabBarItems = listOf<TabBarItem>(libraryTab, searchTab, guessTab, profileTab)

    TabView(tabBarItems, onNavigate)
}

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

@Composable
fun TabView(tabBarItems: List<TabBarItem>, onNavigate: (String) -> Unit) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val colors = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colors.background,
        tonalElevation = AppSize.spacingTiny
    ) {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onNavigate(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = {
                    Text(
                        text = tabBarItem.title,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary,
                    selectedTextColor = colors.primary,
                    indicatorColor = colors.secondary,
                    unselectedIconColor = colors.primary.copy(alpha = 0.7f),
                    unselectedTextColor = colors.primary.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    val colors =  MaterialTheme.colorScheme

    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = title,
            tint = if (isSelected) colors.primary else colors.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}