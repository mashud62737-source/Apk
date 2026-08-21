package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TokTokCyan
import com.example.ui.theme.TokTokPink

enum class ScreenTab {
    FEED,
    SEARCH,
    UPLOAD,
    NOTIFICATIONS,
    PROFILE,
    ADMIN,
    DIRECT_MESSAGES,
    SETTINGS
}

@Composable
fun BottomNavBar(
    currentTab: ScreenTab,
    unreadNotificationsCount: Int,
    isDarkTheme: Boolean,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isFeedScreen = currentTab == ScreenTab.FEED
    val backgroundColor = if (isFeedScreen) Color.Black else MaterialTheme.colorScheme.surface
    val activeColor = if (isFeedScreen) Color.White else MaterialTheme.colorScheme.onSurface
    val inactiveColor = if (isFeedScreen) Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(
            color = if (isFeedScreen) Color.White.copy(alpha = 0.15f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            thickness = 0.5.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Tab
            NavItem(
                icon = if (currentTab == ScreenTab.FEED) Icons.Filled.Home else Icons.Outlined.Home,
                label = "Home",
                isSelected = currentTab == ScreenTab.FEED,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = { onTabSelected(ScreenTab.FEED) }
            )

            // Discover / Search Tab
            NavItem(
                icon = if (currentTab == ScreenTab.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                label = "Discover",
                isSelected = currentTab == ScreenTab.SEARCH,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = { onTabSelected(ScreenTab.SEARCH) }
            )

            // Central Upload Button
            UploadButton(onClick = { onTabSelected(ScreenTab.UPLOAD) })

            // Inbox / Notifications Tab
            NavItem(
                icon = if (currentTab == ScreenTab.NOTIFICATIONS) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                label = "Inbox",
                isSelected = currentTab == ScreenTab.NOTIFICATIONS,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                badgeCount = unreadNotificationsCount,
                onClick = { onTabSelected(ScreenTab.NOTIFICATIONS) }
            )

            // Profile Tab
            NavItem(
                icon = if (currentTab == ScreenTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                label = "Profile",
                isSelected = currentTab == ScreenTab.PROFILE,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                onClick = { onTabSelected(ScreenTab.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        if (badgeCount > 0) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = TokTokPink,
                        contentColor = Color.White
                    ) {
                        Text(text = if (badgeCount > 9) "9+" else badgeCount.toString(), fontSize = 10.sp)
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) activeColor else inactiveColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) activeColor else inactiveColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else inactiveColor
        )
    }
}

@Composable
private fun UploadButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Cyan background pill (left offset)
        Box(
            modifier = Modifier
                .offset(x = (-3).dp)
                .size(width = 38.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TokTokCyan)
        )

        // Pink background pill (right offset)
        Box(
            modifier = Modifier
                .offset(x = 3.dp)
                .size(width = 38.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TokTokPink)
        )

        // Center white button with black Plus
        Box(
            modifier = Modifier
                .size(width = 38.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Video",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
