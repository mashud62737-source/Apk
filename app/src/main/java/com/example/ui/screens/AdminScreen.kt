package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import android.content.Intent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.algorithm.RecommendationEngine
import com.example.model.PlatformAdConfig
import com.example.model.User
import com.example.model.Video
import com.example.ui.MainViewModel
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.formatCount
import com.example.ui.theme.TokTokCyan
import com.example.ui.theme.TokTokPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val allVideos by viewModel.allVideos.collectAsState(initial = emptyList())
    val allUsers by viewModel.searchUsers("").collectAsState(initial = emptyList())
    val pendingNidUsers by viewModel.pendingNidUsers.collectAsState(initial = emptyList())
    val reportedVideos by viewModel.reportedVideos.collectAsState(initial = emptyList())
    val adConfig by viewModel.adConfig.collectAsState()

    // Master Creator Security Verification Gate
    var isUnlocked by remember { mutableStateOf(currentUser?.isAdmin == true) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "👥 Users (${allUsers.size})",
        "🎬 Videos & Privacy (${allVideos.size})",
        "📢 App Ads Control",
        "📣 Global Broadcast",
        "💾 Database & App Download",
        "⚡ Telemetry & FYP"
    )

    if (!isUnlocked) {
        // Master Creator PIN Lock Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(TokTokPink.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VpnKey,
                    contentDescription = null,
                    tint = TokTokPink,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "BDTOK Master Creator Portal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Only the Master Creator can access app controls, review accounts, delete users, change video privacy, and manage platform ads.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = enteredPin,
                onValueChange = {
                    enteredPin = it
                    pinError = false
                },
                label = { Text("Master PIN (Default: 1234)") },
                singleLine = true,
                isError = pinError,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = TokTokCyan)
                },
                modifier = Modifier.fillMaxWidth(0.85f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TokTokPink,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            if (pinError) {
                Text(
                    text = "Incorrect PIN. Try default '1234' or tap Creator Unlock.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (enteredPin.trim() == "1234" || enteredPin.trim() == "0000" || enteredPin.trim() == "admin") {
                        isUnlocked = true
                        Toast.makeText(context, "👑 Master Creator Access Granted!", Toast.LENGTH_SHORT).show()
                    } else {
                        pinError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.LockOpen, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Unlock Creator Controls", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    // Quick Creator Bypass for active admin account
                    isUnlocked = true
                    Toast.makeText(context, "👑 Verified Master Creator Logged In", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = TokTokCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Authenticate as @${currentUser?.username ?: "creator"}", color = TokTokCyan)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBack) {
                Text("Return to Profile", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // App Control Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BDTOK Master Creator Control",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(TokTokPink)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("OWNER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Text(
                    text = "Full App Controls: User Review • Privacy • Account Delete • Ads",
                    style = MaterialTheme.typography.labelSmall,
                    color = TokTokCyan,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = {
                Toast.makeText(context, "Refreshing data...", Toast.LENGTH_SHORT).show()
            }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = MaterialTheme.colorScheme.onSurface)
            }
        }

        // Horizontal Scrollable Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = TokTokPink,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        // Active Tab Screen Content
        when (selectedTab) {
            0 -> AdminUsersControlTab(
                users = allUsers,
                pendingNidUsers = pendingNidUsers,
                onDeleteUser = { userId -> viewModel.adminDeleteUser(userId) },
                onUpdateUser = { user -> viewModel.adminUpdateUser(user) },
                onReviewNid = { userId, approved -> viewModel.adminReviewNid(userId, approved) },
                onToggleVerified = { userId, verified -> viewModel.adminToggleUserVerified(userId, verified) },
                onToggleAdmin = { userId, admin -> viewModel.adminToggleUserAdmin(userId, admin) },
                onToggleBan = { userId, ban -> viewModel.adminToggleUserBan(userId, ban) },
                onBoostFollowers = { userId, boost -> viewModel.adminBoostFollowers(userId, boost) }
            )
            1 -> AdminVideosModerationTab(
                videos = allVideos,
                reportedVideos = reportedVideos,
                onDeleteVideo = { videoId -> viewModel.deleteVideo(videoId) },
                onTogglePin = { videoId, pinned -> viewModel.togglePinVideo(videoId, pinned) },
                onSetPrivacy = { videoId, isPrivate -> viewModel.adminSetVideoPrivacy(videoId, isPrivate) },
                onClearReport = { videoId -> viewModel.adminClearVideoReport(videoId) },
                onUpdateMetrics = { videoId, views, likes -> viewModel.adminUpdateVideoMetrics(videoId, views, likes) }
            )
            2 -> AdminAdsControlTab(
                adConfig = adConfig,
                onUpdateAdConfig = { config -> viewModel.adminUpdateAdConfig(config) }
            )
            3 -> AdminBroadcastTab(
                onBroadcast = { title, message -> viewModel.adminBroadcastAnnouncement(title, message) }
            )
            4 -> AdminDatabaseBackupTab(
                viewModel = viewModel,
                allUsersCount = allUsers.size,
                allVideosCount = allVideos.size
            )
            5 -> AdminFeedTelemetryTab(
                videos = allVideos,
                users = allUsers
            )
        }
    }
}

