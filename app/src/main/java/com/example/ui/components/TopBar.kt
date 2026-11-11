package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BorderGoldMuted
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.viewmodel.AppScreen

@Composable
fun BarbershopTopBar(
    currentScreen: AppScreen,
    isAdminLoggedIn: Boolean,
    onNavigateToAdmin: () -> Unit,
    onNavigateToAccount: () -> Unit = {},
    isUserLoggedIn: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .statusBarsPadding()
            .border(width = 1.dp, color = BorderGoldMuted.copy(alpha = 0.4f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("brand_header")
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, GoldPrimary, CircleShape)
                        .background(DarkSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_barber_logo),
                        contentDescription = "Crown & Blade Emblem",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "CROWN & BLADE",
                        style = MaterialTheme.typography.titleLarge,
                        color = GoldPrimary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "EST. 2014 • MASTER PARLOUR",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            // Action Buttons: WhatsApp, Account & Admin
            Row(verticalAlignment = Alignment.CenterVertically) {
                // WhatsApp quick contact
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(WhatsAppGreen.copy(alpha = 0.15f))
                        .border(1.dp, WhatsAppGreen.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .clickable {
                            openWhatsApp(context)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("whatsapp_top_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(WhatsAppGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WhatsApp",
                            color = WhatsAppGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Account / Auth Button
                IconButton(
                    onClick = onNavigateToAccount,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isUserLoggedIn) GoldPrimary.copy(alpha = 0.2f)
                            else DarkSurface
                        )
                        .border(
                            1.dp,
                            if (isUserLoggedIn) GoldPrimary else BorderGoldMuted,
                            CircleShape
                        )
                        .testTag("account_top_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account & Security",
                        tint = if (isUserLoggedIn) GoldPrimary else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Admin Dashboard Button
                IconButton(
                    onClick = onNavigateToAdmin,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isAdminLoggedIn) GoldPrimary.copy(alpha = 0.2f)
                            else DarkSurface
                        )
                        .border(
                            1.dp,
                            if (isAdminLoggedIn) GoldPrimary else BorderGoldMuted,
                            CircleShape
                        )
                        .testTag("admin_top_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Dashboard",
                        tint = if (isAdminLoggedIn) GoldPrimary else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

fun openWhatsApp(context: Context) {
    try {
        val phoneNumber = "923413274132" // Ali Murtaza - Crown & Blade Parlour
        val message = "Hello Ali Murtaza / Crown & Blade, I would like to inquire about booking a salon appointment."
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback dialer
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+923413274132"))
        context.startActivity(dialIntent)
    }
}
