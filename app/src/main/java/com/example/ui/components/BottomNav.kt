package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderGoldMuted
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.AppScreen

sealed class NavItem(
    val screen: AppScreen,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    data object Home : NavItem(
        AppScreen.HOME,
        "Parlour",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "nav_home"
    )

    data object Booking : NavItem(
        AppScreen.BOOKING,
        "Book",
        Icons.Filled.CalendarMonth,
        Icons.Outlined.CalendarMonth,
        "nav_book"
    )

    data object AiStylist : NavItem(
        AppScreen.AI_STYLIST,
        "AI Stylist",
        Icons.Filled.AutoAwesome,
        Icons.Outlined.AutoAwesome,
        "nav_ai_stylist"
    )

    data object MyBookings : NavItem(
        AppScreen.MY_BOOKINGS,
        "Passes",
        Icons.Filled.ConfirmationNumber,
        Icons.Outlined.ConfirmationNumber,
        "nav_my_bookings"
    )

    data object Account : NavItem(
        AppScreen.ACCOUNT,
        "Account",
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle,
        "nav_account"
    )

    data object Admin : NavItem(
        AppScreen.ADMIN,
        "Admin",
        Icons.Filled.AdminPanelSettings,
        Icons.Outlined.AdminPanelSettings,
        "nav_admin"
    )
}

@Composable
fun BarbershopBottomNav(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem.Home,
        NavItem.Booking,
        NavItem.AiStylist,
        NavItem.MyBookings,
        NavItem.Account,
        NavItem.Admin
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .border(width = 1.dp, color = BorderGoldMuted.copy(alpha = 0.5f))
            .navigationBarsPadding()
            .padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentScreen == item.screen
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onNavigate(item.screen) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag(item.testTag)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (selected) GoldPrimary.copy(alpha = 0.18f) else DarkBackground)
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = if (selected) GoldPrimary else TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (selected) GoldPrimary else TextMuted
                    )
                }
            }
        }
    }
}