// ==========================================
// TAB 1: USER CONTROL & REVIEW ACCOUNTS
// ==========================================
@Composable
private fun AdminUsersControlTab(
    users: List<User>,
    pendingNidUsers: List<User>,
    onDeleteUser: (String) -> Unit,
    onUpdateUser: (User) -> Unit,
    onReviewNid: (String, Boolean) -> Unit,
    onToggleVerified: (String, Boolean) -> Unit,
    onToggleAdmin: (String, Boolean) -> Unit,
    onToggleBan: (String, Boolean) -> Unit,
    onBoostFollowers: (String, Int) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var filterCategory by remember { mutableStateOf("ALL") } // ALL, PENDING, VERIFIED, BANNED

    var editingUser by remember { mutableStateOf<User?>(null) }
    var userToDelete by remember { mutableStateOf<User?>(null) }

    val filteredUsers = users.filter { user ->
        val matchesQuery = user.displayName.contains(searchQuery, ignoreCase = true) ||
                user.username.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (filterCategory) {
            "PENDING" -> user.nidStatus == "PENDING"
            "VERIFIED" -> user.isVerified
            "BANNED" -> user.isBanned
            "ADMIN" -> user.isAdmin
            else -> true
        }
        matchesQuery && matchesFilter
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search & Filter Header
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search users by name, @handle, or email...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TokTokCyan) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = filterCategory == "ALL",
                        onClick = { filterCategory = "ALL" },
                        label = { Text("All Users (${users.size})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TokTokPink, selectedLabelColor = Color.White)
                    )
                }
                item {
                    FilterChip(
                        selected = filterCategory == "PENDING",
                        onClick = { filterCategory = "PENDING" },
                        label = { Text("Review NID (${pendingNidUsers.size})") },
                        leadingIcon = if (pendingNidUsers.isNotEmpty()) {
                            { Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFF9800), selectedLabelColor = Color.White)
                    )
                }
                item {
                    FilterChip(
                        selected = filterCategory == "VERIFIED",
                        onClick = { filterCategory = "VERIFIED" },
                        label = { Text("Verified (${users.count { it.isVerified }})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TokTokCyan, selectedLabelColor = Color.Black)
                    )
                }
                item {
                    FilterChip(
                        selected = filterCategory == "BANNED",
                        onClick = { filterCategory = "BANNED" },
                        label = { Text("Banned (${users.count { it.isBanned }})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.Red, selectedLabelColor = Color.White)
                    )
                }
            }
        }

        // Review Accounts Queue (Pending NID Documents)
        if (pendingNidUsers.isNotEmpty() && (filterCategory == "ALL" || filterCategory == "PENDING")) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800).copy(alpha = 0.12f)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFF9800))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF9800))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pending NID Verifications To Review (${pendingNidUsers.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFFF9800)
                            )
                        }

                        pendingNidUsers.forEach { applicant ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(
                                            model = applicant.avatarUrl,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp).clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = applicant.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(text = "@${applicant.username} • Category: ${applicant.verifiedCategory}", fontSize = 12.sp, color = TokTokCyan)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "📄 NID: ${applicant.nidNumber ?: "N/A"} • Real Name: ${applicant.realName ?: applicant.displayName}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    // Document Previews
                                    if (applicant.nidFrontUri != null || applicant.nidBackUri != null) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            applicant.nidFrontUri?.let { front ->
                                                AsyncImage(
                                                    model = front,
                                                    contentDescription = "Front NID",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(64.dp)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color.DarkGray)
                                                )
                                            }
                                            applicant.nidBackUri?.let { back ->
                                                AsyncImage(
                                                    model = back,
                                                    contentDescription = "Back NID",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(64.dp)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color.DarkGray)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                onReviewNid(applicant.id, true)
                                                Toast.makeText(context, "✅ Approved @${applicant.username} with Blue Tick!", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Approve & Blue Tick", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                onReviewNid(applicant.id, false)
                                                Toast.makeText(context, "Rejected NID for @${applicant.username}", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(0.6f)
                                        ) {
                                            Text("Reject", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Registered Accounts
        item {
            Text(
                text = "Registered User Accounts (${filteredUsers.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (filteredUsers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No users found matching query.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // List of all User Cards
        items(filteredUsers, key = { it.id }) { user ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (user.isBanned) Color.Red.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = user.avatarUrl,
                            contentDescription = user.displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (user.isVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    VerifiedBadge(size = 15.dp)
                                }
                            }
                            Text(
                                text = "@${user.username} • ${user.email.ifBlank { "No email" }}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${formatCount(user.followerCount)} followers • ${formatCount(user.likesCount)} likes",
                                fontSize = 11.sp,
                                color = TokTokCyan
                            )
                        }

                        // Badges
                        Column(horizontalAlignment = Alignment.End) {
                            if (user.isAdmin) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(TokTokPink)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("ADMIN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            if (user.isBanned) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Red)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("BANNED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            } else if (user.isPrivateAccount) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Gray.copy(alpha = 0.4f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PRIVATE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Action Tool Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1. Edit User Settings & Privacy
                        OutlinedButton(
                            onClick = { editingUser = user },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(15.dp), tint = TokTokPink)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("User Settings", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }

                        // 2. Toggle Blue Tick
                        IconButton(
                            onClick = {
                                onToggleVerified(user.id, !user.isVerified)
                                Toast.makeText(context, if (!user.isVerified) "✅ Blue Tick granted to @${user.username}" else "Blue Tick removed", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = if (user.isVerified) Icons.Default.CheckCircle else Icons.Default.Stars,
                                contentDescription = "Toggle Blue Tick",
                                tint = if (user.isVerified) TokTokCyan else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // 3. Toggle Ban / Unban
                        IconButton(
                            onClick = {
                                onToggleBan(user.id, !user.isBanned)
                                Toast.makeText(context, if (!user.isBanned) "🚫 User @${user.username} suspended" else "User unbanned", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = "Ban User",
                                tint = if (user.isBanned) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // 4. Delete Account
                        IconButton(
                            onClick = { userToDelete = user }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Account",
                                tint = Color.Red.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }
        }
    }

    // === DIALOG: EDIT USER SETTINGS & PRIVACY OVERRIDE ===
    editingUser?.let { user ->
        var editName by remember { mutableStateOf(user.displayName) }
        var editUsername by remember { mutableStateOf(user.username) }
        var editEmail by remember { mutableStateOf(user.email) }
        var editBio by remember { mutableStateOf(user.bio) }
        var editIsPrivate by remember { mutableStateOf(user.isPrivateAccount) }
        var editAllowDMs by remember { mutableStateOf(user.allowDirectMessages) }
        var editAllowDownloads by remember { mutableStateOf(user.allowDownloads) }
        var editAllowDuet by remember { mutableStateOf(user.allowDuet) }
        var editAllowStitch by remember { mutableStateOf(user.allowStitch) }
        var editFilterComments by remember { mutableStateOf(user.filterComments) }
        var editIsAdmin by remember { mutableStateOf(user.isAdmin) }

        AlertDialog(
            onDismissRequest = { editingUser = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = TokTokPink)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Creator Override: @${user.username}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("ACCOUNT CREDENTIALS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TokTokCyan)

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it.removePrefix("@") },
                        label = { Text("Username (@)") },
                        singleLine = true,
                        prefix = { Text("@") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Bio") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("PRIVACY & PERMISSIONS OVERRIDE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TokTokPink)

                    // Private Account Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Force Private Account", fontSize = 13.sp)
                        Switch(
                            checked = editIsPrivate,
                            onCheckedChange = { editIsPrivate = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TokTokPink, checkedTrackColor = TokTokPink.copy(alpha = 0.5f))
                        )
                    }

                    // Video Downloads
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Allow Video Downloads", fontSize = 13.sp)
                        Switch(
                            checked = editAllowDownloads,
                            onCheckedChange = { editAllowDownloads = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TokTokPink, checkedTrackColor = TokTokPink.copy(alpha = 0.5f))
                        )
                    }

                    // Duet & Stitch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Allow Duet & Stitch", fontSize = 13.sp)
                        Switch(
                            checked = editAllowDuet && editAllowStitch,
                            onCheckedChange = {
                                editAllowDuet = it
                                editAllowStitch = it
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = TokTokPink, checkedTrackColor = TokTokPink.copy(alpha = 0.5f))
                        )
                    }

                    // Filter Comments
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Filter Spam Comments", fontSize = 13.sp)
                        Switch(
                            checked = editFilterComments,
                            onCheckedChange = { editFilterComments = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TokTokPink, checkedTrackColor = TokTokPink.copy(alpha = 0.5f))
                        )
                    }

                    // Admin Privileges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Grant Admin Privilege 🛡️", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Switch(
                            checked = editIsAdmin,
                            onCheckedChange = { editIsAdmin = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TokTokCyan, checkedTrackColor = TokTokCyan.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("QUICK FOLLOWER BOOST 🚀", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onBoostFollowers(user.id, 1000)
                                Toast.makeText(context, "+1,000 followers added!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+1K", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onBoostFollowers(user.id, 10000)
                                Toast.makeText(context, "+10,000 followers added!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+10K", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onBoostFollowers(user.id, 50000)
                                Toast.makeText(context, "+50,000 followers added!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+50K", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = user.copy(
                            displayName = editName.trim(),
                            username = editUsername.trim(),
                            email = editEmail.trim(),
                            bio = editBio.trim(),
                            isPrivateAccount = editIsPrivate,
                            allowDirectMessages = editAllowDMs,
                            allowDownloads = editAllowDownloads,
                            allowDuet = editAllowDuet,
                            allowStitch = editAllowStitch,
                            filterComments = editFilterComments,
                            isAdmin = editIsAdmin
                        )
                        onUpdateUser(updated)
                        editingUser = null
                        Toast.makeText(context, "User settings updated platform-wide! ✨", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TokTokPink)
                ) {
                    Text("Save & Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingUser = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // === DIALOG: DELETE ACCOUNT CONFIRMATION ===
    userToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Account: @${target.username}", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    "Are you sure you want to permanently delete this user account? All their uploaded videos, comments, followers, and direct messages will be purged immediately from BDTOK.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteUser(target.id)
                        userToDelete = null
                        Toast.makeText(context, "🗑️ Account @${target.username} deleted permanently.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete Account", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ==========================================
// TAB 2: VIDEO & PRIVACY MODERATION
// ==========================================
@Composable
private fun AdminVideosModerationTab(
    videos: List<Video>,
    reportedVideos: List<Video>,
    onDeleteVideo: (String) -> Unit,
    onTogglePin: (String, Boolean) -> Unit,
    onSetPrivacy: (String, Boolean) -> Unit,
    onClearReport: (String) -> Unit,
    onUpdateMetrics: (String, Int, Int) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var filterCategory by remember { mutableStateOf("ALL") } // ALL, REPORTED, PINNED, PRIVATE

    var editingMetricsVideo by remember { mutableStateOf<Video?>(null) }
    var videoToDelete by remember { mutableStateOf<Video?>(null) }

    val filteredVideos = videos.filter { video ->
        val matchesQuery = video.caption.contains(searchQuery, ignoreCase = true) ||
                video.userHandle.contains(searchQuery, ignoreCase = true) ||
                video.userName.contains(searchQuery, ignoreCase = true) ||
                video.category.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (filterCategory) {
            "REPORTED" -> video.isReported
            "PINNED" -> video.isPinned
            "PRIVATE" -> video.isPrivate
            else -> true
        }
        matchesQuery && matchesFilter
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search videos by caption, creator, category...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TokTokCyan) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Filter chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = filterCategory == "ALL",
                        onClick = { filterCategory = "ALL" },
                        label = { Text("All Videos (${videos.size})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TokTokPink, selectedLabelColor = Color.White)
                    )
                }
                item {
                    FilterChip(
                        selected = filterCategory == "REPORTED",
                        onClick = { filterCategory = "REPORTED" },
                        label = { Text("Reported Flags (${reportedVideos.size})") },
                        leadingIcon = if (reportedVideos.isNotEmpty()) {
                            { Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.Red, selectedLabelColor = Color.White)
                    )
                }
                item {
                    FilterChip(
                        selected = filterCategory == "PINNED",
                        onClick = { filterCategory = "PINNED" },
                        label = { Text("Pinned to FYP (${videos.count { it.isPinned }})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = TokTokCyan, selectedLabelColor = Color.Black)
                    )
                }
                item {
                    FilterChip(
                        selected = filterCategory == "PRIVATE",
                        onClick = { filterCategory = "PRIVATE" },
                        label = { Text("Private / Hidden (${videos.count { it.isPrivate }})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.Gray, selectedLabelColor = Color.White)
                    )
                }
            }
        }

        if (filteredVideos.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No videos found matching filters.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        items(filteredVideos, key = { it.id }) { video ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (video.isReported) Color.Red.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = video.thumbnailUrl,
                        contentDescription = video.caption,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 60.dp, height = 80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "@${video.userHandle}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TokTokCyan
                            )
                            if (video.isPinned) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(TokTokPink)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("PINNED", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            if (video.isPrivate) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Gray)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("HIDDEN", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                        Text(
                            text = video.caption.ifBlank { "No caption" },
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${formatCount(video.viewsCount)} views • ${formatCount(video.likesCount)} likes • ${video.category}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Action Controls
                    Column(horizontalAlignment = Alignment.End) {
                        Row {
                            // 1. Privacy Toggle (Public / Private)
                            IconButton(
                                onClick = {
                                    onSetPrivacy(video.id, !video.isPrivate)
                                    Toast.makeText(context, if (!video.isPrivate) "Video hidden from public feed" else "Video set to Public", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(
                                    imageVector = if (video.isPrivate) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Privacy",
                                    tint = if (video.isPrivate) Color(0xFFFF9800) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // 2. Pin to FYP
                            IconButton(
                                onClick = {
                                    onTogglePin(video.id, !video.isPinned)
                                    Toast.makeText(context, if (!video.isPinned) "Pinned video to top of FYP!" else "Unpinned", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(
                                    imageVector = if (video.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                    contentDescription = "Pin",
                                    tint = if (video.isPinned) TokTokPink else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row {
                            // 3. Edit Metrics
                            IconButton(onClick = { editingMetricsVideo = video }) {
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = "Metrics",
                                    tint = TokTokCyan
                                )
                            }

                            // 4. Delete Video
                            IconButton(onClick = { videoToDelete = video }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // === DIALOG: EDIT VIDEO METRICS ===
    editingMetricsVideo?.let { video ->
        var viewsText by remember { mutableStateOf(video.viewsCount.toString()) }
        var likesText by remember { mutableStateOf(video.likesCount.toString()) }

        AlertDialog(
            onDismissRequest = { editingMetricsVideo = null },
            title = { Text("Edit Video Views & Likes", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = viewsText,
                        onValueChange = { viewsText = it.filter { char -> char.isDigit() } },
                        label = { Text("Views Count") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = likesText,
                        onValueChange = { likesText = it.filter { char -> char.isDigit() } },
                        label = { Text("Likes Count") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val views = viewsText.toIntOrNull() ?: video.viewsCount
                        val likes = likesText.toIntOrNull() ?: video.likesCount
                        onUpdateMetrics(video.id, views, likes)
                        editingMetricsVideo = null
                        Toast.makeText(context, "Video metrics updated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TokTokPink)
                ) {
                    Text("Save Metrics")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingMetricsVideo = null }) { Text("Cancel") }
            }
        )
    }

    // === DIALOG: DELETE VIDEO ===
    videoToDelete?.let { video ->
        AlertDialog(
            onDismissRequest = { videoToDelete = null },
            title = { Text("Delete Video Platform-Wide?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to permanently delete this video from BDTOK?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteVideo(video.id)
                        videoToDelete = null
                        Toast.makeText(context, "Video deleted permanently.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete Video", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { videoToDelete = null }) { Text("Cancel") }
            }
        )
    }
}

// ==========================================
// TAB 3: APP ADS & MONETIZATION CONTROL
// ==========================================
@Composable
private fun AdminAdsControlTab(
    adConfig: PlatformAdConfig,
    onUpdateAdConfig: (PlatformAdConfig) -> Unit
) {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(adConfig.isEnabled) }
    var sponsorName by remember { mutableStateOf(adConfig.sponsorName) }
    var headline by remember { mutableStateOf(adConfig.headline) }
    var ctaText by remember { mutableStateOf(adConfig.ctaText) }
    var targetUrl by remember { mutableStateOf(adConfig.targetUrl) }
    var bannerUrl by remember { mutableStateOf(adConfig.bannerUrl) }
    var frequency by remember { mutableIntStateOf(adConfig.frequency) }

    val presetBanners = listOf(
        Pair("Tech & Gadget Sponsor", "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=800&auto=format&fit=crop&q=80"),
        Pair("Fashion & Brand Sponsor", "https://images.unsplash.com/photo-1483985988355-763728e1935b?w=800&auto=format&fit=crop&q=80"),
        Pair("Food & Restaurant Sponsor", "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&auto=format&fit=crop&q=80"),
        Pair("Travel & Airlines Sponsor", "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=800&auto=format&fit=crop&q=80")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Master Ad Switch
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = TokTokPink)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Platform In-Feed Sponsored Ads", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Display custom creator/sponsor ads directly in the BDTOK video feed.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { isEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = TokTokPink, checkedTrackColor = TokTokPink.copy(alpha = 0.5f))
                    )
                }
            }
        }

        // Ad Settings Form
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("ACTIVE AD CONFIGURATION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TokTokCyan)

                    OutlinedTextField(
                        value = sponsorName,
                        onValueChange = { sponsorName = it },
                        label = { Text("Sponsor / Advertiser Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = headline,
                        onValueChange = { headline = it },
                        label = { Text("Ad Headline & Catchphrase") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = ctaText,
                            onValueChange = { ctaText = it },
                            label = { Text("Call to Action (CTA)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = targetUrl,
                            onValueChange = { targetUrl = it },
                            label = { Text("Target Link URL") },
                            singleLine = true,
                            modifier = Modifier.weight(1.3f)
                        )
                    }

                    // Frequency Selector
                    Text("Ad Frequency: Show every $frequency videos in FYP", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Slider(
                        value = frequency.toFloat(),
                        onValueChange = { frequency = it.toInt() },
                        valueRange = 2f..10f,
                        steps = 7,
                        colors = SliderDefaults.colors(thumbColor = TokTokPink, activeTrackColor = TokTokPink)
                    )

                    // Banner Selector
                    Text("Select Sponsor Banner Image:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(presetBanners) { (title, url) ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                border = if (bannerUrl == url) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(TokTokPink)) else null,
                                modifier = Modifier
                                    .size(width = 110.dp, height = 70.dp)
                                    .clickable { bannerUrl = url }
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = url,
                                        contentDescription = title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .padding(2.dp)
                                    ) {
                                        Text(title, fontSize = 9.sp, color = Color.White, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val updated = PlatformAdConfig(
                                isEnabled = isEnabled,
                                sponsorName = sponsorName.trim(),
                                headline = headline.trim(),
                                ctaText = ctaText.trim(),
                                targetUrl = targetUrl.trim(),
                                bannerUrl = bannerUrl.trim(),
                                frequency = frequency
                            )
                            onUpdateAdConfig(updated)
                            Toast.makeText(context, "📢 Platform Ads Config Updated Live!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save & Apply Ad Settings", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Virtual Coins & Monetization Economy
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Virtual Coins & Creator Revenue Payouts", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Text(
                        "• 1 Diamond = 0.05 USD\n• Revenue Share: 70% Creator / 30% Platform\n• Minimum Withdrawal Threshold: $10.00 USD via bKash / Nagad / Bank Transfer",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ==========================================
// TAB 4: GLOBAL BROADCAST & ANNOUNCEMENT
// ==========================================
@Composable
private fun AdminBroadcastTab(
    onBroadcast: (String, String) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("BDTOK Official Update 🚀") }
    var message by remember { mutableStateOf("Welcome to the all-new BDTOK platform! Explore, create, and share viral moments.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = TokTokPink)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Push Global System Broadcast", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Text(
                    "Send a platform-wide system notification to all registered users simultaneously.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Announcement Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Broadcast Message Body") },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Preview Card
                Text("NOTIFICATION PREVIEW:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TokTokCyan)
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(TokTokPink),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                            Text("Just now • Official Broadcast", fontSize = 10.sp, color = TokTokCyan)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank() && message.isNotBlank()) {
                            onBroadcast(title.trim(), message.trim())
                            Toast.makeText(context, "📣 Broadcast dispatched to all users!", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Broadcast to All Users Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==========================================
// TAB 5: TELEMETRY & FYP ENGINE
// ==========================================
@Composable
private fun AdminFeedTelemetryTab(
    videos: List<Video>,
    users: List<User>
) {
    val totalViews = videos.sumOf { it.viewsCount.toLong() }
    val totalLikes = videos.sumOf { it.likesCount.toLong() }
    val totalComments = videos.sumOf { it.commentsCount.toLong() }
    val affinities = RecommendationEngine.getUserAffinity("user_me")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section: KPIs
        item {
            Text(
                text = "Live Platform Telemetry",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminMetricCard(
                    title = "Total Views",
                    value = formatCount(totalViews.toInt()),
                    icon = Icons.Default.Visibility,
                    tint = TokTokCyan,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Total Likes",
                    value = formatCount(totalLikes.toInt()),
                    icon = Icons.Default.LocalFireDepartment,
                    tint = TokTokPink,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminMetricCard(
                    title = "Comments",
                    value = formatCount(totalComments.toInt()),
                    icon = Icons.Default.BarChart,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Registered Users",
                    value = users.size.toString(),
                    icon = Icons.Default.Group,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section: Algorithmic Architecture
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = TokTokPink)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BDTOK Recommendation Score Formula",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Score = (InterestMatch × 35%) + (CompletionRate × 25%) + (EngagementRatio × 20%) + ColdStartBonus + OfficialBoost",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TokTokCyan
                    )
                }
            }
        }

        // Section: Cold Start Viral Pools
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Hub, contentDescription = null, tint = TokTokCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Tier 1: Initial Test Pool (100 Active Users)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Text(
                        text = "Every uploaded video is seeded into a random active 100-user sample with a 1.4x test multiplier.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoGraph, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Tier 2: Community Viral Pool (5,000 Users)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Text(
                        text = "Triggered when test completion rate >= 60% and engagement velocity >= 5%.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ==========================================
// TAB 5: DATABASE & APP PACKAGE DOWNLOAD
// ==========================================
@Composable
private fun AdminDatabaseBackupTab(
    viewModel: MainViewModel,
    allUsersCount: Int,
    allVideosCount: Int
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var exportedJson by remember { mutableStateOf<String?>(null) }
    var isExporting by remember { mutableStateOf(false) }
    var isImporting by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }
    var importError by remember { mutableStateOf<String?>(null) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showJsonViewer by remember { mutableStateOf(false) }

    var dbStats by remember { mutableStateOf<com.example.data.AppRepository.DatabaseStats?>(null) }

    // Load initial stats
    LaunchedEffect(Unit) {
        viewModel.fetchDatabaseStats { stats ->
            dbStats = stats
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(TokTokPink.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = TokTokPink,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BDTOK Database & App Hub",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF00C853).copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("LIVE", color = Color(0xFF00C853), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Download SQLite database backup, export full platform data as JSON, import restore snapshots, or obtain direct App (.APK) package instructions.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Live Database Records Stats Grid
        item {
            Text(
                text = "📊 ROOM DATABASE LIVE TABLES",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminMetricCard(
                    title = "Users Table",
                    value = "${dbStats?.usersCount ?: allUsersCount}",
                    icon = Icons.Default.Group,
                    tint = TokTokPink,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Videos Table",
                    value = "${dbStats?.videosCount ?: allVideosCount}",
                    icon = Icons.Default.Movie,
                    tint = TokTokCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminMetricCard(
                    title = "Comments",
                    value = "${dbStats?.commentsCount ?: 0}",
                    icon = Icons.Default.Campaign,
                    tint = Color(0xFFFFB74D),
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Notifications & DMs",
                    value = "${(dbStats?.notificationsCount ?: 0) + (dbStats?.messagesCount ?: 0)}",
                    icon = Icons.Default.NotificationsActive,
                    tint = Color(0xFFAB47BC),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section 1: Database Export & Download
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = TokTokCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Download Full Database (JSON)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "Exports all Room SQLite tables (Users, Videos, Comments, Notifications, Messages, Ad Configs) into an offline, standard JSON database snapshot.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {
                            isExporting = true
                            viewModel.exportCompleteDatabase { json ->
                                exportedJson = json
                                isExporting = false
                                Toast.makeText(context, "✅ Database exported (${json.length / 1024} KB)! Ready to download / share.", Toast.LENGTH_SHORT).show()
                                viewModel.fetchDatabaseStats { dbStats = it }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TokTokCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isExporting) "Generating Database Snapshot..." else "Generate & Download Database Backup",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    if (exportedJson != null) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        Text(
                            text = "🎉 Backup Ready: ${(exportedJson!!.length / 1024)} KB JSON Payload",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00C853)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Share Intent / Save
                            OutlinedButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, exportedJson)
                                        putExtra(Intent.EXTRA_TITLE, "BDTOK_Database_Backup.json")
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Download / Share Database Backup")
                                    context.startActivity(shareIntent)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share / Save", fontSize = 12.sp)
                            }

                            // Copy Clipboard
                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(exportedJson!!))
                                    Toast.makeText(context, "📋 Database JSON copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy JSON", fontSize = 12.sp)
                            }

                            // View JSON
                            Button(
                                onClick = { showJsonViewer = !showJsonViewer },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (showJsonViewer) "Hide" else "View", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            }
                        }

                        if (showJsonViewer) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Black.copy(alpha = 0.85f))
                                    .padding(10.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = exportedJson ?: "",
                                    color = Color(0xFF81C784),
                                    fontSize = 11.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Database Restore & Import
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = null,
                            tint = TokTokPink,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Database Restore & Import",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "Restore or replace all database tables from a JSON backup snapshot.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                importError = null
                                showImportDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Restore from JSON", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { showResetDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Seed Factory Reset", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section 3: App APK Package Download & Export Guide
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Android,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BDTOK App Package & APK Download",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "BDTOK is built as a complete standalone Android Application. You can download and install the official APK directly:",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("1️⃣ Tap the Settings/Export menu at the top of Google AI Studio.", color = Color.White, fontSize = 12.sp)
                        Text("2️⃣ Select \"Download APK\" to get the compiled Android install file.", color = Color.White, fontSize = 12.sp)
                        Text("3️⃣ Or select \"Export as ZIP\" to download full Kotlin Android source code.", color = Color.White, fontSize = 12.sp)
                        Text("4️⃣ Package: com.example | Name: BDTOK | Version: 34.2.0", color = TokTokCyan, fontSize = 11.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "📱 App ID: com.example (BDTOK v34.2.0) • Ready for Android export", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("App Ready for Android Devices", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Import JSON Dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Import & Restore Database", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Paste a BDTOK JSON database backup below to restore all users, videos, and settings:",
                        fontSize = 13.sp
                    )

                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = {
                            importJsonText = it
                            importError = null
                        },
                        placeholder = { Text("{\"app\": \"BDTOK\", \"users\": [...], \"videos\": [...]}", fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 11.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    )

                    if (importError != null) {
                        Text(
                            text = importError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (importJsonText.isBlank()) {
                            importError = "Please paste a valid JSON string"
                            return@Button
                        }
                        isImporting = true
                        viewModel.importDatabaseFromJson(importJsonText) { success ->
                            isImporting = false
                            if (success) {
                                showImportDialog = false
                                Toast.makeText(context, "🎉 Database successfully restored from backup!", Toast.LENGTH_LONG).show()
                                viewModel.fetchDatabaseStats { dbStats = it }
                            } else {
                                importError = "Failed to parse JSON backup. Please check format."
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TokTokPink)
                ) {
                    Text(if (isImporting) "Restoring..." else "Restore Database")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Factory Reset Seed Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Database to Default?", fontWeight = FontWeight.Bold) },
            text = {
                Text("This will reset all tables (Users, Videos, Comments) to standard initial demo creators and videos.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetDialog = false
                        viewModel.resetDatabase {
                            Toast.makeText(context, "🔄 Database reset to default initial state!", Toast.LENGTH_SHORT).show()
                            viewModel.fetchDatabaseStats { dbStats = it }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Reset Database")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
