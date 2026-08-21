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
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Message
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
    onFollowBack: (String) -> Unit,
    onOpenDirectMessages: () -> Unit = {}
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
                text = "Inbox & Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Direct Messages / Chat Shortcut
                IconButton(onClick = onOpenDirectMessages) {
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = "Direct Messages",
                        tint = TokTokPink
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

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
        }

        // Direct Messages Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(TokTokCyan.copy(alpha = 0.12f))
                .clickable { onOpenDirectMessages() }
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TokTokCyan),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Direct Messages",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Chat with creators, mutual friends & share videos",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = "Open >",
                fontWeight = FontWeight.Bold,
                color = TokTokCyan,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

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
                NotificationType.MESSAGE -> TokTokCyan
                NotificationType.VERIFICATION -> TokTokCyan
                NotificationType.SYSTEM -> Color(0xFF4CAF50)
            }

            val badgeIcon = when (notif.type) {
                NotificationType.LIKE -> Icons.Default.Favorite
                NotificationType.COMMENT -> Icons.Default.ChatBubble
                NotificationType.FOLLOW -> Icons.Default.PersonAdd
                NotificationType.MENTION -> Icons.Default.Star
                NotificationType.MESSAGE -> Icons.Default.Message
                NotificationType.VERIFICATION -> Icons.Default.Check
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
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Message text
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = notif.sourceUserName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = notif.message,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
            Text(
                text = formatTimeAgo(notif.timestamp),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Action right side (Follow button or Video Thumbnail)
        if (notif.type == NotificationType.FOLLOW) {
            Button(
                onClick = onFollowBack,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Follow back", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        } else if (notif.videoThumbnail != null) {
            AsyncImage(
                model = notif.videoThumbnail,
                contentDescription = "Target video",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        }
    }
}
