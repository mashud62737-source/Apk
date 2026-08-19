package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.ui.components.formatTimeAgo
import com.example.ui.theme.TokTokCyan
import com.example.ui.theme.TokTokPink

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onMarkAllRead: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit,
    onUserClick: (String) -> Unit,
    onFollowBack: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filterTabs = listOf("All", "Likes", "Comments", "Followers")

    val filteredNotifications = remember(selectedFilter, notifications) {
        when (selectedFilter) {
            "Likes" -> notifications.filter { it.type == NotificationType.LIKE }
            "Comments" -> notifications.filter { it.type == NotificationType.COMMENT }
            "Followers" -> notifications.filter { it.type == NotificationType.FOLLOW }
            else -> notifications
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Activity & Inbox",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onMarkAllRead() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = "Mark all read",
                    tint = TokTokCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Mark all read",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Category Filter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterTabs) { tab ->
                val isSelected = selectedFilter == tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) TokTokPink else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { selectedFilter = tab }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tab,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(top = 6.dp)
        )

        // Notifications List
        if (filteredNotifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No notifications yet",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Activity related to your videos and profile will appear here",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredNotifications, key = { it.id }) { notif ->
                    NotificationRow(
                        notif = notif,
                        onClick = { onNotificationClick(notif) },
                        onUserClick = { onUserClick(notif.sourceUserId) },
                        onFollowBack = { onFollowBack(notif.sourceUserId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(
    notif: NotificationItem,
    onClick: () -> Unit,
    onUserClick: () -> Unit,
    onFollowBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (!notif.isRead) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Source Avatar with action badge icon
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = notif.sourceUserAvatar,
                contentDescription = notif.sourceUserName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .clickable { onUserClick() }
            )

            val badgeColor = when (notif.type) {
                NotificationType.LIKE -> TokTokPink
                NotificationType.COMMENT -> TokTokCyan
                NotificationType.FOLLOW -> Color(0xFF7C4DFF)
                NotificationType.MENTION -> Color(0xFFFF9800)
                NotificationType.SYSTEM -> Color(0xFF4CAF50)
            }

            val badgeIcon = when (notif.type) {
                NotificationType.LIKE -> Icons.Default.Favorite
                NotificationType.COMMENT -> Icons.Default.ChatBubble
                NotificationType.FOLLOW -> Icons.Default.PersonAdd
                NotificationType.MENTION -> Icons.Default.Star
                NotificationType.SYSTEM -> Icons.Default.Check
            }

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badgeIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Text & Timestamp
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${notif.sourceUserName} ${notif.message}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (!notif.isRead) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formatTimeAgo(notif.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Trailing thumbnail or follow button
        if (notif.type == NotificationType.FOLLOW) {
            Button(
                onClick = onFollowBack,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("Follow Back", fontSize = 11.5.sp, color = Color.White)
            }
        } else if (notif.videoThumbnail != null) {
            AsyncImage(
                model = notif.videoThumbnail,
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 38.dp, height = 50.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        }
    }
}
