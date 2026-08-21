package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Storage
import android.content.Intent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.MediaStorageHelper
import com.example.model.User
import com.example.ui.MainViewModel
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.TokTokCyan
import com.example.ui.theme.TokTokPink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onBack: () -> Unit,
    onSwitchAccount: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentUser by viewModel.currentUser.collectAsState()

    // Sheet states for all TikTok Settings
    var showAccountDetailsSheet by remember { mutableStateOf(false) }
    var showDownloadDataSheet by remember { mutableStateOf(false) }
    var showPrivacySheet by remember { mutableStateOf(false) }
    var showSecuritySheet by remember { mutableStateOf(false) }
    var showQrCodeSheet by remember { mutableStateOf(false) }
    var showShareProfileSheet by remember { mutableStateOf(false) }
    var showNidVerificationSheet by remember { mutableStateOf(false) }
    var showAvatarPickerSheet by remember { mutableStateOf(false) }

    // Creator & Monetization
    var showCreatorToolsSheet by remember { mutableStateOf(false) }
    var showBalanceSheet by remember { mutableStateOf(false) }
    var showLiveStudioSheet by remember { mutableStateOf(false) }

    // Content & Display
    var showContentPreferencesSheet by remember { mutableStateOf(false) }
    var showPlaybackSheet by remember { mutableStateOf(false) }
    var showDisplaySheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showScreenTimeSheet by remember { mutableStateOf(false) }
    var showFamilyPairingSheet by remember { mutableStateOf(false) }
    var showAccessibilitySheet by remember { mutableStateOf(false) }

    // Cache & Cellular
    var showFreeUpSpaceSheet by remember { mutableStateOf(false) }
    var showDataSaverSheet by remember { mutableStateOf(false) }
    var showOfflineVideosSheet by remember { mutableStateOf(false) }

    // Support & About
    var showReportProblemSheet by remember { mutableStateOf(false) }
    var showSupportInboxSheet by remember { mutableStateOf(false) }
    var showCommunityGuidelinesSheet by remember { mutableStateOf(false) }
    var showTermsPolicySheet by remember { mutableStateOf(false) }
    var showVersionDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Settings and privacy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showReportProblemSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search settings",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f), thickness = 0.5.dp)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // User Mini Profile Card
            item {
                if (currentUser != null) {
                    val user = currentUser!!
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                AsyncImage(
                                    model = user.avatarUrl,
                                    contentDescription = user.displayName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, TokTokPink, CircleShape)
                                        .clickable { showAvatarPickerSheet = true }
                                )
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(TokTokPink)
                                        .clickable { showAvatarPickerSheet = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Change Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = user.displayName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (user.isVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        VerifiedBadge(size = 16.dp)
                                    }
                                }
                                Text(
                                    text = "@${user.username}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                if (user.isVerified) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = TokTokCyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Verified ${user.verifiedCategory} ✅",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TokTokCyan
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Unverified Creator",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Edit Photo button
                            OutlinedButton(
                                onClick = { showAvatarPickerSheet = true },
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Edit Photo", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // ==================== 1. ACCOUNT ====================
            item { SettingsSectionHeader(title = "ACCOUNT") }

            // Account Details (Phone, Email, Password, Switch to Business, Download Data)
            item {
                SettingsTile(
                    icon = Icons.Default.Person,
                    iconTint = Color(0xFF42A5F5),
                    title = "Account",
                    subtitle = "User information, Email, Phone, Password",
                    onClick = { showAccountDetailsSheet = true }
                )
            }

            // Download your data
            item {
                SettingsTile(
                    icon = Icons.Default.Download,
                    iconTint = Color(0xFF26A69A),
                    title = "Download your data",
                    subtitle = "Get a copy of your BDTOK profile, videos, messages and activity data",
                    onClick = { showDownloadDataSheet = true }
                )
            }

            // Privacy
            item {
                SettingsTile(
                    icon = Icons.Default.Lock,
                    iconTint = Color(0xFFFFB74D),
                    title = "Privacy",
                    subtitle = "Private account, Activity status, Comments, DMs, Duet, Downloads",
                    onClick = { showPrivacySheet = true }
                )
            }

            // Security & Permissions
            item {
                SettingsTile(
                    icon = Icons.Default.Lock,
                    iconTint = Color(0xFF66BB6A),
                    title = "Security & permissions",
                    subtitle = "2-step verification, Security alerts, Manage devices",
                    onClick = { showSecuritySheet = true }
                )
            }

            // Share Profile
            item {
                SettingsTile(
                    icon = Icons.Default.Share,
                    iconTint = TokTokPink,
                    title = "Share profile",
                    subtitle = "Quick share to WhatsApp, Instagram, Messenger, Copy link",
                    onClick = { showShareProfileSheet = true }
                )
            }

            // QR Code
            item {
                SettingsTile(
                    icon = Icons.Default.Star,
                    iconTint = TokTokCyan,
                    title = "QR code",
                    subtitle = "Share your TokTok profile QR code or scan others",
                    onClick = { showQrCodeSheet = true }
                )
            }

            // Verification & Blue Tick ✅
            item {
                val isVerified = currentUser?.isVerified == true
                SettingsTile(
                    icon = Icons.Default.CheckCircle,
                    iconTint = if (isVerified) TokTokCyan else TokTokPink,
                    title = "Request verification & Blue Tick ✅",
                    subtitle = if (isVerified) "Verified Official Badge Active ✓" else "Verify National ID (NID Card) for Blue Checkmark",
                    onClick = { showNidVerificationSheet = true }
                )
            }

            // ==================== 2. CREATOR & MONETIZATION ====================
            item { SettingsSectionHeader(title = "CREATOR & MONETIZATION") }

            // Creator Tools & Analytics
            item {
                SettingsTile(
                    icon = Icons.Default.PlayArrow,
                    iconTint = TokTokPink,
                    title = "Creator tools & TikTok Studio",
                    subtitle = "Video analytics, Creator Rewards Program, Engagement metrics",
                    onClick = { showCreatorToolsSheet = true }
                )
            }

            // Balance / Wallet
            item {
                SettingsTile(
                    icon = Icons.Default.Star,
                    iconTint = Color(0xFFFFD54F),
                    title = "Balance",
                    subtitle = "Coins: 1,250 • LIVE Gifts: $48.50 USD • Recharge & Withdraw",
                    onClick = { showBalanceSheet = true }
                )
            }

            // LIVE & Subscriptions
            item {
                SettingsTile(
                    icon = Icons.Default.Notifications,
                    iconTint = Color(0xFFAB47BC),
                    title = "LIVE & Subscriptions",
                    subtitle = "LIVE studio setup, Virtual gifts settings, Moderator controls",
                    onClick = { showLiveStudioSheet = true }
                )
            }

            // ==================== 3. CONTENT & DISPLAY ====================
            item { SettingsSectionHeader(title = "CONTENT & DISPLAY") }

            // Content Preferences
            item {
                SettingsTile(
                    icon = Icons.Default.PlayArrow,
                    iconTint = Color(0xFF26A69A),
                    title = "Content preferences",
                    subtitle = "Filter keywords, Restricted mode, Refresh your For You feed (FYP)",
                    onClick = { showContentPreferencesSheet = true }
                )
            }

            // Playback
            item {
                SettingsTile(
                    icon = Icons.Default.PlayArrow,
                    iconTint = Color(0xFF5C6BC0),
                    title = "Playback",
                    subtitle = "Auto volume adjustment, Open on mute, Auto scroll, HD streaming",
                    onClick = { showPlaybackSheet = true }
                )
            }

            // Display & Theme
            item {
                SettingsTile(
                    icon = if (isDarkTheme) Icons.Default.Star else Icons.Default.Star,
                    iconTint = if (isDarkTheme) Color(0xFFBA68C8) else Color(0xFFFFCA28),
                    title = "Display",
                    subtitle = if (isDarkTheme) "Dark mode (On) • Text size: Standard" else "Light mode (On) • Text size: Standard",
                    onClick = { showDisplaySheet = true }
                )
            }

            // App Language
            item {
                SettingsTile(
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF42A5F5),
                    title = "Language",
                    subtitle = "App language: English (US) • Auto-translate comments",
                    onClick = { showLanguageSheet = true }
                )
            }

            // Screen Time
            item {
                SettingsTile(
                    icon = Icons.Default.Star,
                    iconTint = Color(0xFFFF7043),
                    title = "Screen time & Digital Wellbeing",
                    subtitle = "Daily limit: 2 hours • Screen breaks • Sleep bedtime reminders",
                    onClick = { showScreenTimeSheet = true }
                )
            }

            // Family Pairing
            item {
                SettingsTile(
                    icon = Icons.Default.Person,
                    iconTint = Color(0xFF7E57C2),
                    title = "Family Pairing",
                    subtitle = "Link parent & teen accounts to customize safety settings",
                    onClick = { showFamilyPairingSheet = true }
                )
            }

            // Accessibility
            item {
                SettingsTile(
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF26C6DA),
                    title = "Accessibility",
                    subtitle = "Photosensitivity warnings, Captions, High contrast text",
                    onClick = { showAccessibilitySheet = true }
                )
            }

            // ==================== 4. CACHE & CELLULAR ====================
            item { SettingsSectionHeader(title = "CACHE & CELLULAR") }

            // Free up space
            item {
                SettingsTile(
                    icon = Icons.Default.Delete,
                    iconTint = TokTokCyan,
                    title = "Free up space",
                    subtitle = "Clear cache (24.8 MB), downloads, and draft files",
                    onClick = { showFreeUpSpaceSheet = true }
                )
            }

            // Data Saver
            item {
                SettingsTile(
                    icon = Icons.Default.Refresh,
                    iconTint = Color(0xFF66BB6A),
                    title = "Data Saver",
                    subtitle = "Reduce cellular mobile data consumption when watching videos",
                    onClick = { showDataSaverSheet = true }
                )
            }

            // Offline Videos
            item {
                SettingsTile(
                    icon = Icons.Default.PlayArrow,
                    iconTint = Color(0xFF8D6E63),
                    title = "Offline videos",
                    subtitle = "Download 50–200 videos to watch anywhere without internet",
                    onClick = { showOfflineVideosSheet = true }
                )
            }

            // ==================== 5. SUPPORT & ABOUT ====================
            item { SettingsSectionHeader(title = "SUPPORT & ABOUT") }

            // Report a problem
            item {
                SettingsTile(
                    icon = Icons.Default.Warning,
                    iconTint = Color(0xFFFFB300),
                    title = "Report a problem",
                    subtitle = "Help Center, FAQs, Feedback on Feed, Video, Direct Messages",
                    onClick = { showReportProblemSheet = true }
                )
            }

            // Support Inbox
            item {
                SettingsTile(
                    icon = Icons.Default.Email,
                    iconTint = Color(0xFF42A5F5),
                    title = "Support inbox",
                    subtitle = "Account status: Good Standing 🛡️ • View open reports",
                    onClick = { showSupportInboxSheet = true }
                )
            }

            // Community Guidelines
            item {
                SettingsTile(
                    icon = Icons.Default.CheckCircle,
                    iconTint = TokTokCyan,
                    title = "Community Guidelines",
                    subtitle = "Rules for safe, authentic, and respectful content",
                    onClick = { showCommunityGuidelinesSheet = true }
                )
            }

            // Terms & Privacy
            item {
                SettingsTile(
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF90A4AE),
                    title = "Terms of Service & Privacy Policy",
                    subtitle = "TokTok Security & Encryption Standards",
                    onClick = { showTermsPolicySheet = true }
                )
            }

            // Version info
            item {
                SettingsTile(
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFFB0BEC5),
                    title = "TokTok Version",
                    subtitle = "v34.2.0 (Official 2026 Android Production Release)",
                    onClick = { showVersionDialog = true }
                )
            }

            // ==================== 6. LOGIN ====================
            item { SettingsSectionHeader(title = "LOGIN") }

            // Switch account
            item {
                SettingsTile(
                    icon = Icons.Default.AccountCircle,
                    iconTint = TokTokPink,
                    title = "Switch account",
                    subtitle = "Manage & switch between multiple creator profiles",
                    onClick = onSwitchAccount
                )
            }

            // Log out
            item {
                SettingsTile(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    iconTint = Color(0xFFE53935),
                    title = "Log out",
                    subtitle = "Log out of @${currentUser?.username ?: "account"}",
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    }

    // ==========================================
    // MODAL BOTTOM SHEETS & DIALOGS
    // ==========================================

    // 1. Account Details Sheet
    if (showAccountDetailsSheet && currentUser != null) {
        AccountDetailsSheet(
            user = currentUser!!,
            onDismiss = { showAccountDetailsSheet = false },
            onUpdateUser = { name, handle, bio ->
                viewModel.updateProfile(name, handle, bio, currentUser!!.avatarUrl)
                showAccountDetailsSheet = false
                Toast.makeText(context, "Account details saved!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 1b. Download Data Sheet
    if (showDownloadDataSheet && currentUser != null) {
        DownloadDataSheet(
            user = currentUser!!,
            viewModel = viewModel,
            onDismiss = { showDownloadDataSheet = false }
        )
    }

    // 2. Privacy Hub Sheet
    if (showPrivacySheet && currentUser != null) {
        PrivacySettingsSheet(
            user = currentUser!!,
            onDismiss = { showPrivacySheet = false },
            onSavePrivacy = { isPrivate, dms, allowDownloads, duet, stitch, filterComments ->
                viewModel.updateTikTokPrivacySettings(isPrivate, dms, allowDownloads, duet, stitch, filterComments)
                showPrivacySheet = false
                Toast.makeText(context, "Privacy settings updated!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 3. Security Sheet
    if (showSecuritySheet) {
        SecuritySettingsSheet(
            user = currentUser,
            onDismiss = { showSecuritySheet = false }
        )
    }

    // 4. QR Code Sheet
    if (showQrCodeSheet && currentUser != null) {
        QrCodeSheet(
            user = currentUser!!,
            onDismiss = { showQrCodeSheet = false }
        )
    }

    // 5. Share Profile Sheet
    if (showShareProfileSheet && currentUser != null) {
        ShareProfileSheet(
            user = currentUser!!,
            onDismiss = { showShareProfileSheet = false }
        )
    }

    // 6. NID Verification & Blue Tick Sheet
    if (showNidVerificationSheet && currentUser != null) {
        NidVerificationSheet(
            user = currentUser!!,
            onDismiss = { showNidVerificationSheet = false },
            onSubmitVerification = { realName, nidNumber, category, frontUri, backUri ->
                viewModel.submitNidVerification(realName, nidNumber, category, frontUri, backUri, autoApprove = true)
                showNidVerificationSheet = false
                Toast.makeText(context, "🎉 Account Verified! Blue Tick ✅ has been activated!", Toast.LENGTH_LONG).show()
            },
            onToggleBadgeDirect = { isVerified ->
                viewModel.setVerifiedBadge(currentUser!!.id, isVerified)
                Toast.makeText(context, if (isVerified) "Blue Tick Activated ✅" else "Blue Tick Deactivated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 7. Avatar Picker Sheet
    if (showAvatarPickerSheet && currentUser != null) {
        AvatarPickerSheet(
            currentUser = currentUser!!,
            onDismiss = { showAvatarPickerSheet = false },
            onAvatarSelected = { newUrl ->
                viewModel.updateAvatar(newUrl)
                showAvatarPickerSheet = false
                Toast.makeText(context, "Profile photo updated successfully! ✨", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 8. Creator Tools Sheet
    if (showCreatorToolsSheet) {
        CreatorToolsSheet(onDismiss = { showCreatorToolsSheet = false })
    }

    // 9. Balance & Wallet Sheet
    if (showBalanceSheet) {
        BalanceWalletSheet(onDismiss = { showBalanceSheet = false })
    }

    // 10. LIVE Studio Sheet
    if (showLiveStudioSheet) {
        LiveStudioSheet(onDismiss = { showLiveStudioSheet = false })
    }

    // 11. Content Preferences Sheet
    if (showContentPreferencesSheet) {
        ContentPreferencesSheet(
            categories = viewModel.videoCategories,
            onDismiss = { showContentPreferencesSheet = false },
            onResetAlgorithm = {
                Toast.makeText(context, "For You Page (FYP) algorithm reset to default! 🔄", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 12. Playback Sheet
    if (showPlaybackSheet) {
        PlaybackSettingsSheet(onDismiss = { showPlaybackSheet = false })
    }

    // 13. Display Sheet
    if (showDisplaySheet) {
        DisplaySettingsSheet(
            isDarkTheme = isDarkTheme,
            onToggleDarkTheme = onToggleDarkTheme,
            onDismiss = { showDisplaySheet = false }
        )
    }

    // 14. Language Sheet
    if (showLanguageSheet) {
        LanguageSettingsSheet(onDismiss = { showLanguageSheet = false })
    }

    // 15. Screen Time Sheet
    if (showScreenTimeSheet) {
        ScreenTimeSheet(onDismiss = { showScreenTimeSheet = false })
    }

    // 16. Family Pairing Sheet
    if (showFamilyPairingSheet) {
        FamilyPairingSheet(onDismiss = { showFamilyPairingSheet = false })
    }

    // 17. Accessibility Sheet
    if (showAccessibilitySheet) {
        AccessibilitySettingsSheet(onDismiss = { showAccessibilitySheet = false })
    }

    // 18. Free Up Space Sheet
    if (showFreeUpSpaceSheet) {
        FreeUpSpaceSheet(onDismiss = { showFreeUpSpaceSheet = false })
    }

    // 19. Data Saver Sheet
    if (showDataSaverSheet) {
        DataSaverSheet(onDismiss = { showDataSaverSheet = false })
    }

    // 20. Offline Videos Sheet
    if (showOfflineVideosSheet) {
        OfflineVideosSheet(onDismiss = { showOfflineVideosSheet = false })
    }

    // 21. Report a Problem Sheet
    if (showReportProblemSheet) {
        ReportProblemSheet(onDismiss = { showReportProblemSheet = false })
    }

    // 22. Support Inbox Sheet
    if (showSupportInboxSheet) {
        SupportInboxSheet(onDismiss = { showSupportInboxSheet = false })
    }

    // 23. Community Guidelines Sheet
    if (showCommunityGuidelinesSheet) {
        CommunityGuidelinesSheet(onDismiss = { showCommunityGuidelinesSheet = false })
    }

    // 24. Terms & Policy Sheet
    if (showTermsPolicySheet) {
        TermsPolicySheet(onDismiss = { showTermsPolicySheet = false })
    }

    // 25. Version Dialog
    if (showVersionDialog) {
        AlertDialog(
            onDismissRequest = { showVersionDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = TokTokPink)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("TokTok Version", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("TokTok Android Studio Edition", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Build Version: 34.2.0 (Release 2026.08)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Platform: Jetpack Compose Material 3 & Room Local DB Engine", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Your app is currently up to date!", color = TokTokCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showVersionDialog = false
                        Toast.makeText(context, "Checked for updates: You are on the latest version!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TokTokPink)
                ) {
                    Text("Check for Updates")
                }
            },
            dismissButton = {
                TextButton(onClick = { showVersionDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // 26. Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out of TokTok?", fontWeight = FontWeight.Bold) },
            text = { Text("You can always log back in or switch to another account at any time.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onSwitchAccount()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Log out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// =======================================================
// REUSABLE SETTINGS TILES & HEADERS
// =======================================================

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
        modifier = Modifier.padding(start = 16.dp, top = 22.dp, bottom = 6.dp)
    )
}

@Composable
fun SettingsTile(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(14.dp)
        )
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f),
        modifier = Modifier.padding(start = 68.dp)
    )
}

// =======================================================
// SUB-SHEETS IMPLEMENTATIONS (ALL TIKTOK SETTINGS)
// =======================================================

/**
 * 1. Account Details Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsSheet(
    user: User,
    onDismiss: () -> Unit,
    onUpdateUser: (String, String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var displayName by remember { mutableStateOf(user.displayName) }
    var username by remember { mutableStateOf(user.username) }
    var bio by remember { mutableStateOf(user.bio) }
    var email by remember { mutableStateOf(if (user.email.isNotBlank()) user.email else "user@toktok.app") }
    var phone by remember { mutableStateOf("+1 (555) 839-2041") }
    var isBusinessAccount by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Account Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                prefix = { Text("@") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                maxLines = 3,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Switch to Business Account", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Get access to marketing tools, performance analytics and commercial music", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = isBusinessAccount,
                    onCheckedChange = { isBusinessAccount = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = { onUpdateUser(displayName, username, bio) },
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

/**
 * 2. Privacy Hub Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsSheet(
    user: User,
    onDismiss: () -> Unit,
    onSavePrivacy: (Boolean, String, Boolean, Boolean, Boolean, Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isPrivate by remember { mutableStateOf(user.isPrivateAccount) }
    var allowDMs by remember { mutableStateOf(user.allowDirectMessages) }
    var allowDownloads by remember { mutableStateOf(user.allowDownloads) }
    var allowDuet by remember { mutableStateOf(user.allowDuet) }
    var allowStitch by remember { mutableStateOf(user.allowStitch) }
    var filterComments by remember { mutableStateOf(user.filterComments) }
    var activityStatus by remember { mutableStateOf(true) }
    var suggestToContacts by remember { mutableStateOf(true) }
    var postViewHistory by remember { mutableStateOf(true) }
    var profileViewHistory by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Privacy & Safety",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Private Account Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Private Account", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Only approved followers can view your videos and photos", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = isPrivate,
                    onCheckedChange = { isPrivate = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Activity Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Activity Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Show when you and mutual friends are active on TokTok", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = activityStatus,
                    onCheckedChange = { activityStatus = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Suggest to Contacts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Suggest your account to others", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Allow friends from phone contacts and mutuals to discover your profile", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = suggestToContacts,
                    onCheckedChange = { suggestToContacts = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Direct Messages Permissions
            Column {
                Text("Who Can Send You Direct Messages", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 6.dp)) {
                    listOf("Everyone", "Friends", "No one").forEach { option ->
                        val isSelected = allowDMs == option
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) TokTokPink else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { allowDMs = option }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = option,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Video Downloads
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Allow Video Downloads", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Allow other users to download and share your videos", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = allowDownloads,
                    onCheckedChange = { allowDownloads = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Duet and Stitch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Allow Duet & Stitch", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Allow creators to react and remix your content", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = allowDuet && allowStitch,
                    onCheckedChange = {
                        allowDuet = it
                        allowStitch = it
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Profile View History
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Profile Views History (30 Days)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("See who viewed your profile in the past 30 days and allow others to see when you view theirs", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = profileViewHistory,
                    onCheckedChange = { profileViewHistory = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Filter Comments
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Filter Offensive Comments", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Automatically hide spam, offensive keywords and unverified accounts", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = filterComments,
                    onCheckedChange = { filterComments = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = { onSavePrivacy(isPrivate, allowDMs, allowDownloads, allowDuet, allowStitch, filterComments) },
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Save Privacy Settings", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

/**
 * 3. Security Settings Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecuritySettingsSheet(
    user: User?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var twoFactorAuth by remember { mutableStateOf(true) }
    var incomeLock by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF66BB6A).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF66BB6A))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Security & Permissions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Account Safety Status: Secure 🛡️", color = Color(0xFF66BB6A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF66BB6A).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF66BB6A), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("No Security Alerts", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Your account has had zero suspicious login attempts in the last 30 days.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // 2-Step Verification
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("2-Step Verification", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Verify with SMS / Email Code or Authenticator App on new logins", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = twoFactorAuth,
                    onCheckedChange = { twoFactorAuth = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Manage Devices
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { Toast.makeText(context, "Currently logged in on Android device.", Toast.LENGTH_SHORT).show() },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Your Devices (1 Active)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Android Phone • Active Now • This device", fontSize = 12.sp, color = TokTokCyan)
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Income Security Lock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Creator Payout PIN Security", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Require 6-digit PIN before withdrawing creator fund rewards", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = incomeLock,
                    onCheckedChange = { incomeLock = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 4. Stylized QR Code Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeSheet(user: User, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("TokTok QR Code", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Stylized QR Card
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1F1F2E),
                                Color(0xFF12121A)
                            )
                        )
                    )
                    .border(2.dp, TokTokPink, RoundedCornerShape(24.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, TokTokCyan, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(user.displayName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        if (user.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            VerifiedBadge(size = 14.dp)
                        }
                    }
                    Text("@${user.username}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // QR representation
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Box(modifier = Modifier.size(36.dp).background(Color.Black))
                                Box(modifier = Modifier.size(36.dp).background(Color.Black))
                            }
                            Text("toktok.app/@${user.username}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Box(modifier = Modifier.size(36.dp).background(Color.Black))
                                Box(modifier = Modifier.size(36.dp).background(Color.Black))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Scan with TokTok camera to follow", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { Toast.makeText(context, "QR code image saved to gallery! 📸", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Image", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = {
                        Toast.makeText(context, "Link copied: toktok.app/@${user.username}", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Copy Link", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * 5. Share Profile Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareProfileSheet(user: User, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val shareTargets = listOf(
        "WhatsApp" to Color(0xFF25D366),
        "Instagram" to Color(0xFFE1306C),
        "Messages" to Color(0xFF007AFF),
        "Messenger" to Color(0xFF0084FF),
        "Copy Link" to TokTokCyan,
        "SMS" to Color(0xFFFFA000)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Share Profile", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(shareTargets) { target ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Shared to ${target.first}! 🚀", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(target.second),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(target.first, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

/**
 * 6. Account Verification & Blue Tick ✅ Modal with NID Card Upload
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NidVerificationSheet(
    user: User,
    onDismiss: () -> Unit,
    onSubmitVerification: (String, String, String, String, String) -> Unit,
    onToggleBadgeDirect: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    var realName by remember { mutableStateOf(user.realName ?: user.displayName) }
    var nidNumber by remember { mutableStateOf(user.nidNumber ?: "NID-8849201938") }
    var selectedCategory by remember { mutableStateOf(user.verifiedCategory) }
    var nidFrontUri by remember { mutableStateOf(user.nidFrontUri ?: "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=600&auto=format&fit=crop&q=80") }
    var nidBackUri by remember { mutableStateOf(user.nidBackUri ?: "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=600&auto=format&fit=crop&q=80") }
    var isSubmitting by remember { mutableStateOf(false) }

    val frontLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            nidFrontUri = MediaStorageHelper.saveImageToInternalStorage(context, uri, "nid_front")
        }
    }

    val backLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            nidBackUri = MediaStorageHelper.saveImageToInternalStorage(context, uri, "nid_back")
        }
    }

    val categories = listOf(
        "Content Creator",
        "Public Figure",
        "Brand & Business",
        "Gaming & Esports",
        "Journalist & Media",
        "Organization"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TokTokCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = TokTokCyan, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Apply for Verification", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        VerifiedBadge(size = 18.dp)
                    }
                    Text("Get the verified Blue Tick badge on your TokTok profile", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (user.isVerified) TokTokCyan.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (user.isVerified) "Status: Verified Creator ✅" else "Status: Unverified Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (user.isVerified) TokTokCyan else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (user.isVerified) "Blue Tick is active on all your videos" else "Submit your National ID (NID) to verify",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedButton(
                        onClick = { onToggleBadgeDirect(!user.isVerified) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (user.isVerified) "Remove" else "Instant Verify", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            OutlinedTextField(
                value = realName,
                onValueChange = { realName = it },
                label = { Text("Legal Full Name (as shown on NID Card)") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nidNumber,
                onValueChange = { nidNumber = it },
                label = { Text("National ID (NID) / Passport Number") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Text("Select Account Category", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) TokTokCyan else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Text("Upload National ID Card (NID) Documents", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("NID Front Side", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(105.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, TokTokCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .clickable { frontLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(model = nidFrontUri, contentDescription = "NID Front", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Change Front Photo", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("NID Back Side", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(105.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, TokTokCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .clickable { backLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(model = nidBackUri, contentDescription = "NID Back", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Change Back Photo", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    isSubmitting = true
                    onSubmitVerification(realName, nidNumber, selectedCategory, nidFrontUri, nidBackUri)
                },
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit NID & Activate Blue Tick ✅", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

/**
 * 7. Avatar Picker Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarPickerSheet(
    currentUser: User,
    onDismiss: () -> Unit,
    onAvatarSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val savedLocalUri = MediaStorageHelper.saveImageToInternalStorage(context, uri, "profile_avatar")
            onAvatarSelected(savedLocalUri)
        }
    }

    val avatarPresets = listOf(
        "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=400&auto=format&fit=crop&q=80"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Change Profile Photo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            AsyncImage(
                model = currentUser.avatarUrl,
                contentDescription = "Current Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .border(2.5.dp, TokTokPink, CircleShape)
            )

            Button(
                onClick = { photoPickerLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pick Photo from Gallery", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            Text("Or Choose a Creator Avatar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(avatarPresets) { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Preset avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (currentUser.avatarUrl == url) 2.5.dp else 1.dp,
                                color = if (currentUser.avatarUrl == url) TokTokPink else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .clickable { onAvatarSelected(url) }
                    )
                }
            }
        }
    }
}

/**
 * 8. Creator Tools & TikTok Studio Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorToolsSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("TikTok Studio & Creator Tools", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Analytics Overview Cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Video Views (28d)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("184.2K", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TokTokCyan)
                        Text("+24.8% vs last month", fontSize = 10.sp, color = Color(0xFF4CAF50))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Profile Views", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("14.9K", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TokTokPink)
                        Text("+12.3% engagement", fontSize = 10.sp, color = Color(0xFF4CAF50))
                    }
                }
            }

            // Creator Rewards Program Card
            Card(
                colors = CardDefaults.cardColors(containerColor = TokTokPink.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = TokTokPink, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Creator Rewards Program", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Monetize high quality 1min+ original videos. Current balance: $128.40 USD", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Close Studio", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 9. Balance & Virtual Coins Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceWalletSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val coinPackages = listOf(
        "70 Coins" to "$0.99",
        "350 Coins" to "$4.99",
        "700 Coins" to "$9.99",
        "1,400 Coins" to "$19.99"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Balance & Wallet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Balance Cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD54F).copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Coins Balance", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("🪙 1,250", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF66BB6A).copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("LIVE Gifts Revenue", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$48.50 USD", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF66BB6A))
                    }
                }
            }

            Text("Recharge Coins (Virtual Gifting)", fontWeight = FontWeight.Bold, fontSize = 14.sp)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(coinPackages) { pkg ->
                    Card(
                        modifier = Modifier
                            .width(110.dp)
                            .clickable {
                                Toast.makeText(context, "Recharged ${pkg.first}! 🪙", Toast.LENGTH_SHORT).show()
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🪙", fontSize = 20.sp)
                            Text(pkg.first, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(pkg.second, color = TokTokPink, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Withdraw request for $48.50 USD submitted to PayPal/Bank!", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Withdraw Revenue ($48.50 USD)", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/**
 * 10. LIVE Studio Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStudioSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var allowGifts by remember { mutableStateOf(true) }
    var ageGated by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("LIVE & Subscriptions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("LIVE Gifts & Diamonds", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Allow viewers to send virtual gifts and animated stickers in real time", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = allowGifts,
                    onCheckedChange = { allowGifts = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("18+ Age Restricted Stream", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Limit your LIVE broadcast to viewers aged 18 and older", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = ageGated,
                    onCheckedChange = { ageGated = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 11. Content Preferences Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentPreferencesSheet(
    categories: List<com.example.model.VideoCategory>,
    onDismiss: () -> Unit,
    onResetAlgorithm: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var restrictedMode by remember { mutableStateOf(false) }
    var stemFeed by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Content Preferences & Algorithm", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Refresh FYP Algorithm Card
            Card(
                colors = CardDefaults.cardColors(containerColor = TokTokCyan.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = TokTokCyan)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Refresh Your For You Feed", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Reset your FYP recommendations as if you just signed up for TokTok", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(
                        onClick = onResetAlgorithm,
                        colors = ButtonDefaults.buttonColors(containerColor = TokTokCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reset", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Restricted Mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Restricted Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Limit content that may not be suitable for all audiences", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = restrictedMode,
                    onCheckedChange = { restrictedMode = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // STEM Feed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("STEM Feed", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Show a dedicated feed for Science, Technology, Engineering & Math", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = stemFeed,
                    onCheckedChange = { stemFeed = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Text("Train Your Topics", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                Toast.makeText(context, "Added ${cat.title} to preferred topics! 🎯", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("${cat.iconEmoji} ${cat.title}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 12. Playback Settings Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackSettingsSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var autoVolume by remember { mutableStateOf(true) }
    var openOnMute by remember { mutableStateOf(false) }
    var autoScroll by remember { mutableStateOf(false) }
    var hdPlayback by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Playback Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Auto Volume Adjustment", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Harmonizes unexpectedly loud videos for comfort", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = autoVolume,
                    onCheckedChange = { autoVolume = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Open TokTok on Mute", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Videos start playing on mute when the app opens", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = openOnMute,
                    onCheckedChange = { openOnMute = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Auto-Scroll Videos", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Automatically scroll to the next video when the current video ends", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = autoScroll,
                    onCheckedChange = { autoScroll = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("HD Video Playback on Wi-Fi", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Stream 1080p full 60fps when connected to Wi-Fi", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = hdPlayback,
                    onCheckedChange = { hdPlayback = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 13. Display & Theme Settings Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySettingsSheet(
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var fontScale by remember { mutableFloatStateOf(1.0f) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Display & Appearance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Dark Mode Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Dark Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(if (isDarkTheme) "Dark theme is currently active" else "Light theme is currently active", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onToggleDarkTheme() },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            // Text Size Slider
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Text Size Scale", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${(fontScale * 100).toInt()}%", fontWeight = FontWeight.Bold, color = TokTokPink, fontSize = 14.sp)
                }
                Slider(
                    value = fontScale,
                    onValueChange = { fontScale = it },
                    valueRange = 0.8f..1.3f,
                    colors = SliderDefaults.colors(thumbColor = TokTokPink, activeTrackColor = TokTokPink)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Small (A)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Standard (A)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Large (A)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 14. Language Settings Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedLanguage by remember { mutableStateOf("English (US)") }

    val languages = listOf(
        "English (US)",
        "Español (Spanish)",
        "Français (French)",
        "Deutsch (German)",
        "বাংলা (Bengali)",
        "हिन्दी (Hindi)",
        "العربية (Arabic)",
        "日本語 (Japanese)",
        "한국어 (Korean)",
        "Português (Portuguese)",
        "Bahasa Indonesia"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("App Language", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(languages) { lang ->
                    val isSelected = selectedLanguage == lang
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) TokTokPink.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable {
                                selectedLanguage = lang
                                Toast.makeText(context, "Language set to $lang", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(lang, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
                        if (isSelected) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = TokTokPink)
                        }
                    }
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 15. Screen Time & Digital Wellbeing Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimeSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var dailyLimit by remember { mutableStateOf("2 hours") }
    var screenBreaks by remember { mutableStateOf(true) }
    var sleepReminder by remember { mutableStateOf("11:00 PM") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Screen Time & Digital Wellbeing", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Weekly Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF8A65).copy(alpha = 0.15f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Average This Week", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("1 hr 24 mins / day", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF7043))
                    Text("Within your 2-hour daily screen time goal 👍", fontSize = 11.sp, color = Color(0xFF4CAF50))
                }
            }

            // Daily Limit Picker
            Column {
                Text("Daily Screen Time Limit", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 6.dp)) {
                    listOf("40m", "60m", "90m", "2 hours").forEach { limit ->
                        val isSelected = dailyLimit == limit
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) TokTokPink else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { dailyLimit = limit }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(limit, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Screen Breaks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Screen Time Breaks", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Get a reminder to take a break after 30 mins of continuous watching", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = screenBreaks,
                    onCheckedChange = { screenBreaks = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 16. Family Pairing Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyPairingSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Family Pairing", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Text(
                "Link a parent's TokTok account with a teen's to manage safety settings, direct messaging permissions, and screen time limits.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("I'm a Parent")
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("I'm a Teen")
                }
            }
        }
    }
}

/**
 * 17. Accessibility Settings Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilitySettingsSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var photosensitiveWarning by remember { mutableStateOf(true) }
    var autoCaptions by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Accessibility", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Remove Photosensitive Videos", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Skip videos that contain flashing lights and strobe effects", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = photosensitiveWarning,
                    onCheckedChange = { photosensitiveWarning = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Always Show Captions", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Display auto-generated text subtitles when available", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = autoCaptions,
                    onCheckedChange = { autoCaptions = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 18. Free Up Space Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeUpSpaceSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var cacheSize by remember { mutableStateOf("24.8 MB") }
    var downloadsSize by remember { mutableStateOf("0 MB") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Free Up Space", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Cache Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Cache", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Clear temporary playback chunks ($cacheSize)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                OutlinedButton(
                    onClick = {
                        cacheSize = "0 MB"
                        Toast.makeText(context, "Cleared 24.8 MB of video cache! ✨", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear")
                }
            }

            // Downloads Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Downloads", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Video drafts and offline filters ($downloadsSize)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                OutlinedButton(
                    onClick = {
                        downloadsSize = "0 MB"
                        Toast.makeText(context, "Downloads cache cleared!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear")
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 19. Data Saver Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataSaverSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var dataSaver by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Data Saver", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Data Saver Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Reduces mobile cellular data usage. Videos may be at a lower resolution or take longer to load.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = dataSaver,
                    onCheckedChange = { dataSaver = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TokTokPink)
                )
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 20. Offline Videos Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineVideosSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedPackage by remember { mutableStateOf("100 videos") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Offline Videos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Download videos over Wi-Fi so you can watch your For You Page while traveling or offline without data.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            listOf("50 videos (~15 mins • 120 MB)", "100 videos (~30 mins • 240 MB)", "200 videos (~60 mins • 480 MB)").forEach { opt ->
                val isSelected = selectedPackage == opt
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) TokTokPink.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { selectedPackage = opt }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(opt, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp)
                    if (isSelected) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = TokTokPink)
                    }
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Downloading $selectedPackage for offline viewing! 📥", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Download Now", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 21. Report a Problem Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportProblemSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var customFeedback by remember { mutableStateOf("") }

    val topics = listOf(
        "Account & Profile",
        "Feed, Search & Share",
        "Video and Sound",
        "Follow, Like & Comment",
        "Direct Messages",
        "LIVE & Gifts",
        "Creator Rewards & Monetization"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Help Center & Feedback", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Text("Select a topic to troubleshoot:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

            topics.forEach { topic ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable {
                            Toast.makeText(context, "$topic: Articles & troubleshooting guides loaded.", Toast.LENGTH_SHORT).show()
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(topic, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            OutlinedTextField(
                value = customFeedback,
                onValueChange = { customFeedback = it },
                label = { Text("Tell us your problem / Submit a ticket") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Button(
                onClick = {
                    if (customFeedback.isNotBlank()) {
                        Toast.makeText(context, "Feedback ticket #TK-92841 submitted to TokTok Support! 📨", Toast.LENGTH_LONG).show()
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Please enter your problem description", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Submit Ticket", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 22. Support Inbox Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportInboxSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Support Inbox", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF66BB6A).copy(alpha = 0.15f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF66BB6A), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Account in Good Standing 🛡️", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("0 active Community Guidelines strikes or warnings.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Text("Reports & Appeals", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("You have no open reports or appeals.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 23. Community Guidelines Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityGuidelinesSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val rules = listOf(
        "Safety & Civility" to "We do not tolerate harassment, bullying, hate speech, or dangerous activities.",
        "Mental & Behavioral Health" to "We protect our community and promote positive digital habits.",
        "Integrity & Authenticity" to "Spam, fake engagements, and misleading impersonation are prohibited.",
        "Privacy & Security" to "Protect your personal credentials and respect others' privacy."
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Community Guidelines", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            rules.forEach { (title, desc) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TokTokCyan)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("I Understand & Agree", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 24. Terms & Privacy Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsPolicySheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Terms & Privacy Policy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Text(
                "TokTok is committed to protecting your personal information. All media, messages, and profile information are stored safely using modern on-device Room encrypted persistence and verified pipelines.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                "By using TokTok, you agree to our Terms of Service, Copyright Policy, and Data Privacy disclosures. You retain ownership of all original content you create and upload.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 1b. Download Data Sheet (BDTOK Account & User Database Export)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadDataSheet(
    user: User,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var exportedDataJson by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var dataFormat by remember { mutableStateOf("JSON") } // TXT or JSON

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF26A69A).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = Color(0xFF26A69A),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Download your BDTOK Data",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Get a copy of your personal data file",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            Text(
                text = "Your downloaded file will include:\n• Your profile details (name, username, bio, verification status)\n• Your uploaded videos, view counts, and engagement stats\n• Your direct messages and comment activity\n• Your account settings and privacy preferences",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select File Format", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilterChip(
                            selected = dataFormat == "JSON",
                            onClick = { dataFormat = "JSON" },
                            label = { Text("JSON (Machine-readable / Backup)") }
                        )
                        FilterChip(
                            selected = dataFormat == "TXT",
                            onClick = { dataFormat = "TXT" },
                            label = { Text("TXT (Readable text)") }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    isGenerating = true
                    viewModel.exportUserData(user.id) { json ->
                        exportedDataJson = json
                        isGenerating = false
                        Toast.makeText(context, "🎉 Your BDTOK Data file is ready!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TokTokPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isGenerating) "Compiling your data..." else "Request & Download Data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            if (exportedDataJson != null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00C853))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Data File Ready (${exportedDataJson!!.length} bytes)", fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, exportedDataJson)
                                        putExtra(Intent.EXTRA_TITLE, "${user.username}_BDTOK_Data.json")
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, "Save / Share Data File")
                                    context.startActivity(shareIntent)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save / Share", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(exportedDataJson!!))
                                    Toast.makeText(context, "Data copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
